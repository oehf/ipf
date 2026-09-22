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
 * {@link MappingLoader} for FHIR R4 {@code ConceptMap} resources in the XML wire format, claiming
 * {@code *.conceptmap.r4.xml} by name and {@code conceptmap-r4-xml} by format id.
 *
 * @see ConceptMapLoader
 * @since 6.0
 */
public class ConceptMapXmlLoader extends ConceptMapLoader {

    /**
     * File extension this loader claims.
     *
     * @see ConceptMapJsonLoader#EXTENSION
     */
    public static final String EXTENSION = XML_EXTENSION;

    /**
     * Format id under which this loader can be selected explicitly.
     */
    public static final String FORMAT = XML_FORMAT;

    public ConceptMapXmlLoader() {
        super(FORMAT, EXTENSION, '<');
    }

    @Override
    protected IParser parser() {
        return CONTEXT.newXmlParser();
    }
}
