package org.cache2k.addon;

/*
 * #%L
 * cache2k addon
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
import org.cache2k.Cache2kBuilder;
import org.cache2k.config.CacheBuildContext;
import org.cache2k.config.ConfigBean;
import org.cache2k.config.ConfigBuilder;
import org.cache2k.config.ToggleFeature;

/**
 * @author Jens Wilke
 */
public class UniversalResilienceFeatureExperiment extends ToggleFeature implements ConfigBean<UniversalResilienceFeatureExperiment, UniversalResilienceFeatureExperiment.Builder> {

    static UniversalResilienceFeatureExperiment enable(@Nullable() Cache2kBuilder<?, ?> b) {
        return null;
    }

    static {
        Cache2kBuilder.forUnknownTypes().enable(UniversalResilienceFeatureExperiment.class, b -> b.hello()).setup(UniversalResilienceFeatureExperiment::enable, b -> b.hello());
    }

    @Override
    protected void doEnlist(@Nullable() CacheBuildContext<?, ?> ctx) {
    }

    @Override
    public UniversalResilienceFeatureExperiment.Builder builder() {
        return null;
    }

    static class Builder implements ConfigBuilder<Builder, UniversalResilienceFeatureExperiment> {

        @Override
        public UniversalResilienceFeatureExperiment config() {
            return null;
        }

        public void hello() {
        }
    }
}
