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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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
        AssigningAuthority authority = new AssigningAuthority(getHapiObject().getXon6_AssigningAuthority());
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdNumber() == null) ? 0 : getIdNumber().hashCode());
        result = prime * result + ((getAssigningAuthority() == null) ? 0 : getAssigningAuthority().hashCode());
        result = prime * result + ((getOrganizationName() == null) ? 0 : getOrganizationName().hashCode());
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
        Organization other = (Organization) obj;
        if (getIdNumber() == null) {
            if (other.getIdNumber() != null)
                return false;
        } else if (!getIdNumber().equals(other.getIdNumber()))
            return false;
        if (getAssigningAuthority() == null) {
            if (other.getAssigningAuthority() != null)
                return false;
        } else if (!getAssigningAuthority().equals(other.getAssigningAuthority()))
            return false;
        if (getOrganizationName() == null) {
            if (other.getOrganizationName() != null)
                return false;
        } else if (!getOrganizationName().equals(other.getOrganizationName()))
            return false;
        return true;
    }
        
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getOrganizationName())
                .append("assigningAuthority", getAssigningAuthority())
                .append("idNumber", getIdNumber())
                .toString();
    }
}
