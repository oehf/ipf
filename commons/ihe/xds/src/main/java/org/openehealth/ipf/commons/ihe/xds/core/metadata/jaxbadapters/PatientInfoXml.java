/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Address;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientInfo")
public class PatientInfoXml {

    @XmlElement(name = "id")
    private List<Identifiable> ids;

    @XmlElement(name = "name")
    private List<Name> names;

    @XmlElement(name = "birthTime")
    private Timestamp dateOfBirth;

    @XmlElement(name = "gender")
    private String gender;

    @XmlElement(name = "address")
    private List<Address> addresses;

    @XmlJavaTypeAdapter(StringMapAdapter.class)
    @XmlElement(name = "other", type = StringMap.class)
    private Map<String, List<String>> other;

    public List<Identifiable> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return ids;
    }

    public void setIds(List<Identifiable> ids) {
        this.ids = ids;
    }

    public List<Name> getNames() {
        if (names == null) {
            names = new ArrayList<>();
        }
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Address> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, List<String>> getOther() {
        if (other == null) {
            other = new HashMap<>();
        }
        return other;
    }

    public void setOther(Map<String, List<String>> other) {
        this.other = other;
    }
}
