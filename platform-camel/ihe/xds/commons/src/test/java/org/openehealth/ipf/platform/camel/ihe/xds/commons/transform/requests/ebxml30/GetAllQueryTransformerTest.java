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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.ebxml30;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLSlot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetAllQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query.GetAllQueryTransformer;

/**
 * Tests for {@link GetAllQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetAllQueryTransformerTest {
    private GetAllQueryTransformer transformer;
    private GetAllQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new GetAllQueryTransformer();
        query = new GetAllQuery();
        
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("name1", "uni1", "uniType1")));
        query.getConfidentialityCodes().getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        query.getConfidentialityCodes().getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.getFormatCodes().add(new Code("code13", null, null));
        query.getFormatCodes().add(new Code("code14", null, null));
        query.getStatusDocuments().add(AvailabilityStatus.APPROVED);
        query.getStatusDocuments().add(AvailabilityStatus.SUBMITTED);
        query.getStatusFolders().add(AvailabilityStatus.DEPRECATED);
        query.getStatusSubmissionSets().add(AvailabilityStatus.SUBMITTED);

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_ALL.getId(), ebXML.getId());
        assertEquals(Arrays.asList("'id1^^^name1&uni1&uniType1'"),
                ebXML.getSlotValues(QueryParameter.PATIENT_ID.getSlotName()));
        
        List<EbXMLSlot> slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code10^^scheme10')", "('code11^^scheme11')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code12^^scheme12')"), slots.get(1).getValueList());
        
        assertEquals(Arrays.asList("('code13')", "('code14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE.getSlotName()));

        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName()));

        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_STATUS.getSlotName()));

        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.SUBMISSION_SET_STATUS.getSlotName()));
        
        assertEquals(7, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new GetAllQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        GetAllQuery result = new GetAllQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        GetAllQuery result = new GetAllQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new GetAllQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        GetAllQuery result = new GetAllQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new GetAllQuery(), result);
    }
}
