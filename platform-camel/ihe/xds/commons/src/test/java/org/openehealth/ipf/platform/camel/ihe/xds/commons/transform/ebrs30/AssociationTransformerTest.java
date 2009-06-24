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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.AssociationType1;

/**
 * Tests for {@link AssociationTransformer}.
 * @author Jens Riemschneider.
 */
public class AssociationTransformerTest {
    private AssociationTransformer transformer;
    private Map<String, Object> knownObjects;
    private Association association;
    private Object obj1;
    private Object obj2;
    
    @Before
    public void setUp() {
        transformer = new AssociationTransformer();

        knownObjects = new HashMap<String, Object>();
        obj1 = new Object();
        obj2 = new Object();
        knownObjects.put("id1", obj1);
        knownObjects.put("id2", obj2);
        association = new Association();
        association.setAssociationType(AssociationType.REPLACE);
        association.setSourceUUID("id1");
        association.setTargetUUID("id2");        
    }
    
    @Test
    public void testToEbXML30() {
        AssociationType1 ebXML = transformer.toEbXML30(association);
        assertNotNull(ebXML);
        
        assertEquals("RPLC", ebXML.getAssociationType());
        assertSame(obj1, knownObjects.get(ebXML.getSourceObject()));
        assertSame(obj2, knownObjects.get(ebXML.getTargetObject()));
    }
    
    @Test
    public void testToEbXML30Null() {
        assertNull(transformer.toEbXML30(null));
    }

    @Test
    public void testToEbXML30Empty() {
        AssociationType1 ebXML = transformer.toEbXML30(new Association());
        
        assertNotNull(ebXML);
        
        assertNull(ebXML.getAssociationType());
        assertNull(ebXML.getSourceObject());
        assertNull(ebXML.getTargetObject());
    }
    
    
    @Test
    public void testFromEbXML30() {
        AssociationType1 ebXML = transformer.toEbXML30(association);
        Association result = transformer.fromEbXML30(ebXML);
        assertNotNull(result);
        
        assertEquals(AssociationType.REPLACE, result.getAssociationType());
        assertEquals("id1", result.getSourceUUID());
        assertEquals("id2", result.getTargetUUID());
    }
    
    @Test
    public void testFromEbXML30Null() {
        assertNull(transformer.fromEbXML30(null));
    }

    @Test
    public void testFromEbXML30Empty() {
        Association result = transformer.fromEbXML30(new AssociationType1());
        assertNotNull(result);
        assertNull(result.getAssociationType());
        assertNull(result.getSourceUUID());
        assertNull(result.getTargetUUID());
    }
}
