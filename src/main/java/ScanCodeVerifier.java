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

        private static final String INPUT_DIR = "/Users/rtueremis/Documents/output";
        private static final String VERIFIED_DIR = "/Users/rtueremis/Documents/verified";

        private static int verifiedFileCount = 0;

        public static void main(String[] args) throws IOException {
            prepareOutputDirectory(Paths.get(VERIFIED_DIR));
            verifyFilesInDirectory(Paths.get(INPUT_DIR), Paths.get(VERIFIED_DIR));
            logger.info("Total files verified and copied: {}", verifiedFileCount);
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

        private static void verifyFilesInDirectory(Path inputDir, Path outputDir) throws IOException {
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

                    if (verifyFile(file, metadataPath)) {
                        Files.copy(file, outputDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(metadataPath, outputDir.resolve(metadataPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        verifiedFileCount++;
                    }
                }
            }
        }

        private static boolean verifyFile(Path filePath, Path metadataPath) {
            try {
                String content = Files.readString(filePath, StandardCharsets.UTF_8);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(metadataPath.toFile());

                List<String> copyrights =
                        extractTextArray(root.path("copyrights"));

                for (String text : copyrights) {
                    if (!content.contains(text)) {
                        logger.warn("Text not found in {}: {}", filePath.getFileName(), text);
                        return false;
                    }
                }

                logger.debug("Verified file: {}", filePath.getFileName());
                return true;

            } catch (IOException e) {
                logger.error("Error verifying {}: {}", filePath.getFileName(), e.getMessage());
                return false;
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

