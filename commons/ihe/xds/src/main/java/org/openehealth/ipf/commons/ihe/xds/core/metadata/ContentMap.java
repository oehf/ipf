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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import org.openehealth.ipf.commons.core.config.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe content map that stores and manages typed document content.
 * Supports automatic type conversion using a configurable {@link TypeConverter}.
 * 
 * <p>This class allows storing multiple representations of the same content
 * in different types and automatically converts between them when needed.</p>
 * 
 * <p><strong>Thread Safety:</strong> This class is thread-safe and can be used
 * in concurrent environments.</p>
 * 
 * @author Dmytro Rud
 */
@XmlTransient
public class ContentMap implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(ContentMap.class);

    private final transient Map<Class<?>, Object> contentStore = new ConcurrentHashMap<>();

    @Getter
    private static volatile TypeConverter conversionService;


    /**
     * Retrieves document content of the specified type.
     * 
     * <p>If content of the requested type is not directly available, this method
     * attempts to convert existing content from another type using the configured
     * {@link TypeConverter}. Successfully converted content is cached for future use.</p>
     * 
     * @param targetType the desired content type (must not be null)
     * @param <T> the inferred desired content type
     * @return content of the given type, or {@code null} when content of the
     *         desired type is neither already present nor can be automatically
     *         generated from an existing one
     * @throws NullPointerException if targetType is null
     */
    @SuppressWarnings("unchecked")
    public <T> T getContent(Class<T> targetType) {
        Objects.requireNonNull(targetType, "Target type must not be null");
        
        T result = (T) contentStore.get(targetType);
        if (result != null) {
            log.debug("Returning existing content of type {}", targetType.getName());
            return result;
        }

        if (conversionService == null) {
            log.debug("Conversion service not configured, cannot convert to type {}", targetType.getName());
            return null;
        }

        return attemptConversion(targetType);
    }

    /**
     * Attempts to convert existing content to the target type.
     * 
     * @param targetType the desired content type
     * @param <T> the inferred desired content type
     * @return converted content, or {@code null} if conversion is not possible
     */
    private <T> T attemptConversion(Class<T> targetType) {
        for (Map.Entry<Class<?>, Object> sourceEntry : contentStore.entrySet()) {
            Class<?> sourceType = sourceEntry.getKey();
            
            if (conversionService.canConvert(sourceType, targetType)) {
                T convertedResult = conversionService.convert(sourceEntry.getValue(), targetType);
                
                if (convertedResult != null) {
                    log.debug("Successfully converted {} to {}", sourceType.getName(), targetType.getName());
                    setContent(targetType, convertedResult);
                    return convertedResult;
                }
            }
        }

        log.debug("No suitable converter found for target type {}", targetType.getName());
        return null;
    }

    /**
     * Stores or replaces document content of the specified type.
     * 
     * @param contentType the content type (must not be null)
     * @param content the content to store (must not be null)
     * @param <T> the inferred content type
     * @return the previous content associated with the type, or {@code null} if none existed
     * @throws NullPointerException if contentType or content is null
     */
    @SuppressWarnings("unchecked")
    public <T> T setContent(Class<T> contentType, T content) {
        Objects.requireNonNull(contentType, "Content type must not be null");
        Objects.requireNonNull(content, "Content must not be null");
        
        T previousContent = (T) contentStore.put(contentType, content);
        
        if (log.isDebugEnabled()) {
            log.debug("Stored content of type {}{}", 
                contentType.getName(), 
                previousContent != null ? " (replaced existing)" : "");
        }
        
        return previousContent;
    }


    /**
     * Removes (invalidates) document content of the specified type.
     * 
     * @param contentType the content type to remove (must not be null)
     * @param <T> the inferred content type
     * @return the removed content of the given type, or {@code null} if no content existed
     * @throws NullPointerException if contentType is null
     */
    @SuppressWarnings("unchecked")
    public <T> T removeContent(Class<T> contentType) {
        Objects.requireNonNull(contentType, "Content type must not be null");
        
        T removedContent = (T) contentStore.remove(contentType);
        
        if (removedContent != null) {
            log.debug("Removed content of type {}", contentType.getName());
        }
        
        return removedContent;
    }


    /**
     * Checks whether this content map contains content of the specified type.
     * 
     * @param contentType the content type to check (must not be null)
     * @return {@code true} if content of the given type exists, {@code false} otherwise
     * @throws NullPointerException if contentType is null
     */
    public boolean hasContent(Class<?> contentType) {
        Objects.requireNonNull(contentType, "Content type must not be null");
        return contentStore.containsKey(contentType);
    }


    /**
     * Returns the number of different content types currently stored in this map.
     * 
     * @return the count of available content types
     */
    public int getContentsCount() {
        return contentStore.size();
    }

    /**
     * Checks whether this content map is empty.
     * 
     * @return {@code true} if no content is stored, {@code false} otherwise
     */
    public boolean isEmpty() {
        return contentStore.isEmpty();
    }

    /**
     * Removes all content from this map.
     */
    public void clear() {
        contentStore.clear();
        log.debug("Cleared all content from ContentMap");
    }


    /**
     * Configures the type conversion service for all {@link ContentMap} instances.
     * 
     * <p>This is a global setting that affects all instances of this class.</p>
     * 
     * @param conversionService the conversion service to use, or {@code null} to disable conversion
     */
    public static void setConversionService(TypeConverter conversionService) {
        ContentMap.conversionService = conversionService;
        log.info("ContentMap conversion service {}", 
            conversionService != null ? "configured" : "disabled");
    }
}
