import os
import re
import argparse

# Initialize counters
total_nullable_count = 0
total_nonnull_count = 0
total_nonnull_strict_count = 0
total_notnull_count = 0
total_not_null_strict_count = 0
total_monotonic_nonnull_count = 0
total_suppress_warnings_count = 0

suppress_warnings_lines = []  # To store the actual lines containing @SuppressWarnings

def count_annotations(file_path, include_suppress_warnings):
    global total_nullable_count, total_nonnull_count, total_nonnull_strict_count
    global total_notnull_count, total_not_null_strict_count, total_monotonic_nonnull_count
    global total_suppress_warnings_count, suppress_warnings_lines

    with open(file_path, 'r') as file:
        lines = file.readlines()

    content = ''.join(lines)

    # Regex patterns
    nullable_pattern = r'@\bNullable\b'
    nonnull_pattern = r'@\bNonNull\b'
    nonnull_strict_pattern = r'@\bNonnull\b'
    notnull_pattern = r'@\bNotnull\b'
    not_null_strict_pattern = r'@\bNotNull\b'
    monotonic_nonnull_pattern = r'@\bMonotonicNonNull\b'
    suppress_warnings_pattern = r'@(?:java\.lang\.)?SuppressWarnings\([^\)]*\)'

    # Count matches
    total_nullable_count += len(re.findall(nullable_pattern, content))
    total_nonnull_count += len(re.findall(nonnull_pattern, content))
    total_nonnull_strict_count += len(re.findall(nonnull_strict_pattern, content))
    total_notnull_count += len(re.findall(notnull_pattern, content))
    total_not_null_strict_count += len(re.findall(not_null_strict_pattern, content))
    total_monotonic_nonnull_count += len(re.findall(monotonic_nonnull_pattern, content))

    if include_suppress_warnings:
        for line in lines:
            if re.search(suppress_warnings_pattern, line):
                suppress_warnings_lines.append(f"{file_path}: {line.strip()}")
                total_suppress_warnings_count += 1  # Count line here

def process_directory(directory, include_suppress_warnings):
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                count_annotations(file_path, include_suppress_warnings)

def write_results_to_file(output_path, include_suppress_warnings):
    with open(output_path, 'a') as f:
        f.write("\nTotal annotation occurrences:\n")
        f.write(f'  @Nullable: {total_nullable_count}\n')
        f.write(f'  @NonNull: {total_nonnull_count}\n')
        f.write(f'  @Nonnull: {total_nonnull_strict_count}\n')
        f.write(f'  @Notnull: {total_notnull_count}\n')
        f.write(f'  @NotNull: {total_not_null_strict_count}\n')
        f.write(f'  @MonotonicNonNull: {total_monotonic_nonnull_count}\n')
        if include_suppress_warnings:
            f.write(f'  @SuppressWarnings: {total_suppress_warnings_count}\n')
            f.write("\nLines containing @SuppressWarnings:\n")
            for line in suppress_warnings_lines:
                f.write(f"{line}\n")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Count specific annotations in Java files.')
    parser.add_argument('--src_directory', type=str, default='./src', help='The source directory to scan.')
    parser.add_argument('--include_suppress_warnings', action='store_true', help='Include @SuppressWarnings annotations in the count.')
    parser.add_argument('--output_file', type=str, required=True, help='Path to the file where results should be appended.')

    args = parser.parse_args()

    process_directory(args.src_directory, args.include_suppress_warnings)

    # Print totals to console
    print("\nTotal annotation occurrences:")
    print(f'  @Nullable: {total_nullable_count}')
    print(f'  @NonNull: {total_nonnull_count}')
    print(f'  @Nonnull: {total_nonnull_strict_count}')
    print(f'  @Notnull: {total_notnull_count}')
    print(f'  @NotNull: {total_not_null_strict_count}')
    print(f'  @MonotonicNonNull: {total_monotonic_nonnull_count}')
    if args.include_suppress_warnings:
        print(f'  @SuppressWarnings: {total_suppress_warnings_count}')
        print(f"\n✅ {len(suppress_warnings_lines)} lines with @SuppressWarnings saved.")

    write_results_to_file(args.output_file, args.include_suppress_warnings)
    print(f"\n✅ Results appended to: {args.output_file}")
