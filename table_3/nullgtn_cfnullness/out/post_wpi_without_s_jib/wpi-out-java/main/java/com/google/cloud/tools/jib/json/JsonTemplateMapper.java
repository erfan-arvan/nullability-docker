/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.tools.jib.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.Blobs;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// TODO: Add JsonFactory for HTTP response parsing.
/**
 * Helper class for serializing and deserializing JSON.
 *
 * <p>The interface uses Jackson as the JSON parser. Some useful annotations to include on classes
 * used as templates for JSON are:
 *
 * <p>{@code @JsonInclude(JsonInclude.Include.NON_NULL)}
 *
 * <ul>
 *   <li>Does not serialize fields that are {@code null}.
 * </ul>
 *
 * {@code @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)}
 *
 * <ul>
 *   <li>Fields that are private are also accessible for serialization/deserialization.
 * </ul>
 *
 * @see <a href="https://github.com/FasterXML/jackson">https://github.com/FasterXML/jackson</a>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class JsonTemplateMapper {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Deserializes a JSON file via a JSON object template.
     *
     * @param jsonFile a file containing a JSON string
     * @param templateClass the template to deserialize the string to
     * @return the template filled with the values parsed from {@code jsonFile}
     * @throws IOException if an error occurred during reading the file or parsing the JSON
     */
    @org.checkerframework.dataflow.qual.Impure
    public static <T extends JsonTemplate> T readJsonFromFile(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path jsonFile, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> templateClass) throws IOException {
        return objectMapper.readValue(Files.newInputStream(jsonFile), templateClass);
    }

    /**
     * Deserializes a JSON object from a JSON string.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static <T extends JsonTemplate> T readJson(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String jsonString, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> templateClass) throws IOException {
        return objectMapper.readValue(jsonString, templateClass);
    }

    /**
     * Convert a {@link JsonTemplate} to a {@link Blob} of the JSON string.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob toBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonTemplate template) {
        return Blobs.from(outputStream -> {
            objectMapper.writeValue(outputStream, template);
        });
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private JsonTemplateMapper() {
    }
}
