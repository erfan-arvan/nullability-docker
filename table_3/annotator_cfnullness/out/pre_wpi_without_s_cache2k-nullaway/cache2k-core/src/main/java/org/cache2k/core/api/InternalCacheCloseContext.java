package org.cache2k.core.api;

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
import org.cache2k.CacheManager;

/**
 * @author Jens Wilke
 */
public interface InternalCacheCloseContext {

    /**
     * The cache name
     */
    String getName();

    /**
     * The cache manager
     */
    CacheManager getCacheManager();

    /**
     * Call close on the customization if the {@link java.io.Closeable} interface
     * is implemented
     */
    void closeCustomization(@Nullable() Object customization, @Nullable() String name);
}
