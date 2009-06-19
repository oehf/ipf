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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

/**
 * Represents the address of a patient.
 * <p>
 * This class contains a subset of the fields from the HL7v2.5 XAD data type.
 * @author Jens Riemschneider
 */
public class Address {
    private String streetAddress;               // XAD.1  
    private String otherDesignation;            // XAD.2  
    private String city;                        // XAD.3  
    private String stateOrProvince;             // XAD.4  
    private String zipOrPostalCode;             // XAD.5  
    private String country;                     // XAD.6  
    private String countyParishCode;            // XAD.9  
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getOtherDesignation() {
        return otherDesignation;
    }
    
    public void setOtherDesignation(String otherDesignation) {
        this.otherDesignation = otherDesignation;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getStateOrProvince() {
        return stateOrProvince;
    }
    
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }
    
    public String getZipOrPostalCode() {
        return zipOrPostalCode;
    }
    
    public void setZipOrPostalCode(String zipOrPostalCode) {
        this.zipOrPostalCode = zipOrPostalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCountyParishCode() {
        return countyParishCode;
    }

    public void setCountyParishCode(String countyParishCode) {
        this.countyParishCode = countyParishCode;
    }
}
