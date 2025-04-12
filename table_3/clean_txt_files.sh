#!/bin/bash

RESULTS_DIR="./results"

echo "Step 1: Removing trailing 'X warnings' lines..."

# Loop 1: Remove "X warnings" from the end of each file
for file in "$RESULTS_DIR"/*.txt; do
    if [[ -f "$file" ]]; then
        last_line=$(tail -n 1 "$file")
        if [[ "$last_line" =~ ^[0-9]+\ warnings$ ]]; then
            echo "Cleaning warnings from: $(basename "$file")"
            # Remove last line
            sed -i '$d' "$file"
        fi
    fi
done

echo "Step 2: Ensuring last line is just a number (no 'errors')..."

# Loop 2: Remove " errors" from the last line if present
for file in "$RESULTS_DIR"/*.txt; do
    if [[ -f "$file" ]]; then
        last_line=$(tail -n 1 "$file")
        if [[ "$last_line" =~ ^[0-9]+\ errors$ ]]; then
            echo "Fixing errors suffix in: $(basename "$file")"
            # Replace last line with just the number
            count=$(echo "$last_line" | grep -o '^[0-9]\+')
            sed -i '$s/.*/'"$count"'/' "$file"
        fi
    fi
done

echo "✅ Cleanup complete."
