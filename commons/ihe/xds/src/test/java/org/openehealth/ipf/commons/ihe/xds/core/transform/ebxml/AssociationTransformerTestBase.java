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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link AssociationTransformer}.
 * @author Jens Riemschneider.
 */
public abstract class AssociationTransformerTestBase implements FactoryCreator {
    private AssociationTransformer transformer;
    private EbXMLObjectLibrary objectLibrary;
    protected Association association;
    
    @BeforeEach
    public void baseSetUp() {
        var factory = createFactory();
        transformer = new AssociationTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        objectLibrary.put("id1", new Object());
        objectLibrary.put("id2", new Object());
        
        association = new Association();
        association.setAssociationType(AssociationType.REPLACE);
        association.setSourceUuid("id1");
        association.setTargetUuid("id2");        
        association.setLabel(AssociationLabel.ORIGINAL);
        association.setEntryUuid("uuid");
        association.setDocCode(new Code("code", new LocalizedString("display", "en-US", "UTF-8"), "scheme"));
    }
    
    @Test
    public void testToEbXML() {
        var ebXML = transformer.toEbXML(association, objectLibrary);
        assertNotNull(ebXML);
        
        assertEquals(AssociationType.REPLACE, ebXML.getAssociationType());
        assertEquals("id1", ebXML.getSource());
        assertEquals("id2", ebXML.getTarget());
        assertEquals("Original", ebXML.getSingleSlotValue(Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS));
        assertEquals("uuid", ebXML.getId());

        var classifications = ebXML.getClassifications(Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME);
        assertEquals(1, classifications.size());
        var classification = classifications.get(0);
        assertEquals("uuid", classification.getClassifiedObject());
        assertEquals("code", classification.getNodeRepresentation());
        assertEquals("display", classification.getNameAsInternationalString().getSingleLocalizedString().getValue());
        assertEquals("scheme", classification.getSingleSlotValue("codingScheme"));

        checkExtraValues(ebXML);
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }

    @Test
    public void testToEbXMLEmpty() {
        var ebXML = transformer.toEbXML(new Association(), objectLibrary);
        assertNotNull(ebXML);
        assertNull(ebXML.getAssociationType());
        assertNull(ebXML.getSource());
        assertNull(ebXML.getTarget());
        assertNull(ebXML.getSingleSlotValue(Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS));
        assertNull(ebXML.getId());
        assertEquals(0, ebXML.getClassifications().size());
    }
    
    
    @Test
    public void testFromEbXML() {
        var ebXML = transformer.toEbXML(association, objectLibrary);
        assertEquals(association, transformer.fromEbXML(ebXML));
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }

    @Test
    public void testFromEbXMLEmpty() {
        var ebXML = transformer.toEbXML(new Association(), objectLibrary);
        assertEquals(new Association(), transformer.fromEbXML(ebXML));
    }

    protected abstract void checkExtraValues(EbXMLAssociation ebXML);
}
