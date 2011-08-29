/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Extract similar static methods for transmogrifiers parameter access.
 * 
 * @author Stefan Ivanov
 * 
 */
public class ParametersHelper {

    private final static ResourceLoader resourceLoader = new DefaultResourceLoader(
        ParametersHelper.class.getClassLoader());
    public static final String RESOURCE_LOCATION = "org.openehealth.ipf.commons.xml.ResourceLocation";

    /**
     * Retrieves the dynamic parameters
     * 
     * @param params
     * @return parameters map
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> parameters(Object... params) {
        if (params[0] instanceof Map) {
            return (Map<String, Object>) params[0];
        } else if (params.length > 1 && params[1] instanceof Map) {
            return (Map<String, Object>) params[1];
        } else {
            return null;
        }
    }

    /**
     * @param params
     * @return the full path to the resource
     */
    static String resource(Object... params) {
        String resourceLocation = null;
        if (params[0] instanceof String) {
            resourceLocation = (String) params[0];
        } else if (params[0] instanceof Map) {
            resourceLocation = (String) ((Map<?, ?>) params[0])
                .get(RESOURCE_LOCATION);
        }
        return resourceLocation;
    }

    /**
     * 
     * @param resource
     * @return the resource as a stream
     * @throws IOException
     */
    static InputStream source(String resource) throws IOException {
        Resource r = resourceLoader.getResource(resource);
        if (r != null) {
            return r.getInputStream();
        } else {
            throw new IllegalArgumentException("Resource location not specified properly");
        }
    }

}
