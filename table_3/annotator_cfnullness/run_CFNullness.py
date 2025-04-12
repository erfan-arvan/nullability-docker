import os
import shutil
import subprocess
import shlex
import zipfile
import sys

BENCHMARKS_FOLDER = "../dataset"
COMPILED_CLASSES_FOLDER = "cf_classes"
SRC_FILES = "cf_srcs.txt"
CF_BINARY = "../scripts/tools/checker-framework-3.34.0/checker/bin/javac"
CF_COMMAND = "-processor org.checkerframework.checker.nullness.NullnessChecker -Adetailedmsgtext -AassumePure"

def unzip_jars(lib_folder, extract_to):
    if os.path.exists(extract_to):
        shutil.rmtree(extract_to)
    os.makedirs(extract_to)
    jar_files = [os.path.join(lib_folder, f) for f in os.listdir(lib_folder) if f.endswith('.jar')]
    for jar_file in jar_files:
        with zipfile.ZipFile(jar_file, 'r') as jar:
            jar.extractall(extract_to)

i, total = 1, len(os.listdir(BENCHMARKS_FOLDER))
for benchmark in os.listdir(BENCHMARKS_FOLDER):
    print(f"{i} of {total}: {benchmark}", file=sys.stderr)

    benchmark_path = os.path.join(BENCHMARKS_FOLDER, benchmark)
    if not os.path.isdir(benchmark_path):
        i += 1
        continue

    lib_folder = os.path.join(benchmark_path, "lib")
    if not os.path.exists(lib_folder):
        print(f"Skipping {benchmark} — no lib directory found.", file=sys.stderr)
        i += 1
        continue

    if not os.path.exists(COMPILED_CLASSES_FOLDER):
        os.mkdir(COMPILED_CLASSES_FOLDER)

    find_srcs_command = f'find {benchmark_path}/src -name "*.java" > {SRC_FILES}'
    os.system(find_srcs_command)

    classpath_dir = os.path.join(benchmark_path, 'classpath')
    unzip_jars(lib_folder, classpath_dir)

    command = (
        f'{CF_BINARY} '
        f'{CF_COMMAND} '
        '-J-Xmx32G '
        '-Xmaxerrs 10000 '
        '-AassumePure '
        '-Adetailedmsgtext '
        '-AshowPrefixInWarningMessages '
        '-g '
        '-d '
        f'{COMPILED_CLASSES_FOLDER} '
        f'-cp "{classpath_dir}" '
        f'@{SRC_FILES}'
    )

    try:
        process = subprocess.run(shlex.split(command), stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        sys.stderr.write(process.stderr)  # this is where CF writes errors/warnings
    except Exception as e:
        print(f"Error running Checker Framework on {benchmark}: {e}", file=sys.stderr)

    shutil.rmtree(COMPILED_CLASSES_FOLDER)
    shutil.rmtree(classpath_dir)
    print("-------------------------------------------------- \n", file=sys.stderr)

    i += 1
