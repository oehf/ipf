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
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import static org.apache.commons.lang3.Validate.notNull;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_DOCUMENT_FOR_DOC_ENTRY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_DOC_ENTRY_FOR_DOCUMENT;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import javax.activation.DataHandler;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Validates a {@link EbXMLSubmitObjectsRequest} request.
 * @author Jens Riemschneider
 */
public class ProvideAndRegisterDocumentSetRequestValidator implements Validator<EbXMLProvideAndRegisterDocumentSetRequest, ValidationProfile>{
    private final SubmitObjectsRequestValidator submitObjectsRequestValidator = 
        new SubmitObjectsRequestValidator();
    
    @Override
    public void validate(EbXMLProvideAndRegisterDocumentSetRequest request, ValidationProfile profile) {
        notNull(request, "request cannot be null");

        submitObjectsRequestValidator.validate(request, profile);
        
        validateDocuments(request);
    }

    private void validateDocuments(EbXMLProvideAndRegisterDocumentSetRequest request) {
        Map<String, DataHandler> documents = request.getDocuments();

        Set<String> docEntryIds = new HashSet<String>();
        for (EbXMLExtrinsicObject docEntry : request.getExtrinsicObjects(DocumentEntryType.STABLE.getUuid())) {
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
