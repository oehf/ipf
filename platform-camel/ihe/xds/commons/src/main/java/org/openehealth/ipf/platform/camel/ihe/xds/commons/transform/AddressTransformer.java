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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7Delimiter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Address;

/**
 * Transformation logic for a {@link Address}.
 * <p>
 * This class offers transformation between {@link Address} and an HL7v2.5
 * XAD string.
 * @author Jens Riemschneider
 */
public class AddressTransformer {
    /**
     * Creates an address instance via an HL7 XAD string.
     * @param hl7XAD
     *          the HL7 XAD string.
     * @return the created Address instance.
     */
    public Address fromHL7(String hl7XAD) {
        List<String> parts = HL7.parse(HL7Delimiter.COMPONENT, hl7XAD);
        List<String> sad = HL7.parse(HL7Delimiter.SUBCOMPONENT, HL7.get(parts, 1, false));

        String streetAddress = HL7.get(sad, 1, true);
        String otherDesignation = HL7.get(parts, 2, true);
        String city = HL7.get(parts, 3, true);
        String stateOrProvince = HL7.get(parts, 4, true);
        String zipOrPostalCode = HL7.get(parts, 5, true);
        String country = HL7.get(parts, 6, true);
        String countyParishCode = HL7.get(parts, 9, true);

        if (streetAddress == null && otherDesignation == null && city == null && 
                stateOrProvince == null && zipOrPostalCode == null && country == null && 
                countyParishCode == null) {
            return null;
        }
        
        Address address = new Address();
        address.setStreetAddress(streetAddress);
        address.setOtherDesignation(otherDesignation);
        address.setCity(city);
        address.setStateOrProvince(stateOrProvince);
        address.setZipOrPostalCode(zipOrPostalCode);
        address.setCountry(country);
        address.setCountyParishCode(countyParishCode);
        return address;
    }
    
    /**
     * Transforms an address instance into an HL7v2 XAD string.
     * @param address
     *          the address to transform.
     * @return the HL7 representation.
     */
    public String toHL7(Address address) {
        if (address == null) {
            return null;
        }
        
        return HL7.render(HL7Delimiter.COMPONENT,
                HL7.escape(address.getStreetAddress()), 
                HL7.escape(address.getOtherDesignation()), 
                HL7.escape(address.getCity()), 
                HL7.escape(address.getStateOrProvince()),
                HL7.escape(address.getZipOrPostalCode()), 
                HL7.escape(address.getCountry()),
                null,
                null,
                HL7.escape(address.getCountyParishCode()));
    }
}
