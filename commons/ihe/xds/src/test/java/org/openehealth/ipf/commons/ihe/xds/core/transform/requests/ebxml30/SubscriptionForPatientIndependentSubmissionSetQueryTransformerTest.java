package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForPatientIndependentSubmissionSetQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.SubscriptionForPatientIndependentSubmissionSetQueryTransformer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionForPatientIndependentSubmissionSetQueryTransformerTest extends AbstractQueryTransformerTest<SubscriptionForPatientIndependentSubmissionSetQuery, SubscriptionForPatientIndependentSubmissionSetQueryTransformer> {

    @BeforeEach
    public void setUp() {
        transformer = SubscriptionForPatientIndependentSubmissionSetQueryTransformer.getInstance();
        query = (SubscriptionForPatientIndependentSubmissionSetQuery) SampleData.createSubscriptionForPatientIndependentSubmissionSetQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_SUBMISSION_SET.getId(), ebXML.getId());
        var intendedRecipients = ebXML.getSlotValues(QueryParameter.SUBMISSION_SET_INTENDED_RECIPIENT.getSlotName());
        assertEquals(List.of("('Some Hospital%')", "('|Welby%')"), intendedRecipients);
        var sourceIds = ebXML.getSlotValues(QueryParameter.SUBMISSION_SET_SOURCE_ID.getSlotName());
        assertEquals(List.of("('1.2.5.7.8')"), sourceIds);
    }

    @Override
    protected SubscriptionForPatientIndependentSubmissionSetQuery emptyQuery() {
        return new SubscriptionForPatientIndependentSubmissionSetQuery();
    }
}
