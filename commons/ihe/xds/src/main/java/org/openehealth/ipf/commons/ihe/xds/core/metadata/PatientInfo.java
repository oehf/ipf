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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

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
public class PatientInfo implements Serializable {
    private static final long serialVersionUID = 7202574584233259959L;

    @XmlElement(name = "id")
    private final List<Identifiable> ids = new ArrayList<Identifiable>();   // PID-3
    private Name name;                                                      // PID-5
    @XmlElement(name = "birthTime")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(value = IHEDateAdapter.class)
    private String dateOfBirth;                                             // PID-7
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
            BeanUtils.copyProperties(name, this.name);
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
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    /**
     * @param dateOfBirth
     *          the date of birth (PID-7).
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((ids == null) ? 0 : ids.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PatientInfo other = (PatientInfo) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (dateOfBirth == null) {
            if (other.dateOfBirth != null)
                return false;
        } else if (!dateOfBirth.equals(other.dateOfBirth))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (ids == null) {
            if (other.ids != null)
                return false;
        } else if (!ids.equals(other.ids))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
