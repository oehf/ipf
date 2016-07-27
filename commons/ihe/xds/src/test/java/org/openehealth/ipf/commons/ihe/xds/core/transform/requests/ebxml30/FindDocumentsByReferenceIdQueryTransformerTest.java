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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsByReferenceIdQueryTransformer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FindDocumentsByReferenceIdQueryTransformer}.
 * @author Dmytro Rud
 */
public class FindDocumentsByReferenceIdQueryTransformerTest {
    private FindDocumentsByReferenceIdQueryTransformer transformer;
    private FindDocumentsByReferenceIdQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new FindDocumentsByReferenceIdQueryTransformer();
        query = (FindDocumentsByReferenceIdQuery) SampleData.createFindDocumentsByReferenceIdQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.FIND_DOCUMENTS_BY_REFERENCE_ID.getId(), ebXML.getId());
        assertEquals("12.21.41", ebXML.getHome());
        assertEquals(Arrays.asList("'id3^^^&1.3&ISO'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));

        List<EbXMLSlot> referenceIdSlots = ebXML.getSlots(QueryParameter.DOC_ENTRY_REFERENCE_IDS.getSlotName());
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

    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new FindDocumentsByReferenceIdQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        FindDocumentsByReferenceIdQuery result = new FindDocumentsByReferenceIdQuery();
        transformer.fromEbXML(result, ebXML);
        assertEquals(query, result);
    }

    @Test
    public void testFromEbXMLNull() {
        FindDocumentsByReferenceIdQuery result = new FindDocumentsByReferenceIdQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new FindDocumentsByReferenceIdQuery(), result);
    }

    @Test
    public void testFromEbXMLEmpty() {
        FindDocumentsByReferenceIdQuery result = new FindDocumentsByReferenceIdQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new FindDocumentsByReferenceIdQuery(), result);
    }

}
