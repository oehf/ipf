/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsByReferenceIdQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsByReferenceIdQueryTransformer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link FindDocumentsByReferenceIdQueryTransformer}.
 * @author Dmytro Rud
 */
public class FindDocumentsByReferenceIdQueryTransformerTest extends AbstractQueryTransformerTest<FindDocumentsByReferenceIdQuery, FindDocumentsByReferenceIdQueryTransformer> {
    
    @BeforeEach
    public void setUp() {
        transformer = FindDocumentsByReferenceIdQueryTransformer.getInstance();
        query = (FindDocumentsByReferenceIdQuery) SampleData.createFindDocumentsByReferenceIdQuery().getQuery();
        query.setTargetCommunityIds(List.of("urn:oid:2.1.1", "urn:oid:2.2.2"));
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.FIND_DOCUMENTS_BY_REFERENCE_ID.getId(), ebXML.getId());
        assertEquals("urn:oid:1.21.41", ebXML.getHome());
        assertEquals(Collections.singletonList("'id3^^^&1.3&ISO'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));

        var referenceIdSlots = ebXML.getSlots(QueryParameter.DOC_ENTRY_REFERENCE_IDS.getSlotName());
        assertEquals(2, referenceIdSlots.size());
        assertEquals(Arrays.asList(
                "('ref-id-11^^^&1.1.1.1&ISO^urn:ihe:iti:xds:2013:uniqueId')",
                "('ref-id-12^^^^urn:ihe:iti:xdw:2013:workflowInstanceId')",
                "('ref-id-13^^^^urn:ihe:iti:xds:2013:referral')"),
                referenceIdSlots.get(0).getValueList());
        assertEquals(Arrays.asList(
                "('ref-id-21^^^&1.1.1.2&ISO^urn:ihe:iti:xds:2013:accession')",
                "('ref-id-22^^^^urn:ihe:iti:xds:2013:order')"),
                referenceIdSlots.get(1).getValueList());

        var targetCommunityIdSlots = ebXML.getSlots(QueryParameter.TARGET_COMMUNITY_IDS.getSlotName());
        assertEquals(1, targetCommunityIdSlots.size());
        assertEquals(List.of("('urn:oid:2.1.1')", "('urn:oid:2.2.2')"), targetCommunityIdSlots.get(0).getValueList());
    }

    @Override
    protected FindDocumentsByReferenceIdQuery emptyQuery() {
        return new FindDocumentsByReferenceIdQuery();
    }
}
