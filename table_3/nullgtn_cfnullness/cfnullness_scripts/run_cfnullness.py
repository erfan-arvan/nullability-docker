import os
import shutil
import time
import subprocess
import shlex
import zipfile

BENCHMARKS_FOLDER = "../dataset"
RESULTS_FOLDER = "checkerframework_3.34.0_results_WPI"
COMPILED_CLASSES_FOLDER = "cf_classes"
SRC_FILES = "cf_srcs.txt"
CF_BINARY = "../scripts/tools/checker-framework-3.34.0/checker/bin/javac"
CF_COMMAND = "-processor org.checkerframework.checker.nullness.NullnessChecker -Adetailedmsgtext -AassumePure"
SKIP_COMPLETED = False  # skips if the output file is already there.

if not os.path.exists(RESULTS_FOLDER):
    os.mkdir(RESULTS_FOLDER)

def unzip_jars(lib_folder, extract_to):
    if os.path.exists(extract_to):
        shutil.rmtree(extract_to)
    os.makedirs(extract_to)
    jar_files = [os.path.join(lib_folder, f) for f in os.listdir(lib_folder) if f.endswith('.jar')]
    for jar_file in jar_files:
        with zipfile.ZipFile(jar_file, 'r') as jar:
            jar.extractall(extract_to)

print("Completed Benchmarks")
i, total = 1, len(os.listdir(BENCHMARKS_FOLDER))
for benchmark in os.listdir(BENCHMARKS_FOLDER):
    print(f"{i} of {total}: {benchmark}")
    if SKIP_COMPLETED:
        if os.path.exists(f'{RESULTS_FOLDER}/{benchmark}-before-wpi.txt'):
            print("skipping completed benchmark.")
            i += 1
            continue
        else:
            print("running benchmark.")

    if not os.path.isdir(f'{BENCHMARKS_FOLDER}/{benchmark}'):
        continue

    lib_folder = f'{BENCHMARKS_FOLDER}/{benchmark}/lib'
    if not os.path.exists(lib_folder):
        print(f"Skipping {benchmark} — no lib directory found.")
        i += 1
        continue

    if not os.path.exists(COMPILED_CLASSES_FOLDER):
        os.mkdir(COMPILED_CLASSES_FOLDER)

    find_srcs_command = f'find {BENCHMARKS_FOLDER}/{benchmark}/src -name "*.java" > {SRC_FILES}'
    os.system(find_srcs_command)

    classpath_dir = os.path.join(BENCHMARKS_FOLDER, benchmark, 'classpath')
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
        f'@{SRC_FILES} '
        f'2> {RESULTS_FOLDER}/{benchmark}-before-wpi.txt'
    )

    os.system(command)

    shutil.rmtree(COMPILED_CLASSES_FOLDER)
    shutil.rmtree(classpath_dir)
    print("-------------------------------------------------- \n")

    i += 1
