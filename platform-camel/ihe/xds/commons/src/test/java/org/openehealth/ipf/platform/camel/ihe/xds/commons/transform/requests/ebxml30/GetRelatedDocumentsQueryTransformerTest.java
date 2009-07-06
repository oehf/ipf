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
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetRelatedDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.QueryParameter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query.GetDocumentsQueryTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.query.GetRelatedDocumentsQueryTransformer;

/**
 * Tests for {@link GetDocumentsQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetRelatedDocumentsQueryTransformerTest {
    private GetRelatedDocumentsQueryTransformer transformer;
    private GetRelatedDocumentsQuery query;
    private EbXMLAdhocQueryRequest ebXML;
    
    @Before
    public void setUp() {
        transformer = new GetRelatedDocumentsQueryTransformer();
        query = new GetRelatedDocumentsQuery();

        query.getUUIDs().add("uuid1");
        query.getUUIDs().add("uuid2");
        query.getUniqueIDs().add("uniqueId1");
        query.getUniqueIDs().add("uniqueId2");
        query.setHomeCommunityID("home");
        query.getAssociationTypes().add(AssociationType.HAS_MEMBER);
        query.getAssociationTypes().add(AssociationType.TRANSFORM_AND_REPLACE);

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_RELATED_DOCUMENTS.getId(), ebXML.getId());
        
        assertEquals(Arrays.asList("('uuid1')", "('uuid2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_UUID.getSlotName()));
        
        assertEquals(Arrays.asList("('uniqueId1')", "('uniqueId2')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_UNIQUE_ID.getSlotName()));

        assertEquals(Arrays.asList("'home'"),
                ebXML.getSlotValues(QueryParameter.HOME.getSlotName()));
        
        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember')", "('urn:ihe:iti:2007:AssociationType:XFRM_RPLC')"),
                ebXML.getSlotValues(QueryParameter.ASSOCIATION_TYPE.getSlotName()));
        
        assertEquals(4, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new GetRelatedDocumentsQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        GetRelatedDocumentsQuery result = new GetRelatedDocumentsQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        GetRelatedDocumentsQuery result = new GetRelatedDocumentsQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new GetRelatedDocumentsQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        GetRelatedDocumentsQuery result = new GetRelatedDocumentsQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new GetRelatedDocumentsQuery(), result);
    }
}
