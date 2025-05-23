#!/bin/bash

# Check for correct usage
if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <directory>"
  exit 1
fi

TARGET_DIR="$1"

# Check if the directory exists
if [ ! -d "$TARGET_DIR" ]; then
  echo "Error: '$TARGET_DIR' is not a valid directory."
  exit 1
fi

# Count files (not directories)
file_count=$(find "$TARGET_DIR" -maxdepth 1 -type f | wc -l)

echo "Number of files in '$TARGET_DIR': $file_count"