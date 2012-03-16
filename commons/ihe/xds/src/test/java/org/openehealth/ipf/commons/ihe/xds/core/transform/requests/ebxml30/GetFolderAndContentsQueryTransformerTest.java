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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFolderAndContentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.GetDocumentsQueryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.GetFolderAndContentsQueryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.QueryParameterValidation;

/**
 * Tests for {@link GetDocumentsQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetFolderAndContentsQueryTransformerTest {
    private GetFolderAndContentsQueryTransformer transformer;
    private GetFolderAndContentsQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new GetFolderAndContentsQueryTransformer();
        query = new GetFolderAndContentsQuery();

        query.setUuid("uuid1");
        query.setUniqueId("uniqueId1");
        query.setHomeCommunityId("home");
        QueryList<Code> confidentialityCodes = new QueryList<Code>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.setConfidentialityCodes(confidentialityCodes);
        query.setFormatCodes(Arrays.asList(new Code("code13", null, null), new Code("code14", null, null)));
        query.setDocumentEntryTypes(Arrays.asList(DocumentEntryType.STABLE));

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_FOLDER_AND_CONTENTS.getId(), ebXML.getId());
        
        assertEquals(Arrays.asList("'uuid1'"),
                ebXML.getSlotValues(QueryParameter.FOLDER_UUID.getSlotName()));
        
        assertEquals(Arrays.asList("'uniqueId1'"),
                ebXML.getSlotValues(QueryParameter.FOLDER_UNIQUE_ID.getSlotName()));

        assertEquals("home", ebXML.getHome());

        assertEquals(Arrays.asList("('code13')", "('code14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE.getSlotName()));
        
        List<EbXMLSlot> slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code10^^scheme10')", "('code11^^scheme11')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code12^^scheme12')"), slots.get(1).getValueList());

        assertEquals(Arrays.asList("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE.getSlotName()));

        assertEquals(6, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new GetFolderAndContentsQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        GetFolderAndContentsQuery result = new GetFolderAndContentsQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        GetFolderAndContentsQuery result = new GetFolderAndContentsQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new GetFolderAndContentsQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        GetFolderAndContentsQuery result = new GetFolderAndContentsQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new GetFolderAndContentsQuery(), result);
    }
}
