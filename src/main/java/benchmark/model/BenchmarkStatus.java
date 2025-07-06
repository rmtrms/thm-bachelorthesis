package benchmark.model;

/**
 * Represents the final outcome of a single benchmark trial.
 */
public enum BenchmarkStatus {
    /**
     * The trial completed successfully and produced a result.
     */
    SUCCESS,

    /**
     * The trial was terminated because it exceeded the predefined time limit.
     */
    TIMEOUT,

    /**
     * The trial failed due to an exception (e.g., network error, processing error).
     */
    ERROR
}