/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.util;

import org.apache.camel.CamelContext;

import java.util.Set;

/**
 * @author Martin Krasser
 */
public class Contexts {

    public static <T> T bean(Class<T> type, CamelContext camelContext) {
        T bean = beanOrNull(type, camelContext);
        if (bean == null) {
            throw new IllegalArgumentException(
                    "No bean available in the application context of type: " 
                    + type);
        }
        return bean;
    }
    
    public static <T> T beanOrNull(Class<T> type, CamelContext camelContext) {
        Set<T> beans = camelContext.getRegistry().findByType(type);
        int count = beans.size();
        if (count == 1) {
            return beans.iterator().next();
        } else if (count == 0) {
            return null; 
        } else {
            throw new IllegalArgumentException(
                    "Too many beans in the application context of type: " 
                    + type + ". Found: " + count);
        }
    }
    
}
