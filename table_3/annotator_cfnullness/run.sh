#!/bin/bash

# === SETUP ===
BASE_DIR="$(pwd)"
DATASET_DIR="$BASE_DIR/dataset"
RESULTS_DIR="$BASE_DIR/results"
COUNT_SCRIPT="$BASE_DIR/count_nullable_errors_for_maven_projects.sh"
WPI_LOG_DIR="$BASE_DIR/cfnullness_scripts/checkerframework_3.34.0_results_WPI"

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
mkdir -p "$DATASET_DIR"

clone_version() {
  local project=$1
  local version=$2
  local branch=$3
  local url=$4
  local clean_version="${version/_wpi/}"  # strip "wpi" from version name
  local target_dir="$DATASET_DIR/${project}_${clean_version}"

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
    pre_branch="pre_nullaway_without_s_nullaway"
    post_branch="post_nullaway_without_s_nullaway"
  elif [[ "$project" == "cache2k-CFNullness" ]]; then
    pre_branch="pre_nullaway_without_s_CFNullness"
    post_branch="post_nullaway_without_s_CFNullness"
  else
    pre_branch="pre_nullaway_without_s"
    post_branch="post_nullaway_without_s"
  fi

  clone_version "$project" "pre_nullaway" "$pre_branch" "$url"
  clone_version "$project" "post_nullaway" "$post_branch" "$url"
done

# === STEP 2.0: Add missing libs for select projects ===
echo "📦 Copying missing lib folders into dataset projects"

declare -A lib_copies=(
  ["picasso_pre_nullaway"]="pre_wpi_without_s_picasso"
  ["picasso_post_nullaway"]="post_wpi_without_s_picasso"
  ["butterknife_pre_nullaway"]="pre_wpi_without_s_butterknife"
  ["butterknife_post_nullaway"]="post_wpi_without_s_butterknife"
)

for project_dir in "${!lib_copies[@]}"; do
  src_lib_dir="$BASE_DIR/all-libs/${lib_copies[$project_dir]}/lib"
  target_dir="$BASE_DIR/dataset/${project_dir}"
  if [ -d "$target_dir" ]; then
    echo "🔗 Copying lib from $src_lib_dir to $target_dir"
    cp -r "$src_lib_dir" "$target_dir/"
  else
    echo "⚠️ Warning: Target directory $target_dir not found. Skipping."
  fi
done

# === STEP 2: Run global WPI preprocessing ===
echo "⚙ Running: ./cfnullness_scripts/run_cfnullness.py"
cd "$BASE_DIR/cfnullness_scripts"
python3 run_cfnullness.py
cd "$BASE_DIR"

# === STEP 2.5: Copy WPI output files into results folder ===
echo "Copying WPI output files to results folder"

mkdir -p "$RESULTS_DIR"

for result_file in "$WPI_LOG_DIR"/*.txt; do
filename=$(basename "$result_file")  # e.g., table-wrapper-api_post_nullaway-before-wpi.txt
benchmark="${filename%-before-wpi.txt}"  # remove suffix

project="${benchmark%_*_*}"                     # remove last two parts (version and tag)
version="${benchmark#${project}_}"              # what's after project_
version="${version%%_*}"                        # just 'pre' or 'post'
tag="${benchmark##*_}"                          # just 'nullaway'

cf_type="CFNullness"
out_file="$RESULTS_DIR/${project}_${version}_${cf_type}_${tag}.txt"

rm -f "$out_file"
cp "$result_file" "$out_file"
done


# === STEP 3: Compile + collect logs for special projects ===
for project in "${!repos[@]}"; do
  for version in pre_nullaway post_nullaway; do
    proj_dir="$DATASET_DIR/${project}_${version}"
    echo "Processing $project ($version) in $proj_dir"

    # --- cache2k special: run in cache2k-api subdir ---
    if [[ "$project" == cache2k* ]]; then
      sub="$proj_dir/cache2k-api"
      if [ -d "$sub" ]; then
        cd "$sub"
        echo "🛠️  Patching pom.xml in $sub"
        find . -name "pom.xml" -type f -exec sed -i 's/3\.45\.1-SNAPSHOT/3.47.1-SNAPSHOT/g' {} +

        echo "Running mvn compile -PCheckerFramework"
        cf_type="CFNullness"
        version_clean="${version%%_*}"  # pre or post
        tag="${version#*_}"             # nullaway
        out_file="$RESULTS_DIR/${project}_${version_clean}_${cf_type}_${tag}.txt"

        mvn clean > /dev/null 2>&1
        rm -f "$out_file"
        mvn compile -PCheckerFramework > "$out_file" 2>&1
        echo -e "\n==== Count Script Output ====\n" >> "$out_file"
        bash "$COUNT_SCRIPT" >> "$out_file"

        cd "$BASE_DIR"
      fi
    fi

    # --- table-* projects: run in root ---
    if [[ "$project" == table-* ]]; then
      if [ -d "$proj_dir" ]; then
        cd "$proj_dir"
        echo "🛠️  Patching pom.xml in $proj_dir"
        find . -name "pom.xml" -type f -exec sed -i 's/3\.45\.1-SNAPSHOT/3.47.1-SNAPSHOT/g' {} +

        echo "Running mvn compile -PCheckerFramework"
        mvn clean > /dev/null 2>&1
        cf_type="CFNullness"
        version_clean="${version%%_*}"
        tag="${version#*_}"
        out_file="$RESULTS_DIR/${project}_${version_clean}_${cf_type}_${tag}.txt"

        rm -f "$out_file"
        mvn compile -PCheckerFramework > "$out_file" 2>&1
        echo -e "\n==== Count Script Output ====\n" >> "$out_file"
        bash "$COUNT_SCRIPT" >> "$out_file"

        cd "$BASE_DIR"
      fi
    fi

  done
done
