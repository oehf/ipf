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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAllQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.IdentifiableTransformer;

/**
 * Transforms between a {@link GetAllQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class GetAllQueryTransformer {
    private final IdentifiableTransformer identifiableTransformer = 
        new IdentifiableTransformer();
    
    /**
     * Transforms the query into its ebXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>. 
     * @param query
     *          the query. Can be <code>null</code>.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     */
    public void toEbXML(GetAllQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        ebXML.setId(query.getType().getId());
        ebXML.setHome(query.getHomeCommunityId());

        String value = identifiableTransformer.toEbXML(query.getPatientId());
        slots.fromString(PATIENT_ID, value);
        
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatusDocuments());
        slots.fromStatus(SUBMISSION_SET_STATUS, query.getStatusSubmissionSets());
        slots.fromStatus(FOLDER_STATUS, query.getStatusFolders());
        
        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromCode(DOC_ENTRY_CONFIDENTIALITY_CODE, query.getConfidentialityCodes());

        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
    }
    
    /**
     * Transforms the ebXML representation of a query into a query object.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>. 
     * @param query
     *          the query. Can be <code>null</code>.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     */
    public void fromEbXML(GetAllQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        String patientId = slots.toString(PATIENT_ID);
        query.setPatientId(identifiableTransformer.fromEbXML(patientId));

        query.setStatusDocuments(slots.toStatus(DOC_ENTRY_STATUS));
        query.setStatusFolders(slots.toStatus(FOLDER_STATUS));
        query.setStatusSubmissionSets(slots.toStatus(SUBMISSION_SET_STATUS));
        
        query.setConfidentialityCodes(slots.toCodeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME));
        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));
        query.setHomeCommunityId(ebXML.getHome());

        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
    }
}
