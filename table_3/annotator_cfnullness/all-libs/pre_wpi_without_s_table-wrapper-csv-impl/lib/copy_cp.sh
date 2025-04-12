# assign the correct value to CLASSPATH variable and run this in the lib dir of your project to copy all of the build classpaths to lib dir

#!/bin/bash


CLASSPATH="/Users/erfanarvan/.m2/repository/com/github/spacious-team/table-wrapper-api/791d437bdf/table-wrapper-api-791d437bdf.jar:/Users/erfanarvan/.m2/repository/com/univocity/univocity-parsers/2.9.1/univocity-parsers-2.9.1.jar:/Users/erfanarvan/.m2/repository/org/projectlombok/lombok/1.18.24/lombok-1.18.24.jar:/Users/erfanarvan/.m2/repository/org/checkerframework/checker-qual/3.26.0/checker-qual-3.26.0.jar:/Users/erfanarvan/.m2/repository/org/junit/jupiter/junit-jupiter/5.9.0/junit-jupiter-5.9.0.jar:/Users/erfanarvan/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.9.0/junit-jupiter-api-5.9.0.jar:/Users/erfanarvan/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar:/Users/erfanarvan/.m2/repository/org/junit/platform/junit-platform-commons/1.9.0/junit-platform-commons-1.9.0.jar:/Users/erfanarvan/.m2/repository/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar:/Users/erfanarvan/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.9.0/junit-jupiter-params-5.9.0.jar:/Users/erfanarvan/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.9.0/junit-jupiter-engine-5.9.0.jar:/Users/erfanarvan/.m2/repository/org/junit/platform/junit-platform-engine/1.9.0/junit-platform-engine-1.9.0.jar:/Users/erfanarvan/.m2/repository/org/mockito/mockito-junit-jupiter/4.8.1/mockito-junit-jupiter-4.8.1.jar:/Users/erfanarvan/.m2/repository/org/mockito/mockito-core/4.8.1/mockito-core-4.8.1.jar:/Users/erfanarvan/.m2/repository/net/bytebuddy/byte-buddy/1.12.16/byte-buddy-1.12.16.jar:/Users/erfanarvan/.m2/repository/net/bytebuddy/byte-buddy-agent/1.12.16/byte-buddy-agent-1.12.16.jar:/Users/erfanarvan/.m2/repository/org/objenesis/objenesis/3.2/objenesis-3.2.jar
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
