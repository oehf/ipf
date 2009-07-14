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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.requests;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidatorAssertions.*;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.*;
import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationProfile;

/**
 * Validates a {@link EbXMLSubmitObjectsRequest} request.
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetRequestValidator implements Validator<EbXMLProvideAndRegisterDocumentSetRequest, ValidationProfile>{
    private final SubmitObjectsRequestValidator submitObjectsRequestValidator = 
        new SubmitObjectsRequestValidator();
    
    public void validate(EbXMLProvideAndRegisterDocumentSetRequest request, ValidationProfile profile) {
        notNull(request, "request cannot be null");

        submitObjectsRequestValidator.validate(request, profile);
        
        validateDocuments(request);
    }

    private void validateDocuments(EbXMLProvideAndRegisterDocumentSetRequest request) {
        Map<String, DataHandler> documents = request.getDocuments();

        Set<String> docEntryIds = new HashSet<String>();
        for (EbXMLExtrinsicObject docEntry : request.getExtrinsicObjects(DOC_ENTRY_CLASS_NODE)) {
            String docId = docEntry.getId();
            if (docId != null) {
                docEntryIds.add(docId);
                metaDataAssert(documents.get(docId) != null, MISSING_DOCUMENT_FOR_DOC_ENTRY, docId);                
            }
        }
                
        for (String docId : documents.keySet()) {
            metaDataAssert(docEntryIds.contains(docId), MISSING_DOC_ENTRY_FOR_DOCUMENT, docId);
        }
    }
}
