# assign the correct value to CLASSPATH variable and run this in the lib dir of your project to copy all of the build classpaths to lib dir

#!/bin/bash


CLASSPATH="/Users/erfanarvan/.m2/repository/com/google/code/gson/gson/2.9.0/gson-2.9.0.jar:/Users/erfanarvan/.m2/repository/com/google/guava/guava/23.0/guava-23.0.jar:/Users/erfanarvan/.m2/repository/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:/Users/erfanarvan/.m2/repository/com/google/errorprone/error_prone_annotations/2.0.18/error_prone_annotations-2.0.18.jar:/Users/erfanarvan/.m2/repository/com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar:/Users/erfanarvan/.m2/repository/org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar:/Users/erfanarvan/.m2/repository/org/slf4j/slf4j-api/2.0.0-alpha7/slf4j-api-2.0.0-alpha7.jar:/Users/erfanarvan/.m2/repository/org/jetbrains/annotations/23.0.0/annotations-23.0.0.jar:/Users/erfanarvan/.m2/repository/com/github/mizosoft/methanol/methanol/1.6.0/methanol-1.6.0.jar
"

# Define the target directory as the current directory
TARGET_DIR="."

# Split the classpath into an array
IFS=':' read -r -a paths <<< "$CLASSPATH"

# Create a temporary directory for extracting AAR files
TEMP_DIR=$(mktemp -d)

# Function to extract JAR files from AAR files and copy them to the target directory
extract_jar_from_aar() {
    aar_file=$1
    aar_name=$(basename "$aar_file" .aar)
    dest_dir="$TARGET_DIR"

    unzip -o "$aar_file" -d "$TEMP_DIR" > /dev/null
    
    if [ -f "$TEMP_DIR/classes.jar" ]; then
        echo "Copying classes.jar from $aar_file to $dest_dir/$aar_name.jar"
        mv "$TEMP_DIR/classes.jar" "$dest_dir/$aar_name.jar"
    else
        echo "classes.jar not found in $aar_file"
    fi
    
    if [ -d "$TEMP_DIR/libs" ]; then
        for jar in "$TEMP_DIR/libs/"*.jar; do
            if [ -f "$jar" ]; then
                jar_name=$(basename "$jar")
                echo "Copying $jar from $aar_file to $dest_dir/$aar_name-$jar_name"
                mv "$jar" "$dest_dir/$aar_name-$jar_name"
            else
                echo "No JAR files found in libs directory of $aar_file"
            fi
        done
    else
        echo "libs directory not found in $aar_file"
    fi
    
    # Clean up the temporary directory after processing each AAR
    rm -rf "$TEMP_DIR"/*
}

# Copy each item in the classpath to the target directory
for path in "${paths[@]}"; do
    if [[ "$path" == *.aar ]]; then
        echo "Extracting JAR from $path"
        extract_jar_from_aar "$path"
    elif [[ "$path" == *.jar ]]; then
        jar_name=$(basename "$path")
        echo "Copying JAR from $path to $TARGET_DIR"
        cp -rf "$path" "$TARGET_DIR/$jar_name"
    fi
done

# Clean up the temporary directory
rm -rf "$TEMP_DIR"

echo "Classpath files have been copied to $TARGET_DIR"
