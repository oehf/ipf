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

import jakarta.xml.bind.annotation.XmlTransient;
import org.openehealth.ipf.commons.core.config.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Content map based on Spring type conversion framework.
 * @author Dmytro Rud
 *
 * @deprecated moved to ipf-commons-ihe-xds, as it is only used there
 */
@Deprecated(forRemoval = true)
@XmlTransient
public class ContentMap {
    private static final Logger log = LoggerFactory.getLogger(ContentMap.class);

    // synchronized manually
    private transient final Map<Class<?>, Object> map = new HashMap<>();

    private static TypeConverter conversionService;


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
        var result = (T) map.get(targetType);
        if (result != null) {
            log.debug("Return existing content of type {}", targetType);
            return result;
        }

        if (conversionService == null) {
            log.debug("Conversion service not configured");
            return null;
        }

        synchronized (map) {
            // TODO: optimise conversion using some sophisticated iteration ordering ???
            for (var sourceTypeEntry : map.entrySet()) {
                if (conversionService.canConvert(sourceTypeEntry.getKey(), targetType)) {
                    result = conversionService.convert(sourceTypeEntry.getValue(), targetType);
                    if (result != null) {
                        log.debug("Successfully generated {} from {}", targetType, sourceTypeEntry.getKey());
                        setContent(targetType, result);
                        return result;
                    }
                }
            }
        }

        log.debug("Could not find appropriate converter for the target type {}", targetType);
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
        if (key == null) throw new IllegalArgumentException("content type must be provided");
        if (content == null) throw new IllegalArgumentException("null content is not allowed");
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
    public static TypeConverter getConversionService() {
        return conversionService;
    }


    /**
     * Sets conversion service for document contents.
     */
    public static void setConversionService(TypeConverter conversionService) {
        ContentMap.conversionService = conversionService;
    }

}
