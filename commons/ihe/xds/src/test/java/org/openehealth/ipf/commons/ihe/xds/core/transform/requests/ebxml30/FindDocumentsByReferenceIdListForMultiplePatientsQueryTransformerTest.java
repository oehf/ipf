/*
 * Copyright 2024 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsByReferenceIdForMultiplePatientsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer}.
 * @author Dmytro Rud
 */
public class FindDocumentsByReferenceIdListForMultiplePatientsQueryTransformerTest extends AbstractQueryTransformerTest<FindDocumentsByReferenceIdForMultiplePatientsQuery, FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer> {
    
    @BeforeEach
    public void setUp() {
        transformer = FindDocumentsByReferenceIdForMultiplePatientsQueryTransformer.getInstance();
        query = (FindDocumentsByReferenceIdForMultiplePatientsQuery) SampleData.createFindDocumentsByReferenceIdForMultiplePatientsQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.FIND_DOCUMENTS_BY_REFERENCE_ID_MPQ.getId(), ebXML.getId());
        assertEquals("urn:oid:1.21.41", ebXML.getHome());
        assertEquals(Arrays.asList("('id3^^^&1.3&ISO')", "('id4^^^&1.4&ISO')"),
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
    }

    @Override
    protected FindDocumentsByReferenceIdForMultiplePatientsQuery emptyQuery() {
        return new FindDocumentsByReferenceIdForMultiplePatientsQuery();
    }
}
