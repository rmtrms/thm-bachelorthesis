import pandas as pd
import matplotlib.pyplot as plt
from adjustText import adjust_text

# Load the data
file_path = "../data/benchmark_summary_all_fields.csv"
df = pd.read_csv(file_path)

# Create the plot
plt.figure(figsize=(12, 8))
texts = []

# Plot each point and collect labels for adjustment
for _, row in df.iterrows():
    x = row["averageTokensPerSecondSuccessful"]
    y = row["percentageExactMatchesOverall"]
    label = row["modelName"]
    plt.scatter(x, y, color='blue', s=70, alpha=0.8, edgecolors='w', linewidth=0.5)
    # small vertical nudge so the text isn't on top of the dot
    texts.append(plt.text(x, y + 1.0, label, fontsize=10, ha='center', va='bottom'))

# --- AXIS LIMITS ---
# Set the y-axis to range from 0 to 100
plt.ylim(0, 100)
plt.xlim(left=0)

# Adjust label positions to avoid overlaps (move labels vertically only)
adjust_text(
    texts,
    only_move={'points': 'y', 'text': 'y'},
    force_points=0.3,
    force_text=0.8,
    expand_points=(1.1, 1.3),
    arrowprops=dict(arrowstyle="-", color='gray', lw=0.5)
)

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