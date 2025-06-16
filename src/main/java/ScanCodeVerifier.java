import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String DATA_BASE_DIR = "/Volumes/Data";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/extracted";
    private static final String EXACT_MATCHES_DIR = DATA_BASE_DIR + "/exact_matches";
    private static final String NORMALIZED_MATCHES_DIR = DATA_BASE_DIR + "/normalized_matches";

    private static int exactMatchCount = 0;
    private static int normalizedMatchCount = 0;

    public static void main(String[] args) throws IOException {
        prepareOutputDirectory(Paths.get(EXACT_MATCHES_DIR));
        prepareOutputDirectory(Paths.get(NORMALIZED_MATCHES_DIR));
        verifyFilesInDirectory(Paths.get(INPUT_DIR));
        logger.info("Exact matches copied   : {}", exactMatchCount);
        logger.info("Normalized matches   : {}", normalizedMatchCount);
        logger.info("Total verified files   : {}", (exactMatchCount + normalizedMatchCount));
    }

    private static void prepareOutputDirectory(Path outputDir) throws IOException {
        if (Files.notExists(outputDir)) {
            Files.createDirectories(outputDir);
        } else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(outputDir)) {
                for (Path entry : stream) {
                    Files.deleteIfExists(entry);
                }
            }
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
                if (result == VerificationResult.EXACT) {
                    copyToVerified(file, metadataPath, Paths.get(EXACT_MATCHES_DIR));
                    exactMatchCount++;
                } else if (result == VerificationResult.FORMATTING_MATCH) {
                    copyToVerified(file, metadataPath, Paths.get(NORMALIZED_MATCHES_DIR));
                    normalizedMatchCount++;
                }
            }
        }
    }

    private enum VerificationResult {
        NONE,
        EXACT,
        FORMATTING_MATCH
    }

    private static VerificationResult verifyFile(Path filePath, Path metadataPath) {
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            String strippedContent = content.replaceAll("\\s+", "");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(metadataPath.toFile());

            List<String> copyrights =
                    extractTextArray(root.path("copyrights"));

            boolean allExactMatch = true;

            for (String text : copyrights) {
                if (content.contains(text)) {
                    continue;
                }

                String strippedText = text.replaceAll("\\s+", "");

                if (strippedContent.contains(strippedText)) {
                    allExactMatch = false;
                } else {
                    return VerificationResult.NONE;
                }
            }

            return allExactMatch ? VerificationResult.EXACT : VerificationResult.FORMATTING_MATCH;

        } catch (IOException e) {
            logger.error("Error verifying {}: {}", filePath.getFileName(), e.getMessage());
            return VerificationResult.NONE;
        }
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

