import os

def append_count_of_nullaway_errors(path):
    if not os.path.exists(path):
        return "X"

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
    return num


def reduction_percentage(original, new):
    if original == "X" or new == "X":
        return "X"
    if original == 0:
        return "X"
    original = int(original)
    new = int(new)
    return str(round((original - new) / original * 100, 2)) + "%"


def append(row):
    with open("reduction.csv", "a") as f:
        f.write(row)
    

reductions = {}
reductions["CFNullness"] = {}
reductions["NullAway"] = {}
reductions["CFNullness"]["WPI"] = []
reductions["CFNullness"]["Annotator"] = []
reductions["CFNullness"]["NullGTN"] = []
reductions["NullAway"]["WPI"] = []
reductions["NullAway"]["Annotator"] = []
reductions["NullAway"]["NullGTN"] = []
row = "Benchmark,CFNullness_Original,CFNullness_WPI,reduction,CFNullness_Annotator,reduction,CFNullness_NullGTN,reduction,NullAway_Original,NullAway_WPI,reduction,NullAway_Annotator,reduction,NullAway_NullGTN,reduction\n"
print(row)
append(row)
for benchmark in os.listdir("../NJR/original"):
    cf_original = append_count_of_nullaway_errors(f"scripts/checkers/cfnullness/original/{benchmark}.txt")
    cf_wpi = append_count_of_nullaway_errors(f"scripts/checkers/cfnullness/wpi/{benchmark}.txt")
    cf_wpi_reduction = reduction_percentage(cf_original, cf_wpi)
    cf_annotator = append_count_of_nullaway_errors(f"scripts/checkers/cfnullness/annotator/{benchmark}.txt")
    cf_annotator_reduction = reduction_percentage(cf_original, cf_annotator)
    cf_nullgtn = append_count_of_nullaway_errors(f"scripts/checkers/cfnullness/nullgtn/{benchmark}.txt")
    cf_nullgtn_reduction = reduction_percentage(cf_original, cf_nullgtn)
    
    nw_original = append_count_of_nullaway_errors(f"scripts/checkers/nullaway/original/{benchmark}.txt")
    nw_wpi = append_count_of_nullaway_errors(f"scripts/checkers/nullaway/wpi/{benchmark}.txt")
    nw_wpi_reduction = reduction_percentage(nw_original, nw_wpi)
    nw_annotator = append_count_of_nullaway_errors(f"scripts/checkers/nullaway/annotator/{benchmark}.txt")
    nw_annotator_reduction = reduction_percentage(nw_original, nw_annotator)
    nw_nullgtn = append_count_of_nullaway_errors(f"scripts/checkers/nullaway/nullgtn/{benchmark}.txt")
    nw_nullgtn_reduction = reduction_percentage(nw_original, nw_nullgtn)

    if(cf_wpi_reduction != "X"):
        reductions["CFNullness"]["WPI"].append(cf_wpi_reduction)
    if(cf_annotator_reduction != "X"):
        reductions["CFNullness"]["Annotator"].append(cf_annotator_reduction)
    if(cf_nullgtn_reduction != "X"):
        reductions["CFNullness"]["NullGTN"].append(cf_nullgtn_reduction)
    if(nw_wpi_reduction != "X"):
        reductions["NullAway"]["WPI"].append(nw_wpi_reduction)
    if(nw_annotator_reduction != "X"):
        reductions["NullAway"]["Annotator"].append(nw_annotator_reduction)
    if(nw_nullgtn_reduction != "X"):
        reductions["NullAway"]["NullGTN"].append(nw_nullgtn_reduction)

    row = f"{benchmark},{cf_original},{cf_wpi},{reduction_percentage(cf_original, cf_wpi)},{cf_annotator},{reduction_percentage(cf_original, cf_annotator)},{cf_nullgtn},{reduction_percentage(cf_original, cf_nullgtn)},{nw_original},{nw_wpi},{reduction_percentage(nw_original, nw_wpi)},{nw_annotator},{reduction_percentage(nw_original, nw_annotator)},{nw_nullgtn},{reduction_percentage(nw_original, nw_nullgtn)}"
    append(row + "\n")
    print(row)

print("CFNullness WPI Average reduction: " + str(round(sum([float(x[:-1]) for x in reductions["CFNullness"]["WPI"]]) / len(reductions["CFNullness"]["WPI"]), 2)) + "%")
print("CFNullness Annotator Average reduction: " + str(round(sum([float(x[:-1]) for x in reductions["CFNullness"]["Annotator"]]) / len(reductions["CFNullness"]["Annotator"]), 2)) + "%")
print("CFNullness NullGTN Average reduction: " + str(round(sum([float(x[:-1]) for x in reductions["CFNullness"]["NullGTN"]]) / len(reductions["CFNullness"]["NullGTN"]), 2)) + "%")

print("NullAway WPI Average reduction: " + str(round(sum([float(x[:-1]) for x in reductions["NullAway"]["WPI"]]) / len(reductions["NullAway"]["WPI"]), 2)) + "%")
print("NullAway Annotator Average reduction: " + str(round(sum([float(x[:-1]) for x in reductions["NullAway"]["Annotator"]]) / len(reductions["NullAway"]["Annotator"]), 2)) + "%")
print("NullAway NullGTN Average reduction: " + str(round(sum([float(x[:-1]) for x in reductions["NullAway"]["NullGTN"]]) / len(reductions["NullAway"]["NullGTN"]), 2)) + "%")

print("CFNulless WPI MIN-MAX reduction: " + str(min([float(x[:-1]) for x in reductions["CFNullness"]["WPI"]])) + " - " + str(max([float(x[:-1]) for x in reductions["CFNullness"]["WPI"]])) + "%")
print("CFNulless Annotator MIN-MAX reduction: " + str(min([float(x[:-1]) for x in reductions["CFNullness"]["Annotator"]])) + " - " + str(max([float(x[:-1]) for x in reductions["CFNullness"]["Annotator"]])) + "%")
print("CFNulless NullGTN MIN-MAX reduction: " + str(min([float(x[:-1]) for x in reductions["CFNullness"]["NullGTN"]])) + " - " + str(max([float(x[:-1]) for x in reductions["CFNullness"]["NullGTN"]])) + "%")

print("NullAway WPI MIN-MAX reduction: " + str(min([float(x[:-1]) for x in reductions["NullAway"]["WPI"]])) + " - " + str(max([float(x[:-1]) for x in reductions["NullAway"]["WPI"]])) + "%")
print("NullAway Annotator MIN-MAX reduction: " + str(min([float(x[:-1]) for x in reductions["NullAway"]["Annotator"]])) + " - " + str(max([float(x[:-1]) for x in reductions["NullAway"]["Annotator"]])) + "%")
print("NullAway NullGTN MIN-MAX reduction: " + str(min([float(x[:-1]) for x in reductions["NullAway"]["NullGTN"]])) + " - " + str(max([float(x[:-1]) for x in reductions["NullAway"]["NullGTN"]])) + "%")

