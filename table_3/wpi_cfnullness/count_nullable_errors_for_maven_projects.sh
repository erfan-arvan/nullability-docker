#!/bin/bash

# Function to count and calculate the number of errors in preCheck.out
function count_nullness_errors {
    file="postCheck.out"

    # Count occurrences of [nullness:
    count_nullness=$(grep -c "\[nullness:" "$file")

    # Count occurrences of [nullness:type.anno.before.modifier
    count_modifier=$(grep -c "\[nullness:type\.anno\.before\.modifier" "$file")

    # Calculate the difference
    difference=$((count_nullness - count_modifier))

    # Only print the number of errors
    echo "$difference errors"
}

# Run the function
count_nullness_errors
