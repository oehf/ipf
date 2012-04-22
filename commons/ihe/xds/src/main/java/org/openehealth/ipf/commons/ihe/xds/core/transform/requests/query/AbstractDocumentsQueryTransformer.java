/*
 * Copyright 2012 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.AbstractDocumentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between an {@link AbstractDocumentsQuery} derivative and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
abstract class AbstractDocumentsQueryTransformer<T extends AbstractDocumentsQuery> {

    /**
     * Transforms the query into its ebXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>. 
     * @param query
     *          the query. Can be <code>null</code>.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     */
    public void toEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        
        ebXML.setId(query.getType().getId());
        ebXML.setHome(query.getHomeCommunityId());

        slots.fromString(DOC_ENTRY_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        
        slots.fromStringList(DOC_ENTRY_AUTHOR_PERSON, query.getAuthorPersons());

        slots.fromNumber(DOC_ENTRY_CREATION_TIME_FROM, query.getCreationTime().getFrom());
        slots.fromNumber(DOC_ENTRY_CREATION_TIME_TO, query.getCreationTime().getTo());

        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_FROM, query.getServiceStartTime().getFrom());
        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_TO, query.getServiceStartTime().getTo());
        
        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM, query.getServiceStopTime().getFrom());
        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO, query.getServiceStopTime().getTo());

        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
        
        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromCode(DOC_ENTRY_CLASS_CODE, query.getClassCodes());
        slots.fromCode(DOC_ENTRY_TYPE_CODE, query.getTypeCodes());
        slots.fromCode(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE, query.getHealthcareFacilityTypeCodes());
        slots.fromCode(DOC_ENTRY_PRACTICE_SETTING_CODE, query.getPracticeSettingCodes());
        slots.fromCode(DOC_ENTRY_EVENT_CODE, query.getEventCodes());
        slots.fromCode(DOC_ENTRY_CONFIDENTIALITY_CODE, query.getConfidentialityCodes());
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
    public void fromEbXML(T query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }
        
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        String patientId = slots.toString(DOC_ENTRY_PATIENT_ID);
        query.setPatientId(Hl7v2Based.parse(patientId, Identifiable.class));
        
        query.setClassCodes(slots.toCodeList(DOC_ENTRY_CLASS_CODE));
        query.setTypeCodes(slots.toCodeList(DOC_ENTRY_TYPE_CODE));
        query.setPracticeSettingCodes(slots.toCodeList(DOC_ENTRY_PRACTICE_SETTING_CODE));
        query.setHealthcareFacilityTypeCodes(slots.toCodeList(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE));
        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));

        query.setEventCodes(slots.toCodeQueryList(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME));
        query.setConfidentialityCodes(slots.toCodeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME));
        
        query.setAuthorPersons(slots.toStringList(DOC_ENTRY_AUTHOR_PERSON));
        
        query.getCreationTime().setFrom(slots.toNumber(DOC_ENTRY_CREATION_TIME_FROM));
        query.getCreationTime().setTo(slots.toNumber(DOC_ENTRY_CREATION_TIME_TO));
        
        query.getServiceStartTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_FROM));
        query.getServiceStartTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_TO));

        query.getServiceStopTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM));
        query.getServiceStopTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO));
        
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
        query.setHomeCommunityId(ebXML.getHome());
    }
}
