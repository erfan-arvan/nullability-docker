#!/bin/bash

# if fresh passed rerun nullaway.py
if [ "$1" == "fresh" ]; then
    python3 nullaway.py
fi

# if fresh passed, run cfnullness and copy results
if [ "$1" == "fresh" ]; then
    cd cfnullness
    ./run.sh
    cp results/* ../results/
    cd ..
fi

bash rename.sh
bash clean_txt_files.sh
python3 show.py
