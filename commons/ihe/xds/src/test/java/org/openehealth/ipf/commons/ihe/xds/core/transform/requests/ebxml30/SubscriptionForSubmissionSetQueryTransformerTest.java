package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForSubmissionSetQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.SubscriptionForSubmissionSetQueryTransformer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionForSubmissionSetQueryTransformerTest extends AbstractQueryTransformerTest<SubscriptionForSubmissionSetQuery, SubscriptionForSubmissionSetQueryTransformer> {

    @BeforeEach
    public void setUp() {
        transformer = SubscriptionForSubmissionSetQueryTransformer.getInstance();
        query = (SubscriptionForSubmissionSetQuery) SampleData.createSubscriptionForSubmissionSetQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.SUBSCRIPTION_FOR_SUBMISSION_SET.getId(), ebXML.getId());
        assertEquals(Collections.singletonList("'st3498702^^^&1.3.6.1.4.1.21367.2005.3.7&ISO'"),
            ebXML.getSlotValues(QueryParameter.SUBMISSION_SET_PATIENT_ID.getSlotName()));
        var intendedRecipients = ebXML.getSlotValues(QueryParameter.SUBMISSION_SET_INTENDED_RECIPIENT.getSlotName());
        assertEquals(List.of("('Some Hospital%')", "('|Welby%')"), intendedRecipients);
    }

    @Override
    protected SubscriptionForSubmissionSetQuery emptyQuery() {
        return new SubscriptionForSubmissionSetQuery();
    }
}
