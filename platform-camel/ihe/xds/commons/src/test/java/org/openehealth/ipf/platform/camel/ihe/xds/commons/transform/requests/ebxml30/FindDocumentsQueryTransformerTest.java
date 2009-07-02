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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query.FindDocumentsQueryTransformer;

/**
 * Tests for {@link FindDocumentsQueryTransformer}.
 * @author Jens Riemschneider
 */
public class FindDocumentsQueryTransformerTest {
    private FindDocumentsQueryTransformer transformer;
    private FindDocumentsQuery query;
    private AdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new FindDocumentsQueryTransformer();
        query = new FindDocumentsQuery();
        
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("name1", "uni1", "uniType1")));
        query.getClassCodes().add(new Code("code1", null, "scheme1"));
        query.getClassCodes().add(new Code("code2", null, "scheme2"));
        query.getPracticeSettingCodes().add(new Code("code3", null, "scheme3"));
        query.getPracticeSettingCodes().add(new Code("code4", null, "scheme4"));
        query.getCreationTime().setFrom("1");
        query.getCreationTime().setTo("2");
        query.getServiceStartTime().setFrom("3");
        query.getServiceStartTime().setTo("4");
        query.getServiceStopTime().setFrom("5");
        query.getServiceStopTime().setTo("6");
        query.getHealthCareFacilityTypeCodes().add(new Code("code5", null, "scheme5"));
        query.getHealthCareFacilityTypeCodes().add(new Code("code6", null, "scheme6"));
        query.getEventCodes().getOuterList().add(
                Arrays.asList(new Code("code7", null, "scheme7"), new Code("code8", null, "scheme8")));
        query.getEventCodes().getOuterList().add(
                Arrays.asList(new Code("code9", null, "scheme9")));
        query.getConfidentialityCodes().getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        query.getConfidentialityCodes().getOuterList().add(
                Arrays.asList(new Code("code12", null, "scheme12")));
        query.getAuthorPersons().add("per'son1");
        query.getAuthorPersons().add("person2");
        query.getFormatCodes().add(new Code("code13", null, null));
        query.getFormatCodes().add(new Code("code14", null, null));
        query.getStatus().add(AvailabilityStatus.APPROVED);
        query.getStatus().add(AvailabilityStatus.SUBMITTED);

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.FIND_DOCUMENTS.getId(), ebXML.getId());
        assertEquals(Arrays.asList("'id1^^^name1&uni1&uniType1'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));
        
        assertEquals(Arrays.asList("('code1^^scheme1')", "('code2^^scheme2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()));

        assertEquals(Arrays.asList("('code3^^scheme3')", "('code4^^scheme4')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PRACTICE_SETTING_CODE.getSlotName()));
        
        assertEquals(Arrays.asList("1"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CREATION_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("2"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CREATION_TIME_TO.getSlotName()));

        assertEquals(Arrays.asList("3"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_START_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("4"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_START_TIME_TO.getSlotName()));

        assertEquals(Arrays.asList("5"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_STOP_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("6"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_STOP_TIME_TO.getSlotName()));
        
        assertEquals(Arrays.asList("('code5^^scheme5')", "('code6^^scheme6')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_HEALTH_CARE_FACILITY_TYPE_CODE.getSlotName()));

        List<Slot> slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_EVENT_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code7^^scheme7')", "('code8^^scheme8')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code9^^scheme9')"), slots.get(1).getValueList());
        
        slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code10^^scheme10')", "('code11^^scheme11')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code12^^scheme12')"), slots.get(1).getValueList());
        
        assertEquals(Arrays.asList("('per''son1')", "('person2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_AUTHOR_PERSON.getSlotName()));

        assertEquals(Arrays.asList("('code13')", "('code14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE.getSlotName()));
        
        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName()));
        
        assertEquals(17, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new FindDocumentsQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        FindDocumentsQuery result = new FindDocumentsQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        FindDocumentsQuery result = new FindDocumentsQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new FindDocumentsQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        FindDocumentsQuery result = new FindDocumentsQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new FindDocumentsQuery(), result);
    }
}
