#!/bin/bash

set -e

# Traverse all 2-level deep subfolders
for dir in table_2/*/*; do
  if [ -d "$dir" ]; then
    echo ""
    echo "🔍 Processing $dir"

    # Check for existing .git
    if [ -d "$dir/.git" ]; then
      url=$(git -C "$dir" config --get remote.origin.url)
    else
      echo "⚠️  Skipping $dir (no .git directory)"
      continue
    fi

    if [ -z "$url" ]; then
      echo "⚠️  Skipping $dir (no remote URL)"
      continue
    fi

    # Remove from index forcibly in case it was already added
    git rm --cached -r -f "$dir" > /dev/null 2>&1 || true

    # Clean up local .git folder to prepare for submodule add
    rm -rf "$dir/.git"

    echo "📌 Adding submodule: $url -> $dir"
    git submodule add "$url" "$dir" || {
      echo "❌ Failed to add $dir — skipping"
      continue
    }
  fi
done

# Commit changes if any
git add .gitmodules
git commit -m "Added all valid submodules with multiple versions handled" || echo "✅ No changes to commit"
