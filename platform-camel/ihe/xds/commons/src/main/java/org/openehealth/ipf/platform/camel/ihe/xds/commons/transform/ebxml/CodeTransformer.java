/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLClassification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;

/**
 * Transforms between {@link Code} and its ebXML 2.1 representation.
 * @author Jens Riemschneider
 */
public class CodeTransformer {
    private final EbXMLFactory factory;

    public CodeTransformer(EbXMLFactory ebXMLFactory) {
        notNull(ebXMLFactory, "ebXMLFactory cannot be null");
        this.factory = ebXMLFactory; 
    }

    /**
     * Transforms a {@link Code} instance to a {@link EbXMLClassification}. 
     * @param code
     *          the code instance to transform.
     * @param objectLibrary 
     *          the object library.
     * @return the {@link EbXMLClassification}.
     */
    public EbXMLClassification toEbXML(Code code, EbXMLObjectLibrary objectLibrary) {
        if (code == null) {
            return null;
        }
        
        EbXMLClassification classification = factory.createClassification(objectLibrary);
        classification.setNodeRepresentation(code.getCode());
        classification.setName(code.getDisplayName());
        
        if (code.getSchemeName() != null) {
            classification.addSlot(SLOT_NAME_CODING_SCHEME, code.getSchemeName());
        }
        
        return classification;
    }
    
    /**
     * Transforms a {@link EbXMLClassification} to a {@link Code} instance. 
     * @param classification
     *          {@link EbXMLClassification}
     * @return the code instance.
     */
    public Code fromEbXML(EbXMLClassification classification) {
        if (classification == null) {
            return null;
        }
        
        Code code = new Code();
        code.setCode(classification.getNodeRepresentation());
        code.setDisplayName(classification.getName());

        List<String> slotValues = classification.getSlotValues(SLOT_NAME_CODING_SCHEME);
        if (slotValues.size() > 0) {
            code.setSchemeName(slotValues.get(0));
        }
        
        return code;
    }
}
