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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Organization;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Recipient;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.RecipientTransformer;

/**
 * Tests for {@link RecipientTransformer}.
 * @author Jens Riemschneider
 */
public class RecipientTransformerTest {
    private RecipientTransformer transformer;
    private Recipient recipient;
    
    @Before
    public void setUp() {
        transformer = new RecipientTransformer();

        AssigningAuthority assigningAuthority1 = new AssigningAuthority("namespace1", "uni1", "uniType1");        
        Organization organization = new Organization("orgName", "orgId", assigningAuthority1);
        
        AssigningAuthority assigningAuthority2 = new AssigningAuthority("namespace2", "uni2", "uniType2");        
        Identifiable id = new Identifiable("personId", assigningAuthority2);        
        Name name = new Name("familyName", "givenName", "second", "suffix", "prefix");
        Person person = new Person(id, name);

        recipient = new Recipient();
        recipient.setOrganization(organization);
        recipient.setPerson(person);
    }
    
    @Test
    public void testToEbXML() {
        String ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);
        
        assertEquals("orgName^^orgId^namespace1&uni1&uniType1|personId^familyName^givenName^second^suffix^prefix^^^namespace2&uni2&uniType2", 
                ebXML);
    }
    
    @Test
    public void testToEbXMLNoPerson() {
        recipient.setPerson(null);
        String ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);
        
        assertEquals("orgName^^orgId^namespace1&uni1&uniType1", ebXML);
    }
    
    @Test
    public void testToEbXMLNoOrganization() {
        recipient.setOrganization(null);
        String ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);
        
        assertEquals("|personId^familyName^givenName^second^suffix^prefix^^^namespace2&uni2&uniType2", 
                ebXML);
    }
    
    @Test
    public void testToEbXMLEmpty() {
        assertNull(transformer.toEbXML(new Recipient()));
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testFromEbXML() {
        assertEquals(recipient, 
                transformer.fromEbXML("orgName^^orgId^namespace1&uni1&uniType1|personId^familyName^givenName^second^suffix^prefix^^^namespace2&uni2&uniType2"));
    }
    
    @Test
    public void testFromEbXMLNoPerson() {
        recipient.setPerson(null);
        assertEquals(recipient, 
                transformer.fromEbXML("orgName^^orgId^namespace1&uni1&uniType1"));
    }
    
    @Test
    public void testFromEbXMLNoOrganization() {
        recipient.setOrganization(null);
        assertEquals(recipient, 
                transformer.fromEbXML("|personId^familyName^givenName^second^suffix^prefix^^^namespace2&uni2&uniType2"));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        assertNull(transformer.fromEbXML(""));
    }

    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
}
