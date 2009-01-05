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
package org.openehealth.ipf.commons.lbs.store;

import static org.apache.commons.lang.Validate.notNull;

import java.net.URI;
import java.util.UUID;

/**
 * Provides an interface for conversion strategies that convert a {@code UUID}
 * into a {@code URI}.
 * @author Jens Riemschneider
 */
public abstract class UuidUriConversionStrategy {
    private URI baseUri;
    
    /**
     * Constructs the strategy via its base {@code URI}.
     * @param baseUri
     *          {@code URI} used as a basis to generate {@code URI}s
     */
    public UuidUriConversionStrategy(URI baseUri) {
        notNull(baseUri, "baseUri cannot be null");
        this.baseUri = baseUri;
    }
    
    /**
     * @return {@code URI} used as a basis to generate {@code URI}s
     */
    public URI getBaseUri() {
        return baseUri;
    }
    
    /**
     * Converts a given {@code UUID} to a {@code URI}.
     * @param uuid
     *          {@code UUID} to convert
     * @return {@code URI} that represents the {@code UUID}
     */
    public abstract URI toUri(UUID uuid);
    
    /**
     * Converts a given {@code URI} to a {@code UUID}.
     * @param uri
     *          {@code URI} to convert
     * @return {@code UUID} that corresponds to the {@code URI} or {@code null} 
     *          if the {@code UUID} could not be determined from the {@code URI}
     */
    public abstract UUID toUuid(URI uri);
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: baseUri=%2$s}", getClass().getSimpleName(),
                baseUri);
    }
}
