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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Address;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.AddressTransformer;

/**
 * Tests for {@link AddressTransformer}.
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
                new AddressTransformer().toHL7(address));
    }

    @Test
    public void testToHL7Empty() {
        Address address = new Address();
        assertNull(new AddressTransformer().toHL7(address));
    }

    @Test
    public void testToHL7WithNullParam() {
        assertNull(new AddressTransformer().toHL7(null));
    }
    

    @Test
    public void testFromHL7() {
        Address address = new AddressTransformer().fromHL7(
                "Laliluna Str. 2\\T\\3^Licht\\T\\Lampen^Sonn\\S\\hofen^Lamp\\F\\ing^123\\T\\WARM^ECU^^^STRAHL\\F\\EMANN");
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
        Address address = new AddressTransformer().fromHL7(
                "Laliluna Str. 2\\T\\3&whatever^Licht\\T\\Lampen^Sonn\\S\\hofen^Lamp\\F\\ing^123\\T\\WARM^ECU^^^STRAHL\\F\\EMANN");
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
        assertNull(new AddressTransformer().fromHL7(""));
    }
    
    @Test
    public void testFromHL7WithNullParam() {
        assertNull(new AddressTransformer().fromHL7(null));
    }    
}
