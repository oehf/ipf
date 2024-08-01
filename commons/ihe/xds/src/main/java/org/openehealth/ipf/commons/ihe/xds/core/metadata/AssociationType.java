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

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Lists all possible types of associations between two documents.
 *
 * @author Jens Riemschneider
 */
@XmlType(name = "AssociationType")
@XmlEnum()
public enum AssociationType {
    /** An entry that is appended to another one. */
    @XmlEnumValue("APND") APPEND("urn:ihe:iti:2007:AssociationType:APND"),
    /** An entry that replaced another one. */
    @XmlEnumValue("RPLC") REPLACE("urn:ihe:iti:2007:AssociationType:RPLC"),
    /** An entry that transforms another one. */
    @XmlEnumValue("XFRM") TRANSFORM("urn:ihe:iti:2007:AssociationType:XFRM"),
    /** An entry that transforms and replaces another one. */
    @XmlEnumValue("XFRM_RPLC") TRANSFORM_AND_REPLACE("urn:ihe:iti:2007:AssociationType:XFRM_RPLC"),
    /** An entry that is a member of another one. */
    @XmlEnumValue("HasMember") HAS_MEMBER("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember"),
    /** An entry that represents a signature of another one. */
    @XmlEnumValue("signs") SIGNS("urn:ihe:iti:2007:AssociationType:signs"),
    /** An entry that represents a link to the On-Demand DocumentEntry. */
    @XmlEnumValue("IsSnapshotOf") IS_SNAPSHOT_OF("urn:ihe:iti:2010:AssociationType:IsSnapshotOf"),
    /** An entry that represents an association for update availability status trigger. */
    @XmlEnumValue("UpdateAvailabilityStatus") UPDATE_AVAILABILITY_STATUS("urn:ihe:iti:2010:AssociationType:UpdateAvailabilityStatus"),
    /** An entry that represents an association for submit association trigger. */
    @XmlEnumValue("SubmitAssociation") SUBMIT_ASSOCIATION("urn:ihe:iti:2010:AssociationType:SubmitAssociation"),
    /** An entry that represents an association which is used as a flag to avoid the versioning of an updated document. */
    @XmlEnumValue("NonVersioningUpdate") NON_VERSIONING_UPDATE("urn:elga-bes:AssociationType:NonVersioningUpdate");

    @Getter
    private final String opcode30;

    AssociationType(String opcode30) {
        this.opcode30 = opcode30;
    }

    /**
     * <code>null</code>-safe version of {@link #getOpcode30()}.
     * @param type
     *          the type for which to get the opcode. Can be <code>null</code>.
     * @return the opcode or <code>null</code> if type was <code>null</code>.
     */
    public static String getOpcode30(AssociationType type) {
        return type != null ? type.getOpcode30() : null;
    }

    /**
     * Returns the association type that is represented by the given opcode.
     * <p>
     * This method looks up the opcode via the ebXML 3.0 representations.
     * @param opcode
     *          the string representation. Can be <code>null</code>.
     * @return the association type or <code>null</code> if the opcode was <code>null</code>.
     */
    public static AssociationType valueOfOpcode30(String opcode) {
        if (opcode == null) {
            return null;
        }

        for (var type : AssociationType.values()) {
            if (opcode.equals(type.getOpcode30())) {
                return type;
            }
        }

        throw new XDSMetaDataException(ValidationMessage.INVALID_ASSOCIATION_TYPE);
    }

    /**
     * @return <code>true</code> if the association contains a replacement.
     */
    public boolean isReplace() {
        return this == REPLACE || this == TRANSFORM_AND_REPLACE;
    }
}
