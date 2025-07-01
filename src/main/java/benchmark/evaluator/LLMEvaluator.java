package benchmark.evaluator;

import benchmark.model.CopyrightInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LLMEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(LLMEvaluator.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();
    private static final double SIMILARITY_THRESHOLD = 0.95; // Adjust as needed for non-exact matches

    /**
     * Parses a JSON string into a CopyrightInfo object. This method attempts to handle cases
     * where the LLM might return a single object or a list containing a single object.
     * @param jsonString The JSON string from the LLM's response.
     * @return A CopyrightInfo object, or null if parsing fails or list is empty.
     */
    public static CopyrightInfo parseJsonOutput(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            logger.warn("Received empty or null JSON string for parsing.");
            return null;
        }

        try {
            // Attempt 1: Try to parse as a single CopyrightInfo object (standard case: { ... })
            return objectMapper.readValue(jsonString, CopyrightInfo.class);
        } catch (JsonParseException | JsonMappingException e) {
            // If parsing as a single object fails (e.g., it's an array root), try as a List.
            try {
                // Attempt 2: Try to parse as a List of CopyrightInfo objects (case: [ { ... } ] )
                List<CopyrightInfo> list = objectMapper.readValue(jsonString, new TypeReference<List<CopyrightInfo>>() {});
                if (list != null && !list.isEmpty()) {
                    // If it's a list and not empty, take the first object.
                    // IMPORTANT: If you expect an array of *multiple* objects to be merged,
                    // this logic needs to be expanded to iterate and combine them.
                    return list.get(0);
                } else {
                    logger.warn("Parsed JSON as an empty list or null list for: {}", jsonString);
                    return null; // Return null if it's an empty list
                }
            } catch (IOException innerE) {
                // If parsing as a list also fails
                logger.error("Failed to parse JSON output as single object or list: {}", innerE.getMessage());
                return null;
            }
        } catch (IOException e) {
            // Catch other general IOExceptions during readValue
            logger.error("General IOException during JSON parsing: {}", e.getMessage());
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
            return new EvaluationMetrics(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        if (predicted == null) {
            // If predicted is null (e.g., invalid JSON), all counts are 0, and F1s are 0.
            return new EvaluationMetrics(0, 0, 0, 0, 0, 0, 0, 0,
                                         golden.getCopyrights().size(),
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

            if (!foundMatch) {
                for (int j = 0; j < remainingPredicted.size(); j++) {
                    String predictedItem = remainingPredicted.get(j);
                    double similarity;

                    if (exactMatchForCopyright) {
                        similarity = jaroWinkler.apply(goldenItem, predictedItem);
                    } else {
                        similarity = jaroWinkler.apply(normalizeName(goldenItem), normalizeName(predictedItem));
                    }

                    if (similarity >= threshold) {
                        if (similarity > bestSimilarity) {
                            bestSimilarity = similarity;
                            bestMatchIndex = j;
                            foundMatch = true;
                        }
                    }
                }

                if (foundMatch) {
                    tp++;
                    remainingPredicted.remove(bestMatchIndex);
                } else {
                    fn++;
                }
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