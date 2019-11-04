package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Pharm1StableDocumentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.toHL7;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

abstract class Pharm1StableDocumentsQueryTransformer<T extends Pharm1StableDocumentsQuery> extends AbstractStoredQueryTransformer<T> {

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

        slots.fromString(SUBMISSION_SET_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromStringList(DOC_ENTRY_AUTHOR_PERSON, query.getAuthorPersons());

        slots.fromNumber(DOC_ENTRY_CREATION_TIME_FROM, toHL7(query.getCreationTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_CREATION_TIME_TO, toHL7(query.getCreationTime().getTo()));

        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_FROM, toHL7(query.getServiceStartTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_START_TIME_TO, toHL7(query.getServiceStartTime().getTo()));

        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM, toHL7(query.getServiceStopTime().getFrom()));
        slots.fromNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO, toHL7(query.getServiceStopTime().getTo()));

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

        super.fromEbXML(query, ebXML);

        QuerySlotHelper slots = new QuerySlotHelper(ebXML);

        query.setPracticeSettingCodes(slots.toCodeList(DOC_ENTRY_PRACTICE_SETTING_CODE));
        query.setHealthcareFacilityTypeCodes(slots.toCodeList(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE));

        query.setEventCodes(slots.toCodeQueryList(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME));
        query.setConfidentialityCodes(slots.toCodeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME));


        String patientId = slots.toString(SUBMISSION_SET_PATIENT_ID);
        query.setPatientId(Hl7v2Based.parse(patientId, Identifiable.class));
        query.setAuthorPersons(slots.toStringList(DOC_ENTRY_AUTHOR_PERSON));

        query.getCreationTime().setFrom(slots.toNumber(DOC_ENTRY_CREATION_TIME_FROM));
        query.getCreationTime().setTo(slots.toNumber(DOC_ENTRY_CREATION_TIME_TO));

        query.getServiceStartTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_FROM));
        query.getServiceStartTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_START_TIME_TO));

        query.getServiceStopTime().setFrom(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_FROM));
        query.getServiceStopTime().setTo(slots.toNumber(DOC_ENTRY_SERVICE_STOP_TIME_TO));
    }
}
