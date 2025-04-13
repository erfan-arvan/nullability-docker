import os


names = {
    "butterknife" : "ButterKnife",
    "cache2k_cf" : "Cache2k-CF",
    "cache2k_nw" : "Cache2k-NW",
    "floating" : "FloatingActionButtonSpeedDial",
    "jib" : "Jib",
    "mealplanner" : "Meal-Planner",
    "nameless" : "Nameless",
    "picasso"  : "Picasso",
    "table_api" : "Table-wrapper-api",
    "table_csv" : "Table-wrapper-csv-impl",
}

OUTPUT_DIR = "/home/nima/Desktop/nullability-docker/table_3/results"

def append_count_of_nullaway_errors(path):
    # start from last line stop at line with format regex "X errors" and extract the number
    num = 0
    with open(path, 'r') as f:
        lines = f.readlines()
        for line in lines[::-1]:
            if "errors" in line or "error" in line:
                if(len(line.split(" ")) != 2):
                    continue
                num = line.split(" ")[0]
                break
    # append the number to the file
    with open(path, 'a') as f:
        f.write("{}\n".format(num))
                

for benchmark in names.keys():
    for tool in ["ann", "wpi", "ngt"]:
        print(f"Running NullAway on benchmark: {names[benchmark]} with tool: {tool.upper()}")
        # pre
        print("Running on PreV version")
        os.system(f"cd versions/nullaway/{benchmark}-{tool}-pre/src && ./gradlew build -x test --rerun-tasks 2> {OUTPUT_DIR}/{names[benchmark]}_pre_nullaway_{tool}.txt")
        append_count_of_nullaway_errors(f"{OUTPUT_DIR}/{names[benchmark]}_pre_nullaway_{tool}.txt")
        # post
        print("Running on PostV version")
        os.system(f"cd versions/nullaway/{benchmark}-{tool}-post/src && ./gradlew build -x test --rerun-tasks 2> {OUTPUT_DIR}/{names[benchmark]}_post_nullaway_{tool}.txt")
        append_count_of_nullaway_errors(f"{OUTPUT_DIR}/{names[benchmark]}_post_nullaway_{tool}.txt")