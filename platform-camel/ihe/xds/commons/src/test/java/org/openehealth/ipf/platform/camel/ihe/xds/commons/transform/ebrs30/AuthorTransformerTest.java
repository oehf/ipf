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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.SlotType1;

/**
 * Tests for {@link AuthorTransformer}.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class AuthorTransformerTest {
    private AuthorTransformer transformer;
    private Author author;
    
    @Before
    public void setUp() {
        transformer = new AuthorTransformer();

        Name name = new Name();
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
        
        author.getAuthorInstitution().add("inst1");
        author.getAuthorInstitution().add("inst2");
        
        author.getAuthorRole().add("role1");
        author.getAuthorRole().add("role2");
        
        author.getAuthorSpecialty().add("spec1");
        author.getAuthorSpecialty().add("spec2");
    }
    
    @Test
    public void testToEbXML30() {
        ClassificationType ebXML = transformer.toEbXML30(author);        
        assertNotNull(ebXML);
        assertSame(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME, 
                ebXML.getClassificationScheme());
        
        assertEquals("", ebXML.getNodeRepresentation());
        
        List<SlotType1> slots = ebXML.getSlot();
        assertEquals(4, slots.size());
        
        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_PERSON, slots.get(0).getName());
        assertEquals("123^Adams^^^^^^^&1.2.840.113619.6.197&ISO", slots.get(0).getValueList().getValue().get(0));
        
        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION, slots.get(1).getName());
        assertEquals(Arrays.asList("inst1", "inst2"), slots.get(1).getValueList().getValue());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_ROLE, slots.get(2).getName());
        assertEquals(Arrays.asList("role1", "role2"), slots.get(2).getValueList().getValue());

        assertEquals(Vocabulary.SLOT_NAME_AUTHOR_SPECIALTY, slots.get(3).getName());
        assertEquals(Arrays.asList("spec1", "spec2"), slots.get(3).getValueList().getValue());
    }
    
    @Test
    public void testToEbXML30WithNull() {
        assertNull(transformer.toEbXML30(null));
    }
    
    @Test
    public void testToEbXML30WithEmptyAuthor() {
        ClassificationType ebXML = transformer.toEbXML30(new Author());
        assertNotNull(ebXML);
        assertSame(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME,  
                ebXML.getClassificationScheme());        
        assertEquals("", ebXML.getNodeRepresentation());
        
        assertEquals(0, ebXML.getSlot().size());
    }
    
    @Test
    public void testFromEbXML30() {
        ClassificationType classification = transformer.toEbXML30(author);
        Author result = transformer.fromEbXML30(classification);
        assertNotNull(result);
        assertEquals("Adams", result.getAuthorPerson().getName().getFamilyName());
        assertEquals("123", result.getAuthorPerson().getId().getId());
        assertEquals("1.2.840.113619.6.197", result.getAuthorPerson().getId().getAssigningAuthority().getUniversalId());
        assertEquals(Vocabulary.UNIVERSAL_ID_TYPE_OID, result.getAuthorPerson().getId().getAssigningAuthority().getUniversalIdType());
        
        assertEquals(Arrays.asList("inst1", "inst2"), result.getAuthorInstitution());
        assertEquals(Arrays.asList("role1", "role2"), result.getAuthorRole());
        assertEquals(Arrays.asList("spec1", "spec2"), result.getAuthorSpecialty());
    }
    
    @Test
    public void testFromEbXML30Null() {
        assertNull(transformer.fromEbXML30(null));
    }
    
    @Test
    public void testFromEbXML30Empty() {
        ClassificationType ebXML = transformer.toEbXML30(new Author());
        Author result = transformer.fromEbXML30(ebXML);
        assertNotNull(result);
        assertNull(result.getAuthorPerson());
        assertEquals(0, result.getAuthorInstitution().size());
        assertEquals(0, result.getAuthorRole().size());
        assertEquals(0, result.getAuthorSpecialty().size());
    }
}
