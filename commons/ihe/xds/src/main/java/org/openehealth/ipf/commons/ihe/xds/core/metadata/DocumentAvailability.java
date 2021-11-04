/*
 * Copyright 2013 the original author or authors.
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

import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlType(name = "DocumentAvailability")
@XmlEnum()
public enum DocumentAvailability {

    /** Online indicates the Document in the Document Repository is available to be retrieved. */
    @XmlEnumValue("Offline") OFFLINE("Offline", "urn:ihe:iti:2010:DocumentAvailability:Offline"),
    /** Offline indicates the Document in the Document Repository is not available to be retrieved. */
    @XmlEnumValue("Online") ONLINE("Online", "urn:ihe:iti:2010:DocumentAvailability:Online");

    private final String opcode;
    private final String fullQualified;

    DocumentAvailability(String opcode, String fullQualified) {
        this.opcode = opcode;
        this.fullQualified = fullQualified;
    }


    public String getOpcode() {
        return opcode;
    }

    public String getFullQualified() {
        return fullQualified;
    }

    /**
     * Returns the document availability represented by the given opcode.
     * This method takes standard opcodes and fully qualified opcodes into account.
     * @param opcode
     *          the opcode to look up. Can be <code>null</code>.
     * @return the status. <code>null</code> if the opcode was <code>null</code>
     *          or could not be found.
     *          <br>See IHE_ITI_Suppl_XDS_Metadata_Update Table 4.2.3.2-1.
     */
    public static DocumentAvailability valueOfOpcode(String opcode) {
        if (opcode == null) {
            return null;
        }

        for (var documentAvailability : DocumentAvailability.values()) {
            if (opcode.equals(documentAvailability.getOpcode()) || opcode.equals(documentAvailability.getFullQualified())) {
                return documentAvailability;
            }
        }

        throw new XDSMetaDataException(ValidationMessage.INVALID_DOCUMENT_AVAILABILITY, opcode);
    }

    /**
     * Retrieves the representation of a given document availability.
     * <p>
     * This is a <code>null</code>-safe version of {@link #getOpcode()}.
     * @param documentAvailability
     *          the documentAvailability. Can be <code>null</code>.
     * @return the representation or <code>null</code> if the status was <code>null</code>.
     */
    public static String toOpcode(DocumentAvailability documentAvailability) {
        return documentAvailability != null ? documentAvailability.getOpcode() : null;
    }

    /**
     * Retrieves the fully qualified representation of a document availability.
     * <p>
     * This is a <code>null</code>-safe version of {@link #getFullQualified()}.
     * @param status
     *          the status. Can be <code>null</code>.
     * @return the representation or <code>null</code> if the status was <code>null</code>.
     */
    public static String toFullQualifiedOpcode(DocumentAvailability status) {
        return status != null ? status.getFullQualified() : null;
    }
}
