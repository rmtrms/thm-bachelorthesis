# Create a line chart for Exact Matches (%) and F1-Score across prompt versions.
# The data below is taken explicitly from the provided screenshot.

import matplotlib.pyplot as plt

versions = ["P0","P1","P2","P3","P4","P5","P6","P7","P8","P9","P7.1","P7.2","P7.2-AI"]

exact_matches = [
    62.732919254658384,
    63.35403726708074,
    78.26086956521739,
    79.5031055900621,
    82.6086956521739,
    90.06211180124224,
    90.6832298136646,
    91.92546583850931,
    90.6832298136646,
    90.6832298136646,
    94.409937888819875,
    96.27329192546584,
    91.869708994709,
]

f1_scores = [
    78.56230158730159,
    85.11137566137567,
    89.44153439153439,
    89.82248677248676,
    90.60026455026455,
    92.93359788359788,
    92.53082010582011,
    93.06693121693121,
    93.05859788359788,
    92.83637566137566,
    93.21137566137565,
    93.35859788359788,
    92.54658385093167,
]

x = range(len(versions))

plt.figure(figsize=(10,6))
plt.plot(x, exact_matches, marker="o", label="Exact Matches (%)")
plt.plot(x, f1_scores, marker="o", label="F1-Score (x100)")

plt.xticks(x, versions, rotation=45, ha="right")
plt.xlabel("Prompt-Version")
plt.title("Leistungsentwicklung der Prompt-Versionen (Exact Matches und F1-Score)")
plt.grid(True, linestyle="--", linewidth=0.5)
plt.legend()

output_path = "/Users/rtueremis/Metaeffekt/Repositories/thm-bachelorthesis/latex/tex/bilder/prompt-engineering/prompt_versions_performance.png"
plt.tight_layout()
plt.savefig(output_path, dpi=200)
output_path