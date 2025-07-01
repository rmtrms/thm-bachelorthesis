package benchmark.evaluator;

import benchmark.model.BenchmarkResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;

public class OverallBenchmarkResults {
    private static final Logger logger = LoggerFactory.getLogger(OverallBenchmarkResults.class);
    private final String modelName;
    private final List<BenchmarkResult> results;
    private final Map<String, List<BenchmarkResult>> resultsByCategory;

    public OverallBenchmarkResults(String modelName) {
        this.modelName = modelName;
        this.results = new ArrayList<>();
        this.resultsByCategory = new HashMap<>();
    }

    public void addResult(BenchmarkResult result) {
        results.add(result);
        resultsByCategory.computeIfAbsent(result.getCategory(), k -> new ArrayList<>()).add(result);
    }

    public double getAverageOverallF1PerFile() {
        return results.stream()
                .mapToDouble(BenchmarkResult::getOverallF1)
                .average()
                .orElse(0.0);
    }

    public double getAverageOverallF1AcrossCategories() {
        if (getCategoryMetrics().isEmpty()) {
            return 0.0;
        }
        return getCategoryMetrics().values().stream()
                .mapToDouble(metrics -> (Double) metrics.get("averageOverallF1"))
                .average()
                .orElse(0.0);
    }

    // --- NEW: Tokens/Sec Statistics (Overall) ---
    public double getAverageTokensPerSecond() {
        return results.stream()
                .filter(BenchmarkResult::isValidJson)
                .mapToDouble(BenchmarkResult::getTokensPerSecond)
                .average()
                .orElse(0.0);
    }

    public double getMinimumTokensPerSecond() {
        return results.stream()
                .filter(BenchmarkResult::isValidJson)
                .mapToDouble(BenchmarkResult::getTokensPerSecond)
                .min()
                .orElse(0.0);
    }

    public double getMaximumTokensPerSecond() {
        return results.stream()
                .filter(BenchmarkResult::isValidJson)
                .mapToDouble(BenchmarkResult::getTokensPerSecond)
                .max()
                .orElse(0.0);
    }

    // --- NEW: Prompt Tokens Statistics (Overall) ---
    public double getAveragePromptTokens() {
        return results.stream()
                .mapToInt(BenchmarkResult::getPromptTokens)
                .average()
                .orElse(0.0);
    }

    public int getMinimumPromptTokens() {
        return results.stream()
                .mapToInt(BenchmarkResult::getPromptTokens)
                .min()
                .orElse(0);
    }

    public int getMaximumPromptTokens() {
        return results.stream()
                .mapToInt(BenchmarkResult::getPromptTokens)
                .max()
                .orElse(0);
    }

    public double getAverageDurationMs() {
        return results.stream()
                .mapToLong(BenchmarkResult::getDurationMs)
                .average()
                .orElse(0.0);
    }

    public int getTotalGoldenCopyrightsOverall() {
        return results.stream()
                      .mapToInt(BenchmarkResult::getTotalGoldenCopyrights)
                      .sum();
    }

    /**
     * Calculates the total number of exact copyright matches found by the model across all files.
     *
     * @return Total exact copyright matches.
     */
    public int getTotalExactMatchesOverall() {
        return results.stream()
                      .mapToInt(BenchmarkResult::getCopyrightExactMatches)
                      .sum();
    }

    /**
     * Calculates the overall percentage of exact copyright matches for the entire dataset.
     *
     * @return Percentage of exact matches (0.0 - 100.0). Returns 100.0 if totalGoldenCopyrights is 0.
     */
    public double getPercentageExactMatchesOverall() {
        int totalGolden = getTotalGoldenCopyrightsOverall();
        int totalExact = getTotalExactMatchesOverall();
        if (totalGolden == 0) {
            return 100.0; // If no golden copyrights, it's 100% exact match
        }
        return (double) totalExact / totalGolden * 100.0;
    }

    /**
     * Calculates the total count of files where the final parsed JSON (after extraction) was invalid.
     * @return Count of invalid final JSONs.
     */
    public long getTotalInvalidJsonsOverall() {
        return results.stream()
                      .filter(r -> !r.isValidJson())
                      .count();
    }

    /**
     * Calculates the total count of files where the raw LLM response was initially invalid JSON,
     * but the extraction process (extractJson) made it parseable.
     * @return Count of beneficial JSON extractions.
     */
    public long getTotalJsonExtractionBeneficialOverall() {
        return results.stream()
                      .filter(BenchmarkResult::isJsonExtractionBeneficial)
                      .count();
    }

    /**
     * Aggregates metrics (F1s, exact match counts, percentages, and JSON validity counts) per category.
     * @return A map where keys are category names and values are maps of aggregated metrics.
     */
    public Map<String, Map<String, Object>> getCategoryMetrics() {
        Map<String, Map<String, Object>> categoryMetrics = new HashMap<>();
        resultsByCategory.forEach((category, categoryResults) -> {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("averageCopyrightF1", categoryResults.stream().mapToDouble(BenchmarkResult::getCopyrightF1).average().orElse(0.0));
            metrics.put("averageHolderF1", categoryResults.stream().mapToDouble(BenchmarkResult::getHolderF1).average().orElse(0.0));
            metrics.put("averageAuthorF1", categoryResults.stream().mapToDouble(BenchmarkResult::getAuthorF1).average().orElse(0.0));
            metrics.put("averageOverallF1", categoryResults.stream().mapToDouble(BenchmarkResult::getOverallF1).average().orElse(0.0));

            // Per-category exact match calculation
            int categoryTotalGolden = categoryResults.stream().mapToInt(BenchmarkResult::getTotalGoldenCopyrights).sum();
            int categoryTotalExactMatches = categoryResults.stream().mapToInt(BenchmarkResult::getCopyrightExactMatches).sum();
            double categoryPercentageExactMatches = (categoryTotalGolden == 0) ? 100.0 : (double) categoryTotalExactMatches / categoryTotalGolden * 100.0;

            metrics.put("totalGoldenCopyrights", categoryTotalGolden);
            metrics.put("totalExactMatches", categoryTotalExactMatches);
            metrics.put("percentageExactMatches", categoryPercentageExactMatches);

            // Per-category invalid JSON counts
            long categoryInvalidJsons = categoryResults.stream().filter(r -> !r.isValidJson()).count();
            long categoryJsonExtractionBeneficial = categoryResults.stream().filter(BenchmarkResult::isJsonExtractionBeneficial).count();

            metrics.put("invalidJsonsCount", categoryInvalidJsons);
            metrics.put("jsonExtractionBeneficialCount", categoryJsonExtractionBeneficial);

            // Tokens/Sec Statistics (Per Category)
            DoubleSummaryStatistics tokensPerSecondStats = categoryResults.stream()
                    .filter(BenchmarkResult::isValidJson) // Only count if JSON was valid
                    .mapToDouble(BenchmarkResult::getTokensPerSecond)
                    .summaryStatistics();
            metrics.put("averageTokensPerSecond", tokensPerSecondStats.getAverage());
            metrics.put("minimumTokensPerSecond", tokensPerSecondStats.getMin());
            metrics.put("maximumTokensPerSecond", tokensPerSecondStats.getMax());

            // Prompt Tokens Statistics (Per Category)
            IntSummaryStatistics promptTokensStats = categoryResults.stream()
                    .mapToInt(BenchmarkResult::getPromptTokens)
                    .summaryStatistics();
            metrics.put("averagePromptTokens", promptTokensStats.getAverage());
            metrics.put("minimumPromptTokens", promptTokensStats.getMin());
            metrics.put("maximumPromptTokens", promptTokensStats.getMax());


            categoryMetrics.put(category, metrics);
        });
        return categoryMetrics;
    }

    /**
     * Saves the overall benchmark results for the model to a JSON file.
     *
     * @param outputPath The directory where the results file should be saved.
     */
    public void saveResults(String outputPath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            Map<String, Object> finalReport = new HashMap<>();
            finalReport.put("modelName", modelName);
            finalReport.put("totalFilesProcessed", results.size());
            finalReport.put("averageOverallF1PerFile", getAverageOverallF1PerFile());
            finalReport.put("averageOverallF1AcrossCategories", getAverageOverallF1AcrossCategories());

            // Add Overall Statistics to Report
            finalReport.put("averageTokensPerSecondOverall", getAverageTokensPerSecond());
            finalReport.put("minimumTokensPerSecondOverall", getMinimumTokensPerSecond());
            finalReport.put("maximumTokensPerSecondOverall", getMaximumTokensPerSecond());

            finalReport.put("averagePromptTokensOverall", getAveragePromptTokens());
            finalReport.put("minimumPromptTokensOverall", getMinimumPromptTokens());
            finalReport.put("maximumPromptTokensOverall", getMaximumPromptTokens());

            finalReport.put("averageDurationMs", getAverageDurationMs()); // This is overall duration average

            finalReport.put("totalGoldenCopyrightsOverall", getTotalGoldenCopyrightsOverall());
            finalReport.put("totalExactMatchesOverall", getTotalExactMatchesOverall());
            finalReport.put("percentageExactMatchesOverall", getPercentageExactMatchesOverall());

            // Overall invalid JSON counts
            finalReport.put("totalInvalidJsonsOverall", getTotalInvalidJsonsOverall());
            finalReport.put("totalJsonExtractionBeneficialOverall", getTotalJsonExtractionBeneficialOverall());


            finalReport.put("categoryAggregatedMetrics", getCategoryMetrics());
            finalReport.put("individualFileResults", results);

            File outputFile = new File(outputPath, modelName + "_overall_benchmark_results.json");
            mapper.writeValue(outputFile, finalReport);
            logger.info("Saved overall benchmark results to: {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error saving overall benchmark results: {}", e.getMessage());
        }
    }

    // Getters for Jackson serialization
    public String getModelName() {
        return modelName;
    }

    public List<BenchmarkResult> getResults() {
        return results;
    }

    public Map<String, List<BenchmarkResult>> getResultsByCategory() {
        return resultsByCategory;
    }
}