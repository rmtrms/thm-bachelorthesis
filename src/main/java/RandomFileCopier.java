import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class RandomFileCopier {

    public static void main(String[] args) throws IOException {

        String inputDirPath = "/Volumes/Data/extracted/exact_matches/exact_matches_with_single_copyright/_total";
        String outputDirPath = "/Volumes/Data/benchmark/no_copyrights";
        int numberOfFilesToCopy = 25;

        Path inputDir = Paths.get(inputDirPath);
        Path outputDir = Paths.get(outputDirPath);

        if (!Files.isDirectory(inputDir)) {
            System.err.println("Input directory does not exist.");
            return;
        }

        Files.createDirectories(outputDir);

        // Filter original files (exclude *_scancode.json)
        List<Path> originalFiles = Files.list(inputDir)
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> !p.getFileName().toString().endsWith("_scancode.json"))
                .collect(Collectors.toList());

        if (numberOfFilesToCopy > originalFiles.size()) {
            System.err.println("Requested number of files exceeds available original files.");
            return;
        }

        // Pick n random files
        Collections.shuffle(originalFiles);
        List<Path> selectedFiles = originalFiles.subList(0, numberOfFilesToCopy);

        for (Path original : selectedFiles) {
            Path targetOriginal = outputDir.resolve(original.getFileName());
            Files.copy(original, targetOriginal, StandardCopyOption.REPLACE_EXISTING);

            // Try to copy corresponding _scancode.json
            String baseName = original.getFileName().toString();
            int dotIndex = baseName.lastIndexOf('.');
            String sha = (dotIndex == -1) ? baseName : baseName.substring(0, dotIndex);
            Path scanJson = inputDir.resolve(sha + "_scancode.json");

            if (Files.exists(scanJson)) {
                Path targetScan = outputDir.resolve(scanJson.getFileName());
                Files.copy(scanJson, targetScan, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        System.out.println("Copied " + numberOfFilesToCopy + " files and their corresponding _scancode.json files.");
    }
}