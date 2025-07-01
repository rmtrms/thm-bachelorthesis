import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public class ContainsAuthors {

    private static final Logger logger = LoggerFactory.getLogger(ContainsAuthors.class);

    private static final String DATA_BASE_DIR = "/Volumes/Data";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/extracted/lowercase_matches/restored_case/exact_matches/exact_matches_with_single_copyright/_total";
    private static final String OUTPUT_WITH_AUTHORS = "/Users/rtueremis/Metaeffekt/tmp" + "/exact_matches_with_single_copyright_with_authors/_total";
    private static final String OUTPUT_NO_AUTHORS = "/Users/rtueremis/Metaeffekt/tmp" + "/exact_matches_with_single_copyright_without_authors/_total";

    private static int totalFileCount = 0;
    private static int withAuthorsCount = 0;
    private static int noAuthorsCount = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get(INPUT_DIR);
        Path withAuthorsPath = Paths.get(OUTPUT_WITH_AUTHORS);
        Path noAuthorsPath = Paths.get(OUTPUT_NO_AUTHORS);

        prepareOutputDirectory(withAuthorsPath);
        prepareOutputDirectory(noAuthorsPath);

        processFiles(inputPath, withAuthorsPath, noAuthorsPath);

        logger.info("Processing complete.");
        logger.info("Total original files processed: {}", totalFileCount);
        logger.info("Files with authors: {}", withAuthorsCount);
        logger.info("Files without authors: {}", noAuthorsCount);
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

    private static void processFiles(Path inputDir, Path withAuthorsDir, Path noAuthorsDir) throws IOException {
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

                boolean hasAuthors = containsAuthors(metadataFile);
                Path targetDir = hasAuthors ? withAuthorsDir : noAuthorsDir;

                Files.copy(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(metadataFile, targetDir.resolve(metadataFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);

                if (hasAuthors) {
                    withAuthorsCount++;
                } else {
                    noAuthorsCount++;
                }

                logger.info("Copied '{}' and metadata to '{}'", fileName, targetDir.getFileName());
            }
        }
    }

    private static boolean containsAuthors(Path metadataPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(metadataPath.toFile());

            JsonNode authors = root.path("authors");
            return authors.isArray() && !authors.isEmpty();

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
