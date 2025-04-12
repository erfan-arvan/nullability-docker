#!/bin/bash
set -e

ROOT_DIR=$(pwd)

# Check if the first argument is "fresh"
IS_FRESH=false
if [[ "$1" == "fresh" ]]; then
  IS_FRESH=true
  echo "> Fresh run requested. Cleaning up old data..."

  # Remove old project directories
  rm -rf "$ROOT_DIR/projects_for_annotation_counts_study"
  rm -rf "$ROOT_DIR/projects_for_diff_counts"

  # Remove contents of results subfolders if they exist
  rm -f "$ROOT_DIR/results/diffs/"*
  rm -f "$ROOT_DIR/results/annotated_diffs/"*
  rm -f "$ROOT_DIR/results/counts/"*

  echo ""
  echo "> Running annotation count analysis..."
  bash "$ROOT_DIR/run_annotation_counts_study.sh"

  echo ""
  echo "> Running diff category analysis..."
  bash "$ROOT_DIR/run_diff_categories_study.sh"

else
  echo ""
  echo "> Note: You ran this without 'fresh', so existing repositories were reused."
  echo "> To freshly clone all repositories and rerun everything, run the script with: bash $0 fresh"
  echo ""
fi

echo "------------------------------------------------------------"
echo "> All processing complete."
echo ""
echo "> Diff files:                 $ROOT_DIR/results/diffs"
echo "> Annotated diff files:       $ROOT_DIR/results/annotated_diffs"
echo "> Annotation counts (post):   $ROOT_DIR/results/counts"
echo "> Change category counts:     $ROOT_DIR/project_diff_categories_counts.csv"
echo ""
echo "> To view the category summary, run:"
echo ">     python3 scripts/show_results.py"
echo "------------------------------------------------------------"
