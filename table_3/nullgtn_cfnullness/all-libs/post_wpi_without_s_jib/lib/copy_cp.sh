#!/bin/bash


CLASSPATH="/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.cloud.tools/jib-build-plan/0.4.0/b16394e7eda9aeff338841c6dc47ed5a8a9d8120/jib-build-plan-0.4.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.http-client/google-http-client-apache-v2/1.42.2/d89fc070f427f8200b7401cda1ed4ce3004ec58d/google-http-client-apache-v2-1.42.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auth/google-auth-library-oauth2-http/1.10.0/4eb85f801b8424f9ba440a8520023e390135658c/google-auth-library-oauth2-http-1.10.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.http-client/google-http-client-gson/1.42.2/863fbfbdb35c203c43cefd9e3cc1e3a7c8e9f51e/google-http-client-gson-1.42.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.http-client/google-http-client/1.42.2/6d6ab266972f0afc6987fe3b88ce321179515fa3/google-http-client-1.42.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-compress/1.26.0/659feffdd12280201c8aacb8f7be94f9a883c824/commons-compress-1.26.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.truth.extensions/truth-java8-extension/1.1.5/e7ae0cd758528ea1c4d92f00eeb29dfa272cb84b/truth-java8-extension-1.1.5.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.truth/truth/1.1.5/93c2e0d029cea42ab21cfcaf8084a2ccfd10feef/truth-1.1.5.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/io.opencensus/opencensus-contrib-http-util/0.31.1/3c13fc5715231fadb16a9b74a44d9d59c460cfa8/opencensus-contrib-http-util-0.31.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.guava/guava/32.1.2-jre/5e64ec7e056456bef3a4bc4c6fdaef71e8ab6318/guava-32.1.2-jre.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.datatype/jackson-datatype-jsr310/2.15.2/30d16ec2aef6d8094c5e2dce1d95034ca8b6cb42/jackson-datatype-jsr310-2.15.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-databind/2.15.2/9353b021f10c307c00328f52090de2bdb4b6ff9c/jackson-databind-2.15.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-annotations/2.15.2/4724a65ac8e8d156a24898d50fd5dbd3642870b8/jackson-annotations-2.15.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.fasterxml.jackson.core/jackson-core/2.15.2/a6fe1836469a69b3ff66037c324d75fc66ef137c/jackson-core-2.15.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm/9.5/dc6ea1875f4d64fbc85e1691c95b96a3d8569c90/asm-9.5.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.mindrot/jbcrypt/0.4/af7e61017f73abb18ac4e036954f9f28c6366c07/jbcrypt-0.4.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.github.stefanbirkner/system-rules/1.19.0/d541c9a1cff0dda32e2436c74562e2e4aa6c88cd/system-rules-1.19.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/junit/junit/4.13.2/8ac9e16d933b6fb43bc7f576336b8f4d7eb5ba12/junit-4.13.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.mockito/mockito-core/4.11.0/ce5226440c2ee78915716d4ce3d10aed2dbf26fb/mockito-core-4.11.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-api/2.0.7/41eb7184ea9d556f23e18b5cb99cad1f8581fc00/slf4j-api-2.0.7.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.github.luben/zstd-jni/1.5.5-5/74ffdc5f140080adacf5278287aadd950179f848/zstd-jni-1.5.5-5.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpclient/4.5.13/e5f6cae5ca7ecaac1ec2827a9e2d65ae2869cada/httpclient-4.5.13.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpcore/4.4.15/7f2e0c573eaa7a74bac2e89b359e1f73d92a0a1d/httpcore-4.4.15.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.code.findbugs/jsr305/3.0.2/25ea2e8b0c338a877313bd4672d3fe056ea78f0d/jsr305-3.0.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.j2objc/j2objc-annotations/2.8/c85270e307e7b822f1086b93689124b89768e273/j2objc-annotations-2.8.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/io.opencensus/opencensus-api/0.31.1/66a60c7201c2b8b20ce495f0295b32bb0ccbbc57/opencensus-api-0.31.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auto.value/auto-value-annotations/1.10.1/9e5162c15f6033c524134cba05a5e93dc1d37c4b/auto-value-annotations-1.10.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auth/google-auth-library-credentials/1.10.0/ca4b2f754fa3abfcb57a3f8e56b153a2277d00b2/google-auth-library-credentials-1.10.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/commons-io/commons-io/2.15.1/f11560da189ab563a5c8e351941415430e9304ea/commons-io-2.15.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-lang3/3.14.0/1ed471194b02f2c6cb734a0cd6f6f107c673afae/commons-lang3-3.14.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.guava/failureaccess/1.0.1/1dcf1de382a0bf95a3d8b0849546c88bac1292c9/failureaccess-1.0.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/b421526c5f297295adef1c886e5246c39d4ac629/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.checkerframework/checker-qual/3.35.0/3baabaf8eecea6e9e22b8e39cd25953ff4813e0e/checker-qual-3.35.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.19.1/94c9e3872d81c44fdd7bbe76f96430df763a02af/error_prone_annotations-2.19.1.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/net.bytebuddy/byte-buddy/1.12.19/178d26e6a95e50502ae16673e08269797f8b254a/byte-buddy-1.12.19.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/net.bytebuddy/byte-buddy-agent/1.12.19/450917cf3b358b691a824acf4c67aa89c826f67e/byte-buddy-agent-1.12.19.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/commons-logging/commons-logging/1.2/4bfc12adfe4842bf07b657f0369c4cb522955686/commons-logging-1.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/commons-codec/commons-codec/1.11/3acb4705652e16236558f0f4f2192cc33c3bd189/commons-codec-1.11.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/io.grpc/grpc-context/1.27.2/1789190601b7a5361e4fa52b6bc95ec2cd71e854/grpc-context-1.27.2.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.9.0/8a1167e089096758b49f9b34066ef98b2f4b37aa/gson-2.9.0.jar:/Users/erfanarvan/Desktop/comparisonPaper/wpi-njr-debug/dataset/post_wpi_with_s_jib/jib-core/build/classes/java/main:/Users/erfanarvan/Desktop/comparisonPaper/wpi-njr-debug/dataset/post_wpi_with_s_jib/jib-core/build/resources/main:/Users/erfanarvan/Desktop/comparisonPaper/wpi-njr-debug/dataset/post_wpi_with_s_jib/jib-core/build/classes/java/test:/Users/erfanarvan/Desktop/comparisonPaper/wpi-njr-debug/dataset/post_wpi_with_s_jib/jib-core/build/resources/test:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.auto.value/auto-value-annotations/1.9/25a0fcef915f663679fcdb447541c5d86a9be4ba/auto-value-annotations-1.9.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/org.checkerframework/checker-qual/3.33.0/de2b60b62da487644fc11f734e73c8b0b431238f/checker-qual-3.33.0.jar:/Users/erfanarvan/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.18.0/89b684257096f548fa39a7df9fdaa409d4d4df91/error_prone_annotations-2.18.0.jar
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
