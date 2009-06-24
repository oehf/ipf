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
 * Represents an ID.
 * <p>
 * This class is derived from an HL7v2 CX data type. The XDS profile
 * limits the data type to two of its components (CX.1 and CX.4).
 * @author Jens Riemschneider
 */
public class Identifiable {
    private String id;                                // CX.1
    private AssigningAuthority assigningAuthority;    // CX.4

    public Identifiable() {}
    
    public Identifiable(String id, AssigningAuthority assigningAuthority) {
        this.id = id;
        this.assigningAuthority = assigningAuthority;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public AssigningAuthority getAssigningAuthority() {
        return assigningAuthority;
    }
    
    public void setAssigningAuthority(AssigningAuthority assigningAuthority) {
        this.assigningAuthority = assigningAuthority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((assigningAuthority == null) ? 0 : assigningAuthority.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Identifiable other = (Identifiable) obj;
        if (assigningAuthority == null) {
            if (other.assigningAuthority != null)
                return false;
        } else if (!assigningAuthority.equals(other.assigningAuthority))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
