package org.cache2k.jcache.provider.generic.storeByValueSimulation;

/*
 * #%L
 * cache2k JCache provider
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
/**
 * Copying objects is not an easy task, so better have a factory for that.
 *
 * @author Jens Wilke
 */
public interface ObjectCopyFactory {

    /**
     * Create a transformer class which actually does no type changes.
     * If the factory does know how to handle the type, it returns null.
     */
    <T> ObjectTransformer<T, T> createCopyTransformer(@Nullable() Class<T> clazz, @Nullable() ClassLoader classLoader);

    <T> ObjectTransformer<T, T> createCopyTransformer(@Nullable() Class<T> clazz);
}
