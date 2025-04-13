#!/bin/bash

# if fresh passed rerun nullaway.py
if [ "$1" == "--fresh" ]; then
    python3 scripts/run_nullaway.py
    python3 scripts/run_cfnullness.py
fi

python3 count_error_reduction.py