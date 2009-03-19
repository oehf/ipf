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
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;

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
        ApplicationContext springContext = getApplicationContext(camelContext);
        String[] names = springContext.getBeanNamesForType(type, true, true);
        int count = names == null ? 0 : names.length;
        if (count == 1) {
            return (T)springContext.getBean(names[0]);
        } else if (count == 0) {
            return null; 
        } else {
            throw new IllegalArgumentException(
                    "Too many beans in the application context of type: " 
                    + type + ". Found: " + count);
        }
    }

    public static ApplicationContext getApplicationContext(CamelContext camelContext) {
        if (camelContext instanceof SpringCamelContext) {
            SpringCamelContext springContext = (SpringCamelContext)camelContext;
            return springContext.getApplicationContext();
        }
        throw new IllegalArgumentException("context argument is not a SpringCamelContext");
    }
    
}
