#!/bin/bash

# === SETUP ===
BASE_DIR="$(pwd)"
DATASET_DIR="$BASE_DIR/dataset"
RESULTS_DIR="$BASE_DIR/wpi_analysis_results"
WPI_OUT_DIR="$BASE_DIR/wpi/checkerframework_3.34.0_results_WPI"
COUNT_SCRIPT="$BASE_DIR/count_nullable_errors_for_maven_projects.sh"

# Create output folders
mkdir -p "$DATASET_DIR"
mkdir -p "$RESULTS_DIR"
mkdir -p "$WPI_OUT_DIR"

# Define GitHub repos
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

# === STEP 1: Clone all pre/post branches ===
clone_version() {
  local project=$1
  local version=$2
  local branch=$3
  local url=$4
  local target_dir="$DATASET_DIR/${project}_${version}"

  echo "📦 Cloning $project ($version) from $branch into $target_dir"
  if [ ! -d "$target_dir" ]; then
    git clone "$url" "$target_dir"
  fi

  cd "$target_dir" || return
  git checkout "$branch"
  cd "$BASE_DIR"
}

for project in "${!repos[@]}"; do
  url="${repos[$project]}"

  if [[ "$project" == "cache2k-nullaway" ]]; then
    pre_branch="pre_wpi_without_s_nullaway"
    post_branch="post_wpi_without_s_nullaway"
  elif [[ "$project" == "cache2k-CFNullness" ]]; then
    pre_branch="pre_wpi_without_s_CFNullness"
    post_branch="post_wpi_without_s_CFNullness"
  else
    pre_branch="pre_wpi_without_s"
    post_branch="post_wpi_without_s"
  fi

  clone_version "$project" "pre_wpi" "$pre_branch" "$url"
  clone_version "$project" "post_wpi" "$post_branch" "$url"
done

# === STEP 2: Run global WPI preprocessing ===
echo "⚙️ Running: ./wpi/run_wpi.py"
cd "$BASE_DIR/wpi"
python3 run_wpi.py
cd "$BASE_DIR"

# === STEP 3: Run wpi.sh and collect results ===
for project in "${!repos[@]}"; do
  for version in pre_wpi post_wpi; do
    proj_dir="$DATASET_DIR/${project}_${version}"
    echo "🔍 Processing $project ($version) in $proj_dir"

    # --- cache2k special: run in cache2k-api subdir ---
    if [[ "$project" == cache2k* ]]; then
      sub="$proj_dir/cache2k-api"
      if [ -d "$sub" ]; then
        cd "$sub"
        echo "🛠️  Patching pom.xml in $sub"
        find . -name "pom.xml" -type f -exec sed -i 's/3\.45\.1-SNAPSHOT/3.47.1-SNAPSHOT/g' {} +

        echo "🚀 Running wpi.sh"
        ./wpi.sh

        echo "📥 Copying pre/postCheck.out to result dir"
        [ -f preCheck.out ] && cp preCheck.out "$WPI_OUT_DIR/${project}_${version}-before-wpi.txt"
        [ -f postCheck.out ] && cp postCheck.out "$WPI_OUT_DIR/${project}_${version}-after-wpi.txt"

        echo "📊 Running count script"
        bash "$COUNT_SCRIPT" > "$RESULTS_DIR/${project}_${version}_count.txt"
        cd "$BASE_DIR"
      fi
    fi

    # --- table-* projects: run in root ---
    if [[ "$project" == table-* ]]; then
      if [ -d "$proj_dir" ]; then
        cd "$proj_dir"
        echo "🛠️  Patching pom.xml in $proj_dir"
        find . -name "pom.xml" -type f -exec sed -i 's/3\.45\.1-SNAPSHOT/3.47.1-SNAPSHOT/g' {} +

        echo "🚀 Running wpi.sh"
        ./wpi.sh

        echo "📥 Copying pre/postCheck.out to result dir"
        [ -f preCheck.out ] && cp preCheck.out "$WPI_OUT_DIR/${project}_${version}-before-wpi.txt"
        [ -f postCheck.out ] && cp postCheck.out "$WPI_OUT_DIR/${project}_${version}-after-wpi.txt"

        echo "📊 Running count script"
        bash "$COUNT_SCRIPT" > "$RESULTS_DIR/${project}_${version}_count.txt"
        cd "$BASE_DIR"
      fi
    fi

  done
done

echo "✅ All done!"
echo "📁 WPI output files: $WPI_OUT_DIR"
echo "📁 Nullable count results: $RESULTS_DIR"
