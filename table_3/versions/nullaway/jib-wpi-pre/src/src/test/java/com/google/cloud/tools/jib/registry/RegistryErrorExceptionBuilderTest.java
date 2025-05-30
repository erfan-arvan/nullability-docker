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
package com.google.cloud.tools.jib.registry;

import com.google.api.client.http.HttpResponseException;
import com.google.cloud.tools.jib.registry.json.ErrorEntryTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link RegistryErrorExceptionBuilder}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class RegistryErrorExceptionBuilderTest {

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull HttpResponseException mockHttpResponseException;

    @org.checkerframework.dataflow.qual.Impure
    public void testAddErrorEntry() {
        RegistryErrorExceptionBuilder builder = new RegistryErrorExceptionBuilder("do something", mockHttpResponseException);
        builder.addReason(new ErrorEntryTemplate(ErrorCodes.MANIFEST_INVALID.name(), "manifest invalid"));
        builder.addReason(new ErrorEntryTemplate(ErrorCodes.BLOB_UNKNOWN.name(), "blob unknown"));
        builder.addReason(new ErrorEntryTemplate(ErrorCodes.MANIFEST_UNKNOWN.name(), "manifest unknown"));
        builder.addReason(new ErrorEntryTemplate(ErrorCodes.TAG_INVALID.name(), "tag invalid"));
        builder.addReason(new ErrorEntryTemplate(ErrorCodes.MANIFEST_UNVERIFIED.name(), "manifest unverified"));
        builder.addReason(new ErrorEntryTemplate(ErrorCodes.UNSUPPORTED.name(), "some other error happened"));
        builder.addReason(new ErrorEntryTemplate("unknown", "some unknown error happened"));
        try {
            throw builder.build();
        } catch (RegistryErrorException ex) {
            Assert.assertEquals("Tried to do something but failed because: manifest invalid (something went wrong), blob unknown (something went wrong), manifest unknown, tag invalid, manifest unverified, other: some other error happened, unknown: some unknown error happened | If this is a bug, please file an issue at https://github.com/google/jib/issues/new", ex.getMessage());
        }
    }
}
