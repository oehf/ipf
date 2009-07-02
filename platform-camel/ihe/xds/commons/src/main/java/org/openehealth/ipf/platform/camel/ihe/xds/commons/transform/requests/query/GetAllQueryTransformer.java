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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query;

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter.*;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetAllQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.IdentifiableTransformer;

/**
 * Transforms between a {@link GetAllQuery} and {@link AdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class GetAllQueryTransformer {
    private final IdentifiableTransformer identifiableTransformer = 
        new IdentifiableTransformer();
    
    public void toEbXML(GetAllQuery query, AdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        ebXML.setId(query.getType().getId());
        
        String value = identifiableTransformer.toEbXML(query.getPatientId());
        slots.fromString(PATIENT_ID, value);
        
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatusDocuments());
        slots.fromStatus(SUBMISSION_SET_STATUS, query.getStatusSubmissionSets());
        slots.fromStatus(FOLDER_STATUS, query.getStatusFolders());
        
        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromCode(DOC_ENTRY_CONFIDENTIALITY_CODE, query.getConfidentialityCodes());
    }
    
    public void fromEbXML(GetAllQuery query, AdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        String patientId = slots.toString(PATIENT_ID);
        query.setPatientId(identifiableTransformer.fromEbXML(patientId));

        slots.toStatus(DOC_ENTRY_STATUS, query.getStatusDocuments());
        slots.toStatus(FOLDER_STATUS, query.getStatusFolders());
        slots.toStatus(SUBMISSION_SET_STATUS, query.getStatusSubmissionSets());
        
        slots.toCode(DOC_ENTRY_CONFIDENTIALITY_CODE, query.getConfidentialityCodes());
        slots.toCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
    }
}
