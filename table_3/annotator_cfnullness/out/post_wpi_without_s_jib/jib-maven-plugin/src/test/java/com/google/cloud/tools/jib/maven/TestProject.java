/*
 * Copyright 2018 Google Inc.
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
package com.google.cloud.tools.jib.maven;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.rules.TemporaryFolder;
import javax.annotation.Nullable;

/**
 * Works with the test Maven project in the {@code resources/project} directory.
 */
class TestProject extends TemporaryFolder implements Closeable {

    private static final String PROJECTS_PATH_IN_RESOURCES = "/projects/";

    private final TestPlugin testPlugin;

    private final String projectDir;

    private Path projectRoot;

    /**
     * Initialize to a specific project directory.
     */
    TestProject(TestPlugin testPlugin, String projectDir) {
        this.testPlugin = testPlugin;
        this.projectDir = projectDir;
    }

    Path getProjectRoot() {
        return projectRoot;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        copyProject();
    }

    private void copyProject() throws IOException {
        projectRoot = ResourceExtractor.extractResourcePath(TestProject.class, PROJECTS_PATH_IN_RESOURCES + projectDir, newFolder(), true).toPath();
        // Puts the correct plugin version into the test project pom.xml.
        Path pomXml = projectRoot.resolve("pom.xml");
        Files.write(pomXml, new String(Files.readAllBytes(pomXml), StandardCharsets.UTF_8).replace("@@PluginVersion@@", testPlugin.getVersion()).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void close() {
        after();
    }
}
