import pandas as pd
import matplotlib.pyplot as plt
from adjustText import adjust_text

# --- CONFIG SWITCH ---
USE_MANUAL_DATA = True  # Set to False to load from CSV

# --- DATA LOADING ---
if USE_MANUAL_DATA:
    # Manually define the data
    data = [
        {"modelName": "ModelA", "averageTokensPerSecondSuccessful": 120, "percentageExactMatchesOverall": 85},
        {"modelName": "ModelB", "averageTokensPerSecondSuccessful": 200, "percentageExactMatchesOverall": 78},
        {"modelName": "ModelC", "averageTokensPerSecondSuccessful": 95, "percentageExactMatchesOverall": 90},
        {"modelName": "ModelD", "averageTokensPerSecondSuccessful": 300, "percentageExactMatchesOverall": 70},
        {"modelName": "ModelE", "averageTokensPerSecondSuccessful": 150, "percentageExactMatchesOverall": 88},
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
plt.ylim(0, 100)
plt.xlim(left=0)

# Customize the plot
plt.xlabel("Tokens/sec", fontsize=12)
plt.ylabel("Percentage of Exact Matches (%)", fontsize=12)
plt.title("Exact Matches vs. Tokens/sec", fontsize=16, fontweight='bold')
plt.grid(True, which='both', linestyle='--', linewidth=0.5)
plt.tight_layout()

# Save the plot to a file
output_file = "../plots/exact_matches_vs_tokens_per_sec.png"
plt.savefig(output_file, format='png', dpi=300, bbox_inches='tight')
plt.close()

print(f"Plot successfully saved as '{output_file}'")