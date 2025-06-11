import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ScanCodeExtractor {

    private static final Logger logger = LoggerFactory.getLogger(ScanCodeExtractor.class);

    private static final String ANALYSIS_DIR = "/Volumes/Data 1/analysis";
    private static final String OUTPUT_DIR = "/Users/romeo/metaeffekt/Repositories/thm-bachelorthesis/target/output";

    private static final boolean INCLUDE_PATH_IN_JSON = false;
    private static final Pattern SEGMENT_FILE_PATTERN = Pattern.compile("segment-\\d+\\.txt");

    private static int copiedFileCount = 0;

    public static void main(String[] args) throws IOException {
        cleanOutputDirectory();
        Files.walk(Paths.get(ANALYSIS_DIR))
                .filter(path -> path.getFileName().toString().endsWith("_scancode.json"))
                .forEach(ScanCodeExtractor::processJsonFile);

        logger.info("Total files copied: {}", copiedFileCount);
    }

    private static void cleanOutputDirectory() throws IOException {
        Path outputPath = Paths.get(OUTPUT_DIR);
        if (Files.notExists(outputPath)) {
            Files.createDirectories(outputPath);
            return;
        }

        try (DirectoryStream<Path> entries = Files.newDirectoryStream(outputPath)) {
            for (Path entry : entries) {
                Files.deleteIfExists(entry);
            }
        }

        logger.debug("Cleaned output directory: {}", OUTPUT_DIR);
    }

    private static void processJsonFile(Path jsonPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonPath.toFile());

            JsonNode files = root.path("files");
            if (!files.isArray()) return;

            Path analysisDir = jsonPath.getParent().getParent(); // parent of "-analysis"

            for (JsonNode fileNode : files) {
                String relPath = fileNode.path("path").asText();
                String fileName = fileNode.path("name").asText();

                Path fullPath;
                boolean isSegment = SEGMENT_FILE_PATTERN.matcher(fileName).matches();

                if (isSegment) {
                    // Remove segment-X.txt
                    int segmentIndex = relPath.lastIndexOf("/segment-");
                    if (segmentIndex == -1) continue;

                    String originalPath = relPath.substring(0, segmentIndex);

                    // Remove '-intermediate' from the first occurrence
                    originalPath = originalPath.replaceFirst("-intermediate", "");

                    fullPath = analysisDir.resolve(originalPath).normalize();
                } else {
                    fullPath = analysisDir.resolve(relPath).normalize();
                }

                if (!Files.exists(fullPath) || !Files.isRegularFile(fullPath)) {
                    logger.warn("File not found or not a regular file: {}", fullPath);
                    continue;
                }

                JsonNode copyrights = fileNode.path("copyrights");
                JsonNode holders = fileNode.path("holders");
                JsonNode authors = fileNode.path("authors");

                if (copyrights.isEmpty() && holders.isEmpty() && authors.isEmpty()) {
                    continue;
                }

                byte[] content = Files.readAllBytes(fullPath);
                String sha1 = getSha1(content);

                // Preserve file extension from actual file being copied
                String actualFileName = fullPath.getFileName().toString();
                String extension = "";
                int i = actualFileName.lastIndexOf('.');
                if (i > 0 && i < actualFileName.length() - 1) {
                    extension = actualFileName.substring(i);
                }

                Path outputFile = Paths.get(OUTPUT_DIR, sha1 + extension);
                Files.write(outputFile, content);

                Map<String, Object> metadata = new HashMap<>();
                if (INCLUDE_PATH_IN_JSON) {
                    metadata.put("path", isSegment ? fullPath.toString() : relPath);
                }
                metadata.put("copyrights", extractTextArray(copyrights, "copyright"));
                metadata.put("holders", extractTextArray(holders, "holder"));
                metadata.put("authors", extractTextArray(authors, "author"));

                Path metadataFile = Paths.get(OUTPUT_DIR, sha1 + "_scancode.json");
                mapper.writerWithDefaultPrettyPrinter().writeValue(metadataFile.toFile(), metadata);

                copiedFileCount++;
            }
        } catch (Exception e) {
            logger.error("Error processing {}: {}", jsonPath, e.getMessage());
        }
    }

    private static List<String> extractTextArray(JsonNode arrayNode, String key) {
        if (arrayNode == null || !arrayNode.isArray()) return Collections.emptyList();

        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(node -> node.path(key).asText())
                .filter(text -> !text.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private static String getSha1(byte[] content) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(content);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}