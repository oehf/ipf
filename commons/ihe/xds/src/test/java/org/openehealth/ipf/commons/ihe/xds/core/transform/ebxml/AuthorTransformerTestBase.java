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

import ca.uhn.hl7v2.model.v25.datatype.XCN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link AuthorTransformer}.
 * @author Jens Riemschneider
 */
public abstract class AuthorTransformerTestBase implements FactoryCreator {
    private AuthorTransformer transformer;
    private EbXMLObjectLibrary objectLibrary;
    private Author author;
    
    @BeforeEach
    public final void baseSetUp() {
        var factory = createFactory();
        transformer = new AuthorTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        
        Name<XCN> name = new XcnName();
        name.setFamilyName("Adams");

        var assigningAuthority = new AssigningAuthority();
        assigningAuthority.setUniversalId("1.2.840.113619.6.197");
        assigningAuthority.setUniversalIdType(Vocabulary.UNIVERSAL_ID_TYPE_OID);

        var id = new Identifiable();
        id.setId("123");
        id.setAssigningAuthority(assigningAuthority);

        var authorPerson = new Person();
        authorPerson.setName(name);
        authorPerson.setId(id);

        author = new Author();
        author.setAuthorPerson(authorPerson);
        
        author.getAuthorInstitution().add(new Organization("inst1"));
        author.getAuthorInstitution().add(new Organization("inst2"));
        
        author.getAuthorRole().add(new Identifiable("role1", new AssigningAuthority("2.3.1", "ISO")));
        author.getAuthorRole().add(new Identifiable("role2"));
        
        author.getAuthorSpecialty().add(new Identifiable("spec1", new AssigningAuthority("2.3.3", "ISO")));
        author.getAuthorSpecialty().add(new Identifiable("spec2"));

        author.getAuthorTelecom().add(new Telecom(null, null, 7771L, null));
        author.getAuthorTelecom().add(new Telecom(null, null, 7772L, null));
    }
    
    @Test
    public void testToEbXML() {
        var ebXML = transformer.toEbXML(author, objectLibrary);
        assertNotNull(ebXML);
        assertNull(ebXML.getClassificationScheme());        
        assertEquals("", ebXML.getNodeRepresentation());

        var slots = ebXML.getSlots();
        assertEquals(5, slots.size());
        
        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_PERSON, slots.get(0).getName());
        assertEquals("123^Adams^^^^^^^&1.2.840.113619.6.197&ISO", slots.get(0).getValueList().get(0));
        
        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION, slots.get(1).getName());
        assertEquals(Arrays.asList("inst1", "inst2"), slots.get(1).getValueList());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_ROLE, slots.get(2).getName());
        assertEquals(Arrays.asList("role1^^^&2.3.1&ISO", "role2"), slots.get(2).getValueList());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_SPECIALTY, slots.get(3).getName());
        assertEquals(Arrays.asList("spec1^^^&2.3.3&ISO", "spec2"), slots.get(3).getValueList());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_TELECOM, slots.get(4).getName());
        assertEquals(Arrays.asList("^PRN^PH^^^^7771", "^PRN^PH^^^^7772"), slots.get(4).getValueList());
    }
    
    @Test
    public void testToEbXMLWithNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }
    
    @Test
    public void testToEbXMLWithEmptyAuthor() {
        var ebXML = transformer.toEbXML(new Author(), objectLibrary);
        assertNotNull(ebXML);
        assertNull(ebXML.getClassificationScheme());        
        assertEquals("", ebXML.getNodeRepresentation());
        
        assertEquals(0, ebXML.getSlots().size());
    }
    
    
    
    @Test
    public void testFromEbXML() {
        var classification = transformer.toEbXML(author, objectLibrary);
        assertEquals(author, transformer.fromEbXML(classification));
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        var ebXML = transformer.toEbXML(new Author(), objectLibrary);
        assertEquals(new Author(), transformer.fromEbXML(ebXML));
    }
}
