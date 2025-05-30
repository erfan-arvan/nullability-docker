package org.cache2k.core;

/*
 * #%L
 * cache2k core implementation
 * %%
 * Copyright (C) 2000 - 2020 headissue GmbH, Munich
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import org.cache2k.core.util.Util;
import org.cache2k.expiry.ExpiryTimeValues;
import org.cache2k.io.CacheLoaderException;
import org.cache2k.io.LoadExceptionInfo;
import org.cache2k.io.ExceptionPropagator;

/**
 * Standard behavior of the exception propagator.
 *
 * @author Jens Wilke
 */
public final class StandardExceptionPropagator implements ExceptionPropagator {

    @Override
    public RuntimeException propagateException(LoadExceptionInfo loadExceptionInfo) {
        long expiry = loadExceptionInfo.getUntil();
        String txt = "";
        if (expiry > 0) {
            if (expiry == ExpiryTimeValues.ETERNAL) {
                txt = "expiry=ETERNAL, cause: ";
            } else {
                txt = "expiry=" + Util.formatMillis(expiry) + ", cause: ";
            }
        }
        return new CacheLoaderException(txt + loadExceptionInfo.getException(), loadExceptionInfo.getException());
    }
}
