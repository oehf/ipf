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

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.toHL7;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindMedicationListQuery;

/**
 * Transforms between {@link FindMedicationListQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Quentin Ligier
 */
public class FindMedicationListQueryTransformer extends AbstractStoredQueryTransformer<FindMedicationListQuery> {

    /**
     * Transforms the query into its EbXML representation.
     * <p>
     * Does not perform any transformation if one of the parameters is <code>null</code>.
     * @param query
     *          the query to transform.
     * @param ebXML
     *          the EbXML representation.
     */
    @Override
    public void toEbXML(FindMedicationListQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.toEbXML(query, ebXML);

        // TODO:QLIG
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        slots.fromString(SUBMISSION_SET_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));

        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_FROM, toHL7(query.getServiceStartTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_TO, toHL7(query.getServiceStartTime().getTo()));

        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM, toHL7(query.getServiceStopTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO, toHL7(query.getServiceStopTime().getTo()));

        slots.fromCode(DOC_ENTRY_FORMAT_CODE, query.getFormatCodes());
        slots.fromCode(DOC_ENTRY_TYPE_CODE, query.getTypeCodes());
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
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
    @Override
    public void fromEbXML(FindMedicationListQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.fromEbXML(query, ebXML);

        // TODO:QLIG
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        String patientId = slots.toString(SUBMISSION_SET_PATIENT_ID);
        query.setPatientId(Hl7v2Based.parse(patientId, Identifiable.class));

        query.getServiceStartTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_FROM));
        query.getServiceStartTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_TO));

        query.getServiceStopTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM));
        query.getServiceStopTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO));

        query.setFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE));
        query.setTypeCodes(slots.toCodeList(DOC_ENTRY_TYPE_CODE));
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }
}
