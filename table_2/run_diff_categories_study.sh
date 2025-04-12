#!/bin/bash
set -e

ROOT_DIR=$(pwd)
RESULTS_DIR="$ROOT_DIR/results"
PROJECTS_DIR="$ROOT_DIR/projects_for_diff_counts"
SCRIPTS_DIR="$ROOT_DIR/scripts"

if [ "$1" == "fresh" ]; then
  echo "Doing fresh setup..."
  rm -rf "$PROJECTS_DIR" 
fi

mkdir -p "$RESULTS_DIR"
mkdir -p "$PROJECTS_DIR"
cd "$PROJECTS_DIR" || exit 1

repos=(
  "floatingActionButtonSpeedDial https://github.com/erfan-arvan/FloatingActionButtonSpeedDial"
  "mealPlanner https://github.com/erfan-arvan/meal-planner"
  "picasso https://github.com/erfan-arvan/picasso"
  "cache2k https://github.com/erfan-arvan/cache2k"
  "butterknife https://github.com/erfan-arvan/butterknife"
  "nameless https://github.com/erfan-arvan/Nameless-Java-API"
  "table-wrapper-api https://github.com/erfan-arvan/table-wrapper-api"
  "table-wrapper-csv-impl https://github.com/erfan-arvan/table-wrapper-csv-impl"
  "table-wrapper-api-dupe https://github.com/erfan-arvan/table-wrapper-api"
  "jib https://github.com/erfan-arvan/jib"
)

DEFAULT_PRE_BRANCH="pre_diff"
DEFAULT_POST_BRANCH="post_diff"

CACHE2K_BRANCH_SETS=(
  "CFNullness pre_diff_CFNullness post_diff_CFNullness"
  "nullaway pre_diff_nullaway post_diff_nullaway"
)

declare -A FOLDER_EXCEPTIONS
FOLDER_EXCEPTIONS["butterknife"]="butterknife-compiler/src"
FOLDER_EXCEPTIONS["jib"]="jib-core/src"
FOLDER_EXCEPTIONS["floatingActionButtonSpeedDial"]="library/src"
FOLDER_EXCEPTIONS["picasso"]="picasso/src"

for repo in "${repos[@]}"; do
  read -r name url <<< "$repo"
  echo -e "\n▶️ Processing $name"

  if [[ ! -d "$name" ]]; then
    git clone "$url" "$name"
    if [[ $? -ne 0 ]]; then
      echo "❌ Failed to clone $url"
      continue
    fi
  else
    echo "📁 Repo $name already exists. Skipping clone."
  fi

  cd "$name" || continue
  git fetch origin

  # Determine folder to diff
  diff_path="src"
  if [[ -n "${FOLDER_EXCEPTIONS[$name]}" ]]; then
    diff_path="${FOLDER_EXCEPTIONS[$name]}"
  fi

  if [[ "$name" == "cache2k" ]]; then
    for set in "${CACHE2K_BRANCH_SETS[@]}"; do
      read -r tag pre_branch post_branch <<< "$set"

      # Check both branches exist
      if ! git rev-parse --verify "origin/$pre_branch" >/dev/null 2>&1 || \
         ! git rev-parse --verify "origin/$post_branch" >/dev/null 2>&1; then
        echo "❌ Missing $pre_branch or $post_branch in $name"
        continue
      fi

      # Checkout post branch to inspect folder
      git checkout -q "origin/$post_branch"

      if [[ ! -d "$diff_path" ]]; then
        echo "⚠️  Directory '$diff_path' not found in $post_branch. Skipping diff for $tag."
        continue
      fi

      diff_file="$RESULTS_DIR/${name}_${tag}_diff.txt"
      #git diff "origin/$pre_branch" "origin/$post_branch" -- "$diff_path/**/*.java" > "$diff_file"
      #echo "> Java diff ($tag) in $diff_path saved to $diff_file"
    done
  else
    # Check both default branches
    if ! git rev-parse --verify "origin/$DEFAULT_PRE_BRANCH" >/dev/null 2>&1 || \
       ! git rev-parse --verify "origin/$DEFAULT_POST_BRANCH" >/dev/null 2>&1; then
      echo "❌ Missing $DEFAULT_PRE_BRANCH or $DEFAULT_POST_BRANCH in $name"
      cd ..
      continue
    fi

    # Checkout post branch to inspect folder
    git checkout -q "origin/$DEFAULT_POST_BRANCH"

    if [[ ! -d "$diff_path" ]]; then
      echo "⚠️  Directory '$diff_path' not found in $DEFAULT_POST_BRANCH. Skipping diff."
      cd ..
      continue
    fi

    diff_file="$RESULTS_DIR/${name}_diff.txt"
    #git diff -w "origin/$DEFAULT_PRE_BRANCH" "origin/$DEFAULT_POST_BRANCH" -- "$diff_path/**/*.java" > "$diff_file"
    #echo "> Java diff in $diff_path saved to $diff_file"
  fi

  cd ..
done

#echo -e "\n> All diffs completed and saved to: $RESULTS_DIR"

echo "-------------------Manual Results Creation-----------------------\n"
cd "$ROOT_DIR" || {
  echo "❌ Failed to cd into $ROOT_DIR"
  exit 1
}
# Create the manual results
chmod +x "$SCRIPTS_DIR/analyze_diffs.sh"
"$SCRIPTS_DIR/analyze_diffs.sh"

echo "------------------------------------"
echo -e "✅ Processing complete.\n- Raw diff files are located in: results/\n- Annotated diff files are saved in: annotated_results/\n- The summary CSV (project_diff_categories_counts.csv) has been recreated and is available in the root directory."
echo "------------------------------------"
echo "project_diff_categories_counts.csv"
python3 "$SCRIPTS_DIR/show_results.py"
