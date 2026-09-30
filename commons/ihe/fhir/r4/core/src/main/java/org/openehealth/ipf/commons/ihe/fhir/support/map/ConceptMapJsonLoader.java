/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.support.map;

import ca.uhn.fhir.parser.IParser;
import org.openehealth.ipf.commons.map.MappingLoader;

/**
 * {@link MappingLoader} for FHIR R4 {@code ConceptMap} resources in the JSON wire format, claiming
 * {@code *.conceptmap.r4.json} by name and {@code conceptmap-r4-json} by format id.
 *
 * @see ConceptMapLoader
 * @since 6.0
 */
public class ConceptMapJsonLoader extends ConceptMapLoader {

    /**
     * File extension this loader claims. The FHIR version is part of it on purpose: a ConceptMap
     * is not version-independent, and a loader for another version has to be able to live on the
     * same classpath without either claiming the other's files.
     */
    public static final String EXTENSION = JSON_EXTENSION;

    /**
     * Format id under which this loader can be selected explicitly.
     */
    public static final String FORMAT = JSON_FORMAT;

    public ConceptMapJsonLoader() {
        super(FORMAT, EXTENSION, '{');
    }

    @Override
    protected IParser parser() {
        return CONTEXT.newJsonParser();
    }
}
