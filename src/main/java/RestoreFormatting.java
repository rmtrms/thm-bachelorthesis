import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;

public class RestoreFormatting {

    private static final Logger logger = LoggerFactory.getLogger(RestoreFormatting.class);

    private static final String DATA_BASE_DIR = "/Volumes/Data 1";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/extracted/normalized_matches/normalized_matches_with_single-copyrights";
    private static final String OUTPUT_DIR = "/Users/romeo/Downloads/tmp/normalized_matches_format_restored";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static int processedFileCount = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get(INPUT_DIR);
        Path outputPath = Paths.get(OUTPUT_DIR);

        prepareOutputDirectory(outputPath);
        processFiles(inputPath, outputPath);

        logger.info("Finished processing all files.");
        logger.info("Amount of files processed: {}", processedFileCount);
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

    private static void processFiles(Path inputDir, Path outputDir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir)) {
            for (Path file : stream) {
                if (!Files.isRegularFile(file)) continue;

                String fileName = file.getFileName().toString();
                if (fileName.endsWith("_scancode.json")) continue;

                String baseName = getBaseFileName(fileName);
                Path metadataFile = inputDir.resolve(baseName + "_scancode.json");

                if (!Files.exists(metadataFile)) {
                    logger.warn("Missing metadata file for: {}", fileName);
                    continue;
                }

                processedFileCount++;

                String fileContent = Files.readString(file, StandardCharsets.UTF_8);
                JsonNode json = mapper.readTree(metadataFile.toFile());

                ArrayNode copyrights = (ArrayNode) json.path("copyrights");
                if (copyrights.isMissingNode() || copyrights.isEmpty()) {
                    continue;
                }

                boolean updated = false;
                for (int i = 0; i < copyrights.size(); i++) {
                    String normalized = copyrights.get(i).asText();
                    if (fileContent.contains(normalized)) continue;

                    String strippedFileContent = fileContent.replaceAll("\\s+", "");
                    String strippedNormalized = normalized.replaceAll("\\s+", "");

                    if (strippedFileContent.contains(strippedNormalized)) {
                        String[] words = normalized.trim().split("\\s+");
                        if (words.length < 2) continue;

                        Pattern pattern = Pattern.compile(
                                Pattern.quote(words[0]) + ".*?" + Pattern.quote(words[words.length - 1]),
                                Pattern.DOTALL
                        );
                        Matcher matcher = pattern.matcher(fileContent);
                        if (matcher.find()) {
                            String originalFormatted = matcher.group();
                            logger.info("Restoring formatting for file '{}'", fileName);
                            logger.info("  Before: {}", normalized);
                            logger.info("  After : {}", originalFormatted.trim());

                            copyrights.set(i, mapper.getNodeFactory().textNode(originalFormatted.trim()));
                            updated = true;
                        }
                    }
                }

                Path outDataFile = outputDir.resolve(file.getFileName());
                Path outJsonFile = outputDir.resolve(metadataFile.getFileName());

                Files.copy(file, outDataFile, StandardCopyOption.REPLACE_EXISTING);
                if (updated) {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(outJsonFile.toFile(), json);
                } else {
                    Files.copy(metadataFile, outJsonFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static String getBaseFileName(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot > 0) ? fileName.substring(0, dot) : fileName;
    }
}