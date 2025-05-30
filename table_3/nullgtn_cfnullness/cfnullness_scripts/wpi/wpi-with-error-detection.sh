#!/bin/bash

# This script is a template for the WPI loop for a project with -Ainfer=ajava
# added to its build file. Fill in the variables at the beginning of the
# script with values that make sense for your project; the values there
# now are examples.

set -eux

# This scripts the project's path as an argument to run the WPI on.
PROJECT_PATH=$1

# Global variables
CF_BINARY="../scripts/tools/checker-framework-3.34.0/checker/bin/javac"
COMPILED_CLASSES_FOLDER="$PROJECT_PATH/classes"
LIB_FOLDER="$PROJECT_PATH/lib"
SRC_FILES="$PROJECT_PATH/cf_srcs.txt"
CLASSPATH_DIR="$PROJECT_PATH/classpath"

echo "CF_BINARY: $CF_BINARY"

# Function to unzip all JAR files into a single directory
unzip_jars() {
    local lib_folder=$1
    local extract_to=$2
    if [ -d "$extract_to" ]; then
        rm -rf "$extract_to"
    fi
    mkdir -p "$extract_to"
    for jar_file in "$lib_folder"/*.jar; do
    unzip -o -q ../dataset/pre_wpi_with_s_floatingActionButtonSpeedDial/lib/res.jar -d ../dataset/pre_wpi_with_s_floatingActionButtonSpeedDial/classpath || true
    done
}

# Unzip all JAR files into the classpath directory
unzip_jars "$LIB_FOLDER" "$CLASSPATH_DIR" || true

# Populate the source files information in SRC_FILES
find $PROJECT_PATH/src -name "*.java" > "$SRC_FILES" || true

# The compile and clean commands for the project's build system.
BUILD_CMD="$CF_BINARY \
-processor org.checkerframework.checker.nullness.NullnessChecker \
-AassumePure \
-Adetailedmsgtext \
-Aajava=$PROJECT_PATH/wpi-out \
-Ainfer=ajava \
-Awarns \
-AshowPrefixInWarningMessages \
-J-Xmx32G \
-Xmaxerrs 10000 \
-g \
-d $COMPILED_CLASSES_FOLDER \
-cp $CLASSPATH_DIR \
@$SRC_FILES"


CLEAN_CMD="rm -rf $COMPILED_CLASSES_FOLDER"

$BUILD_CMD # Compile the program so that WPIOUTDIR is created.

# Where should the output be placed at the end? This directory is also
# used to store intermediate WPI results. The directory does not need to
# exist. If it does exist when this script starts, it will be deleted.
# If you are using the subprojects script, set WPITEMPDIR to "$1"
WPITEMPDIR=$PROJECT_PATH/wpi-out
# Where is WPI's output placed by the Checker Framework? This is some
# directory ending in build/whole-program-inference. For most projects,
# this directory is just ./build/whole-program-inference .
# The example in this script is the output directory when running via the gradle plugin.
# (The CF automatically puts all WPI outputs in ./build/whole-program-inference,
# where . is the directory from which the javac command was invoked (ie, javac's
# working directory). In many build systems (e.g., Maven), that directory would be the project.
# But, some build systems, such as Gradle, cache build outputs in a central location
# per-machine, and as part of that it runs its builds from that central location.)
# The directory to use here might vary between build systems, between machines
# (e.g., depending on your local Gradle settings), and even between projects using the
# same build system (e.g., because of a project's settings.gradle file).

# Program needs to be compiled before running the script so WPI creates this directory.
# If you are using the subprojects script, set WPIOUTDIR to "$2"
WPIOUTDIR=build/whole-program-inference

# Whether to run in debug mode. In debug mode, output is printed to the terminal
# at the beginning of each iteration, and the diff between each pair of iterations is
# saved in a file named iteration$count.diff, starting with iteration1.diff.
# (Note that these files are overwritten if they already exist.)
DEBUG=1

# End of variables. You probably don't need to make changes below this line.

rm -rf ${WPITEMPDIR}
mkdir -p ${WPITEMPDIR}
# Clean up
rm -f iteration*.diff

# Store all the intermediate ajava files for each iteration
WPIITERATIONOUTPUTS=$PROJECT_PATH/wpi-iterations
rm -rf ${WPIITERATIONOUTPUTS} || true
mkdir -p ${WPIITERATIONOUTPUTS} || true

count=1
while : ; do
    if [[ ${DEBUG} == 1 ]]; then
        SECONDS=0
        echo "entering iteration ${count}"
    fi
    $BUILD_CMD
    $CLEAN_CMD
    # This mkdir is needed when the project has subprojects.
    mkdir -p "${WPITEMPDIR}"
    mkdir -p "${WPIOUTDIR}"

    DIFF_RESULT=$(diff -r ${WPITEMPDIR} ${WPIOUTDIR} || true)
    if [[ ${DEBUG} == 1 ]]; then
        echo "putting the diff for iteration $count into $(realpath iteration$count.diff)"
        echo ${DIFF_RESULT} > iteration$count.diff
    fi
    [[ "$DIFF_RESULT" != "" ]] || break
    rm -rf ${WPITEMPDIR}
    mv ${WPIOUTDIR} ${WPITEMPDIR}
    # Also store the intermediate WPI results
    mkdir -p "${WPIITERATIONOUTPUTS}/iteration${count}"
    cp -rf ${WPITEMPDIR}/* "${WPIITERATIONOUTPUTS}/iteration${count}"
    echo "ending iteration ${count}, time taken: $SECONDS seconds"
    echo
    ((count++))

    if [ $count -eq 11 ]; then
        break
    fi
done

# Clean up
# rm -f $SRC_FILES iteration*.diff
# rm -rf $PROJECT_PATH/build build