#!/bin/bash

# if fresh passed rerun nullaway.py
if [ "$1" == "fresh" ]; then
    python3 nullaway.py
fi

# if fresh passed rerun nullness.py
if [ "$1" == "fresh" ]; then
    python3 nullness.py

    cd wpi_cfnullness
    bash run2.sh
    cp results/* ../results/
    cd ..

    cd nullgtn_cfnullness
    bash run2.sh
    cp results/* ../results/
    cd ..

    cd annotator_cfnullness
    bash run2.sh
    cp results/* ../results/
    cd ..
fi

bash rename.sh
bash clean_txt_files.sh
python3 show.py

