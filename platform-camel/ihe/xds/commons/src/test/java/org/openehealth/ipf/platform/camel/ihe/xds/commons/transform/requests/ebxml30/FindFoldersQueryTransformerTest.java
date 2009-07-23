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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindFoldersQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryList;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query.FindFoldersQueryTransformer;

/**
 * Tests for {@link FindFoldersQueryTransformer}.
 * @author Jens Riemschneider
 */
public class FindFoldersQueryTransformerTest {
    private FindFoldersQueryTransformer transformer;
    private FindFoldersQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new FindFoldersQueryTransformer();
        query = new FindFoldersQuery();
        
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("name1", "uni1", "uniType1")));
        query.getLastUpdateTime().setFrom("1");
        query.getLastUpdateTime().setTo("2");
        QueryList<Code> codes = new QueryList<Code>();
        codes.getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        codes.getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.setCodes(codes);
        query.setStatus(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.SUBMITTED));

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.FIND_FOLDERS.getId(), ebXML.getId());
        assertEquals(Arrays.asList("'id1^^^name1&uni1&uniType1'"),
                ebXML.getSlotValues(QueryParameter.FOLDER_PATIENT_ID.getSlotName()));
        
        assertEquals(Arrays.asList("1"),
                ebXML.getSlotValues(QueryParameter.FOLDER_LAST_UPDATE_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("2"),
                ebXML.getSlotValues(QueryParameter.FOLDER_LAST_UPDATE_TIME_TO.getSlotName()));

        List<EbXMLSlot> slots = ebXML.getSlots(QueryParameter.FOLDER_CODES.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code7^^scheme7')", "('code8^^scheme8')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code9^^scheme9')"), slots.get(1).getValueList());
        
        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_STATUS.getSlotName()));
        
        assertEquals(6, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new FindFoldersQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        FindFoldersQuery result = new FindFoldersQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLLineBreakInAValueList() {
        transformer.toEbXML(query, ebXML);
        ebXML.getSlots().get(5).getValueList().clear();
        ebXML.getSlots().get(5).getValueList().add("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved',\n'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')");
        FindFoldersQuery result = new FindFoldersQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLNull() {
        FindFoldersQuery result = new FindFoldersQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new FindFoldersQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        FindFoldersQuery result = new FindFoldersQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new FindFoldersQuery().toString(), result.toString());
    }
}
