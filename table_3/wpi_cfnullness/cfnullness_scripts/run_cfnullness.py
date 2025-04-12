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
SKIP_COMPLETED = False
WPI_TIMEOUT = 6 * 60 * 60

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
        after_file = f'{RESULTS_FOLDER}/{benchmark}-after-wpi.txt'
        if os.path.exists(after_file):
            print(f"Skipping {benchmark} — after-wpi.txt already exists.")
            i += 1
            continue
        else:
            print("Running benchmark.")

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
        '2> /dev/null'
    )

    os.system(command)

    time_start = time.time()
    print(f"Running WPI on {benchmark}")
    wpi_log_file = f'{BENCHMARKS_FOLDER}/{benchmark}/wpi-log.txt'
    with open(wpi_log_file, 'w') as log_file:
        try:
            subprocess.run(shlex.split(f'./wpi/wpi.sh {BENCHMARKS_FOLDER}/{benchmark}'), stdout=log_file, stderr=subprocess.STDOUT, check=True)
        except subprocess.CalledProcessError as e:
            print(f"Error running WPI on {benchmark}: {e}")
            print("-------------------------------------------------- \n")
            continue
        except subprocess.TimeoutExpired:
            print(f"Command timed out after {WPI_TIMEOUT} seconds")
            process.kill()
            process.wait()
            os.system("killall -9 java")
            continue

    command = (
        f'{CF_BINARY} '
        f'{CF_COMMAND} '
        f'-Aajava={BENCHMARKS_FOLDER}/{benchmark}/wpi-out '
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
        f'2> {RESULTS_FOLDER}/{benchmark}-after-wpi.txt'
    )

    os.system(command)
    time_end = time.time()
    total_time = time_end - time_start
    with open("checkerframework_timings.csv", "a") as f:
        f.write(f'{benchmark},{str(total_time)}\n')

    shutil.rmtree(COMPILED_CLASSES_FOLDER)
    shutil.rmtree(classpath_dir)
    print("-------------------------------------------------- \n")

    i += 1
