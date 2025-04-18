package org.cache2k.core.util;

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
import org.cache2k.core.CacheInternalError;
import org.cache2k.core.log.Log;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides an instance of a tunable after applying changes taken
 * from configuration file or system properties.
 *
 * @see TunableConstants
 * @author Jens Wilke
 */
public final class TunableFactory {

    static Log log = Log.getLog(TunableFactory.class);

    /**
     * Special defaults for GraalVM. This is the only defaults file we ship at the moment.
     * Maybe we ship different platform defaults in the future. The file name might get
     * extended with CPU architecture (aarch / amd64), VM vendor (android, graal, ...) etc.
     */
    public static final String DEFAULT_TUNING_GRAALVM_FILE_NAME = "/org/cache2k/core/default-tuning-graalvm-nativeimage.properties";

    public static final String DEFAULT_TUNING_FILE_NAME = "/org/cache2k/core/default-tuning.properties";

    public static final String CUSTOM_TUNING_FILE_NAME = "/org/cache2k/core/tuning.properties";

    public static final String TUNE_MARKER = "org.cache2k.tuning";

    @Nullable
    private static Map<Class<?>, Object> map;

    @Nullable
    private static Properties defaultProperties;

    private static Properties customProperties;

    /**
     * Detect GraalVM nativeimage enable different tuning defaults.
     */
    private static boolean isGraalVMNativeImage() {
        return System.getProperty("org.graalvm.nativeimage.kind") != null;
    }

    /**
     * Reload the tunable configuration from the system properties
     * and the configuration file.
     */
    private static void load() {
        map = new HashMap<Class<?>, Object>();
        customProperties = loadFile(CUSTOM_TUNING_FILE_NAME);
        if (isGraalVMNativeImage()) {
            defaultProperties = loadFile(DEFAULT_TUNING_GRAALVM_FILE_NAME);
        }
        if (defaultProperties == null) {
            defaultProperties = loadFile(DEFAULT_TUNING_FILE_NAME);
        }
    }

    @Nullable()
    static Properties loadFile(String fileName) {
        InputStream in = TunableConstants.class.getResourceAsStream(fileName);
        if (in != null) {
            try {
                Properties p = new Properties();
                p.load(in);
                in.close();
                return p;
            } catch (IOException ex) {
                throw new CacheInternalError("tuning properties not readable", ex);
            }
        }
        return null;
    }

    /**
     * Provide tuning object with initialized information from the properties file.
     *
     * @param p Properties from the execution context
     * @param c Tunable class
     * @param <T> type of requested tunable class
     * @return Created and initialized object
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T extends TunableConstants> T get(Properties p, Class<T> c) {
        T cfg = getDefault(c);
        if (p != null && p.containsKey(TUNE_MARKER) && p.containsKey(cfg.getClass().getName() + ".tuning")) {
            cfg = (T) cfg.clone();
            apply(p, cfg);
        }
        return cfg;
    }

    /**
     * Provide tuning object with initialized information from properties in the class path
     * or system properties.
     *
     * @param c Tunable class
     * @param <T> type of requested tunable class
     * @return Created and initialized object
     */
    public static synchronized <T extends TunableConstants> T get(Class<T> c) {
        return getDefault(c);
    }

    private static <T extends TunableConstants> T getDefault(Class<T> c) {
        if (map == null) {
            load();
        }
        @SuppressWarnings("unchecked")
        T cfg = (T) map.get(c);
        if (cfg == null) {
            try {
                cfg = c.newInstance();
            } catch (Exception ex) {
                throw new CacheInternalError("cannot instantiate tunable constants", ex);
            }
            apply(defaultProperties, cfg);
            apply(customProperties, cfg);
            apply(System.getProperties(), cfg);
            map.put(c, cfg);
        }
        return cfg;
    }

    static void apply(Properties p, Object cfg) {
        if (p == null) {
            return;
        }
        String propName = null;
        try {
            for (Field f : cfg.getClass().getFields()) {
                propName = cfg.getClass().getName().replace('$', '.') + "." + f.getName();
                String o = p.getProperty(propName);
                if (o != null) {
                    Class<?> fieldType = f.getType();
                    if (fieldType == Boolean.TYPE) {
                        o = o.toLowerCase();
                        if ("off".equals(o) || "false".equals(o) || "disable".equals(o)) {
                            f.set(cfg, false);
                        } else {
                            f.set(cfg, true);
                        }
                        if (log.isDebugEnabled()) {
                            log.debug(propName + "=" + f.get(cfg));
                        }
                    } else if (fieldType == Integer.TYPE) {
                        f.set(cfg, Integer.valueOf(o));
                        if (log.isDebugEnabled()) {
                            log.debug(propName + "=" + f.get(cfg));
                        }
                    } else if (fieldType == Long.TYPE) {
                        f.set(cfg, Long.valueOf(o));
                        if (log.isDebugEnabled()) {
                            log.debug(propName + "=" + f.get(cfg));
                        }
                    } else if (fieldType == String.class) {
                        f.set(cfg, o);
                        if (log.isDebugEnabled()) {
                            log.debug(propName + "=" + f.get(cfg));
                        }
                    } else if (fieldType == Class.class) {
                        Class<?> c = Class.forName(o);
                        f.set(cfg, c);
                        if (log.isDebugEnabled()) {
                            log.debug(propName + "=" + c.getName());
                        }
                    } else {
                        throw new CacheInternalError("Unsupported field type. Tunable: " + propName + ", " + fieldType);
                    }
                }
            }
        } catch (Exception ex) {
            if (propName != null) {
                throw new CacheInternalError("error applying tuning setup, property=" + propName, ex);
            } else {
                throw new CacheInternalError("error applying tuning setup", ex);
            }
        }
    }
}
