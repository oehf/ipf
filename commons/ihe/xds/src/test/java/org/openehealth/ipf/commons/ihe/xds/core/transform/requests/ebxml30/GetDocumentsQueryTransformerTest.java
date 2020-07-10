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
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.GetDocumentsQueryTransformer;

/**
 * Tests for {@link GetDocumentsQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetDocumentsQueryTransformerTest {
    private GetDocumentsQueryTransformer transformer;
    private GetDocumentsQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new GetDocumentsQueryTransformer();
        query = new GetDocumentsQuery();

        query.setUuids(Arrays.asList("uuid1", "uuid2"));
        query.setUniqueIds(Arrays.asList("uniqueId1", "uniqueId2"));
        query.setHomeCommunityId("home");

        var extraParams1 = new QueryList<String>();
        extraParams1.getOuterList().add(Arrays.asList("para-11", "para-12"));
        extraParams1.getOuterList().add(Arrays.asList("para-21", "para-22", "para-23"));

        var extraParams2 = new QueryList<String>();
        extraParams2.getOuterList().add(Arrays.asList("dia-31", "dia-32", "dia-33"));
        extraParams2.getOuterList().add(Collections.singletonList("dia-41"));

        query.getExtraParameters().put("$PatientPerimeter", extraParams1);
        query.getExtraParameters().put("$PatientDiameter", extraParams2);

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_DOCUMENTS.getId(), ebXML.getId());
        
        assertEquals(Arrays.asList("('uuid1')", "('uuid2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_UUID.getSlotName()));
        
        assertEquals(Arrays.asList("('uniqueId1')", "('uniqueId2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_UNIQUE_ID.getSlotName()));

        var perimeters = ebXML.getSlots("$PatientPerimeter");
        assertEquals(2, perimeters.size());
        assertEquals(Arrays.asList("('para-11')", "('para-12')"), perimeters.get(0).getValueList());
        assertEquals(Arrays.asList("('para-21')", "('para-22')", "('para-23')"), perimeters.get(1).getValueList());

        var diameters = ebXML.getSlots("$PatientDiameter");
        assertEquals(2, diameters.size());
        assertEquals(Arrays.asList("('dia-31')", "('dia-32')", "('dia-33')"), diameters.get(0).getValueList());
        assertEquals(Collections.singletonList("('dia-41')"), diameters.get(1).getValueList());

        assertEquals(Arrays.asList("('uniqueId1')", "('uniqueId2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_UNIQUE_ID.getSlotName()));

        assertEquals("home", ebXML.getHome());
        assertEquals(6, ebXML.getSlots().size());
    }

    @Test
    public void testRoundtrip() {
        transformer.toEbXML(query, ebXML);
        var otherQuery = new GetDocumentsQuery();
        transformer.fromEbXML(otherQuery, ebXML);
        assertEquals(query, otherQuery);
    }

    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new GetDocumentsQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        var result = new GetDocumentsQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        var result = new GetDocumentsQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new GetDocumentsQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        var result = new GetDocumentsQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new GetDocumentsQuery(), result);
    }
}
