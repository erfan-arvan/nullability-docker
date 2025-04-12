import os
import re
import openpyxl

# Define the path to your files and the Excel workbook
files_path = "."  # Use the current directory
excel_path = "results.xlsx"  # Path to your Excel file

# Load the workbook and the sheet
workbook = openpyxl.load_workbook(excel_path)
sheet = workbook.active

# Define a dictionary to map project names to rows
project_row_mapping = {
    "butterknife": 2,
    "caffeine+nullaway": 3,
    "caffeine+CFNullness": 4,
    "Nameless": 5,
    "meal-planner": 6,
    "floatingActionButtonDial": 7,
    "table-wrapper-csv-impl": 8,
}

# Function to extract the number from the last line of a file
def extract_warnings_from_file(filepath):
    with open(filepath, 'r') as file:
        last_line = file.readlines()[-1]
        match = re.search(r'(\d+)\s+warnings', last_line)
        if match:
            return int(match.group(1))
    return None

# Mapping the files to the correct cells in the Excel sheet
for filename in os.listdir(files_path):
    if filename.endswith(".txt"):
        filepath = os.path.join(files_path, filename)
        warnings = extract_warnings_from_file(filepath)
        
        # Extract project name from filename and determine row
        for project, row in project_row_mapping.items():
            if project in filename:
                if warnings is not None:
                    if "pre_wpi_with_s" in filename:
                        if "before" in filename:
                            cell = "B" + str(row)
                        elif "after" in filename:
                            cell = "C" + str(row)
                    elif "pre_wpi_without_s" in filename:
                        if "before" in filename:
                            cell = "D" + str(row)
                        elif "after" in filename:
                            cell = "E" + str(row)
                    elif "post_wpi_with_s" in filename:
                        if "before" in filename:
                            cell = "F" + str(row)
                        elif "after" in filename:
                            cell = "G" + str(row)
                    elif "post_wpi_without_s" in filename:
                        if "before" in filename:
                            cell = "H" + str(row)
                        elif "after" in filename:
                            cell = "I" + str(row)

                    # Insert the warnings into the correct cell
                    sheet[cell] = warnings

# Save the updated workbook
workbook.save(excel_path)
