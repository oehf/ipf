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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindSubmissionSetsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.IdentifiableTransformer;

/**
 * Transforms between {@link FindSubmissionSetsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class FindSubmissionSetsQueryTransformer {
    private final IdentifiableTransformer identifiableTransformer = 
        new IdentifiableTransformer();

    public void toEbXML(FindSubmissionSetsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        ebXML.setId(query.getType().getId());
        
        String value = identifiableTransformer.toEbXML(query.getPatientId());
        slots.fromString(SUBMISSION_SET_PATIENT_ID, value);
        
        slots.fromStringList(SUBMISSION_SET_SOURCE_ID, query.getSourceIds());
        
        slots.fromNumber(SUBMISSION_SET_SUBMISSION_TIME_FROM, query.getSubmissionTime().getFrom());
        slots.fromNumber(SUBMISSION_SET_SUBMISSION_TIME_TO, query.getSubmissionTime().getTo());

        slots.fromString(SUBMISSION_SET_AUTHOR_PERSON, query.getAuthorPerson());
        
        slots.fromCode(SUBMISSION_SET_CONTENT_TYPE_CODE, query.getContentTypeCodes());
        
        slots.fromStatus(SUBMISSION_SET_STATUS, query.getStatus());
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
    public void fromEbXML(FindSubmissionSetsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        String patientId = slots.toString(SUBMISSION_SET_PATIENT_ID);
        query.setPatientId(identifiableTransformer.fromEbXML(patientId));
        
        slots.toStringList(SUBMISSION_SET_SOURCE_ID, query.getSourceIds());
        
        query.getSubmissionTime().setFrom(slots.toNumber(SUBMISSION_SET_SUBMISSION_TIME_FROM));
        query.getSubmissionTime().setTo(slots.toNumber(SUBMISSION_SET_SUBMISSION_TIME_TO));
        
        query.setAuthorPerson(slots.toString(SUBMISSION_SET_AUTHOR_PERSON));
        
        slots.toCodes(SUBMISSION_SET_CONTENT_TYPE_CODE, SUBMISSION_SET_CONTENT_TYPE_CODE_SCHEME, query.getContentTypeCodes());
        
        slots.toStatus(SUBMISSION_SET_STATUS, query.getStatus());
    }
}
