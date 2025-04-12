#!/bin/bash


CLASSPATH="/Users/erfanarvan/.gradle/caches/4.3.1/generated-gradle-jars/gradle-test-kit-4.3.1.jar:/Users/erfanarvan/.gradle/caches/4.3.1/generated-gradle-jars/gradle-api-4.3.1.jar:/Users/erfanarvan/.gradle/wrapper/dists/gradle-4.3.1-bin/7yzdu24db77gi3zukl2a7o0xx/gradle-4.3.1/lib/groovy-all-2.4.12.jar:/Users/erfanarvan/.gradle/wrapper/dists/gradle-4.3.1-bin/7yzdu24db77gi3zukl2a7o0xx/gradle-4.3.1/lib/gradle-installation-beacon-4.3.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.http-client/google-http-client/1.23.0/8e86c84ff3c98eca6423e97780325b299133d858/google-http-client-1.23.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-compress/1.15/b686cd04abaef1ea7bc5e143c080563668eec17e/commons-compress-1.15.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.guava/guava/23.5-jre/e9ce4989adf6092a3dab6152860e93d989e8cf88/guava-23.5-jre.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-databind/2.9.2/1d8d8cb7cf26920ba57fb61fa56da88cc123b21f/jackson-databind-2.9.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-api/1.7.25/da76ca59f6a57ee3102f8f9bd9cee742973efa8a/slf4j-api-1.7.25.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.mockito/mockito-core/2.12.0/a42fb82aa1eccb27c3f0371c8bc7385ae1c6598d/mockito-core-2.12.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.code.findbugs/jsr305/1.3.9/40719ea6961c0cb6afaeb6a921eaa1f6afd4cfdf/jsr305-1.3.9.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpclient/4.0.1/1d7d28fa738bdbfe4fbd895d9486308999bdf440/httpclient-4.0.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.objenesis/objenesis/2.6/639033469776fd37c08358c6b92a4761feb2af4b/objenesis-2.6.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.checkerframework/checker-qual/2.0.0/518929596ee3249127502a8573b2e008e2d51ed3/checker-qual-2.0.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.0.18/5f65affce1684999e2f4024983835efc3504012e/error_prone_annotations-2.0.18.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.j2objc/j2objc-annotations/1.1/ed28ded51a8b1c6b112568def5f4b455e6809019/j2objc-annotations-1.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.codehaus.mojo/animal-sniffer-annotations/1.14/775b7e22fb10026eed3f86e8dc556dfafe35f2d5/animal-sniffer-annotations-1.14.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-annotations/2.9.0/7c10d545325e3a6e72e06381afe469fd40eb701/jackson-annotations-2.9.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-core/2.9.2/aed20e50152a2f19adc1995c8d8f307c7efa414d/jackson-core-2.9.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/net.bytebuddy/byte-buddy/1.7.9/51218a01a882c04d0aba8c028179cce488bbcb58/byte-buddy-1.7.9.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/net.bytebuddy/byte-buddy-agent/1.7.9/a6c65f9da7f467ee1f02ff2841ffd3155aee2fc9/byte-buddy-agent-1.7.9.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpcore/4.0.1/e813b8722c387b22e1adccf7914729db09bcb4a9/httpcore-4.0.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/commons-logging/commons-logging/1.1.1/5043bfebc3db072ed80fbd362e7caf00e885d8ae/commons-logging-1.1.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/commons-codec/commons-codec/1.3/fd32786786e2adb664d5ecc965da47629dca14ba/commons-codec-1.3.jar:/Users/erfanarvan/Desktop/comparisonPaper/experiments/tests/jib/jib/jib-core/build/classes/java/main:/Users/erfanarvan/Desktop/comparisonPaper/experiments/tests/jib/jib/jib-core/build/resources/main:/Users/erfanarvan/Desktop/comparisonPaper/experiments/tests/jib/jib/jib-core/build/classes/java/test:/Users/erfanarvan/Desktop/comparisonPaper/experiments/tests/jib/jib/jib-core/build/resources/test
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
