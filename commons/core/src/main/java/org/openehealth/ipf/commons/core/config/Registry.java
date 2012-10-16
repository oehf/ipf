/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.core.config;

import java.util.Map;

/**
 * Technology-independent bean registry interface
 * 
 * @since 2.5
 */
public interface Registry {

    /**
     * @param name
     * @return bean of given name
     * 
     * @since 2.5
     */
    public Object bean(String name);

    /**
     * @param requiredType
     * @return first bean of given type
     * 
     * @since 2.5
     */
    public <T> T bean(Class<T> requiredType);
    
    /**
     * @param requiredType
     * @return all beans of given type
     * 
     * @since 2.5
     */
    public <T> Map<String, T> beans(Class <T> requiredType);
    
}
