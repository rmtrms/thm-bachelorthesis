#!/bin/bash

# Check for correct usage
if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <source_directory> <target_directory>"
  exit 1
fi

SOURCE_DIR="$1"
TARGET_DIR="$2"

# Create the target directory if it doesn't exist
mkdir -p "$TARGET_DIR"

# Find and copy all .txt files in 'license' directories
find "$SOURCE_DIR" -type f -path '*/license/*.txt' -exec cp {} "$TARGET_DIR/" \;

echo "License text files copied to $TARGET_DIR"