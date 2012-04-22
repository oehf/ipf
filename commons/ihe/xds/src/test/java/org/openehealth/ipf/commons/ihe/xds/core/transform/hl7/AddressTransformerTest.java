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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Address;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;

/**
 * Tests for transformation between HL7 v2 and {@link Address}.
 * @author Jens Riemschneider
 */
public class AddressTransformerTest {
    @Test
    public void testToHL7() {
        Address address = new Address();
        address.setStreetAddress("Laliluna Str. 2&3");
        address.setCity("Sonn^hofen");
        address.setCountry("ECU");
        address.setCountyParishCode("STRAHL|EMANN");
        address.setOtherDesignation("Licht&Lampen");
        address.setStateOrProvince("Lamp|ing");
        address.setZipOrPostalCode("123&WARM");
        assertEquals(
                "Laliluna Str. 2\\T\\3^Licht\\T\\Lampen^Sonn\\S\\hofen^Lamp\\F\\ing^123\\T\\WARM^ECU^^^STRAHL\\F\\EMANN",
                Hl7v2Based.render(address));
    }

    @Test
    public void testToHL7Empty() {
        assertNull(Hl7v2Based.render(new Address()));
    }

    @Test
    public void testToHL7WithNullParam() {
        assertNull(Hl7v2Based.render(null));
    }
    

    @Test
    public void testFromHL7() {
        Address address = Hl7v2Based.parse(
                "Laliluna Str. 2\\T\\3^Licht\\T\\Lampen^Sonn\\S\\hofen^Lamp\\F\\ing^123\\T\\WARM^ECU^^^STRAHL\\F\\EMANN",
                Address.class);
        assertEquals("Laliluna Str. 2&3", address.getStreetAddress());
        assertEquals("Sonn^hofen", address.getCity());
        assertEquals("ECU", address.getCountry());
        assertEquals("STRAHL|EMANN", address.getCountyParishCode());
        assertEquals("Licht&Lampen", address.getOtherDesignation());
        assertEquals("Lamp|ing", address.getStateOrProvince());
        assertEquals("123&WARM", address.getZipOrPostalCode());
    }

    @Test
    public void testFromHL7UsingSAD() {
        Address address = Hl7v2Based.parse(
                "Laliluna Str. 2\\T\\3&whatever^Licht\\T\\Lampen^Sonn\\S\\hofen^Lamp\\F\\ing^123\\T\\WARM^ECU^^^STRAHL\\F\\EMANN",
                Address.class);
        assertEquals("Laliluna Str. 2&3", address.getStreetAddress());
        assertEquals("Sonn^hofen", address.getCity());
        assertEquals("ECU", address.getCountry());
        assertEquals("STRAHL|EMANN", address.getCountyParishCode());
        assertEquals("Licht&Lampen", address.getOtherDesignation());
        assertEquals("Lamp|ing", address.getStateOrProvince());
        assertEquals("123&WARM", address.getZipOrPostalCode());
    }

    @Test
    public void testFromHL7Nothing() {
        assertNull(Hl7v2Based.parse("", Address.class));
    }
    
    @Test
    public void testFromHL7WithNullParam() {
        assertNull(Hl7v2Based.parse(null, Address.class));
    }    
}
