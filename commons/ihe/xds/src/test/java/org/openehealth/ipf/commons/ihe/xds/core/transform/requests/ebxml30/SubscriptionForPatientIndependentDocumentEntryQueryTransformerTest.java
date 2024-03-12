package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForPatientIndependentDocumentEntryQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.SubscriptionForPatientIndependentDocumentEntryQueryTransformer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionForPatientIndependentDocumentEntryQueryTransformerTest
    extends AbstractQueryTransformerTest<SubscriptionForPatientIndependentDocumentEntryQuery, SubscriptionForPatientIndependentDocumentEntryQueryTransformer> {

    @BeforeEach
    public void setUp() {
        transformer = SubscriptionForPatientIndependentDocumentEntryQueryTransformer.getInstance();
        query = (SubscriptionForPatientIndependentDocumentEntryQuery) SampleData.createSubscriptionForPatientIndependentDocumentEntryQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_DOCUMENT_ENTRY.getId(), ebXML.getId());
        var healthCareFacilityTypeCodes = ebXML.getSlotValues(QueryParameter.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE.getSlotName());
        assertEquals(List.of("('Emergency Department^^healthcareFacilityCodingScheme')"), healthCareFacilityTypeCodes);
    }

    @Override
    protected SubscriptionForPatientIndependentDocumentEntryQuery emptyQuery() {
        return new SubscriptionForPatientIndependentDocumentEntryQuery();
    }
}
