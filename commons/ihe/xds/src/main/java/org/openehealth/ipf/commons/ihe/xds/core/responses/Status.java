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
package org.openehealth.ipf.commons.ihe.xds.core.responses;

import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
/**
 * Status information according to the XDS specification.
 * @author Jens Riemschneider
 */
@XmlType(name = "Status")
@XmlEnum(String.class)
public enum Status {
    /** The request execution failed. */
    @XmlEnumValue("Failure") FAILURE("Failure", "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"),
    /** The request execution succeeded. */
    @XmlEnumValue("Success") SUCCESS("Success", "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"),
    /** The request execution partially succeeded. */
    @XmlEnumValue("PartialSuccess") PARTIAL_SUCCESS("PartialSuccess", "urn:ihe:iti:2007:ResponseStatusType:PartialSuccess");
    
    private final String opcode21;
    private final String opcode30;
    
    private Status(String opcode21, String opcode30) {
        this.opcode21 = opcode21;
        this.opcode30 = opcode30;
    }

    /**
     * @return a string representation in ebXML 2.1.
     */
    public String getOpcode21() {
        return opcode21;
    }

    /**
     * @return a string representation in ebXML 3.1.
     */
    public String getOpcode30() {
        return opcode30;
    }
    
    /**
     * <code>null</code>-safe version of {@link #getOpcode21()}.
     * @param status
     *          the type for which to get the opcode. Can be <code>null</code>.
     * @return the opcode or <code>null</code> if type was <code>null</code>.
     */
    public static String getOpcode21(Status status) {
        return status != null ? status.getOpcode21() : null;
    }

    /**
     * <code>null</code>-safe version of {@link #getOpcode30()}.
     * @param status
     *          the type for which to get the opcode. Can be <code>null</code>.
     * @return the opcode or <code>null</code> if type was <code>null</code>.
     */
    public static String getOpcode30(Status status) {
        return status != null ? status.getOpcode30() : null;
    }
    
    /**
     * Returns the status that is represented by the given opcode.
     * <p>
     * This method looks up the opcode via the ebXML 2.1 and 3.0 representations.
     * @param opcode
     *          the string representation. Can be <code>null</code>.
     * @return the status or <code>null</code> if the opcode was <code>null</code>.
     */
    public static Status valueOfOpcode(String opcode) {
        if (opcode == null) {
            return null;
        }
        
        for (Status status : values()) {
            if (opcode.equals(status.getOpcode21()) || opcode.equals(status.getOpcode30())) {
                return status;
            }
        }
        
        throw new XDSMetaDataException(ValidationMessage.INVALID_STATUS_IN_RESPONSE);
    }
}
 
