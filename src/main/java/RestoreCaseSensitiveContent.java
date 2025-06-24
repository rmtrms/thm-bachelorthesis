import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.Collectors;

public class RestoreCaseSensitiveContent {

    private static final Logger logger = LoggerFactory.getLogger(RestoreCaseSensitiveContent.class);

    private static final String DATA_BASE_DIR = "/Volumes/Data 1/extracted";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/lowercase_matches/_total";
    private static final String RESTORED_CASE_DIR = DATA_BASE_DIR + "/lowercase_matches/restored_case";

    private static int processedFiles = 0;
    private static int modifiedJsons = 0;

    public static void main(String[] args) throws IOException {
        prepareOutputDirectory(Paths.get(RESTORED_CASE_DIR));

        Map<String, Path> originalFileMap = Files.list(Paths.get(INPUT_DIR))
            .filter(Files::isRegularFile)
            .filter(path -> !path.getFileName().toString().endsWith("_scancode.json"))
            .collect(Collectors.toMap(
                path -> getBaseFileName(path.getFileName().toString()),
                path -> path,
                (p1, p2) -> p1
            ));

        restoreCaseSensitiveCopyrights(Paths.get(INPUT_DIR), originalFileMap);

        logger.info("Total files processed     : {}", processedFiles);
        logger.info("JSONs modified with case  : {}", modifiedJsons);
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

    private static void restoreCaseSensitiveCopyrights(Path inputDir, Map<String, Path> fileMap) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir)) {
            for (Path jsonPath : stream) {
                if (!jsonPath.getFileName().toString().endsWith("_scancode.json")) continue;

                String baseName = jsonPath.getFileName().toString().replace("_scancode.json", "");
                Path originalFile = fileMap.get(baseName);

                if (originalFile == null || !Files.exists(originalFile)) {
                    logger.warn("No original file found for {}", jsonPath);
                    continue;
                }

                byte[] fileBytes = Files.readAllBytes(originalFile);
                String encoding = detectEncoding(fileBytes);
                if (encoding == null) {
                    logger.warn("Encoding not detected for [{}]. Defaulting to UTF-8.", originalFile.getFileName());
                    encoding = StandardCharsets.UTF_8.name();
                }
                String fileContent = new String(fileBytes, encoding);
                String fileContentLower = fileContent.toLowerCase();

                JsonNode root = mapper.readTree(jsonPath.toFile());
                ArrayNode copyrights =
                        (ArrayNode) root.path("copyrights");

                boolean modified = false;
                for (int i = 0; i < copyrights.size(); i++) {
                    String originalJsonValue = copyrights.get(i).asText();
                    if (fileContent.contains(originalJsonValue)) {
                        continue;
                    }

                    String lowerJsonValue = originalJsonValue.toLowerCase();
                    int idx = fileContentLower.indexOf(lowerJsonValue);
                    if (idx != -1) {
                        String restored = fileContent.substring(idx, idx + originalJsonValue.length());
                        ((ArrayNode) copyrights).set(i, mapper.getNodeFactory().textNode(restored));
                        logger.info("Case restored in file {}: {} -> {}", originalFile.getFileName(), originalJsonValue, restored);
                        modified = true;
                    }
                }

                Path outJson = Paths.get(RESTORED_CASE_DIR, jsonPath.getFileName().toString());
                Path outOriginal = Paths.get(RESTORED_CASE_DIR, originalFile.getFileName().toString());

                mapper.writerWithDefaultPrettyPrinter().writeValue(outJson.toFile(), root);
                Files.copy(originalFile, outOriginal, StandardCopyOption.REPLACE_EXISTING);

                if (modified) {
                    modifiedJsons++;
                }

                processedFiles++;
            }
        }
    }

    private static String detectEncoding(byte[] bytes) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    private static String getBaseFileName(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot > 0) ? fileName.substring(0, dot) : fileName;
    }
}