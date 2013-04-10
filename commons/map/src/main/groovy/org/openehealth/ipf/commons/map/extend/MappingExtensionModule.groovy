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
package org.openehealth.ipf.commons.map.extend;

import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.map.MappingService;

import static org.openehealth.ipf.commons.map.extend.MappingExtensionHelper.*;


/**
 * Extensions for mapping strings
 *
 * @DSL
 * @author Christian Ohr
 */
public class MappingExtensionModule {

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static String firstLower(String delegate) {
        delegate?.replaceAll('^.') { String s -> s ? s[0].toLowerCase() : s }
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static Object map(String delegate, Object key) {
        mappingService()?.get(key, delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static Object map(String delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, delegate, defaultValue)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static Object mapReverse(String delegate, Object value) {
        mappingService()?.getKey(value, delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static Object mapReverse(String delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, delegate, defaultValue)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static String keySystem(String delegate) {
        mappingService()?.getKeySystem(delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static String valueSystem(String delegate) {
        mappingService()?.getValueSystem(delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static Collection<?> keys(String delegate) {
        mappingService()?.keys(delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static Collection<?> values(String delegate) {
        mappingService()?.values(delegate)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static boolean hasKey(String delegate, Object key) {
        keys(delegate).contains(key)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Mapping+Service
     */
    public static boolean hasValue(String delegate, Object value) {
        values(delegate).contains(value)
    }

    public static Object methodMissing(String delegate, String name, Object args) {
        simpleMethodMissingLogic(mappingService(), delegate, name, args)
    }
    
    private static MappingService mappingService() {
        ContextFacade.getBean(MappingService)
    }
	
}
