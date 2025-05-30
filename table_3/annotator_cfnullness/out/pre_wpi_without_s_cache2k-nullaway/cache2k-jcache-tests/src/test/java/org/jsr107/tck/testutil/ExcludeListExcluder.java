/**
 *  Copyright 2011-2013 Terracotta, Inc.
 *  Copyright 2011-2013 Oracle, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jsr107.tck.testutil;

import java.util.Set;

/**
 * For the TCK we need to have an exclude list of bad tests so that disabling tests
 * can be done without changing code.
 * <p>
 * This class creates a rule for the class provided
 * </p>
 * The exclude list is created by {@link ExcludeList} by creating a file in the root of your classpath called
 * "ExcludeList". There is an example in the testRI module for testing the RI.
 *
 * @author Yannis Cosmadopoulos
 * @since 1.0
 */
public class ExcludeListExcluder extends AbstractTestExcluder {

    @Nullable
    private final Set<String> excludes;

    /**
     * Constructor for ExcludeListExcluder.
     * Uses the supplied class name and {@link ExcludeList#getExcludes(String)} to
     * determine the methods to be excluded.
     *
     * @param c the class for which tests should be excluded
     */
    public ExcludeListExcluder(Class c) {
        excludes = ExcludeList.INSTANCE.getExcludes(c.getName());
    }

    @Nullable()
    protected boolean isExcluded(String methodName) {
        return excludes != null && excludes.contains(methodName);
    }
}
