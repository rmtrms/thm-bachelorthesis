import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MoveIfNoCopyright {

    // === Configuration ===
    private static final String INPUT_DIR = "/Volumes/Data/extracted/exact_matches/exact_matches_with_single_copyright/exact_matches_with_single_copyright_with_authors/_total";
    private static final String OUTPUT_DIR = "/Volumes/Data/extracted/exact_matches/exact_matches_with_single_copyright/exact_matches_with_single_copyright_with_authors/empty_copyrights";

    public static void main(String[] args) throws IOException {
        Path inputDir = Paths.get(INPUT_DIR);
        Path outputDir = Paths.get(OUTPUT_DIR);

        if (!Files.isDirectory(inputDir)) {
            System.err.println("Input directory does not exist.");
            return;
        }

        Files.createDirectories(outputDir);
        ObjectMapper mapper = new ObjectMapper();

        // Filter original files (exclude *_scancode.json)
        List<Path> originalFiles = Files.list(inputDir)
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> !p.getFileName().toString().endsWith("_scancode.json"))
                .collect(Collectors.toList());

        int movedCount = 0;

        for (Path original : originalFiles) {
            String baseName = original.getFileName().toString();
            int dotIndex = baseName.lastIndexOf('.');
            String sha = (dotIndex == -1) ? baseName : baseName.substring(0, dotIndex);
            Path scanJson = inputDir.resolve(sha + "_scancode.json");

            if (!Files.exists(scanJson)) continue;

            // Read and check JSON
            try (InputStream in = Files.newInputStream(scanJson)) {
                JsonNode root = mapper.readTree(in);
                JsonNode copyrights = root.get("copyrights");

                if (copyrights != null && copyrights.isArray() && copyrights.size() == 0) {
                    // Move original file
                    Files.move(original, outputDir.resolve(original.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    // Move _scancode.json file
                    Files.move(scanJson, outputDir.resolve(scanJson.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    movedCount++;
                }
            } catch (IOException e) {
                System.err.println("Error processing file: " + scanJson.getFileName());
                e.printStackTrace();
            }
        }

        System.out.println("Moved " + movedCount + " original files and their corresponding _scancode.json files.");
    }
}