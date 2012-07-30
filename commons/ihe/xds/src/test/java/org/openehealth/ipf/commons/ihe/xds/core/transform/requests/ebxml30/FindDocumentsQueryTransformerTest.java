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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.DocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsForMultiplePatientsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsForMultiplePatientsQueryTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsQueryTransformer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FindDocumentsQueryTransformer} and {@link FindDocumentsForMultiplePatientsQueryTransformer}
 * @author Jens Riemschneider
 * @author Michael Ottati
 */
public class FindDocumentsQueryTransformerTest {
    private FindDocumentsQueryTransformer transformer;
    private FindDocumentsForMultiplePatientsQueryTransformer multiplePatientsQueryTransformer;
    private FindDocumentsForMultiplePatientsQuery multiplePatientsQuery;
    private FindDocumentsQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new FindDocumentsQueryTransformer();
        query = (FindDocumentsQuery)SampleData.createFindDocumentsQuery().getQuery();
        multiplePatientsQueryTransformer = new FindDocumentsForMultiplePatientsQueryTransformer();
        multiplePatientsQuery = (FindDocumentsForMultiplePatientsQuery)SampleData.createFindDocumentsForMultiplePatientsQuery().getQuery();

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.FIND_DOCUMENTS.getId(), ebXML.getId());
        assertEquals("12.21.41", ebXML.getHome());
        assertEquals(Arrays.asList("'id3^^^&1.3&ISO'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));
        toEbXML(query,ebXML);
    }

    @Test
    public void testToEbXML_MPQ() {
        multiplePatientsQueryTransformer.toEbXML(multiplePatientsQuery,ebXML);
        assertEquals(QueryType.FIND_DOCUMENTS_MPQ.getId(), ebXML.getId());
        assertEquals(Arrays.asList("('id3^^^&1.3&ISO')","('id4^^^&1.4&ISO')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));
        toEbXML(query,ebXML);
    }

    public void toEbXML(DocumentsQuery query,EbXMLAdhocQueryRequest ebXML) {

        assertEquals("12.21.41", ebXML.getHome());

        assertEquals(Arrays.asList("('code1^^scheme1')", "('code2^^scheme2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CLASS_CODE.getSlotName()));

        assertEquals(Arrays.asList("('codet1^^schemet1')", "('codet2^^schemet2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE_CODE.getSlotName()));

        assertEquals(Arrays.asList("('code3^^scheme3')", "('code4^^scheme4')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PRACTICE_SETTING_CODE.getSlotName()));
        
        assertEquals(Arrays.asList("1980"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CREATION_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("1981"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_CREATION_TIME_TO.getSlotName()));

        assertEquals(Arrays.asList("1982"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_START_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("1983"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_START_TIME_TO.getSlotName()));

        assertEquals(Arrays.asList("1984"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_STOP_TIME_FROM.getSlotName()));
        assertEquals(Arrays.asList("1985"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_SERVICE_STOP_TIME_TO.getSlotName()));
        
        assertEquals(Arrays.asList("('code5^^scheme5')", "('code6^^scheme6')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE.getSlotName()));

        List<EbXMLSlot> slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_EVENT_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code7^^scheme7')", "('code8^^scheme8')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code9^^scheme9')"), slots.get(1).getValueList());
        
        slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code10^^scheme10')", "('code11^^scheme11')"), slots.get(0).getValueList());
        assertEquals(Arrays.asList("('code12^^scheme12')"), slots.get(1).getValueList());
        
        assertEquals(Arrays.asList("('per''son1')", "('person2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_AUTHOR_PERSON.getSlotName()));

        assertEquals(Arrays.asList("('code13^^scheme13')", "('code14^^scheme14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE.getSlotName()));
        
        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName()));

        assertEquals(Arrays.asList("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE.getSlotName()));

        assertEquals(19, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    @Test
    public void testToEbXMLNull_MPQ() {
        multiplePatientsQueryTransformer.toEbXML(null,ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new FindDocumentsQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    @Test
    public void testToEbXMLEmpty_MPQ() {
        multiplePatientsQueryTransformer.toEbXML(new FindDocumentsForMultiplePatientsQuery(),ebXML);
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
    public void testFromEbXML_MPQ() {

        multiplePatientsQueryTransformer.toEbXML(multiplePatientsQuery,ebXML);
        FindDocumentsForMultiplePatientsQuery mpResult = new FindDocumentsForMultiplePatientsQuery();
        multiplePatientsQueryTransformer.fromEbXML(mpResult,ebXML);
        assertEquals(multiplePatientsQuery,mpResult);
    }
    
    @Test
    public void testFromEbXMLNull() {
        FindDocumentsQuery result = new FindDocumentsQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new FindDocumentsQuery(), result);
    }

    @Test
    public void testFromEbXMLNull_MPQ() {
        FindDocumentsForMultiplePatientsQuery result = new FindDocumentsForMultiplePatientsQuery();
        multiplePatientsQueryTransformer.fromEbXML(result, null);
        assertEquals(new FindDocumentsForMultiplePatientsQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        FindDocumentsQuery result = new FindDocumentsQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new FindDocumentsQuery(), result);
    }

    @Test
    public void testFromEbXMLEmpty_MPQ() {
        FindDocumentsForMultiplePatientsQuery result = new FindDocumentsForMultiplePatientsQuery();
        multiplePatientsQueryTransformer.fromEbXML(result, ebXML);
        assertEquals(new FindDocumentsForMultiplePatientsQuery(), result);
    }
}
