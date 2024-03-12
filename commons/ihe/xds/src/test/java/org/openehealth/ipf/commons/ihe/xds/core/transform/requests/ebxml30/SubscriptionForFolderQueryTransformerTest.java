package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForFolderQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.SubscriptionForFolderQueryTransformer;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionForFolderQueryTransformerTest extends AbstractQueryTransformerTest<SubscriptionForFolderQuery, SubscriptionForFolderQueryTransformer> {

    @BeforeEach
    public void setUp() {
        transformer = SubscriptionForFolderQueryTransformer.getInstance();
        query = (SubscriptionForFolderQuery) SampleData.createSubscriptionForFolderQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.SUBSCRIPTION_FOR_FOLDER.getId(), ebXML.getId());
        assertEquals(Collections.singletonList("'st3498702^^^&1.3.6.1.4.1.21367.2005.3.7&ISO'"),
            ebXML.getSlotValues(QueryParameter.FOLDER_PATIENT_ID.getSlotName()));
        var folderCodeSlotValues = ebXML.getSlotValues(QueryParameter.FOLDER_CODES.getSlotName());
        assertEquals(Collections.singletonList("('FolderCodeExample^^folderCodeListCodingScheme')"), folderCodeSlotValues);
    }

    @Override
    protected SubscriptionForFolderQuery emptyQuery() {
        return new SubscriptionForFolderQuery();
    }
}
