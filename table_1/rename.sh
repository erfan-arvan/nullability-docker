#!/bin/bash

# Mapping for proper benchmark capitalization
declare -A name_map=(
  ["butterknife"]="ButterKnife"
  ["cache2k-CFNullness"]="Cache2k-CF"
  ["cache2k-nullaway"]="Cache2k-NW"
  ["floatingActionButtonSpeedDial"]="FloatingActionButtonSpeedDial"
  ["jib"]="Jib"
  ["mealPlanner"]="Meal-Planner"
  ["nameless"]="Nameless"
  ["picasso"]="Picasso"
  ["table-wrapper-api"]="Table-wrapper-api"
  ["table-wrapper-csv-impl"]="Table-wrapper-csv-impl"
)

cd results || exit 1

for old_name in *.txt; do
  for key in "${!name_map[@]}"; do
    if [[ "$old_name" == ${name_map[$key]}_pre_* || "$old_name" == ${name_map[$key]}_post_* || "$old_name" == ${key}_pre_* || "$old_name" == ${key}_post_* ]]; then
      
      # Extract version (pre/post) and checker
      if [[ "$old_name" =~ _pre_(.*)\.txt$ ]]; then
        version="pre"
        checker="${BASH_REMATCH[1]}"
      elif [[ "$old_name" =~ _post_(.*)\.txt$ ]]; then
        version="post"
        checker="${BASH_REMATCH[1]}"
      else
        continue
      fi

      # Normalize checker name
      checker=$(echo "$checker" | tr '[:upper:]' '[:lower:]')  # lowercase
      if [[ "$checker" == "cfnullness" ]]; then
        checker="nullness"
      fi

      # Final name
      new_name="${name_map[$key]}_${version}_${checker}.txt"

      # Avoid overwriting
      if [[ "$old_name" != "$new_name" ]]; then
        echo "Renaming: $old_name -> $new_name"
        mv "$old_name" "$new_name"
      fi

    fi
  done
done
