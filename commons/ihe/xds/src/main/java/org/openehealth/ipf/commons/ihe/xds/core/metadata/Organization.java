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

import ca.uhn.hl7v2.model.v25.datatype.XON;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * Represents an organization.
 * <p>
 * This class contains members from the HL7v2.5 XON data type.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are 
 * removed from the HL7 string.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "Organization", propOrder = {"idNumber", "assigningAuthority", "organizationName"})
public class Organization extends Hl7v2Based<XON> {
    private static final long serialVersionUID = 8283797476558181158L;

    /**
     * Constructs the organization.
     */
    public Organization() {
        super(new XON(MESSAGE));
    }

    /**
     * Constructs the organization.
     * @param xon
     *          parsed HL7 v2 element.
     */
    public Organization(XON xon) {
        super(xon);
    }

    /**
     * Constructs the organization.
     * @param organizationName
     *          the name of the organization (XON.1).
     */
    public Organization(String organizationName) {
        this();
        setOrganizationName(organizationName);
    }

    /**
     * Constructs the organization.
     * @param organizationName
     *          the name of the organization (XON.1).
     * @param idNumber
     *          the id of the organization (XON.10).
     * @param assigningAuthority
     *          the assigning authority (XON.6).
     */
    public Organization(String organizationName, String idNumber, AssigningAuthority assigningAuthority) {
        this();
        setOrganizationName(organizationName);
        setIdNumber(idNumber);
        setAssigningAuthority(assigningAuthority);
    }

    /**
     * @return the assigning authority (XON.6).
     */
    public AssigningAuthority getAssigningAuthority() {
        var authority = new AssigningAuthority(getHapiObject().getXon6_AssigningAuthority());
        return authority.isEmpty() ? null : authority;
    }
    
    /**
     * @param assigningAuthority
     *          the assigning authority (XON.6).
     */
    public void setAssigningAuthority(AssigningAuthority assigningAuthority) {
        setAssigningAuthority(assigningAuthority, getHapiObject().getXon6_AssigningAuthority());
    }

    /**
     * @return the name of the organization (XON.1).
     */
    @XmlElement(name = "name")
    public String getOrganizationName() {
        return getHapiObject().getXon1_OrganizationName().getValue();
    }

    /**
     * @param organizationName
     *          the name of the organization (XON.1).
     */
    public void setOrganizationName(String organizationName) {
        setValue(getHapiObject().getXon1_OrganizationName(), organizationName);
    }

    /**
     * @return the id of the organization (XON.10).
     */
    public String getIdNumber() {
        return getHapiObject().getXon10_OrganizationIdentifier().getValue();
    }

    /**
     * @param idNumber
     *          the id of the organization (XON.10).
     */
    public void setIdNumber(String idNumber) {
        setValue(getHapiObject().getXon10_OrganizationIdentifier(), idNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Organization) o;
        return Objects.equals(getAssigningAuthority(), that.getAssigningAuthority()) &&
                Objects.equals(getOrganizationName(), that.getOrganizationName()) &&
                Objects.equals(getIdNumber(), that.getIdNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssigningAuthority(), getOrganizationName(), getIdNumber());
    }

    @Override
    public String toString() {
        return "Organization(" +
                "assigningAuthority=" + getAssigningAuthority() +
                ", organizationName=" + getOrganizationName() +
                ", idNumber=" + getIdNumber() +
                ')';
    }
}
