#!/bin/bash

ROOT_DIR=$(pwd)
RESULTS_DIR="$ROOT_DIR/results"
COUNTS_DIR="$RESULTS_DIR/counts"
DIFFS_DIR="$RESULTS_DIR/diffs"
PROJECTS_DIR="$ROOT_DIR/projects_for_annotation_counts_study"
SCRIPTS_DIR="$ROOT_DIR/scripts"

SCRIPT1="$SCRIPTS_DIR/removeAnnotations.py"
SCRIPT2="$SCRIPTS_DIR/removeCommentsLinesImports.py"
COUNT_SCRIPT="$SCRIPTS_DIR/count_annotations.py"

# Handle "fresh" argument
if [[ "$1" == "fresh" ]]; then
  echo "🔄 Fresh run requested. Removing existing projects directory..."
  rm -rf "$PROJECTS_DIR"
elif [[ -d "$PROJECTS_DIR" ]]; then
  echo "ℹ️  Projects directory already exists. If you want a clean run, use: $0 fresh"
  echo "✅ Existing results are in:"
  echo "   Counts: $COUNTS_DIR"
  echo "   Diffs:  $DIFFS_DIR"
  exit 0
fi

mkdir -p "$COUNTS_DIR"
mkdir -p "$DIFFS_DIR"
mkdir -p "$PROJECTS_DIR"
cd "$PROJECTS_DIR" || exit 1

declare -A PRE_CHECK_COMMITS
declare -A POST_CHECK_COMMITS
declare -A LINKS

# Hardcoded repo details
PRE_CHECK_COMMITS["floatingActionButtonSpeedDial"]="556b84fc516b1b0764aa7e2c20a652ff03207ee9"
POST_CHECK_COMMITS["floatingActionButtonSpeedDial"]="c762d31a32015229c67f2e9a69b2f3bdea54de60"
LINKS["floatingActionButtonSpeedDial"]="https://github.com/leinardi/FloatingActionButtonSpeedDial"

PRE_CHECK_COMMITS["mealPlanner"]="534de2074178ed4cfd34a23cad1212f76016ad9e"
POST_CHECK_COMMITS["mealPlanner"]="fa953d7d669eab706afb675628eb1be68caa7ca1"
LINKS["mealPlanner"]="https://github.com/fzuellich/meal-planner"

PRE_CHECK_COMMITS["picasso"]="589b0d617f9ba0b0e3e1834483810f9d19a874b4"
POST_CHECK_COMMITS["picasso"]="466781f8feca2418e5d36eb45aa0cc0a7d8e1b62"
LINKS["picasso"]="https://github.com/square/picasso/"

PRE_CHECK_COMMITS["cache2k-nullaway"]="495a6491028d8eaa0ed9371ca974290d99c4686e"
POST_CHECK_COMMITS["cache2k-nullaway"]="2c99f5090095554809c46aa6650fd9a4f413d464"
LINKS["cache2k-nullaway"]="https://github.com/cache2k/cache2k"

PRE_CHECK_COMMITS["cache2k-CFNullness"]="13c9b14a033d5f7aa2a33d498df223350d7a9e55"
POST_CHECK_COMMITS["cache2k-CFNullness"]="226d0c917d4e3bef68035782e0abc091e7e971d7"
LINKS["cache2k-CFNullness"]="https://github.com/cache2k/cache2k"

PRE_CHECK_COMMITS["butterknife"]="be4792d8a8c76aebd0f90858a1ea426ed31fd36c"
POST_CHECK_COMMITS["butterknife"]="c17c16c89c69bf280146888bbca5131f06857703"
LINKS["butterknife"]="https://github.com/JakeWharton/butterknife/"

PRE_CHECK_COMMITS["nameless"]="1c239f9f248e6619ce5783a1c0ebd021a7a7526e"
POST_CHECK_COMMITS["nameless"]="0f5266d193a1b20419d49eb3fab0960d326f311c"
LINKS["nameless"]="https://github.com/NamelessMC/Nameless-Java-API/"

PRE_CHECK_COMMITS["table-wrapper-api"]="b213629fbea6168bb9b7fb6f1cf397311ae1abaa"
POST_CHECK_COMMITS["table-wrapper-api"]="73a0fd50f9bf8a9e2218ba5231b97170b91dd273"
LINKS["table-wrapper-api"]="https://github.com/spacious-team/table-wrapper-api/"

PRE_CHECK_COMMITS["table-wrapper-csv-impl"]="b5878d3448c7e39424b2e22600e1fc7474246c5b"
POST_CHECK_COMMITS["table-wrapper-csv-impl"]="13dbe59189e6939c835591c844d6a884e8c9ba39"
LINKS["table-wrapper-csv-impl"]="https://github.com/spacious-team/table-wrapper-csv-impl/"

PRE_CHECK_COMMITS["jib"]="4005c45ff437973d0481244034f806c7b71164ec"
POST_CHECK_COMMITS["jib"]="23306a36bb8f0585427e2399eb3ee6fba109a019"
LINKS["jib"]="https://github.com/GoogleContainerTools/jib"

# Where to run the scripts
declare -A SCRIPT_TARGET_DIRS
SCRIPT_TARGET_DIRS["butterknife"]="butterknife-compiler"
SCRIPT_TARGET_DIRS["jib"]="jib-core"
SCRIPT_TARGET_DIRS["picasso"]="picasso"
SCRIPT_TARGET_DIRS["cache2k-nullaway"]="cache2k-api"
SCRIPT_TARGET_DIRS["cache2k-CFNullness"]="cache2k-api"
SCRIPT_TARGET_DIRS["floatingActionButtonSpeedDial"]="library"

# Folder to run diff on
declare -A FOLDER_EXCEPTIONS
FOLDER_EXCEPTIONS["butterknife"]="butterknife-compiler/src"
FOLDER_EXCEPTIONS["jib"]="jib-core/src"
FOLDER_EXCEPTIONS["floatingActionButtonSpeedDial"]="library/src"
FOLDER_EXCEPTIONS["picasso"]="picasso/src"
FOLDER_EXCEPTIONS["cache2k-nullaway"]="cache2k-api/src"
FOLDER_EXCEPTIONS["cache2k-CFNullness"]="cache2k-api/src"

for project in "${!LINKS[@]}"; do
  url="${LINKS[$project]}"
  pre_commit="${PRE_CHECK_COMMITS[$project]}"
  post_commit="${POST_CHECK_COMMITS[$project]}"
  echo -e "\n▶️ Processing $project"

  cd "$PROJECTS_DIR" || exit 1

  if [[ ! -d "$project" ]]; then
    git clone "$url" "$project"
  fi

  cd "$project" || continue
  git fetch origin

  run_dir="."
  if [[ -n "${SCRIPT_TARGET_DIRS[$project]}" ]]; then
    run_dir="${SCRIPT_TARGET_DIRS[$project]}"
  fi

  count_file="$COUNTS_DIR/${project}_counts.txt"

  ### Pre-check
  git checkout -b pre_check "$pre_commit"
  mkdir -p "$run_dir"
  cp "$SCRIPT1" "$SCRIPT2" "$COUNT_SCRIPT" "$run_dir/"

  echo "pre_check" >> "$count_file"
  (cd "$run_dir" && \
    python3 count_annotations.py --include_suppress_warnings --output_file "$count_file" && \
    python3 removeAnnotations.py --remove_suppress_warnings && \
    python3 removeCommentsLinesImports.py)

  # 🚫 Remove test folders inside src before commit
  find "$run_dir/src" -type d \( -iname "test" -o -iname "tests" -o -iname "testing" \) -exec rm -rf {} +
  
  git add .
  git commit -m "Processed pre_check"

  ### Post-check
  git checkout -b post_check "$post_commit"
  mkdir -p "$run_dir"
  cp "$SCRIPT1" "$SCRIPT2" "$COUNT_SCRIPT" "$run_dir/"

  echo -e "\npost_check" >> "$count_file"
  (cd "$run_dir" && \
    python3 count_annotations.py --include_suppress_warnings --output_file "$count_file" && \
    python3 removeAnnotations.py --remove_suppress_warnings && \
    python3 removeCommentsLinesImports.py)

  # 🚫 Remove test folders inside src before commit
  find "$run_dir/src" -type d \( -iname "test" -o -iname "tests" -o -iname "testing" \) -exec rm -rf {} +

  git add .
  git commit -m "Processed post_check"

  ### Diff
  git checkout post_check
  diff_path="src"
  if [[ -n "${FOLDER_EXCEPTIONS[$project]}" ]]; then
    diff_path="${FOLDER_EXCEPTIONS[$project]}"
  fi

  if [[ ! -d "$diff_path" ]]; then
    echo "⚠️  Directory '$diff_path' not found. Skipping diff."
    cd ..
    continue
  fi

  diff_file="$DIFFS_DIR/${project}_diff.txt"
  git diff -w pre_check post_check -- "$diff_path/**/*.java" > "$diff_file"
  echo "✅ Diff saved to $diff_file"

  cd ..
done

echo -e "\n✅ All projects processed. Annotation counts saved to $COUNTS_DIR, diffs saved to $DIFFS_DIR"
