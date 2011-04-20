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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters;

import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.modules.cda.CDAR2Parser;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

/**
 * Special camel converter for Continua HRN request. Retrieves the CDA/CCD
 * content from the Document and parses it with the provided CDAR2 parser.
 * 
 * @author Stefan Ivanov
 */
@Converter
public class ProvideAndRegisterDocumentSetContentConverters {
    
    /**
     * Retrieves the single document and parses it to MDHT Clinical Document
     * isntance.
     * 
     * @param msg
     * @return the Clinical Document instance.
     */
    @Converter
    public static ClinicalDocument convert(ProvideAndRegisterDocumentSet msg) {
        Document doc = msg.getDocuments().get(0);
        String content = doc.getContents(String.class);
        ClinicalDocument ccd = new CDAR2Parser().parse(content, (Object[]) null);
        return ccd;
    }

}

