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

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serial;
import java.util.Objects;

/**
 * Assigning Authority for the CXi data type, allowing both the Namespace ID and the
 * combination of Universal ID and Universal ID Type.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are
 * removed from the HL7 string.
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@XmlAccessorType()
@XmlType(name = "CXiAssigningAuthority", propOrder = {"namespaceId"})
public class CXiAssigningAuthority extends AssigningAuthority {

    @Serial
    private static final long serialVersionUID = 264032769452660318L;

    public CXiAssigningAuthority() {
        super();
    }

    public CXiAssigningAuthority(Holder<HD> hdHolder) {
        super(hdHolder);
    }

    public CXiAssigningAuthority(HD hd) {
        super(hd);
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
    public CXiAssigningAuthority(String namespaceId, String universalId, String universalIdType) {
        super(universalId, universalIdType);
        setNamespaceId(namespaceId);
    }

    /**
     * Parses an HL7v2 string into a CXiAssigningAuthority object.
     *
     * @param hl7String HL7v2 representation of a CXiAssigningAuthority object.
     * @return parsed CXiAssigningAuthority object.
     */
    public static CXiAssigningAuthority parse(String hl7String) {
        return Hl7v2Based.parse(hl7String, CXiAssigningAuthority.class);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        var that = (CXiAssigningAuthority) o;

        return Objects.equals(getNamespaceId(), that.getNamespaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNamespaceId());
    }

    @Override
    public String toString() {
        return "CXiAssigningAuthority(" +
                "super=" + super.toString() +
                "namespaceId=" + getNamespaceId() +
                ')';
    }
}
