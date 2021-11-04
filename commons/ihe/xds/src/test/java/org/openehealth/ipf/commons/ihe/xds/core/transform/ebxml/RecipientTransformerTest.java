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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link RecipientTransformer}.
 * @author Jens Riemschneider
 */
public class RecipientTransformerTest {
    private RecipientTransformer transformer;
    private Recipient recipient;
    
    @BeforeEach
    public void setUp() {
        transformer = new RecipientTransformer();

        var assigningAuthority1 = new AssigningAuthority("uni1", "uniType1");
        var organization = new Organization("orgName", "orgId", assigningAuthority1);

        var assigningAuthority2 = new AssigningAuthority("uni2", "uniType2");
        var id = new Identifiable("personId", assigningAuthority2);
        Name name = new XpnName("familyName", "givenName", "second", "suffix", "prefix", "degree");
        var person = new Person(id, name);
        var telecom = new Telecom(41L, 162L, 7773L, null);

        recipient = new Recipient();
        recipient.setOrganization(organization);
        recipient.setPerson(person);
        recipient.setTelecom(telecom);
    }
    
    @Test
    public void testToEbXML() {
        var ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);
        
        assertEquals("orgName^^^^^&uni1&uniType1^^^^orgId|personId^familyName^givenName^second^suffix^prefix^degree^^&uni2&uniType2|^PRN^PH^^41^162^7773",
                ebXML);
    }
    
    @Test
    public void testToEbXMLNoPerson() {
        recipient.setPerson(null);
        var ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);
        
        assertEquals("orgName^^^^^&uni1&uniType1^^^^orgId||^PRN^PH^^41^162^7773", ebXML);
    }
    
    @Test
    public void testToEbXMLNoOrganization() {
        recipient.setOrganization(null);
        var ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);
        
        assertEquals("|personId^familyName^givenName^second^suffix^prefix^degree^^&uni2&uniType2|^PRN^PH^^41^162^7773",
                ebXML);
    }
    
    @Test
    public void testToEbXMLNoTelecom() {
        recipient.setTelecom(null);
        var ebXML = transformer.toEbXML(recipient);
        assertNotNull(ebXML);

        assertEquals("orgName^^^^^&uni1&uniType1^^^^orgId|personId^familyName^givenName^second^suffix^prefix^degree^^&uni2&uniType2",
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
                transformer.fromEbXML("orgName^^^^^namespace1&uni1&uniType1^^^^orgId|personId^familyName^givenName^second^suffix^prefix^degree^^namespace2&uni2&uniType2|^PRN^PH^^41^162^7773"));
    }

    @Test
    public void testFromEbXMLNoPerson() {
        recipient.setPerson(null);
        assertEquals(recipient, 
                transformer.fromEbXML("orgName^^^^^namespace1&uni1&uniType1^^^^orgId||^PRN^PH^^41^162^7773"));
    }
    
    @Test
    public void testFromEbXMLNoOrganization() {
        recipient.setOrganization(null);
        assertEquals(recipient, 
                transformer.fromEbXML("|personId^familyName^givenName^second^suffix^prefix^degree^^namespace2&uni2&uniType2|^PRN^PH^^41^162^7773"));
    }
    
    @Test
    public void testFromEbXMLNoTelecom() {
        recipient.setTelecom(null);
        assertEquals(recipient,
                transformer.fromEbXML("orgName^^^^^namespace1&uni1&uniType1^^^^orgId|personId^familyName^givenName^second^suffix^prefix^degree^^namespace2&uni2&uniType2"));
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
