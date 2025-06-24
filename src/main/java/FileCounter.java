import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class FileCounter {

    private static final Logger logger = LoggerFactory.getLogger(FileCounter.class);

    private static final String INPUT_DIR = "/Volumes/Data 1/extracted/extracted";

    public static void main(String[] args) {
        Path dirPath = Paths.get(INPUT_DIR);

        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            logger.error("Invalid directory: {}", dirPath);
            return;
        }

        try (Stream<Path> files = Files.walk(dirPath)) {
            long count = files
                    .filter(Files::isRegularFile)
                    .filter(p -> !p.getFileName().toString().endsWith("_scancode.json"))
                    .count();

            logger.info("Number of files in '{}': {}", dirPath, count);
        } catch (IOException e) {
            logger.error("Error reading directory '{}': {}", dirPath, e.getMessage());
        }
    }
}