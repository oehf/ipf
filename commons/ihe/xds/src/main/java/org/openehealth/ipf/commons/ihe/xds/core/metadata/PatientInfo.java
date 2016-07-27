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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents additional info about a patient.
 * <p>
 * This class contains a subset of the HL7v2 PID segment. The XDS profile prohibits
 * the use of PID-2, PID-4, PID-12 and PID-19.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>. When 
 * transforming to HL7 this indicates that the values are empty. Trailing empty  
 * values are removed from the HL7 string.
 * <p>
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientInfo", propOrder = {"ids", "name", "gender", "dateOfBirth", "address"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class PatientInfo implements Serializable {
    private static final long serialVersionUID = 7202574584233259959L;

    @XmlElement(name = "id")
    private final List<Identifiable> ids = new ArrayList<>();               // PID-3
    private Name name;                                                      // PID-5
    @XmlElement(name = "birthTime")
    private Timestamp dateOfBirth;                                          // PID-7
    private String gender;                                                  // PID-8
    private Address address;                                                // PID-11
    
    /**
     * @return the name (PID-5).
     */
    public Name getName() {
        return name;
    }

    /**
     * @param name
     *          the name (PID-5).
     */
    public void setName(Name name) {
        if (name != null) {
            this.name = new XpnName();
            try {
                BeanUtils.copyProperties(this.name, name);
            } catch (Exception e) {
                throw new RuntimeException("Could not copy properties");
            }
        } else {
            this.name = null;
        }
    }

    /**
     * @return the IDs of the patient (PID-3).
     */
    public List<Identifiable> getIds() {
        return ids;
    }

    /**
     * @return the date of birth (PID-7).
     */
    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }
    
    /**
     * @param dateOfBirth
     *          the date of birth (PID-7).
     */
    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @param dateOfBirth
     *          the date of birth (PID-7).
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = Timestamp.fromHL7(dateOfBirth);
    }

    /**
     * @return the gender (PID-8).
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * @param gender
     *          the gender (PID-8).
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * @return the address (PID-11).
     */
    public Address getAddress() {
        return address;
    }
    
    /**
     * @param address
     *          the address (PID-11).
     */
    public void setAddress(Address address) {
        this.address = address;
    }

}
