import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public class ContainsMultipleCopyrights {

    private static final Logger logger = LoggerFactory.getLogger(ContainsMultipleCopyrights.class);

    private static final String DATA_BASE_DIR = "/Volumes/Data 1";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/extracted/exact_matches/_total";
    private static final String OUTPUT_MULTIPLE = DATA_BASE_DIR + "exact_matches_with_multiple_copyrights/_total";
    private static final String OUTPUT_SINGLE = DATA_BASE_DIR + "exact_matches_with_single_copyright/_total";

    private static int totalFileCount = 0;
    private static int multipleCopyrightsCount = 0;
    private static int singleCopyrightsCount = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get(INPUT_DIR);
        Path multiplePath = Paths.get(OUTPUT_MULTIPLE);
        Path singlePath = Paths.get(OUTPUT_SINGLE);

        prepareOutputDirectory(multiplePath);
        prepareOutputDirectory(singlePath);

        processFiles(inputPath, multiplePath, singlePath);

        logger.info("Processing complete.");
        logger.info("Total original files processed: {}", totalFileCount);
        logger.info("Files with multiple copyrights: {}", multipleCopyrightsCount);
        logger.info("Files with one copyright: {}", singleCopyrightsCount);
    }

    private static void prepareOutputDirectory(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            Files.createDirectories(dir);
        } else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    Files.deleteIfExists(entry);
                }
            }
        }
    }

    private static void processFiles(Path inputDir, Path multipleDir, Path singleOrNoneDir) throws IOException {
        if (!Files.exists(inputDir) || !Files.isDirectory(inputDir)) {
            logger.error("Input directory does not exist or is not a directory: {}", inputDir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir)) {
            for (Path file : stream) {
                if (!Files.isRegularFile(file)) continue;

                String fileName = file.getFileName().toString();
                if (fileName.endsWith("_scancode.json")) continue;

                totalFileCount++;

                String baseName = getBaseFileName(fileName);
                Path metadataFile = inputDir.resolve(baseName + "_scancode.json");

                if (!Files.exists(metadataFile)) {
                    logger.warn("Missing metadata file for: {}", fileName);
                    continue;
                }

                boolean hasMultiple = hasMultipleCopyrights(metadataFile);
                Path targetDir = hasMultiple ? multipleDir : singleOrNoneDir;

                Files.copy(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(metadataFile, targetDir.resolve(metadataFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);

                if (hasMultiple) {
                    multipleCopyrightsCount++;
                } else {
                    singleCopyrightsCount++;
                }

                logger.info("Copied '{}' and metadata to '{}'", fileName, targetDir.getFileName());
            }
        }
    }

    private static boolean hasMultipleCopyrights(Path metadataPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(metadataPath.toFile());

            JsonNode copyrights = root.path("copyrights");
            return copyrights.isArray() && copyrights.size() > 1;

        } catch (IOException e) {
            logger.error("Failed to read metadata file '{}': {}", metadataPath.getFileName(), e.getMessage());
            return false;
        }
    }

    private static String getBaseFileName(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot > 0) ? fileName.substring(0, dot) : fileName;
    }
}