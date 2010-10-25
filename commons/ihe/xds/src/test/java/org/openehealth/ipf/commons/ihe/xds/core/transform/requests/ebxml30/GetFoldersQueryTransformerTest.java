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
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.GetFoldersQueryTransformer;

/**
 * Tests for {@link GetFoldersQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetFoldersQueryTransformerTest {
    private GetFoldersQueryTransformer transformer;
    private GetFoldersQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new GetFoldersQueryTransformer();
        query = new GetFoldersQuery();

        query.setUuids(Arrays.asList("uuid1", "uuid2"));
        query.setUniqueIds(Arrays.asList("uniqueId1", "uniqueId2"));
        query.setHomeCommunityId("home");

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_FOLDERS.getId(), ebXML.getId());
        
        assertEquals(Arrays.asList("('uuid1')", "('uuid2')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_UUID.getSlotName()));
        
        assertEquals(Arrays.asList("('uniqueId1')", "('uniqueId2')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_UNIQUE_ID.getSlotName()));

        assertEquals("home", ebXML.getHome());
        assertEquals(2, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new GetFoldersQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        GetFoldersQuery result = new GetFoldersQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        GetFoldersQuery result = new GetFoldersQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new GetFoldersQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        GetFoldersQuery result = new GetFoldersQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new GetFoldersQuery(), result);
    }
}
