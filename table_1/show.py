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

def read_error_count(benchmark, checker, version):
    filename = f"{benchmark}_{version}_{checker}.txt"
    if os.path.exists(f"results/{filename}"):
        with open(f"results/{filename}", 'r') as f:
            lines = f.readlines()
            return lines[-1].strip()
    return "?"
    

# Header and separator
header = f"{'Project':<30} | {'CF PreV':>8} | {'CF PostV':>9} | {'NW PreV':>8} | {'NW PostV':>9}"
separator = "-" * len(header)

print(separator)
print(header)
print(separator)

# Rows
for benchmark in benchmarks.keys():
    name = benchmarks[benchmark]
    nullness_pre = read_error_count(name, "nullness", "pre")
    nullness_post = read_error_count(name, "nullness", "post")
    nullaway_pre = read_error_count(name, "nullaway", "pre")
    nullaway_post = read_error_count(name, "nullaway", "post")
    
    row = f"{name:<30} | {nullness_pre:>8} | {nullness_post:>9} | {nullaway_pre:>8} | {nullaway_post:>9}"
    print(row)