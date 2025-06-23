import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.StreamSupport;

public class MoveFilesWithoutCopyrights {

    private static final Logger logger = LoggerFactory.getLogger(MoveFilesWithoutCopyrights.class);

    private static final String DATA_BASE_DIR = "/Volumes/Data 1";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/extracted/extracted";
    private static final String OUTPUT_DIR = DATA_BASE_DIR + "/no_copyrights";

    private static int movedFileCount = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get(INPUT_DIR);
        Path outputPath = Paths.get(OUTPUT_DIR);

        prepareOutputDirectory(outputPath);
        processFiles(inputPath, outputPath);

        logger.info("Processing complete.");
        logger.info("Total original files moved: {}", movedFileCount);
    }

    private static void prepareOutputDirectory(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            Files.createDirectories(dir);
        }
    }

    private static void processFiles(Path inputDir, Path outputDir) throws IOException {
        if (!Files.exists(inputDir) || !Files.isDirectory(inputDir)) {
            logger.error("Input directory does not exist or is not a directory: {}", inputDir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*_scancode.json")) {
            for (Path metadataFile : stream) {
                if (!Files.isRegularFile(metadataFile)) continue;

                String baseName = getBaseFileBaseName(metadataFile.getFileName().toString());
                Path originalFile = inputDir.resolve(baseName);

                // Ensure both original file and metadata exist
                if (!Files.exists(originalFile)) {
                    logger.warn("Original file not found for metadata: {}", metadataFile.getFileName());
                    continue;
                }

                if (isCopyrightEmpty(metadataFile)) {
                    Path targetMetadata = outputDir.resolve(metadataFile.getFileName());
                    Path targetOriginal = outputDir.resolve(originalFile.getFileName());

                    Files.move(metadataFile, targetMetadata, StandardCopyOption.REPLACE_EXISTING);
                    Files.move(originalFile, targetOriginal, StandardCopyOption.REPLACE_EXISTING);

                    movedFileCount++;
                    logger.info("Moved '{}' and metadata to '{}'", originalFile.getFileName(), outputDir.getFileName());
                }
            }
        }
    }

    private static boolean isCopyrightEmpty(Path metadataPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(metadataPath.toFile());
            JsonNode copyrights = root.path("copyrights");

            return !copyrights.isArray() || StreamSupport.stream(copyrights.spliterator(), false)
                    .map(JsonNode::asText).allMatch(text -> text.trim().isEmpty());

        } catch (IOException e) {
            logger.error("Error reading metadata '{}': {}", metadataPath.getFileName(), e.getMessage());
            return false;
        }
    }

    private static String getBaseFileBaseName(String metadataFileName) {
        return metadataFileName.replace("_scancode.json", "");
    }
}