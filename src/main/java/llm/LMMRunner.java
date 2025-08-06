package llm;


import benchmark.ChatUtil;
import com.metaeffekt.artifact.analysis.preprocess.filter.TextSieve;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;
import org.json.JSONObject;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LMMRunner {

    private static final Logger logger = LoggerFactory.getLogger(LMMRunner.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final OllamaAPI ollamaAPI;
    private final TextSieve sieve;
    private final String modelName;
    private final String promptTemplate;
    private final ExecutorService executor;

    private static final long REQUEST_TIMEOUT_MINUTES = 3;

    public LMMRunner(String modelName, String promptTemplatePath) throws Exception {
        this.modelName = modelName;
        this.promptTemplate = Files.readString(Paths.get(promptTemplatePath));

        this.ollamaAPI = new OllamaAPI("http://localhost:11434");
        this.ollamaAPI.setVerbose(false);
        this.ollamaAPI.setRequestTimeoutSeconds(300);

        this.executor = Executors.newSingleThreadExecutor();

        // Wordlist as in BenchmarkRunner
        ArrayList<String> wordList = new ArrayList<>();
        wordList.add("copyright");
        wordList.add("(c)");
        wordList.add("all rights reserved");
        wordList.add("rights");
        wordList.add("author");
        wordList.add("authors");
        wordList.add("maintainer");
        wordList.add("Organization");
        wordList.add("Maintainers");
        wordList.add("developer");
        wordList.add("creator");
        wordList.add("created");
        wordList.add("adapted");
        wordList.add("contributor");
        wordList.add("modified by");
        wordList.add("developed by");
        wordList.add("changed by");
        wordList.add("written by");
        wordList.add("edited by");
        wordList.add("added by");
        wordList.add("created by");
        wordList.add("changes by");
        wordList.add("updated by");
        wordList.add("licensed");
        wordList.add("optimization");
        wordList.add("holder");
        wordList.add("license");
        wordList.add("permission");

        this.sieve = TextSieve.builder()
                .wordlist(wordList)
                .includeReach(100)
                .build();
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void extractCopyrights(Path inputDir, Path outputDir) throws IOException, IOException {
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        try (Stream<Path> files = Files.walk(inputDir)) {
            List<Path> inputFiles = files
                    .filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(".txt"))
                    .collect(Collectors.toList());

            for (Path inputFile : inputFiles) {
                processFile(inputFile, outputDir);
            }
        }
    }

    private void processFile(Path inputFile, Path outputDir) {
        String fileName = inputFile.getFileName().toString();
        String sha1 = getBaseName(inputFile);
        String extension = getFileExtension(fileName);
        Charset charset;
        Future<OllamaChatResult> future = null;

        try {
            byte[] fileBytes = Files.readAllBytes(inputFile);
            String encoding = detectEncoding(fileBytes);
            charset = (encoding == null) ? StandardCharsets.UTF_8 : Charset.forName(encoding);

            String fileContent = new String(fileBytes, charset);

            CharSequence filteredContent = sieve.loadFiltered(
                    inputFile.toFile(),
                    charset,
                    outputDir.toFile()
            );

            Path filteredPath = outputDir.resolve(sha1 + "_filtered" + extension);
            Files.writeString(filteredPath, filteredContent.toString(), charset);

            String prompt = promptTemplate.replace("{{FILE_CONTENT}}", escapeJson(filteredContent.toString()));

            Options options = new OptionsBuilder()
                    .setTemperature(0.0f)
                    .setNumCtx(4096)
                    .build();

            OllamaChatRequest request = OllamaChatRequestBuilder
                    .getInstance(modelName)
                    .withMessage(OllamaChatMessageRole.USER, prompt)
                    .withOptions(options)
                    .build();

            long start = System.currentTimeMillis();
            future = executor.submit(() -> ollamaAPI.chat(request));

            OllamaChatResult result = future.get(REQUEST_TIMEOUT_MINUTES, TimeUnit.MINUTES);
            long duration = System.currentTimeMillis() - start;

            String rawResponse = result.getResponseModel().getMessage().getContent();
            JSONObject extractedJson = ChatUtil.extractJsonObject(rawResponse);
            String parsedJson = (extractedJson != null) ? extractedJson.toString(2) : "{}";

            Files.copy(inputFile, outputDir.resolve(sha1 + extension), StandardCopyOption.REPLACE_EXISTING);
            Files.writeString(outputDir.resolve(sha1 + "_llm_response.json"), parsedJson, StandardCharsets.UTF_8);

            int tokens = prompt.length() / 4;
            double tokensPerSec = (tokens > 0 && duration > 0) ? (tokens / (duration / 1000.0)) : 0.0;

            logger.info("Processed [{}] | Tokens: {} | Time: {}ms | Speed: {:.2f} tokens/sec", fileName, tokens, duration, tokensPerSec);

        } catch (TimeoutException e) {
            logger.error("Timeout for file [{}] after {} minutes.", fileName, REQUEST_TIMEOUT_MINUTES);
            if (future != null) future.cancel(true);
        } catch (Exception e) {
            logger.error("Error processing [{}]: {}", fileName, e.getMessage(), e);
            if (future != null) future.cancel(true);
        }
    }

    private static String detectEncoding(byte[] bytes) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    private static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf(".");
        return (i >= 0) ? fileName.substring(i) : "";
    }

    private static String getBaseName(Path file) {
        String name = file.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return (dot == -1) ? name : name.substring(0, dot);
    }

    private static String escapeJson(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"': sb.append("\\\""); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    // ✅ MAIN method with example call
    public static void main(String[] args) {
        try {
            String model = "gemma3:27b";
            String promptTemplatePath = "prompts/ai-prompt_1.txt";
            Path inputDir = Paths.get("data/production_input");
            Path outputDir = Paths.get("data/production_output");

            LMMRunner runner = new LMMRunner(model, promptTemplatePath);
            runner.extractCopyrights(inputDir, outputDir);
            runner.shutdown();

        } catch (Exception e) {
            logger.error("Error during execution: {}", e.getMessage(), e);
        }
    }
}