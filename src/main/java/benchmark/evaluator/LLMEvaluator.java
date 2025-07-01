package benchmark.evaluator;

import benchmark.model.CopyrightInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LLMEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(LLMEvaluator.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();
    private static final double SIMILARITY_THRESHOLD = 0.95; // Adjust as needed for non-exact matches

    /**
     * Parses a JSON string into a CopyrightInfo object.
     * @param jsonString The JSON string from the LLM's response.
     * @return A CopyrightInfo object, or null if parsing fails.
     */
    public static CopyrightInfo parseJsonOutput(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, CopyrightInfo.class);
        } catch (IOException e) {
            logger.error("Failed to parse JSON output: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Evaluates the predicted CopyrightInfo against the golden standard.
     * @param golden The ground truth CopyrightInfo.
     * @param predicted The LLM's predicted CopyrightInfo.
     * @return An EvaluationMetrics object containing F1 scores and exact match counts.
     */
    public static EvaluationMetrics evaluate(CopyrightInfo golden, CopyrightInfo predicted) {
        // Handle cases where golden or predicted data is null (e.g., parsing failed for predicted)
        if (golden == null) {
            // If golden is null, we can't evaluate against a truth.
            // Return 0 for all F1s, and totalGoldenCopyrights as 0.
            return new EvaluationMetrics(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        if (predicted == null) {
            // If predicted is null (e.g., invalid JSON), all counts are 0, and F1s are 0.
            return new EvaluationMetrics(0, 0, 0, 0, 0, 0, 0, 0,
                    golden.getCopyrights().size(), // Still provide golden count
                    0, 0, 0, 0, 0, 0);
        }

        // Evaluate Copyrights (with exact match preference)
        Metrics copyrightMetrics = calculateMetricsForList(golden.getCopyrights(), predicted.getCopyrights(), SIMILARITY_THRESHOLD, true);
        // Evaluate Holders
        Metrics holderMetrics = calculateMetricsForList(golden.getHolders(), predicted.getHolders(), SIMILARITY_THRESHOLD, false);
        // Evaluate Authors
        Metrics authorMetrics = calculateMetricsForList(golden.getAuthors(), predicted.getAuthors(), SIMILARITY_THRESHOLD, false);

        double copyrightF1 = calculateF1(copyrightMetrics.tp, copyrightMetrics.fp, copyrightMetrics.fn);
        double holderF1 = calculateF1(holderMetrics.tp, holderMetrics.fp, holderMetrics.fn);
        double authorF1 = calculateF1(authorMetrics.tp, authorMetrics.fp, authorMetrics.fn);

        // Overall F1 (macro average)
        double overallF1 = (copyrightF1 + holderF1 + authorF1) / 3.0;

        return new EvaluationMetrics(copyrightF1, holderF1, authorF1, overallF1,
                copyrightMetrics.tp, copyrightMetrics.fp, copyrightMetrics.fn, copyrightMetrics.exactMatches,
                golden.getCopyrights().size(), // Total golden copyrights for this file
                holderMetrics.tp, holderMetrics.fp, holderMetrics.fn,
                authorMetrics.tp, authorMetrics.fp, authorMetrics.fn
        );
    }

    /**
     * Calculates TP, FP, FN, and exact matches for a given list type (e.g., copyrights, holders).
     * This method ensures that each predicted item is matched against a golden item only once.
     * @param goldenList The list of ground truth strings.
     * @param predictedList The list of predicted strings by the LLM.
     * @param threshold The similarity threshold for considering a match.
     * @param exactMatchForCopyright If true, prioritizes exact string matching for copyrights.
     * @return A Metrics object containing TP, FP, FN, and exactMatches counts.
     */
    private static Metrics calculateMetricsForList(List<String> goldenList, List<String> predictedList, double threshold, boolean exactMatchForCopyright) {
        int tp = 0;
        int fp = 0;
        int fn = 0;
        int exactMatches = 0;

        // Create a mutable copy of predicted items to mark them as consumed
        List<String> remainingPredicted = new ArrayList<>(predictedList);

        for (String goldenItem : goldenList) {
            boolean foundMatch = false;
            double bestSimilarity = -1.0; // Track the best similarity for non-exact matches
            int bestMatchIndex = -1;    // Index of the best matching predicted item

            // 1. Prioritize exact matches if applicable (for copyrights)
            if (exactMatchForCopyright) {
                for (int j = 0; j < remainingPredicted.size(); j++) {
                    String predictedItem = remainingPredicted.get(j);
                    if (goldenItem.equals(predictedItem)) {
                        exactMatches++; // Count this as an exact match
                        tp++;           // Also counts as a true positive
                        remainingPredicted.remove(j); // Consume the predicted item
                        foundMatch = true;
                        break; // Move to the next golden item
                    }
                }
                if (foundMatch) {
                    continue; // Skip to the next golden item if an exact match was found
                }
            }

            // 2. If no exact match (for copyrights) or for holders/authors, proceed with similarity matching
            // This block only runs if a perfect match wasn't found (for exactMatchForCopyright)
            // or if exactMatchForCopyright is false (for holders/authors).
            for (int j = 0; j < remainingPredicted.size(); j++) {
                String predictedItem = remainingPredicted.get(j);
                double similarity;

                if (exactMatchForCopyright) { // For copyrights, use Jaro-Winkler for near matches
                    similarity = jaroWinkler.apply(goldenItem, predictedItem);
                } else { // For holders/authors, use Jaro-Winkler with normalization
                    similarity = jaroWinkler.apply(normalizeName(goldenItem), normalizeName(predictedItem));
                }

                if (similarity >= threshold) {
                    if (similarity > bestSimilarity) { // Find the most similar match
                        bestSimilarity = similarity;
                        bestMatchIndex = j;
                        foundMatch = true; // A potential match was found
                    }
                }
            }

            if (foundMatch) {
                tp++; // Count as a true positive based on similarity
                remainingPredicted.remove(bestMatchIndex); // Consume the predicted item
            } else {
                fn++; // Golden item not found in prediction (neither exact nor similar)
            }
        }

        // Any predicted items left are false positives
        fp = remainingPredicted.size();

        logger.debug("TP: {}, FP: {}, FN: {}, Exact Matches: {}", tp, fp, fn, exactMatches);
        return new Metrics(tp, fp, fn, exactMatches);
    }

    /**
     * Normalizes a string for similarity comparison (used for holders/authors).
     * @param name The input string.
     * @return The normalized string (trimmed and lowercased).
     */
    private static String normalizeName(String name) {
        return name.trim().toLowerCase();
    }

    /**
     * Calculates the F1 score based on True Positives, False Positives, and False Negatives.
     * @param tp True Positives.
     * @param fp False Positives.
     * @param fn False Negatives.
     * @return The F1 score. Returns 1.0 if all inputs are 0 (perfect match when nothing should be extracted).
     */
    private static double calculateF1(int tp, int fp, int fn) {
        if (tp == 0 && fp == 0 && fn == 0) {
            return 1.0; // Perfect match if both golden and predicted lists are empty
        }
        double precision = (tp == 0 && fp == 0) ? 1.0 : (double) tp / (tp + fp);
        double recall = (tp == 0 && fn == 0) ? 1.0 : (double) tp / (tp + fn);

        if (precision + recall == 0) {
            return 0.0;
        }
        return 2 * (precision * recall) / (precision + recall);
    }

    /**
     * Internal helper class to return multiple metric counts from calculateMetricsForList.
     */
    private static class Metrics {
        int tp;
        int fp;
        int fn;
        int exactMatches;

        Metrics(int tp, int fp, int fn, int exactMatches) {
            this.tp = tp;
            this.fp = fp;
            this.fn = fn;
            this.exactMatches = exactMatches;
        }
    }

    /**
     * Data class to hold evaluation metrics for a single file.
     */
    public static class EvaluationMetrics {
        public final double copyrightF1;
        public final double holderF1;
        public final double authorF1;
        public final double overallF1;

        public final int copyrightTP, copyrightFP, copyrightFN, copyrightExactMatches;
        public final int totalGoldenCopyrights; // Total golden copyrights for this file

        public final int holderTP, holderFP, holderFN;
        public final int authorTP, authorFP, authorFN;


        public EvaluationMetrics(double copyrightF1, double holderF1, double authorF1, double overallF1,
                                 int copyrightTP, int copyrightFP, int copyrightFN, int copyrightExactMatches,
                                 int totalGoldenCopyrights,
                                 int holderTP, int holderFP, int holderFN,
                                 int authorTP, int authorFP, int authorFN) {
            this.copyrightF1 = copyrightF1;
            this.holderF1 = holderF1;
            this.authorF1 = authorF1;
            this.overallF1 = overallF1;
            this.copyrightTP = copyrightTP;
            this.copyrightFP = copyrightFP;
            this.copyrightFN = copyrightFN;
            this.copyrightExactMatches = copyrightExactMatches;
            this.totalGoldenCopyrights = totalGoldenCopyrights;
            this.holderTP = holderTP;
            this.holderFP = holderFP;
            this.holderFN = holderFN;
            this.authorTP = authorTP;
            this.authorFP = authorFP;
            this.authorFN = authorFN;
        }
    }
}