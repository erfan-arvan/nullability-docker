'''
This script runs error-prone on all the benchmarks.
Fill in the correct values for the macros at
the top of the file before executing.
'''
import os
import shutil

CURRENT_DIR = os.path.dirname(os.path.realpath(__file__))
BENCHMARKS_FOLDER = "../../NJR/original"
RESULTS_FOLDER = "annotator-execution-outputs"
COMPILED_CLASSES_FOLDER = "ep_classes"
SRC_FILES = "ep_srcs.txt"
ERRORPRONE_DIR = "tools/error_prone"
ANNOTATOR_DIR = "tools/annotator"
ANNOTATOR_OUT = f"{CURRENT_DIR}/annotator-out"
ERRORPRONE_JARS = f'{ERRORPRONE_DIR}/error_prone_core-2.5.1-with-dependencies.jar:{ERRORPRONE_DIR}/dataflow-nullaway-3.26.0.jar:{ERRORPRONE_DIR}/nullaway-0.10.10.jar:{ANNOTATOR_DIR}/scanner.jar'
ERRORPRONE_COMMAND = f"-XDcompilePolicy=simple -processorpath {ERRORPRONE_JARS} '-Xplugin:ErrorProne -XepDisableAllChecks -Xep:AnnotatorScanner:ERROR -XepOpt:AnnotatorScanner:ConfigPath={ANNOTATOR_OUT}/scanner.xml -Xep:NullAway:ERROR -XepOpt:NullAway:SerializeFixMetadata=true -XepOpt:NullAway:FixSerializationConfigPath={ANNOTATOR_OUT}/nullaway.xml -XepOpt:NullAway:AnnotatedPackages="
SKIP_COMPLETED = False #skips if the output file is already there.
TIMEOUT = 1800
TIMEOUT_CMD = "timeout"
ANNOTATOR_JAR = f'{CURRENT_DIR}/{ANNOTATOR_DIR}/annotator.jar'

def prepare():
    os.makedirs(ANNOTATOR_OUT, exist_ok=True)
    with open(f'{ANNOTATOR_OUT}/paths.tsv', 'w') as o:
        o.write("{}\t{}\n".format(f'{ANNOTATOR_OUT}/nullaway.xml', f'{ANNOTATOR_OUT}/scanner.xml'))

#create the output folder if it doesn't exist
if not os.path.exists(RESULTS_FOLDER):
    os.mkdir(RESULTS_FOLDER)

#Loop through the benchmarks
print("Completed Benchmarks")
for benchmark in os.listdir(BENCHMARKS_FOLDER):
    # print(benchmark)
    if (SKIP_COMPLETED):
        if os.path.exists(f'{RESULTS_FOLDER}/{benchmark}.txt'):
            print("skipping completed benchmark.")
            continue
    #skip non-directories
    if not os.path.isdir(f'{BENCHMARKS_FOLDER}/{benchmark}'):
        continue
    
    #create a folder for the compiled classes if it doesn't exist
    if not os.path.exists(COMPILED_CLASSES_FOLDER):
        os.mkdir(COMPILED_CLASSES_FOLDER)

    #Get a list of Java source code files.
    find_srcs_command = f'find {BENCHMARKS_FOLDER}/{benchmark}/src -name "*.java" > {SRC_FILES}'
    os.system(find_srcs_command)

    src = open(f'{BENCHMARKS_FOLDER}/{benchmark}/info/sources', "r").readlines()
    src = [x.strip() for x in src]
    src = [x[4:x.rfind("/")] for x in src]
    src = [x.replace("/", ".") for x in src]
    src = set(src)
    if '' in src:
        src.remove('')
    src = ",".join(src)
    if(src == ""):
        continue

    #get folder with libraries used by benchmark
    lib_folder = f'{BENCHMARKS_FOLDER}/{benchmark}/lib'

    #build source files
    build_command = (TIMEOUT_CMD 
        + " " + str(TIMEOUT)
        + " " + "javac -d"
        + " " + COMPILED_CLASSES_FOLDER
        + " " + " -cp " + lib_folder + f":{ERRORPRONE_DIR}/nullaway-annotations-0.10.22.jar:{ERRORPRONE_DIR}/jsr305-3.0.1.jar" 
        + " " + ERRORPRONE_COMMAND + src + "'"
        + " " + "-Xmaxerrs 10000"
        + " " + "-J-Xmx32G"
        + " @" + SRC_FILES
        + " 2> " +  RESULTS_FOLDER
        + "/" + benchmark + ".txt"
    )
    # print(build_command)
    # os.system(build_command)
    prepare()
    print("working on: " + benchmark)

    commands = []
    commands += ["java", "-jar", ANNOTATOR_JAR]
    commands += ['-d', ANNOTATOR_OUT]
    commands += ['-bc', f'{build_command}']
    commands += ['-cp', f'{ANNOTATOR_OUT}/paths.tsv']
    commands += ['-i', 'com.uber.nullaway.annotations.Initializer']
    commands += ['-n', 'javax.annotation.Nullable']
    commands += ['-cn', 'NullAway']
    commands += ["--depth", "10"]

    os.system(" ".join(commands))

    #remove the classes folder
    shutil.rmtree(COMPILED_CLASSES_FOLDER)