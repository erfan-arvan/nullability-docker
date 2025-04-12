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
package com.google.cloud.tools.jib.image;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link Image}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ImageTest {

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Layer mockLayer;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull ImageLayers<Layer> mockImageLayers;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Image image;

    @org.checkerframework.dataflow.qual.Impure
    public void test_smokeTest() throws LayerPropertyNotFoundException {
        ImmutableList<String> expectedEnvironment = ImmutableList.of("crepecake=is great", "VARIABLE=VALUE");
        image.setEnvironmentVariable("crepecake", "is great");
        image.setEnvironmentVariable("VARIABLE", "VALUE");
        image.setEntrypoint(Arrays.asList("some", "command"));
        image.addLayer(mockLayer);
        Mockito.verify(mockImageLayers).add(mockLayer);
        Assert.assertEquals(expectedEnvironment, image.getEnvironment());
        Assert.assertEquals(Arrays.asList("some", "command"), image.getEntrypoint());
    }
}
