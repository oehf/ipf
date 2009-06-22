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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;

/**
 * Tests for {@link AuthorTransformer}.
 * @author Jens Riemschneider
 */
public class AuthorTransformerTest {
    private AuthorTransformer transformer;
    private ExtrinsicObjectType docEntry;
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
        
        docEntry = new ExtrinsicObjectType();
    }
    
    @Test
    public void testToEbXML21() {
        ClassificationType ebXML = transformer.toEbXML21(author, docEntry);        
        assertNotNull(ebXML);
        assertSame(Ebrs21.getObjFromLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME), 
                ebXML.getClassificationScheme());        
        assertSame(docEntry, ebXML.getClassifiedObject());
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
    public void testToEbXML21WithNull() {
        assertNull(transformer.toEbXML21(null, new ExtrinsicObjectType()));
    }
    
    @Test
    public void testToEbXML21WithEmptyAuthor() {
        ExtrinsicObjectType docEntry = new ExtrinsicObjectType();
        ClassificationType ebXML = transformer.toEbXML21(new Author(), docEntry);
        assertNotNull(ebXML);
        assertSame(Ebrs21.getObjFromLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME), 
                ebXML.getClassificationScheme());        
        assertSame(docEntry, ebXML.getClassifiedObject());
        assertEquals("", ebXML.getNodeRepresentation());
        
        assertEquals(0, ebXML.getSlot().size());
    }
    
    
    @Test
    public void testFromEbXML21() {
        ClassificationType classification = transformer.toEbXML21(author, docEntry);
        Author result = transformer.fromEbXML21(classification);
        assertNotNull(result);
        assertEquals("Adams", author.getAuthorPerson().getName().getFamilyName());
        assertEquals("123", author.getAuthorPerson().getId().getId());
        assertEquals("1.2.840.113619.6.197", author.getAuthorPerson().getId().getAssigningAuthority().getUniversalId());
        assertEquals(Vocabulary.UNIVERSAL_ID_TYPE_OID, author.getAuthorPerson().getId().getAssigningAuthority().getUniversalIdType());
        
        assertEquals(Arrays.asList("inst1", "inst2"), author.getAuthorInstitution());
        assertEquals(Arrays.asList("role1", "role2"), author.getAuthorRole());
        assertEquals(Arrays.asList("spec1", "spec2"), author.getAuthorSpecialty());
    }
}
