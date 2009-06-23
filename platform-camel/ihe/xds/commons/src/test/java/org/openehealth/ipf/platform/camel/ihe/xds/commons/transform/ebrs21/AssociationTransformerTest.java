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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs21;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.AssociationType1;

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
    private Map<Object, String> reverseKnownObjects;
    
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

        reverseKnownObjects = new HashMap<Object, String>();
        for (Map.Entry<String, Object> entry : knownObjects.entrySet()) {
            reverseKnownObjects.put(entry.getValue(), entry.getKey());
        }
    }
    
    @Test
    public void testToEbXML21() {
        AssociationType1 ebXML = transformer.toEbXML21(knownObjects, association);
        assertNotNull(ebXML);
        
        assertEquals("RPLC", ebXML.getAssociationType());
        assertSame(obj1, ebXML.getSourceObject());
        assertSame(obj2, ebXML.getTargetObject());
    }
    
    @Test
    public void testToEbXML21Null() {
        assertNull(transformer.toEbXML21(knownObjects, null));
    }

    @Test
    public void testToEbXML21Empty() {
        AssociationType1 ebXML = transformer.toEbXML21(knownObjects, new Association());
        
        assertNotNull(ebXML);
        
        assertNull(ebXML.getAssociationType());
        assertNull(ebXML.getSourceObject());
        assertNull(ebXML.getTargetObject());
    }
    
    
    @Test
    public void testFromEbXML21() {
        AssociationType1 ebXML = transformer.toEbXML21(knownObjects, association);
        Association result = transformer.fromEbXML21(reverseKnownObjects, ebXML);
        assertNotNull(result);
        
        assertEquals(AssociationType.REPLACE, result.getAssociationType());
        assertEquals("id1", result.getSourceUUID());
        assertEquals("id2", result.getTargetUUID());
    }
    
    @Test
    public void testFromEbXML21Null() {
        assertNull(transformer.fromEbXML21(reverseKnownObjects, null));
    }

    @Test
    public void testFromEbXML21Empty() {
        Association result = transformer.fromEbXML21(reverseKnownObjects, new AssociationType1());
        assertNotNull(result);
        assertNull(result.getAssociationType());
        assertNull(result.getSourceUUID());
        assertNull(result.getTargetUUID());
    }
}
