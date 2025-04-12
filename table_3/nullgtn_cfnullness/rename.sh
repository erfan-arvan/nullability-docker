#!/bin/bash

RESULTS_DIR="./results"

declare -A PROJECT_NAME_FIXES=(
  ["butterknife"]="ButterKnife"
  ["mealplanner"]="Meal-Planner"
  ["picasso"]="Picasso"
  ["jib"]="Jib"
  ["nameless"]="Nameless"
  ["floatingactionbuttonspeeddial"]="FloatingActionButtonSpeedDial"
  ["cache2k-nullaway"]="Cache2k-NW"
  ["cache2k-cfnullness"]="Cache2k-CF"
  ["table-wrapper-api"]="Table-wrapper-api"
  ["table-wrapper-csv-impl"]="Table-wrapper-csv-impl"
)

for filepath in "$RESULTS_DIR"/*.txt; do
    filename=$(basename "$filepath")

    # 🔧 Fix Meal-Planner dash BEFORE any parsing
    filename_fixed=${filename/Meal-Planner/MealPlanner}

    # If changed, rename immediately to update disk state
    if [[ "$filename" != "$filename_fixed" ]]; then
        echo "Fixing Meal-Planner → MealPlanner: $filename → $filename_fixed"
        mv "$RESULTS_DIR/$filename" "$RESULTS_DIR/$filename_fixed"
        filename="$filename_fixed"
        filepath="$RESULTS_DIR/$filename_fixed"
    fi

    new_name=""

    # Extract first token
    first_token="${filename%%_*}"
    first_token_lower=$(echo "$first_token" | tr '[:upper:]' '[:lower:]')

    for key in "${!PROJECT_NAME_FIXES[@]}"; do
        if [[ "$first_token_lower" == "$key" ]]; then
            fixed_name="${PROJECT_NAME_FIXES[$key]}"
            rest="${filename#$first_token}"
            new_name="${fixed_name}${rest}"
            break
        fi
    done

    # Fallback: capitalize first letter if no match
    if [[ -z "$new_name" ]]; then
        fixed_name=$(echo "$first_token" | sed 's/.*/\u&/')
        rest="${filename#$first_token}"
        new_name="${fixed_name}${rest}"
    fi

    # Normalize suffixes
    new_name=${new_name/_nullgtn.txt/_ngt.txt}
    new_name=${new_name/_nullaway.txt/_ann.txt}
    new_name=${new_name/_CFNullness.txt/_nullness_wpi.txt}
    new_name=${new_name/-CFNullness-wpi.txt/_nullness_wpi.txt}

    # Normalize 3rd underscore part
    IFS='_' read -r part1 part2 part3 rest <<< "$new_name"
    if [[ "$part3" == "CFNullness" ]]; then
        part3="nullness"
        new_name="${part1}_${part2}_${part3}_${rest}"
    fi

    if [[ "$filename" != "$new_name" ]]; then
        echo "Renaming: $filename → $new_name"
        mv "$RESULTS_DIR/$filename" "$RESULTS_DIR/$new_name"
    fi

    unset new_name
done
