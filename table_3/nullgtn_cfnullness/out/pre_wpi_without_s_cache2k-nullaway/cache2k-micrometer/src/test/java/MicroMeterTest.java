/*
 * #%L
 * cache2k micrometer monitoring support
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
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.search.MeterNotFoundException;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.extra.micrometer.Cache2kCacheMetrics;
import static org.junit.Assert.*;
import org.cache2k.extra.micrometer.MicrometerSupport;
import org.junit.Test;

/**
 * Does test the various bind option.
 *
 * @author Jens Wilke
 */
public class MicroMeterTest {

    @Test
    public void programmaticBind() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Cache cache = Cache2kBuilder.forUnknownTypes().build();
        Cache2kCacheMetrics.monitor(registry, cache);
        assertTrue(registry.get("cache.puts").functionCounter().count() >= 0);
        cache.close();
    }

    /**
     * Tests a feature of micrometer. Registering twice has no effect.
     * Metrics registered first stay and subsequent are discarded.
     */
    @Test
    public void doubleBind() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Cache cache = Cache2kBuilder.forUnknownTypes().build();
        Cache2kCacheMetrics.monitor(registry, cache);
        Object counter = registry.get("cache.puts").functionCounter();
        Cache2kCacheMetrics.monitor(registry, cache);
        assertSame(counter, registry.get("cache.puts").functionCounter());
        cache.close();
    }

    @Test
    public void notBoundToGlobalRegistryWhenDisabled() {
        Cache cache = Cache2kBuilder.forUnknownTypes().name("bindToGlobalRegistryWhenDisabled").enable(MicrometerSupport.class).disableMonitoring(true).build();
        try {
            Metrics.globalRegistry.get("cache.puts").tag("cache", cache.getName()).meters();
            fail("exception expected");
        } catch (MeterNotFoundException expected) {
        }
    }

    @Test
    public void bindToGlobalRegistryWhenEnabled() {
        Cache cache = Cache2kBuilder.forUnknownTypes().name("bindToGlobalRegistryWhenEnabled").enable(MicrometerSupport.class).disableMonitoring(false).build();
        assertTrue(Metrics.globalRegistry.get("cache.puts").tag("cache", cache.getName()).functionCounter().count() >= 0);
    }

    @Test
    public void bindWhenStatisticsEnabled() {
        Cache cache = Cache2kBuilder.forUnknownTypes().enable(MicrometerSupport.class).disableStatistics(true).name("bindWhenStatisticsDisabled").build();
        assertTrue(Metrics.globalRegistry.get("cache.puts").tag("cache", cache.getName()).functionCounter().count() >= 0);
    }

    @Test
    public void bindToSpecificRegistry() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Cache cache = Cache2kBuilder.forUnknownTypes().enableWith(MicrometerSupport.class, b -> b.registry(registry)).build();
        try {
            Metrics.globalRegistry.get("cache.puts").tag("cache", cache.getName()).meters();
            fail("not in global registry");
        } catch (MeterNotFoundException expected) {
        }
        assertTrue(registry.get("cache.puts").tag("cache", cache.getName()).functionCounter().count() >= 0);
    }

    @Test
    public void checkBasicMetrics() throws InterruptedException {
        MeterRegistry registry = new SimpleMeterRegistry();
        Cache cache = Cache2kBuilder.forUnknownTypes().build();
        Cache2kCacheMetrics.monitor(registry, cache);
        cache.put(1, 1);
        cache.put(2, 1);
        cache.put(3, 1);
        cache.put(2, 1234);
        cache.get(1);
        cache.get(2);
        cache.get(1234);
        cache.remove(3);
        Thread.sleep(1000);
        assertEquals(4, (int) registry.get("cache.puts").functionCounter().count());
        assertEquals(2, (int) registry.get("cache.size").gauge().value());
        assertEquals(1, (int) registry.get("cache.evictions").functionCounter().count());
        assertEquals(2, (int) registry.get("cache.gets").tag("result", "hit").functionCounter().count());
        assertEquals(1, (int) registry.get("cache.gets").tag("result", "miss").functionCounter().count());
        cache.close();
    }

    @Test
    public void checkLoaderMetrics() throws InterruptedException {
        MeterRegistry registry = new SimpleMeterRegistry();
        Cache cache = Cache2kBuilder.forUnknownTypes().loader(key -> key).build();
        Cache2kCacheMetrics.monitor(registry, cache);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.peek(4);
        cache.get(3);
        Thread.sleep(1000);
        assertEquals(0, (int) registry.get("cache.puts").functionCounter().count());
        assertEquals(3, (int) registry.get("cache.size").gauge().value());
        assertEquals(0, (int) registry.get("cache.evictions").functionCounter().count());
        assertEquals(1, (int) registry.get("cache.gets").tag("result", "hit").functionCounter().count());
        assertEquals(4, (int) registry.get("cache.gets").tag("result", "miss").functionCounter().count());
        assertEquals(3, (int) registry.get("cache.load").tag("result", "success").functionCounter().count());
        assertEquals(0, (int) registry.get("cache.load").tag("result", "failure").functionCounter().count());
        assertEquals(0, registry.get("cache.load.duration").gauge().value(), 0.5);
        cache.close();
    }

    @Test
    public void checkBasicMetricsWithDisabledStatistics() throws InterruptedException {
        MeterRegistry registry = new SimpleMeterRegistry();
        Cache cache = Cache2kBuilder.forUnknownTypes().disableStatistics(true).build();
        Cache2kCacheMetrics.monitor(registry, cache);
        cache.put(1, 1);
        cache.put(2, 1);
        cache.put(3, 1);
        cache.put(2, 1234);
        cache.get(1);
        cache.get(2);
        cache.get(1234);
        cache.remove(3);
        assertEquals(0, (int) registry.get("cache.puts").functionCounter().count());
        assertEquals(2, (int) registry.get("cache.size").gauge().value());
        assertEquals(0, (int) registry.get("cache.gets").tag("result", "hit").functionCounter().count());
        assertTrue("Meter cache.evictions not present", registry.getMeters().stream().map(meter -> meter.getId().getName()).noneMatch(s -> s.equals("cache.evictions")));
        cache.close();
    }
}
