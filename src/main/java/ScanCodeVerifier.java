import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ScanCodeVerifier {

    private static final Logger logger = LoggerFactory.getLogger(ScanCodeVerifier.class);

    private static final String DATA_BASE_DIR = "/Volumes/Data 1/extracted/lowercase_matches";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/restored_case";
    private static final String EXACT_MATCHES_DIR = DATA_BASE_DIR + "/exact_matches";
    private static final String NORMALIZED_MATCHES_DIR = DATA_BASE_DIR + "/normalized_matches";
    private static final String LOWERCASE_MATCHES_DIR = DATA_BASE_DIR + "/lowercase_matches";
    private static final String NORMALIZED_AND_LOWERCASE_MATCHES_DIR = DATA_BASE_DIR + "/normalized_and_lowercase_matches";
    private static final String NO_MATCHES_DIR = DATA_BASE_DIR + "/no_matches";

    private static int exactMatchCount = 0;
    private static int normalizedMatchCount = 0;
    private static int lowercaseMatchCount = 0;
    private static int normalizedLowercaseMatchCount = 0;
    private static int noMatchCount = 0;

    public static void main(String[] args) throws IOException {
        prepareOutputDirectory(Paths.get(EXACT_MATCHES_DIR));
        prepareOutputDirectory(Paths.get(NORMALIZED_MATCHES_DIR));
        prepareOutputDirectory(Paths.get(LOWERCASE_MATCHES_DIR));
        prepareOutputDirectory(Paths.get(NORMALIZED_AND_LOWERCASE_MATCHES_DIR));
        prepareOutputDirectory(Paths.get(NO_MATCHES_DIR));

        verifyFilesInDirectory(Paths.get(INPUT_DIR));

        logger.info("Exact matches copied                    : {}", exactMatchCount);
        logger.info("Normalized matches copied               : {}", normalizedMatchCount);
        logger.info("Lowercase matches copied                : {}", lowercaseMatchCount);
        logger.info("Normalized and lowercase matches copied : {}", normalizedLowercaseMatchCount);
        logger.info("No matches                              : {}", noMatchCount);
        logger.info("Total verified files                    : {}", exactMatchCount + normalizedMatchCount + lowercaseMatchCount + normalizedLowercaseMatchCount + noMatchCount);
    }

    private static void prepareOutputDirectory(Path outputDir) throws IOException {
        if (Files.notExists(outputDir)) {
            Files.createDirectories(outputDir);
            return;
        }

        try {
            Files.list(outputDir).parallel().forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    logger.warn("Failed to delete: {}", path, e);
                }
            });
        } catch (IOException e) {
            logger.error("Failed to list directory: {}", outputDir, e);
            throw e;
        }
    }

    private static void verifyFilesInDirectory(Path inputDir) throws IOException {
        if (!Files.exists(inputDir) || !Files.isDirectory(inputDir)) {
            logger.error("Input directory does not exist or is not a directory: {}", inputDir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir)) {
            for (Path file : stream) {
                if (!Files.isRegularFile(file)) continue;

                String fileName = file.getFileName().toString();
                if (fileName.endsWith("_scancode.json")) continue;

                String baseName = getBaseFileName(fileName);
                Path metadataPath = inputDir.resolve(baseName + "_scancode.json");

                if (!Files.exists(metadataPath)) continue;

                VerificationResult result = verifyFile(file, metadataPath);

                switch (result) {
                    case EXACT -> {
                        copyToVerified(file, metadataPath, Paths.get(EXACT_MATCHES_DIR));
                        exactMatchCount++;
                    }
                    case FORMATTING_MATCH -> {
                        copyToVerified(file, metadataPath, Paths.get(NORMALIZED_MATCHES_DIR));
                        normalizedMatchCount++;
                    }
                    case LOWERCASE_MATCH -> {
                        copyToVerified(file, metadataPath, Paths.get(LOWERCASE_MATCHES_DIR));
                        lowercaseMatchCount++;
                    }
                    case FORMATTING_LOWERCASE_MATCH -> {
                        copyToVerified(file, metadataPath, Paths.get(NORMALIZED_AND_LOWERCASE_MATCHES_DIR));
                        normalizedLowercaseMatchCount++;
                    }
                    case NONE -> {
                        copyToVerified(file, metadataPath, Paths.get(NO_MATCHES_DIR));
                        noMatchCount++;
                    }
                }
            }
        }
    }

    private enum VerificationResult {
        NONE,
        FORMATTING_LOWERCASE_MATCH,
        LOWERCASE_MATCH,
        FORMATTING_MATCH,
        EXACT
    }

    private static VerificationResult verifyFile(Path filePath, Path metadataPath) {
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);

            String encoding = detectEncoding(fileBytes);
            if (encoding == null) {
                logger.warn("Encoding not detected for [{}]. Defaulting to UTF-8.", filePath.getFileName());
                encoding = StandardCharsets.UTF_8.name();
            }

            String content = new String(fileBytes, encoding);
            String strippedContent = content.replaceAll("\\s+", "");
            String lowerContent = content.toLowerCase();
            String strippedLowerContent = strippedContent.toLowerCase();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(metadataPath.toFile());

            List<String> copyrights =
                    extractTextArray(root.path("copyrights"));

            VerificationResult worstMatch = VerificationResult.EXACT;

            for (String text : copyrights) {
                if (content.contains(text)) {
                    continue;
                }

                String strippedText = text.replaceAll("\\s+", "");
                if (strippedContent.contains(strippedText)) {
                    worstMatch = downgrade(worstMatch, VerificationResult.FORMATTING_MATCH);
                    continue;
                }

                String lowerText = text.toLowerCase();
                if (lowerContent.contains(lowerText)) {
                    worstMatch = downgrade(worstMatch, VerificationResult.LOWERCASE_MATCH);
                    continue;
                }

                String strippedLowerText = strippedText.toLowerCase();
                if (strippedLowerContent.contains(strippedLowerText)) {
                    worstMatch = downgrade(worstMatch, VerificationResult.FORMATTING_LOWERCASE_MATCH);
                    continue;
                }

                return VerificationResult.NONE;
            }

            return worstMatch;

        } catch (IOException e) {
            logger.error("Error verifying {}: {}", filePath.getFileName(), e.getMessage());
            return VerificationResult.NONE;
        }
    }

    private static VerificationResult downgrade(VerificationResult current, VerificationResult downgradeTo) {
        if (downgradeTo.ordinal() < current.ordinal()) {
            return downgradeTo;
        }
        return current;
    }

    private static String detectEncoding(byte[] bytes) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    private static void copyToVerified(Path file, Path metadata, Path targetDir) {
        try {
            Files.copy(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(metadata, targetDir.resolve(metadata.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error copying to {}: {}", targetDir, e.getMessage());
        }
    }

    private static List<String> extractTextArray(JsonNode arrayNode) {
        if (arrayNode == null || !arrayNode.isArray()) return List.of();
        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(JsonNode::asText)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    private static String getBaseFileName(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot > 0) ? fileName.substring(0, dot) : fileName;
    }
}

