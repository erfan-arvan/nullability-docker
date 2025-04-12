#!/bin/bash

# Define the base directory and output CSV
BASE_DIR="$(pwd)"
PROJECTS_DIR="$BASE_DIR/projects_for_diff_counts"
OUTPUT_CSV="$BASE_DIR/project_diff_categories_counts.csv"
RESULTS_DIR="$BASE_DIR/results"
ANNOTATED_DIR="$RESULTS_DIR/annotated_diffs"

# Define the projects and their git URLs
declare -A repos=(
  ["floatingActionButtonSpeedDial"]="https://github.com/erfan-arvan/FloatingActionButtonSpeedDial"
  ["mealPlanner"]="https://github.com/erfan-arvan/meal-planner"
  ["picasso"]="https://github.com/erfan-arvan/picasso/"
  ["cache2k-nullaway"]="https://github.com/erfan-arvan/cache2k/"
  ["cache2k-CFNullness"]="https://github.com/erfan-arvan/cache2k/"
  ["butterknife"]="https://github.com/erfan-arvan/butterknife/"
  ["nameless"]="https://github.com/erfan-arvan/Nameless-Java-API/"
  ["table-wrapper-api"]="https://github.com/erfan-arvan/table-wrapper-api/"
  ["table-wrapper-csv-impl"]="https://github.com/erfan-arvan/table-wrapper-csv-impl/"
  ["jib"]="https://github.com/erfan-arvan/jib"
)

# Keywords to count
keywords=("C1: Null Check Introduction" "C2: Precondition enforcement (requireNonNull())" "C3: Field Initialization" "C4: mark elements as final" "C5: Method signature modification" "C6: Qualified this reference" "C7: Method/Constructor definition or Override" "C8: Argument modification" "C9: Return value modification" "C10: Field type modification" "//Others:")

# Create necessary directories
mkdir -p "$PROJECTS_DIR"
mkdir -p "$ANNOTATED_DIR"

# Prepare CSV header
header="Project"
for key in "${keywords[@]}"; do
  header+=",$key"
done
header+=",Others_Info"
echo "$header" > "$OUTPUT_CSV"

# Clone and process each repo
for project in "${!repos[@]}"; do
  url="${repos[$project]}"
  proj_dir="$PROJECTS_DIR/$project"

  echo "Processing $project..."

  # Clone if not already cloned
  if [ ! -d "$proj_dir" ]; then
    git clone "$url" "$proj_dir"
  fi

  cd "$proj_dir" || continue

  # Checkout appropriate branch
  if [[ "$project" == "cache2k-nullaway" ]]; then
    git checkout post_diff_nullaway
  elif [[ "$project" == "cache2k-CFNullness" ]]; then
    git checkout post_diff_CFNullness
  else
    git checkout post_diff
  fi

  # Find diff_out_annotated.txt file
  file_path=$(find . -type f -name "diff_out_annotated.txt" | head -n 1)

  if [ -z "$file_path" ]; then
    echo "$project,File not found" >> "$OUTPUT_CSV"
    cd "$BASE_DIR"
    continue
  fi

  # Copy the annotated file to the annotated_diffs folder
  cp "$file_path" "$ANNOTATED_DIR/${project}_diff_out_annotated.txt"

  # Count occurrences and extract Others_Info
  line="$project"
  others_info=""
  for key in "${keywords[@]}"; do
    if [[ "$key" == "//Others:" ]]; then
      count=$(grep -c "$key" "$file_path")
      others_info=$(grep "$key" "$file_path" | sed -E 's/.*\/\/Others: ?//' | paste -sd "|" -)
    else
      count=$(grep -cF "$key" "$file_path")
    fi
    line+=",$count"
  done
  line+=",\"$others_info\""
  echo "$line" >> "$OUTPUT_CSV"

  cd "$BASE_DIR"
done

echo "✅ Done! Output saved to: $OUTPUT_CSV"
