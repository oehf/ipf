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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.AssociationTransformer;

/**
 * Tests for {@link AssociationTransformer}.
 * @author Jens Riemschneider.
 */
public abstract class AssociationTransformerTestBase implements FactoryCreator {
    private AssociationTransformer transformer;
    private ObjectLibrary objectLibrary;
    private Association association;
    
    @Before
    public final void baseSetUp() {
        EbXMLFactory factory = createFactory();
        transformer = new AssociationTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        objectLibrary.put("id1", new Object());
        objectLibrary.put("id2", new Object());
        
        association = new Association();
        association.setAssociationType(AssociationType.REPLACE);
        association.setSourceUUID("id1");
        association.setTargetUUID("id2");        
    }
    
    @Test
    public void testToEbXML() {
        EbXMLAssociation ebXML = transformer.toEbXML(association, objectLibrary);
        assertNotNull(ebXML);
        
        assertEquals(AssociationType.REPLACE, ebXML.getAssociationType());
        assertEquals("id1", ebXML.getSource());
        assertEquals("id2", ebXML.getTarget());
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }

    @Test
    public void testToEbXMLEmpty() {
        EbXMLAssociation ebXML = transformer.toEbXML(new Association(), objectLibrary);
        
        assertNotNull(ebXML);

        assertNull(ebXML.getAssociationType());
        assertNull(ebXML.getSource());
        assertNull(ebXML.getTarget());
    }
    
    
    @Test
    public void testFromEbXML() {
        EbXMLAssociation ebXML = transformer.toEbXML(association, objectLibrary);
        assertEquals(association, transformer.fromEbXML(ebXML));
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }

    @Test
    public void testFromEbXMLEmpty() {
        EbXMLAssociation ebXML = transformer.toEbXML(new Association(), objectLibrary);
        assertEquals(new Association(), transformer.fromEbXML(ebXML));
    }
}
