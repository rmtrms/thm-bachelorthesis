package benchmark;

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

import benchmark.evaluator.LLMEvaluator;
import benchmark.evaluator.LLMEvaluator.EvaluationMetrics;
import benchmark.evaluator.OverallBenchmarkResults;
import benchmark.model.BenchmarkResult;
import benchmark.model.BenchmarkStatus;
import benchmark.model.CopyrightInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BenchmarkRunner {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkRunner.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    static final String PROMPT_TEMPLATE_PATH = "prompts/base_prompt_7.txt";
    static final String BASE_DATA_DIR = "data/benchmark";
    static final String PROCESSED_OUTPUT_ROOT_DIR = "/Users/rtueremis/documents/benchmark_results";
    static final String WARMUP_PROMPT = "Hello, respond with a single word 'Ready'.";

    private static final long REQUEST_TIMEOUT_MINUTES = 3; // Change this value as needed

    private static boolean PERFORM_WARMUP = true;
    private static boolean LOG_WARMUP_SEPARATELY = true;

    static final List<String> MODELS_TO_BENCHMARK = List.of(
            //"qwen2.5-coder:0.5b",
            //"tinyllama:1.1b",
            //"qwen2.5-coder:1.5b",
            //"qwen2.5:3b",
            //"qwen2.5-coder:3b",
            //"orca-mini:3b",
            //"phi3:3.8b", --> causes stackoverflow error at executor service
            //"qwen3:4b",
            //"gemma3:4b",
            //"deepseek-coder:6.7b",
            "mistral:7b",
            //"mathstral:7b",
            //"olmo2:7b",
            //"llama3:latest",
            //"llava:7b",
            //"qwen2.5-coder:7b",
            //"qwen2.5:7b",
            //"dolphin3:8b",
            //"qwen3:8b",
            //"mistral-nemo:12b",
            //"gemma3n:e4b",
            //"phi3:14b",
            //"qwen2.5-coder:14b",
            //"phi4:14b",
            //"mistral-small:22b",
            //"devstral:24b",
            "mistral-small:24b"
            //"magistral:24b",
            //"mistral-small3.1:24b",
            //"mistral-small3.2:24b",
            //"qwen2.5-coder:32b",
            // "mixtral:8x7b",
            // "llama4:latest",
            //"mixtral:8x22b",
            //"qwen3:30b",
            //"deepseek-r1:8b"
    );

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Path baseDataPath = Paths.get(BASE_DATA_DIR);

            Path overallResultsRootPath = Paths.get(PROCESSED_OUTPUT_ROOT_DIR);
            if (!Files.exists(overallResultsRootPath)) {
                Files.createDirectories(overallResultsRootPath);
                logger.info("Created overall results root directory: {}", overallResultsRootPath);
            }

            String promptTemplate = Files.readString(Paths.get(PROMPT_TEMPLATE_PATH));

            ArrayList<String> wordList = new ArrayList<>();
            wordList.add("copyright");
            wordList.add("(c)");
            wordList.add("all rights reserved");
            wordList.add("rights");
            wordList.add("author");
            wordList.add("authors");
            wordList.add("maintainer");
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

            TextSieve sieve = TextSieve.builder()
                    .wordlist(wordList)
                    .includeReach(100)
                    .build();

            // Initialize OllamaAPI once
            OllamaAPI ollamaAPI = new OllamaAPI("http://localhost:11434");
            ollamaAPI.setVerbose(false);
            ollamaAPI.setRequestTimeoutSeconds(300); // Keep for network-level timeouts

            for (String modelName : MODELS_TO_BENCHMARK) {
                logger.info("Starting benchmark for model: [{}]", modelName);
                Path modelOutputDir = overallResultsRootPath.resolve("llm_results_" + modelName.replace(":", "_").replace("-", "_"));

                if (Files.exists(modelOutputDir)) {
                    try (Stream<Path> files = Files.walk(modelOutputDir)) {
                        files.sorted(java.util.Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                    }
                }
                Files.createDirectories(modelOutputDir);
                logger.info("Created model output directory: {}", modelOutputDir);

                OverallBenchmarkResults overallResults = new OverallBenchmarkResults(modelName);

                long warmupDuration = 0;
                if (PERFORM_WARMUP) {
                    logger.info("Performing warm-up for model [{}]...", modelName);
                    warmupModel(ollamaAPI, modelName); // Note: warmup does not use the executor timeout
                    logger.info("Warm-up for model [{}] completed in {} ms.", modelName, warmupDuration);
                    if (LOG_WARMUP_SEPARATELY) {
                        logger.info("Note: Warm-up time is logged separately and does NOT affect individual file metrics.");
                    } else {
                        logger.warn("Warning: Warm-up time is NOT logged separately and will be implicitly included in the first file's metrics if cold start occurs.");
                    }
                }

                try (Stream<Path> categoryDirs = Files.list(baseDataPath)) {
                    List<Path> categories = categoryDirs
                            .filter(Files::isDirectory)
                            .filter(p -> !p.getFileName().toString().startsWith("."))
                            .collect(Collectors.toList());

                    if (categories.isEmpty()) {
                        logger.warn("No categories found in [{}]. Please ensure your data is structured as benchmark/<category>/...", BASE_DATA_DIR);
                        continue;
                    }

                    for (Path categoryDir : categories) {
                        String categoryName = categoryDir.getFileName().toString();
                        logger.info("Processing category: [{}] for model [{}]", categoryName, modelName);

                        Path currentCategoryOutputDir = modelOutputDir.resolve(categoryName);
                        Files.createDirectories(currentCategoryOutputDir);
                        logger.debug("Created category output directory: {}", currentCategoryOutputDir);

                        List<Path> inputFiles = Files.list(categoryDir)
                                .filter(Files::isRegularFile)
                                .filter(path -> {
                                    String name = path.getFileName().toString();
                                    return !name.startsWith(".") &&
                                            !name.endsWith("_scancode.json") &&
                                            !name.endsWith("_result.json") &&
                                            !name.endsWith("_filtered.ext");
                                })
                                .toList();

                        if (inputFiles.isEmpty()) {
                            logger.warn("No input files found in category [{}]. Skipping.", categoryName);
                            continue;
                        }

                        for (Path inputFile : inputFiles) {
                            processFile(inputFile, categoryName, modelName, currentCategoryOutputDir, ollamaAPI, promptTemplate, sieve, overallResults, executor);
                        }
                    }
                }

                overallResults.saveResults(modelOutputDir.toString());
                logger.info("Benchmark for model [{}] completed. Success: [{}], Timeouts: [{}], Errors: [{}]. Avg F1: [{}], Avg Tokens/Sec (Success): [{}]",
                        modelName,
                        overallResults.getSuccessCount(),
                        overallResults.getTimeoutCount(),
                        overallResults.getErrorCount(),
                        String.format("%.4f", overallResults.getAverageOverallF1PerFile()),
                        String.format("%.2f", overallResults.getAverageTokensPerSecond())
                );
            }
        } finally {
            logger.info("Shutting down the executor service.");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("Executor did not terminate in 60 seconds. Forcing shutdown.");
                    executor.shutdownNow();
                }
            } catch (InterruptedException ie) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        logger.info("All benchmarks completed!");
    }

    private static long warmupModel(OllamaAPI ollamaAPI, String modelName) {
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(modelName);
        Options options = new OptionsBuilder().setTemperature(0.0f).build();
        OllamaChatRequest request = builder
                .withMessage(OllamaChatMessageRole.USER, WARMUP_PROMPT)
                .withOptions(options)
                .build();

        long startTime = System.currentTimeMillis();
        try {
            ollamaAPI.chat(request);
        } catch (Exception e) {
            logger.error("Error during warm-up for model [{}]: {}", modelName, e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static long processFile(Path inputFile, String category, String modelName, Path currentCategoryOutputDir,
                                    OllamaAPI ollamaAPI, String promptTemplate, TextSieve sieve,
                                    OverallBenchmarkResults overallResults, ExecutorService executor) {
        String fileName = inputFile.getFileName().toString();
        String sha1 = getBaseName(inputFile);
        String extension = getFileExtension(fileName);
        Charset charset;

        long duration = 0;
        Future<OllamaChatResult> future = null;
        int promptTokens = 0;

        try {
            // 1. Read and detect encoding of the original file
            byte[] fileBytes = Files.readAllBytes(inputFile);
            String encoding = detectEncoding(fileBytes);
            if (encoding == null) {
                charset = StandardCharsets.UTF_8;
            } else {
                charset = Charset.forName(encoding);
            }
            String fileContent = new String(fileBytes, encoding);

            // 2. Preprocess the file content using TextSieve
            CharSequence filteredContent = sieve.loadFiltered(
                    inputFile.toFile(),
                    charset,
                    currentCategoryOutputDir.toFile() // Pass the category-specific output directory
            );
            // Ensure the filtered file has the correct final path (sieve might save directly, but this is safer)
            Files.writeString(currentCategoryOutputDir.resolve(sha1 + "_filtered" + extension), filteredContent.toString(), charset);


            // 3. Prepare the LLM prompt with the filtered content
            // Escape the filtered content to ensure it's safely embedded within the JSON-like prompt structure
            String userMessage = promptTemplate.replace("{{FILE_CONTENT}}", escapeJson(filteredContent.toString()));
            promptTokens = userMessage.length() / 4; // Crude token estimation for input

            logger.info("Prompting file [{}] from category [{}] to [{}] estimated prompt tokens [{}]",
                    fileName, category, modelName, promptTokens);

            // Configure Ollama options
            Options options = new OptionsBuilder()
                    .setTemperature(0.0f)// Keep temperature low for deterministic output
                    .setNumCtx(4096)
                    .build();

            // Build the chat request for the specified model
            OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(modelName);
            OllamaChatRequest request = builder
                    .withMessage(OllamaChatMessageRole.USER, userMessage)
                    .withOptions(options)
                    .build();

            // 4. Send request to Ollama and time the response
            long startTime = System.currentTimeMillis();
            Callable<OllamaChatResult> ollamaTask = () -> ollamaAPI.chat(request);
            future = executor.submit(ollamaTask);

            try {
                // Get the result with the configured timeout
                OllamaChatResult result = future.get(REQUEST_TIMEOUT_MINUTES, TimeUnit.MINUTES);
                long endTime = System.currentTimeMillis();
                duration = endTime - startTime;

                String rawResponse = result.getResponseModel().getMessage().getContent();

                // Initial check for raw response validity
                boolean wasRawResponseValidJson = LLMEvaluator.parseJsonOutput(rawResponse) != null;

                // Use ChatUtil to robustly extract the JSON object from the response
                JSONObject extractedJson = ChatUtil.extractJsonObject(rawResponse);
                String processedResponse;
                if (extractedJson != null) {
                    // Convert the extracted JSONObject back to a string for parsing by Jackson
                    processedResponse = extractedJson.toString();
                } else {
                    // Fallback for cases where no JSON object is found.
                    // This maintains compatibility with downstream logic that expects a JSON string.
                    processedResponse = "{}";
                    logger.warn("ChatUtil.extractJsonObject could not find a valid JSON object. Raw response: {}", rawResponse);
                }

                // Check if extraction was beneficial: the raw response was invalid AND the processed response is valid
                boolean jsonExtractionBeneficial = !wasRawResponseValidJson && (LLMEvaluator.parseJsonOutput(processedResponse) != null);

                int evalTokens = processedResponse.length() / 4;

                double tokensPerSecond = 0.0;
                if (duration > 0) {
                    tokensPerSecond = (double) evalTokens / (duration / 1000.0);
                }

                // 5. Save all relevant files to the specific category output directory
                // Copy original file
                Files.copy(inputFile, currentCategoryOutputDir.resolve(sha1 + extension), StandardCopyOption.REPLACE_EXISTING);

                // Copy _scancode.json (golden standard) and parse it
                Path scancodePath = inputFile.getParent().resolve(sha1 + "_scancode.json");
                CopyrightInfo goldenCopyrightInfo = null;
                int totalGoldenCopyrights = 0; // Initialize total golden copyrights for this file
                if (Files.exists(scancodePath)) {
                    Files.copy(scancodePath, currentCategoryOutputDir.resolve(sha1 + "_scancode.json"), StandardCopyOption.REPLACE_EXISTING);
                    goldenCopyrightInfo = objectMapper.readValue(Files.readString(scancodePath), CopyrightInfo.class);
                    totalGoldenCopyrights = goldenCopyrightInfo.getCopyrights().size(); // Get count from golden
                } else {
                    logger.warn("Golden standard file not found for {}: {}", fileName, scancodePath);
                }

                // Write the raw LLM response (for debugging)
                Files.writeString(currentCategoryOutputDir.resolve(sha1 + "_raw_result.json"), rawResponse); // Save raw response
                // Write the processed LLM response (the JSON part)
                Files.writeString(currentCategoryOutputDir.resolve(sha1 + "_result.json"), processedResponse); // Save processed JSON

                // Use the processedResponse for evaluation
                CopyrightInfo predictedCopyrightInfo = LLMEvaluator.parseJsonOutput(processedResponse);

                double copyrightF1 = 0.0, holderF1 = 0.0, authorF1 = 0.0, overallF1 = 0.0;
                int copyrightExactMatches = 0;
                boolean validJson = predictedCopyrightInfo != null;
                String errorMessage = null;

                if (validJson) {
                    EvaluationMetrics metrics = LLMEvaluator.evaluate(goldenCopyrightInfo, predictedCopyrightInfo);
                    copyrightF1 = metrics.copyrightF1;
                    holderF1 = metrics.holderF1;
                    authorF1 = metrics.authorF1;
                    overallF1 = metrics.overallF1;
                    copyrightExactMatches = metrics.copyrightExactMatches;
                } else {
                    errorMessage = "Invalid JSON output from LLM (after extraction).";
                    logger.error("Invalid JSON output for file [{}]: Raw Response: \n[{}] \n Processed Response: \n[{}]", fileName, rawResponse, processedResponse);
                }

                // 7. Create and add benchmark result for this file
                BenchmarkResult fileResult = new BenchmarkResult(
                        sha1, category, duration, promptTokens, evalTokens, tokensPerSecond,
                        copyrightF1, holderF1, authorF1, overallF1,
                        copyrightExactMatches, totalGoldenCopyrights,
                        validJson, wasRawResponseValidJson, jsonExtractionBeneficial,
                        errorMessage, BenchmarkStatus.SUCCESS
                );
                overallResults.addResult(fileResult);

                logger.info("SUCCESS: File [{}] processed in [{}] ms. Tokens/sec: [{}]", sha1, duration, String.format("%.2f", tokensPerSecond));

            } catch (TimeoutException e) {
                duration = REQUEST_TIMEOUT_MINUTES * 60 * 1000; // Record duration as the full timeout period
                logger.error("TIMEOUT: Request for file [{}] timed out after {} minutes.", inputFile, REQUEST_TIMEOUT_MINUTES);
                if (future != null) {
                    future.cancel(true); // Attempt to interrupt the running task
                }
                String timeoutMessage = "Failed due to " + REQUEST_TIMEOUT_MINUTES + "-minute timeout.";
                overallResults.addResult(new BenchmarkResult(
                        sha1, category, duration, promptTokens, 0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0, 0,
                        false, false, false, timeoutMessage, BenchmarkStatus.TIMEOUT
                ));
            }

        } catch (Exception e) {
            if (future != null) {
                future.cancel(true);
            }
            Throwable cause = e instanceof ExecutionException ? e.getCause() : e;
            logger.error("ERROR: Failed processing file [{}]: {}", inputFile, cause.getMessage(), cause);
            // Add a failed benchmark result to ensure it's recorded
            overallResults.addResult(new BenchmarkResult(
                    sha1, category, 0, promptTokens, 0, 0.0,
                    0.0, 0.0, 0.0, 0.0, 0, 0,
                    false, false, false, "Exception: " + cause.getMessage(), BenchmarkStatus.ERROR
            ));
        }
        return duration;
    }

    private static String detectEncoding(byte[] bytes) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    /**
     * Extracts the file extension from a file name.
     *
     * @param fileName The full file name.
     * @return The file extension (e.g., ".txt", ".java"), or empty string if no extension.
     */
    private static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf(".");
        return (i >= 0) ? fileName.substring(i) : "";
    }

    /**
     * Extracts the base name (filename without extension) from a Path.
     *
     * @param file The Path object representing the file.
     * @return The base name.
     */
    private static String getBaseName(Path file) {
        String name = file.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return (dot == -1) ? name : name.substring(0, dot);
    }

    /**
     * Escapes special characters in a string to make it safe for embedding within a JSON string literal.
     * This version assumes that '\n' and '\t' sequences in the input string already represent newlines/tabs
     * and should NOT be double-escaped. It only escapes literal backslashes and double quotes.
     *
     * @param text The input text to escape.
     * @return The escaped text.
     */
    private static String escapeJson(String text) {
        // Use StringBuilder for efficient string manipulation
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch (c) {
                case '\\':
                    sb.append("\\\\"); // Escape literal backslashes
                    break;
                case '"':
                    sb.append("\\\""); // Escape literal double quotes
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}