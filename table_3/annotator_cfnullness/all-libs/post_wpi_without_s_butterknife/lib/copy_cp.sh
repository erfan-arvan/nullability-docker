#!/bin/bash


CLASSPATH="/Users/erfanarvan/.sdkman/candidates/java/8.0.402-amzn/lib/tools.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.squareup/javapoet/1.10.0/712c178d35185d8261295913c9f2a7d6867a6007/javapoet-1.10.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auto.service/auto-service/1.0-rc4/44954d465f3b9065388bbd2fc08a3eb8fd07917c/auto-service-1.0-rc4.jar:/Users/erfanarvan/Desktop/comparisonPaper/experiments/organized/projects/butterknife/pre_wpi_with_s_butterknife/butterknife-annotations/build/classes/java/main:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auto/auto-common/0.10/c8f153ebe04a17183480ab4016098055fb474364/auto-common-0.10.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.guava/guava/24.0-jre/41ac1e74d6b4e1ea1f027139cffeb536c732a81/guava-24.0-jre.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.android.support/support-annotations/27.1.1/39ded76b5e1ce1c5b2688e1d25cdc20ecee32007/support-annotations-27.1.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.code.findbugs/jsr305/1.3.9/40719ea6961c0cb6afaeb6a921eaa1f6afd4cfdf/jsr305-1.3.9.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.checkerframework/checker-compat-qual/2.0.0/fc89b03860d11d6213d0154a62bcd1c2f69b9efa/checker-compat-qual-2.0.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.1.3/39b109f2cd352b2d71b52a3b5a1a9850e1dc304b/error_prone_annotations-2.1.3.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.j2objc/j2objc-annotations/1.1/ed28ded51a8b1c6b112568def5f4b455e6809019/j2objc-annotations-1.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.codehaus.mojo/animal-sniffer-annotations/1.14/775b7e22fb10026eed3f86e8dc556dfafe35f2d5/animal-sniffer-annotations-1.14.jar:/Users/erfanarvan/Desktop/comparisonPaper/experiments/organized/projects/butterknife/pre_wpi_with_s_butterknife/butterknife-compiler/build/classes/java/main:/Users/erfanarvan/Desktop/comparisonPaper/experiments/organized/projects/butterknife/pre_wpi_with_s_butterknife/butterknife-compiler/build/resources/main:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.truth/truth/0.42/b5768f644b114e6cf5c3962c2ebcb072f788dcbb/truth-0.42.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.guava/guava/25.1-android/bdaab946ca5ad20253502d873ba0c3313d141036/guava-25.1-android.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.code.findbugs/jsr305/3.0.2/25ea2e8b0c338a877313bd4672d3fe056ea78f0d/jsr305-3.0.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.checkerframework/checker-compat-qual/2.5.3/45f92d2e0676d05ae9297269b8268f93a875d4a/checker-compat-qual-2.5.3.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.3.1/a6a2b2df72fd13ec466216049b303f206bd66c5d/error_prone_annotations-2.3.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.checkerframework/checker-qual/2.5.3/4fe154d21bd734fe8c94ada37cdc41a9a6d61776/checker-qual-2.5.3.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.googlecode.java-diff-utils/diffutils/1.3.0/7e060dd5b19431e6d198e91ff670644372f60fbd/diffutils-1.3.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auto.value/auto-value-annotations/1.6.2/ed193d86e0af90cc2342aedbe73c5d86b03fa09b/auto-value-annotations-1.6.2.jar
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
