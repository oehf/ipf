/*
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.map.extend

import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.map.Mappings

/**
 * Extensions for mapping strings. This has been retrofitted from the deprecated {@link org.openehealth.ipf.commons.map.MappingService}
 * to {@link Mappings}, while the map and mapReverse methods return a mapped String or null, rather than
 * an {@link Optional}.
 * <p>
 * The result is the string the mapping declares, as it is: an entry mapping to an empty string
 * yields the empty string rather than the mapping's fallback or a default, and a value containing
 * {@code ~} is not split into a {@link List}.
 *
 * @DSL
 * @author Christian Ohr
 */
class MappingExtensionModule {

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String firstLower(String delegate) {
        delegate.uncapitalize()
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String map(String delegate, String key) {
        mappingService()?.map(key, delegate)?.orElse(null)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String map(String delegate, String key, String defaultValue) {
        mappingService()?.map(key, delegate, defaultValue)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String mapReverse(String delegate, String value) {
        mappingService()?.mapReverse(value, delegate)?.orElse(null)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String mapReverse(String delegate, String value, String defaultValue) {
        mappingService()?.mapReverse(value, delegate, defaultValue)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String keySystem(String delegate) {
        mappingService()?.keySystem(delegate)?.orElse(null)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static String valueSystem(String delegate) {
        mappingService()?.valueSystem(delegate)?.orElse(null)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static Collection<String> keys(String delegate) {
        mappingService()?.keys(delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static Collection<String> values(String delegate) {
        mappingService()?.values(delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static boolean hasKey(String delegate, String key) {
        keys(delegate).contains(key)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    static boolean hasValue(String delegate, String value) {
        values(delegate).contains(value)
    }

    
    private static Mappings mappingService() {
        ContextFacade.getBean(Mappings)
    }
	
}
