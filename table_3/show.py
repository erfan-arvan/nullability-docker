import os

benchmarks = {
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

def read_error_count(benchmark, checker, version, tool):
    filename = f"{benchmark}_{version}_{checker}_{tool}.txt"
    if os.path.exists(f"results/{filename}"):
        with open(f"results/{filename}", 'r') as f:
            lines = f.readlines()
            return lines[-1].strip()
    return "?"
    

header = (
    f"{'Benchmark':<30} | {'WPI-CF-Pre':>10} | {'WPI-CF-Post':>12} | {'WPI-NW-Pre':>11} | {'WPI-NW-Post':>12} | "
    f"{'ANN-CF-Pre':>10} | {'ANN-CF-Post':>12} | {'ANN-NW-Pre':>11} | {'ANN-NW-Post':>12} | "
    f"{'NGT-CF-Pre':>10} | {'NGT-CF-Post':>12} | {'NGT-NW-Pre':>11} | {'NGT-NW-Post':>12}"
)

# Separator
separator = "-" * len(header)

# Print header
print(separator)
print(header)
print(separator)

# Print each row
for benchmark in benchmarks.keys():
    name = benchmarks[benchmark]
    
    # Annotator
    nullness_pre_ann = read_error_count(name, "nullness", "pre", "ann")
    nullness_post_ann = read_error_count(name, "nullness", "post", "ann")
    nullaway_pre_ann = read_error_count(name, "nullaway", "pre", "ann")
    nullaway_post_ann = read_error_count(name, "nullaway", "post", "ann")
    
    # WPI
    nullness_pre_wpi = read_error_count(name, "nullness", "pre", "wpi")
    nullness_post_wpi = read_error_count(name, "nullness", "post", "wpi")
    nullaway_pre_wpi = read_error_count(name, "nullaway", "pre", "wpi")
    nullaway_post_wpi = read_error_count(name, "nullaway", "post", "wpi")
    
    # NullGTN
    nullness_pre_ngt = read_error_count(name, "nullness", "pre", "ngt")
    nullness_post_ngt = read_error_count(name, "nullness", "post", "ngt")
    nullaway_pre_ngt = read_error_count(name, "nullaway", "pre", "ngt")
    nullaway_post_ngt = read_error_count(name, "nullaway", "post", "ngt")
    
    # Print row
    row = (
        f"{name:<30} | {nullness_pre_wpi:>10} | {nullness_post_wpi:>12} | {nullaway_pre_wpi:>11} | {nullaway_post_wpi:>12} | "
        f"{nullness_pre_ann:>10} | {nullness_post_ann:>12} | {nullaway_pre_ann:>11} | {nullaway_post_ann:>12} | "
        f"{nullness_pre_ngt:>10} | {nullness_post_ngt:>12} | {nullaway_pre_ngt:>11} | {nullaway_post_ngt:>12}"
    )
    print(row)
