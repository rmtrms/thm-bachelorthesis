import pandas as pd
import matplotlib.pyplot as plt
from adjustText import adjust_text

# --- CONFIG SWITCH ---
USE_MANUAL_DATA = True  # Set to False to load from CSV

# --- DATA LOADING ---
if USE_MANUAL_DATA:
    # Manually define the data
    data = [
        {"modelName": "mistral-small:24b", "averageTokensPerSecondSuccessful": 6.022319323591854, "percentageExactMatchesOverall": 99.0},
        {"modelName": "mistral:7b-instruct-v0.3-fine-tuned", "averageTokensPerSecondSuccessful": 20.92550744207152, "percentageExactMatchesOverall": 99.0},
        {"modelName": "qwen2.5-1.5b-instruct-fine-tuned", "averageTokensPerSecondSuccessful": 71.53003974853131, "percentageExactMatchesOverall": 91.5},
    ]
    df = pd.DataFrame(data)
else:
    # Load the data from CSV
    file_path = "../data/benchmark_summary_all_fields.csv"
    df = pd.read_csv(file_path)

# --- PLOTTING ---
plt.figure(figsize=(12, 8)) # Increased figure size for better readability
texts = []

for _, row in df.iterrows():
    x = row["averageTokensPerSecondSuccessful"]
    y = row["percentageExactMatchesOverall"]
    label = row["modelName"]
    plt.scatter(x, y, color='blue', s=70, alpha=0.8, edgecolors='w', linewidth=0.5)
    texts.append(plt.text(x, y, label, fontsize=10))

# Adjust label positions
adjust_text(texts, arrowprops=dict(arrowstyle="-", color='gray', lw=0.5))

# --- AXIS LIMITS ---
plt.ylim(90, 100)
plt.xlim(left=0)

# Customize the plot
plt.xlabel("Tokens/sec", fontsize=12)
plt.ylabel("Percentage of Exact Matches (%)", fontsize=12)
plt.title("Exact Matches vs. Tokens/sec", fontsize=16, fontweight='bold')
plt.grid(True, which='both', linestyle='--', linewidth=0.5)
plt.tight_layout()

# Save the plot to a file
output_file = "/Users/rtueremis/Metaeffekt/Repositories/thm-bachelorthesis/latex/tex/bilder/fine-tuning/exact_matches_vs_tokens_per_sec-fine-tuned.png"
plt.savefig(output_file, format='png', dpi=300, bbox_inches='tight')
plt.close()

print(f"Plot successfully saved as '{output_file}'")