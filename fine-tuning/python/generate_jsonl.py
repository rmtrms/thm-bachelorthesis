import os
import json
import random
from pathlib import Path

# Configuration
DATA_ROOT = Path("/Volumes/Data/test/destillation")
DIRECTORIES = [
    "result"
]

random.seed(42)

# Output containers
split_data = {
    "train": [],
    "test": [],
    "valid": []
}

# Instruction string for Mistral
INSTRUCTION = "Extract the copyrights, holders and authors in JSON format from the following file:"

def make_text_field(txt_path, json_path):
    with open(txt_path, 'r', encoding='utf-8', errors='replace') as f:
        file_content = f.read().strip()

    with open(json_path, 'r', encoding='utf-8', errors='replace') as f:
        metadata = json.load(f)

    # Ensure the response JSON contains all required fields
    response = {
        "copyrights": metadata.get("copyrights", []),
        "holders": metadata.get("holders", []),
        "authors": metadata.get("authors", [])
    }

    # Format full training string
    return {
        "text": f"<s>[INST] {INSTRUCTION}\n\n{file_content}\n\n[/INST] {json.dumps(response, separators=(',', ':'))} </s>"
    }

# Process each subdirectory independently
for subdir in DIRECTORIES:
    dir_path = DATA_ROOT / subdir
    all_files = os.listdir(dir_path)

    # Find only *_filtered.txt files
    filtered_txts = [f for f in all_files if f.endswith("_filtered.txt")]
    examples = []

    for txt_file in filtered_txts:
        sha1 = txt_file.replace("_filtered.txt", "")
        json_file = f"{sha1}_llm_response.json"
        txt_path = dir_path / txt_file
        json_path = dir_path / json_file

        if not json_path.exists():
            print(f"⚠️ Missing JSON: {json_file}, skipping.")
            continue

        try:
            example = make_text_field(txt_path, json_path)
            examples.append(example)
        except Exception as e:
            print(f"❌ Error processing {txt_file}: {e}")

    # Shuffle and split
    random.shuffle(examples)
    n = len(examples)
    n_train = int(n * 0.8)
    n_test = int(n * 0.1)

    split_data["train"].extend(examples[:n_train])
    split_data["test"].extend(examples[n_train:n_train + n_test])
    split_data["valid"].extend(examples[n_train + n_test:])

# Write JSONL output files
for split, items in split_data.items():
    with open(f"{split}.jsonl", "w", encoding="utf-8") as f:
        for item in items:
            json.dump(item, f)
            f.write("\n")

print("✅ Done! Created train.jsonl, test.jsonl, valid.jsonl.")