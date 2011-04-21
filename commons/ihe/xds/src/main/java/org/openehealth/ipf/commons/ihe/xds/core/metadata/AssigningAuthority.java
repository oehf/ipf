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

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.*;

/**
 * Represents an authority that assigns IDs.
 * <p>
 * This class is based on the HL7 HD data type.
 * <p>
 * Note that most assigning authorities used in XSD only allow the definition of the
 * universal ID. The ID type must be {@code ISO} and the namespace ID has to be empty.
 * The constructor {@link #AssigningAuthority(String)} can be used to create such
 * authorities.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are 
 * removed from the HL7 string.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssigningAuthority")
public class AssigningAuthority implements Serializable {
    private static final long serialVersionUID = 5350057820250191032L;
    
    @XmlAttribute
    private String namespaceId;     // HD.1
    @XmlValue
    private String universalId;     // HD.2
    @XmlAttribute
    private String universalIdType; // HD.3

    /**
     * Constructs an assigning authority.
     */
    public AssigningAuthority() {}

    /**
     * Constructs an assigning authority.
     * @param namespaceId
     *          the namespace ID (HD.1).
     * @param universalId
     *          the universal ID (HD.2).
     * @param universalIdType
     *          the type of the universal ID (HD.3).
     */
    public AssigningAuthority(String namespaceId, String universalId, String universalIdType) {
        this.namespaceId = namespaceId;
        this.universalId = universalId;
        this.universalIdType = universalIdType;
    }

    /**
     * Constructs an assigning authority that complies with the rules of the XDS profile.
     * @param universalId
     *          the universal ID (HD.2).
     */
    public AssigningAuthority(String universalId) {
        this.universalId = universalId;
        universalIdType = "ISO";
    }

    /**
     * @return the namespace ID (HD.1).
     */
    public String getNamespaceId() {
        return namespaceId;
    }
    
    /**
     * @param namespaceId
     *          the namespace ID (HD.1).
     */
    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    /**
     * @return the universal ID (HD.2).
     */
    public String getUniversalId() {
        return universalId;
    }
    
    /**
     * @param universalId
     *          the universal ID (HD.2).
     */
    public void setUniversalId(String universalId) {
        this.universalId = universalId;
    }
    
    /**
     * @return the universal type ID (HD.3).
     */
    public String getUniversalIdType() {
        return universalIdType;
    }

    /**
     * @param universalIdType
     *          the universal type ID (HD.3).
     */
    public void setUniversalIdType(String universalIdType) {
        this.universalIdType = universalIdType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((namespaceId == null) ? 0 : namespaceId.hashCode());
        result = prime * result + ((universalId == null) ? 0 : universalId.hashCode());
        result = prime * result + ((universalIdType == null) ? 0 : universalIdType.hashCode());
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
        AssigningAuthority other = (AssigningAuthority) obj;
        if (namespaceId == null) {
            if (other.namespaceId != null)
                return false;
        } else if (!namespaceId.equals(other.namespaceId))
            return false;
        if (universalId == null) {
            if (other.universalId != null)
                return false;
        } else if (!universalId.equals(other.universalId))
            return false;
        if (universalIdType == null) {
            if (other.universalIdType != null)
                return false;
        } else if (!universalIdType.equals(other.universalIdType))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
