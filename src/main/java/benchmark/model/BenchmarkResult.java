package benchmark.model;

public class BenchmarkResult {
    private final String sha1;
    private final String category;
    private final long durationMs;
    private final int evalTokens; // Output tokens from LLM
    private final int promptTokens; // Input tokens to LLM
    private final double tokensPerSecond;
    private final double copyrightF1;
    private final double holderF1;
    private final double authorF1;
    private final double overallF1; // e.g., macro average of the three F1s
    private final int copyrightExactMatches;
    private final int totalGoldenCopyrights; // Total golden copyrights for this file
    private final boolean validJson; // Is final parsed JSON valid?
    private final boolean wasRawResponseValidJson; // Was the raw LLM response valid JSON?
    private final boolean jsonExtractionBeneficial; // Did extractJson change invalid to valid?
    private final String errorMessage; // If JSON parsing failed or other error
    private final BenchmarkStatus status; // The final outcome of the trial

    public BenchmarkResult(String sha1, String category, long durationMs, int promptTokens, int evalTokens,
                           double tokensPerSecond, double copyrightF1, double holderF1, double authorF1, double overallF1,
                           int copyrightExactMatches, int totalGoldenCopyrights,
                           boolean validJson, boolean wasRawResponseValidJson, boolean jsonExtractionBeneficial,
                           String errorMessage, BenchmarkStatus status) {
        this.sha1 = sha1;
        this.category = category;
        this.durationMs = durationMs;
        this.promptTokens = promptTokens;
        this.evalTokens = evalTokens;
        this.tokensPerSecond = tokensPerSecond;
        this.copyrightF1 = copyrightF1;
        this.holderF1 = holderF1;
        this.authorF1 = authorF1;
        this.overallF1 = overallF1;
        this.copyrightExactMatches = copyrightExactMatches;
        this.totalGoldenCopyrights = totalGoldenCopyrights;
        this.validJson = validJson;
        this.wasRawResponseValidJson = wasRawResponseValidJson;
        this.jsonExtractionBeneficial = jsonExtractionBeneficial;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public String getSha1() { return sha1; }
    public String getCategory() { return category; }
    public long getDurationMs() { return durationMs; }
    public int getEvalTokens() { return evalTokens; }
    public int getPromptTokens() { return promptTokens; }
    public double getTokensPerSecond() { return tokensPerSecond; }
    public double getCopyrightF1() { return copyrightF1; }
    public double getHolderF1() { return holderF1; }
    public double getAuthorF1() { return authorF1; }
    public double getOverallF1() { return overallF1; }
    public int getCopyrightExactMatches() { return copyrightExactMatches; }
    public int getTotalGoldenCopyrights() { return totalGoldenCopyrights; }
    public boolean isValidJson() { return validJson; }
    public boolean isWasRawResponseValidJson() { return wasRawResponseValidJson; }
    public boolean isJsonExtractionBeneficial() { return jsonExtractionBeneficial; }
    public String getErrorMessage() { return errorMessage; }
    public BenchmarkStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "BenchmarkResult{" +
               "sha1='" + sha1 + '\'' +
               ", category='" + category + '\'' +
               ", durationMs=" + durationMs +
               ", status=" + status +
               ", promptTokens=" + promptTokens +
               ", evalTokens=" + evalTokens +
               ", tokensPerSecond=" + String.format("%.2f", tokensPerSecond) +
               ", copyrightF1=" + String.format("%.4f", copyrightF1) +
               ", holderF1=" + String.format("%.4f", holderF1) +
               ", authorF1=" + String.format("%.4f", authorF1) +
               ", overallF1=" + String.format("%.4f", overallF1) +
               ", copyrightExactMatches=" + copyrightExactMatches +
               ", totalGoldenCopyrights=" + totalGoldenCopyrights +
               ", validJson=" + validJson +
               ", wasRawResponseValidJson=" + wasRawResponseValidJson +
               ", jsonExtractionBeneficial=" + jsonExtractionBeneficial +
               ", errorMessage='" + errorMessage + '\'' +
               '}';
    }
}