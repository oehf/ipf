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

import ca.uhn.hl7v2.model.v25.datatype.HD;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based.Holder;

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
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "AssigningAuthority")
public class AssigningAuthority extends Hl7v2Based<Holder<HD>> {
    private static final long serialVersionUID = 5350057820250191032L;

    public AssigningAuthority() {
        super(new Holder<HD>(new HD(MESSAGE)));
    }


    public AssigningAuthority(Holder<HD> hdHolder) {
        super(hdHolder);
    }


    public AssigningAuthority(HD hd) {
        super(new Holder<HD>(hd));
    }


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
        this();
        setNamespaceId(namespaceId);
        setUniversalId(universalId);
        setUniversalIdType(universalIdType);
    }

    /**
     * Constructs an assigning authority that complies with the rules of the XDS profile.
     * @param universalId
     *          the universal ID (HD.2).
     */
    public AssigningAuthority(String universalId) {
        this();
        setUniversalId(universalId);
        setUniversalIdType("ISO");
    }

    /**
     * @return the namespace ID (HD.1).
     */
    @XmlAttribute
    public String getNamespaceId() {
        return getHapiObject().getInternal().getHd1_NamespaceID().getValue();
    }

    /**
     * @param namespaceId
     *          the namespace ID (HD.1).
     */
    public void setNamespaceId(String namespaceId) {
        setValue(getHapiObject().getInternal().getHd1_NamespaceID(), namespaceId);
    }

    /**
     * @return the universal ID (HD.2).
     */
    @XmlAttribute
    public String getUniversalId() {
        return getHapiObject().getInternal().getHd2_UniversalID().getValue();
    }

    /**
     * @param universalId
     *          the universal ID (HD.2).
     */
    public void setUniversalId(String universalId) {
        setValue(getHapiObject().getInternal().getHd2_UniversalID(), universalId);
    }

    /**
     * @return the universal type ID (HD.3).
     */
    @XmlAttribute
    public String getUniversalIdType() {
        return getHapiObject().getInternal().getHd3_UniversalIDType().getValue();
    }

    /**
     * @param universalIdType
     *          the universal type ID (HD.3).
     */
    public void setUniversalIdType(String universalIdType) {
        setValue(getHapiObject().getInternal().getHd3_UniversalIDType(), universalIdType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNamespaceId() == null) ? 0 : getNamespaceId().hashCode());
        result = prime * result + ((getUniversalId() == null) ? 0 : getUniversalId().hashCode());
        result = prime * result + ((getUniversalIdType() == null) ? 0 : getUniversalIdType().hashCode());
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
        if (getNamespaceId() == null) {
            if (other.getNamespaceId() != null)
                return false;
        } else if (!getNamespaceId().equals(other.getNamespaceId()))
            return false;
        if (getUniversalId() == null) {
            if (other.getUniversalId() != null)
                return false;
        } else if (!getUniversalId().equals(other.getUniversalId()))
            return false;
        if (getUniversalIdType() == null) {
            if (other.getUniversalIdType() != null)
                return false;
        } else if (!getUniversalIdType().equals(other.getUniversalIdType()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("namespaceId", getNamespaceId())
                .append("universalId", getUniversalId())
                .append("universalIdType", getUniversalIdType())
                .toString();
    }
}
