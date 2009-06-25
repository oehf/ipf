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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents an organization.
 * <p>
 * This class contains members from the HL7v2 XON data type.
 * @author Jens Riemschneider
 */
public class Organization {
    private String organizationName;                // XON.1
    private String idNumber;                        // XON.3
    private AssigningAuthority assigningAuthority;  // XON.4
    
    public Organization() {}
    
    public Organization(String organizationName, String idNumber, AssigningAuthority assigningAuthority) {
        this.organizationName = organizationName;
        this.idNumber = idNumber;
        this.assigningAuthority = assigningAuthority;
    }

    public AssigningAuthority getAssigningAuthority() {
        return assigningAuthority;
    }
    
    public void setAssigningAuthority(AssigningAuthority assigningAuthority) {
        this.assigningAuthority = assigningAuthority;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((assigningAuthority == null) ? 0 : assigningAuthority.hashCode());
        result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
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
        if (assigningAuthority == null) {
            if (other.assigningAuthority != null)
                return false;
        } else if (!assigningAuthority.equals(other.assigningAuthority))
            return false;
        if (idNumber == null) {
            if (other.idNumber != null)
                return false;
        } else if (!idNumber.equals(other.idNumber))
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
}
