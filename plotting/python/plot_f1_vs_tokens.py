import pandas as pd
import matplotlib.pyplot as plt
from adjustText import adjust_text

# Load the data
# Make sure the CSV file is in the same directory as this script,
# or provide the full path to the file.
file_path = "../data/benchmark_summary_all_fields.csv"
df = pd.read_csv(file_path)

# Create the plot
plt.figure(figsize=(12, 8)) # Increased figure size for better readability
texts = []

# Plot each point and collect labels for adjustment
# This loop is necessary for adjust_text to work with individual labels
for _, row in df.iterrows():
    x = row["averageTokensPerSecondSuccessful"]
    y = row["averageOverallF1AcrossCategories"]
    label = row["modelName"]
    plt.scatter(x, y, color='blue', s=70, alpha=0.8, edgecolors='w', linewidth=0.5)
    texts.append(plt.text(x, y, label, fontsize=10))

# Adjust label positions to avoid overlaps
# This function intelligently moves text labels to prevent them from colliding.
adjust_text(texts, arrowprops=dict(arrowstyle="-", color='gray', lw=0.5))

# --- AXIS LIMITS ---
# Set the y-axis to range from 0 to 1.0 for F1 Score
plt.ylim(0.3, 1.0)

plt.xlim(left=0)

# Customize the plot
plt.xlabel("Tokens/sec", fontsize=12)
plt.ylabel("F1 Score", fontsize=12)
plt.title("F1 Score vs. Tokens/sec", fontsize=16, fontweight='bold')
plt.grid(True, which='both', linestyle='--', linewidth=0.5)
plt.tight_layout() # Adjusts plot to ensure everything fits without overlapping

# Save the plot to a file
output_file = "../plots/f1_vs_tokens_per_sec.png"
plt.savefig(output_file, format='png', dpi=300, bbox_inches='tight')
plt.close() # Close the plot figure to free up memory

print(f"Plot successfully saved as '{output_file}'")