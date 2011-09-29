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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an organization.
 * <p>
 * This class contains members from the HL7v2.5 XON data type.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are 
 * removed from the HL7 string.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Organization", propOrder = {"id", "organizationName"})
public class Organization implements Serializable {
    private static final long serialVersionUID = 8283797476558181158L;

    @XmlElement(name = "name")
    private String organizationName;                // XON.1
    private Identifiable id;

    /**
     * Constructs the organization.
     */
    public Organization() {}
    
    /**
     * Constructs the organization.
     * @param organizationName
     *          the name of the organization (XON.1).
     */
    public Organization(String organizationName) {
        this.organizationName = organizationName;
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
        this.organizationName = organizationName;
        this.id = new Identifiable(idNumber, assigningAuthority);
        normalizeEmptyId();
    }

    /**
     * @return the assigning authority (XON.6).
     */
    public AssigningAuthority getAssigningAuthority() {
        return id == null ? null : id.getAssigningAuthority();
    }
    
    /**
     * @param assigningAuthority
     *          the assigning authority (XON.6).
     */
    public void setAssigningAuthority(AssigningAuthority assigningAuthority) {
        lazilyCreateIdIfNecessary();
        id.setAssigningAuthority(assigningAuthority);
        normalizeEmptyId();
    }

    /**
     * @return the name of the organization (XON.1).
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName
     *          the name of the organization (XON.1).
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the id of the organization (XON.10).
     */
    public String getIdNumber() {
        return id == null ? null : id.getId();
    }

    /**
     * @param idNumber
     *          the id of the organization (XON.10).
     */
    public void setIdNumber(String idNumber) {
        lazilyCreateIdIfNecessary();
        id.setId(idNumber);
        normalizeEmptyId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((organizationName == null) ? 0 : organizationName.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (organizationName == null) {
            if (other.organizationName != null)
                return false;
        } else if (!organizationName.equals(other.organizationName))
            return false;
        return true;
    }
        
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    private void normalizeEmptyId() {
        if ( id.getId() == null && id.getAssigningAuthority() == null) {
            id = null;
        }
    }

    private void lazilyCreateIdIfNecessary()
    {
        if ( id == null ) {
            id = new Identifiable();
        }
    }
}
