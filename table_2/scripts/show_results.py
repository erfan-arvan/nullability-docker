import csv

csv_file = 'project_diff_categories_counts.csv'

with open(csv_file, newline='', encoding='utf-8') as f:
    reader = csv.reader(f)
    rows = list(reader)

headers = rows[0]
if "Others_Info" not in headers:
    raise ValueError("Column 'Others_Info' not found.")
idx_to_remove = headers.index("Others_Info")

short_headers = ["Project"]
short_headers += [f"C{i}" for i in range(1, len(headers) - 2)]
short_headers.append("//Others:")

trimmed_rows = [row[:idx_to_remove] + row[idx_to_remove+1:] for row in rows]
trimmed_rows[0] = short_headers

col_widths = [max(len(row[i]) for row in trimmed_rows) for i in range(len(trimmed_rows[0]))]

def format_row(row):
    return " | ".join(cell.ljust(width) for cell, width in zip(row, col_widths))

print("-" * (sum(col_widths) + 3 * (len(col_widths) - 1)))
print(format_row(trimmed_rows[0]))
print("-" * (sum(col_widths) + 3 * (len(col_widths) - 1)))
for row in trimmed_rows[1:]:
    print(format_row(row))
print("-" * (sum(col_widths) + 3 * (len(col_widths) - 1)))

print("\nColumn descriptions:")
full_descriptions = headers[1:idx_to_remove] + [headers[-2]]
for i, desc in enumerate(full_descriptions):
    label = f"C{i+1}" if i < len(full_descriptions) - 1 else "//Others:"
    print(f"{label}: {desc}")

print("\n\"Other\" type of changes for each project")
print("-----------------------------------------")
print("PROJECT: Others_Info")
print("---------------------")
for row in rows[1:]:
    print(f"{row[0].ljust(8)}: {row[idx_to_remove]}\n")
