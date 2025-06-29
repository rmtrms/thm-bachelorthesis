package benchmark;

import com.metaeffekt.artifact.analysis.preprocess.filter.TextSieve;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequest;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;
import io.github.ollama4j.types.OllamaModelType;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleBenchmarkTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBenchmarkTest.class);

    static final String MODEL_NAME = "qwen3";
    static final String PROMPT_TEMPLATE_PATH = "prompts/base_prompt_5.txt";
    static final String DATA_DIR = "/Volumes/Data/benchmark_test";
    static final String INPUT_DIR = DATA_DIR + "/input";
    static final String OUTPUT_DIR = DATA_DIR + "/" + MODEL_NAME + "_result";

    public static void main(String[] args) throws Exception {
        Path inputPath = Paths.get(INPUT_DIR);
        Path outputPath = Paths.get(OUTPUT_DIR);

        // Clean result directory
        if (Files.exists(outputPath)) {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(outputPath)) {
                for (Path file : files) {
                    Files.deleteIfExists(file);
                }
            }
        } else {
            Files.createDirectories(outputPath);
        }

        // Define wordlist for filtering
        ArrayList<String> wordList = new ArrayList<>();
        wordList.add("copyright");
        wordList.add("(c)");
        wordList.add("all rights reserved");
        wordList.add("rights");
        wordList.add("reserved");
        wordList.add("author");
        wordList.add("modified by");
        wordList.add("edited by");
        wordList.add("created by");
        wordList.add("licensed by");
        wordList.add("holder");
        wordList.add("license");
        wordList.add("permission");
        wordList.add("permission");

        // Initialize TextSieve
        TextSieve sieve = TextSieve.builder()
                .wordlist(wordList)
                .includeReach(100)
                .build();

        List<Path> inputFiles = Files.list(inputPath)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String name = path.getFileName().toString();
                    return !name.startsWith(".") &&
                            !name.endsWith("_scancode.json") &&
                            !name.endsWith("_response.json");
                })
                .toList();

        if (inputFiles.isEmpty()) {
            logger.warn("No files to process. Exiting.");
            return;
        }

        // Initialize OllamaAPI with default localhost
        OllamaAPI ollamaAPI = new OllamaAPI("http://localhost:11434");
        ollamaAPI.setVerbose(false);
        ollamaAPI.setRequestTimeoutSeconds(120);

        // Select model type (adapt if using Qwen etc.)
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(OllamaModelType.LLAMA3);
        String promptTemplate = Files.readString(Paths.get(PROMPT_TEMPLATE_PATH));

        long totalDuration = 0;
        int processedCount = 0;

        for (Path inputFile : inputFiles) {
            String fileName = inputFile.getFileName().toString();
            String sha1 = getBaseName(inputFile);
            String extension = getFileExtension(fileName);
            Charset charset;

            byte[] fileBytes = Files.readAllBytes(inputFile);
            String encoding = detectEncoding(fileBytes);
            if (encoding == null) {
                charset = StandardCharsets.UTF_8;
            } else {
                charset = Charset.forName(encoding);
            }
            String fileContent = new String(fileBytes, encoding);

            // Use TextSieve to filter the file content
            CharSequence filteredContent = sieve.loadFiltered(
                    inputFile.toFile(),
                    charset,
                    outputPath.toFile()
            );

            String userMessage = promptTemplate.replace("{{FILE_CONTENT}}", filteredContent.toString());
            int estimatedTokens = userMessage.length() / 4;

            logger.info("Prompting file [{}] to [{}] estimated tokens [{}]",
                    fileName, MODEL_NAME, estimatedTokens);

            Options options = new OptionsBuilder()
                    .setTemperature(0.0f)
                    .build();

            // Build the chat request
            OllamaChatRequest request = builder
                    .withMessage(OllamaChatMessageRole.USER, userMessage)
                    .withOptions(options)
                    .build();

            // Start timing
            long startTime = System.currentTimeMillis();

            // Send request to Ollama
            OllamaChatResult result = ollamaAPI.chat(request);

            // End timing
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            totalDuration += duration;
            processedCount++;

            String response = result.getResponseModel().getMessage().getContent();

            // Save original file
            Files.write(outputPath.resolve(sha1 + extension), fileBytes);

            // Save filtered file
            Files.writeString(outputPath.resolve(sha1 + "_filtered" + extension), filteredContent.toString(), charset);

            // Copy _scancode.json if it exists
            Path scancodePath = inputPath.resolve(sha1 + "_scancode.json");
            if (Files.exists(scancodePath)) {
                Files.copy(scancodePath, outputPath.resolve(sha1 + "_scancode.json"), StandardCopyOption.REPLACE_EXISTING);
            }

            // Write _response.json
            Files.writeString(outputPath.resolve(sha1 + "_result.json"), response);

            logger.info("Saved output of file: [{}] in [{}] ms", sha1, duration);
        }

        if (processedCount > 0) {
            long averageDuration = totalDuration / processedCount;
            logger.info("Processed [{}] files. Average duration per file: [{}] ms", processedCount, averageDuration);
        } else {
            logger.info("No files were processed.");
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
}