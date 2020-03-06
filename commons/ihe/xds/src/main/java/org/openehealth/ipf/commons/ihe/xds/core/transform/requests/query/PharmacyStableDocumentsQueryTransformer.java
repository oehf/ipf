/*
 * Copyright 2020 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.PharmacyStableDocumentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.toHL7;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Base transformations for all stable-documents PHARM-1 queries.
 * @author Quentin Ligier
 * @since 3.7
 */
abstract class PharmacyStableDocumentsQueryTransformer<T extends PharmacyStableDocumentsQuery> extends PharmacyDocumentsQueryTransformer<T> {

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

        super.toEbXML(query, ebXML);
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        slots.fromStringList(DOC_ENTRY_AUTHOR_PERSON, query.getAuthorPersons());
        slots.fromStringList(DOC_ENTRY_UUID, query.getUuids());
        slots.fromStringList(DOC_ENTRY_UNIQUE_ID, query.getUniqueIds());
        slots.fromNumber(DOC_ENTRY_CREATION_TIME_FROM, toHL7(query.getCreationTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_CREATION_TIME_TO, toHL7(query.getCreationTime().getTo()));
        slots.fromCode(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE, query.getHealthcareFacilityTypeCodes());
        slots.fromCode(DOC_ENTRY_PRACTICE_SETTING_CODE, query.getPracticeSettingCodes());
        slots.fromCode(DOC_ENTRY_EVENT_CODE, query.getEventCodes());
        slots.fromCode(DOC_ENTRY_CONFIDENTIALITY_CODE, query.getConfidentialityCodes());

        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_FROM, toHL7(query.getServiceStartTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_TO, toHL7(query.getServiceStartTime().getTo()));

        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM, toHL7(query.getServiceStopTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO, toHL7(query.getServiceStopTime().getTo()));
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

        super.fromEbXML(query, ebXML);
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        query.setAuthorPersons(slots.toStringList(DOC_ENTRY_AUTHOR_PERSON));
        query.setConfidentialityCodes(slots.toCodeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME));
        query.getCreationTime().setFrom(slots.toNumber(DOC_ENTRY_CREATION_TIME_FROM));
        query.getCreationTime().setTo(slots.toNumber(DOC_ENTRY_CREATION_TIME_TO));
        query.setEventCodes(slots.toCodeQueryList(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME));
        query.setHealthcareFacilityTypeCodes(slots.toCodeList(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE));
        query.setPracticeSettingCodes(slots.toCodeList(DOC_ENTRY_PRACTICE_SETTING_CODE));
        query.setUniqueIds(slots.toStringList(DOC_ENTRY_UNIQUE_ID));
        query.setUuids(slots.toStringList(DOC_ENTRY_UUID));

        query.getServiceStartTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_FROM));
        query.getServiceStartTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_TO));

        query.getServiceStopTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM));
        query.getServiceStopTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO));
    }
}
