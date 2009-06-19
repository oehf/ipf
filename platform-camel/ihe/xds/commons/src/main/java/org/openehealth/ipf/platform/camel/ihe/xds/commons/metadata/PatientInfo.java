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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents additional info about a patient.
 * <p>
 * This class contains a subset of the HL7v2 PID segment. The XDS profile prohibits
 * the use of PID-2, PID-4, PID-12 and PID-19.
 * @author Jens Riemschneider
 */
public class PatientInfo {
    private final List<Identifiable> ids = new ArrayList<Identifiable>();   // PID-3
    private Name name;                                                      // PID-5
    private String dateOfBirth;                                             // PID-7
    private String gender;                                                  // PID-8
    private Address address;                                                // PID-11
    
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Identifiable> getIds() {
        return ids;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
}
