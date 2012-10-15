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
package org.openehealth.ipf.commons.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.ConversionService;

import java.util.HashMap;
import java.util.Map;

/**
 * Content map based on Spring type conversion framework.
 * @author Dmytro Rud
 */
public class ContentMap {
    private static transient Log LOG = LogFactory.getLog(ContentMap.class);

    // synchronized manually
    private transient final Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

    private static transient ConversionService conversionService;


    /**
     * Returns document content of the given type.
     * @param targetType
     *      desired content type.
     * @param <T>
     *      inferred desired content type.
     * @return
     *      content of the given type, or <code>null</code> when content of the
     *      desired type is neither already present nor can be automatically
     *      generated from an existing one.
     */
    @SuppressWarnings("unchecked")
    public <T> T getContent(Class<T> targetType) {
        T result = (T) map.get(targetType);
        if (result != null) {
            LOG.debug("Return existing content of type " + targetType);
            return result;
        }

        if (conversionService == null) {
            LOG.debug("Conversion service not configured");
            return null;
        }

        synchronized (map) {
            // TODO: optimise conversion using some sophisticated iteration ordering ???
            for (Class<?> sourceType : map.keySet()) {
                if (conversionService.canConvert(sourceType, targetType)) {
                    result = conversionService.convert(map.get(sourceType), targetType);
                    if (result != null) {
                        LOG.debug("Successfully generated " + targetType + " from " + sourceType);
                        setContent(targetType, result);
                        return result;
                    }
                }
            }
        }

        LOG.debug("Could not find appropriate converter for the target type " + targetType);
        return null;
    }

    /**
     * Adds or replaces document content of the given type.
     * @param key
     *      content type.
     * @param content
     *      content; <code>null</code> values are not allowed.
     * @param <T>
     *      inferred content type.
     * @return
     *      the given content, as convenience.
     */
    @SuppressWarnings("unchecked")
    public <T> T setContent(Class<T> key, T content) {
        Validate.notNull(key, "content type must be provided");
        Validate.notNull(content, "null content is not allowed");
        synchronized (map) {
            return (T) map.put(key, content);
        }
    }


    /**
     * Removes (invalidates) document content of the given type.
     * @param key
     *      content type.
     * @param <T>
     *      inferred content type.
     * @return
     *      Obsolete content of the given type, or <code>null</code>,
     *      when no content of the given type was present.
     */
    @SuppressWarnings("unchecked")
    public <T> T removeContent(Class<T> key) {
        synchronized (map) {
            return (T) map.remove(key);
        }
    }


    /**
     * Returns <code>true</code> when this content map already contains
     * an element of the given type.
     */
    public boolean hasContent(Class<?> key) {
        return map.containsKey(key);
    }


    /**
     * Returns count of currently available content types in this content map.
     */
    public int getContentsCount() {
        return map.size();
    }


    /**
     * Returns conversion service for document contents.
     */
    public static ConversionService getConversionService() {
        return conversionService;
    }


    /**
     * Sets conversion service for document contents.
     */
    public static void setConversionService(ConversionService conversionService) {
        ContentMap.conversionService = conversionService;
    }

}
