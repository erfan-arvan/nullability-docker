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
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.testing.category.FastTests;
import org.junit.experimental.categories.Category;

/**
 * Test ConcurrentMap methods with cache.
 *
 * @author Jens Wilke
 */
@Category(FastTests.class)
public class ConcurrentMapWithCacheTest extends ConcurrentMapTest {

    Cache<Integer, String> cache;

    @Override
    public void setUp() {
        cache = Cache2kBuilder.of(Integer.class, String.class).name(this.getClass().getName()).eternal(true).build();
        map = cache.asMap();
    }

    @Override
    public void tearDown() {
        cache.close();
        map = null;
        cache = null;
    }
}
