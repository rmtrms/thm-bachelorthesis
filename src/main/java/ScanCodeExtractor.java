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

    private static final String DATA_BASE_DIR = "/Volumes/Data 1";
    private static final String INPUT_DIR = DATA_BASE_DIR + "/analysis";
    private static final String OUTPUT_DIR = "/Users/romeo/Downloads/tmp/extracted";

    private static final boolean INCLUDE_PATH_IN_JSON = false;
    private static final Pattern SEGMENT_FILE_PATTERN = Pattern.compile("segment-\\d+\\.txt");

    private static int copiedFileCount = 0;
    private static int duplicatesCount = 0;
    private static int noCopyrightsCount = 0;

    private static final Set<String> seenSha1s = new HashSet<>();

    public static void main(String[] args) throws IOException {
        cleanOutputDirectory(Paths.get(OUTPUT_DIR));
        Files.walk(Paths.get(INPUT_DIR))
                .filter(path -> path.getFileName().toString().endsWith("_scancode.json"))
                .forEach(ScanCodeExtractor::processJsonFile);

        logger.info("Total files copied (including duplicates): {}", copiedFileCount);
        logger.info("Total duplicate files (same SHA-1): {}", duplicatesCount);
        logger.info("Total files with no copyrights, holders or authors: {}",
                    noCopyrightsCount);
    }

    private static void cleanOutputDirectory(Path outputPath) throws IOException {
        if (Files.notExists(outputPath)) {
            Files.createDirectories(outputPath);
            return;
        }

        try (DirectoryStream<Path> entries = Files.newDirectoryStream(outputPath)) {
            for (Path entry : entries) {
                Files.deleteIfExists(entry);
            }
        }

        logger.debug("Cleaned output directory: {}", outputPath);
    }

    private static void processJsonFile(Path jsonPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonPath.toFile());

            JsonNode files = root.path("files");
            if (!files.isArray()) return;

            Path analysisDir = jsonPath.getParent().getParent();
            Map<Path, Map<String, Set<String>>> fileAggregation = new HashMap<>();

            for (JsonNode fileNode : files) {
                String relPath = fileNode.path("path").asText();
                String fileName = fileNode.path("name").asText();

                Path fullPath;
                boolean isSegment = SEGMENT_FILE_PATTERN.matcher(fileName).matches();

                if (isSegment) {
                    int segmentIndex = relPath.lastIndexOf("/segment-");
                    if (segmentIndex == -1) continue;

                    String originalPath = relPath.substring(0, segmentIndex);
                    originalPath = originalPath.replaceFirst("-intermediate", "");
                    fullPath = analysisDir.resolve(originalPath).normalize();
                } else {
                    fullPath = analysisDir.resolve(relPath).normalize();
                }

                if (!Files.exists(fullPath) || !Files.isRegularFile(fullPath)) {
                    logger.warn("File not found or not a regular file: {}", fullPath);
                    continue;
                }

                List<String> copyrights = extractTextArray(fileNode.path("copyrights"), "copyright");
                List<String> holders = extractTextArray(fileNode.path("holders"), "holder");
                List<String> authors = extractTextArray(fileNode.path("authors"), "author");

                if (copyrights.isEmpty() && holders.isEmpty() && authors.isEmpty()) {
                    noCopyrightsCount++;
                    continue;
                }

                fileAggregation.putIfAbsent(fullPath, new HashMap<>());
                Map<String, Set<String>> metadata = fileAggregation.get(fullPath);
                metadata.putIfAbsent("copyrights", new HashSet<>());
                metadata.putIfAbsent("holders", new HashSet<>());
                metadata.putIfAbsent("authors", new HashSet<>());

                copyrights.forEach(metadata.get("copyrights")::add);
                holders.forEach(metadata.get("holders")::add);
                authors.forEach(metadata.get("authors")::add);
            }

            for (Map.Entry<Path, Map<String, Set<String>>> entry : fileAggregation.entrySet()) {
                Path fullPath = entry.getKey();
                Map<String, Set<String>> metadataMap = entry.getValue();

                byte[] content = Files.readAllBytes(fullPath);
                String sha1 = getSha1(content);

                String actualFileName = fullPath.getFileName().toString();
                String extension = "";
                int i = actualFileName.lastIndexOf('.');
                if (i > 0 && i < actualFileName.length() - 1) {
                    extension = actualFileName.substring(i);
                }

                if (!seenSha1s.add(sha1)) {
                    duplicatesCount++;
                }

                Path outputFile = Paths.get(OUTPUT_DIR, sha1 + extension);
                Files.write(outputFile, content);

                Map<String, Object> metadataOut = new HashMap<>();
                if (INCLUDE_PATH_IN_JSON) {
                    metadataOut.put("path", fullPath.toString());
                }
                metadataOut.put("copyrights", new ArrayList<>(metadataMap.get("copyrights")));
                metadataOut.put("holders", new ArrayList<>(metadataMap.get("holders")));
                metadataOut.put("authors", new ArrayList<>(metadataMap.get("authors")));

                Path metadataFile = Paths.get(OUTPUT_DIR, sha1 + "_scancode.json");
                mapper.writerWithDefaultPrettyPrinter().writeValue(metadataFile.toFile(), metadataOut);

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