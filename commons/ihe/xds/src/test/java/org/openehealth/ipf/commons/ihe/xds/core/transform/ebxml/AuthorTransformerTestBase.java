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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Tests for {@link AuthorTransformer}.
 * @author Jens Riemschneider
 */
public abstract class AuthorTransformerTestBase implements FactoryCreator {
    private AuthorTransformer transformer;
    private EbXMLObjectLibrary objectLibrary;
    private Author author;
    
    @Before
    public final void baseSetUp() {
        EbXMLFactory factory = createFactory();
        transformer = new AuthorTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        
        Name name = new XcnName();
        name.setFamilyName("Adams");

        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setUniversalId("1.2.840.113619.6.197");
        assigningAuthority.setUniversalIdType(Vocabulary.UNIVERSAL_ID_TYPE_OID);

        Identifiable id = new Identifiable();
        id.setId("123");
        id.setAssigningAuthority(assigningAuthority);        

        Person authorPerson = new Person();
        authorPerson.setName(name);
        authorPerson.setId(id);

        author = new Author();
        author.setAuthorPerson(authorPerson);
        
        author.getAuthorInstitution().add(new Organization("inst1"));
        author.getAuthorInstitution().add(new Organization("inst2"));
        
        author.getAuthorRole().add("role1");
        author.getAuthorRole().add("role2");
        
        author.getAuthorSpecialty().add("spec1");
        author.getAuthorSpecialty().add("spec2");

        author.getAuthorTelecom().add(new Telecom("5.25 in", "Floppynet"));
        author.getAuthorTelecom().add(new Telecom("2:465/46.40", "Fidonet"));
    }
    
    @Test
    public void testToEbXML() {
        EbXMLClassification ebXML = transformer.toEbXML(author, objectLibrary);        
        assertNotNull(ebXML);
        assertNull(ebXML.getClassificationScheme());        
        assertEquals("", ebXML.getNodeRepresentation());
        
        List<EbXMLSlot> slots = ebXML.getSlots();
        assertEquals(5, slots.size());
        
        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_PERSON, slots.get(0).getName());
        assertEquals("123^Adams^^^^^^^&1.2.840.113619.6.197&ISO", slots.get(0).getValueList().get(0));
        
        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION, slots.get(1).getName());
        assertEquals(Arrays.asList("inst1", "inst2"), slots.get(1).getValueList());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_ROLE, slots.get(2).getName());
        assertEquals(Arrays.asList("role1", "role2"), slots.get(2).getValueList());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_SPECIALTY, slots.get(3).getName());
        assertEquals(Arrays.asList("spec1", "spec2"), slots.get(3).getValueList());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_TELECOM, slots.get(4).getName());
        assertEquals(Arrays.asList("^^Floppynet^5.25 in", "^^Fidonet^2:465/46.40"), slots.get(4).getValueList());
    }
    
    @Test
    public void testToEbXMLWithNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }
    
    @Test
    public void testToEbXMLWithEmptyAuthor() {
        EbXMLClassification ebXML = transformer.toEbXML(new Author(), objectLibrary);
        assertNotNull(ebXML);
        assertNull(ebXML.getClassificationScheme());        
        assertEquals("", ebXML.getNodeRepresentation());
        
        assertEquals(0, ebXML.getSlots().size());
    }
    
    
    
    @Test
    public void testFromEbXML() {
        EbXMLClassification classification = transformer.toEbXML(author, objectLibrary);
        assertEquals(author, transformer.fromEbXML(classification));
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLClassification ebXML = transformer.toEbXML(new Author(), objectLibrary);
        assertEquals(new Author(), transformer.fromEbXML(ebXML));
    }
}
