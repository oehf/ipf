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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
/**
 * Describes the availability of an entry.
 * 
 * @author Jens Riemschneider
 */
@XmlType(name = "AvailabilityStatus")
@XmlEnum(String.class)
public enum AvailabilityStatus {
    /** The entry is approved. */
    @XmlEnumValue("Approved") APPROVED("Approved", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved"),
    /** The entry is deprecated. */
    @XmlEnumValue("Deprecated") DEPRECATED("Deprecated", "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated"),
    /** The entry is submitted. */
    @XmlEnumValue("Submitted") SUBMITTED("Submitted", "urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted");
    
    private final String opcode;
    private final String queryOpcode;

    /**
     * @return the opcode used as a string representation in non-query transformations.
     */
    public String getOpcode() {
        return opcode;
    }
    
    /**
     * @return the opcode used as a string representation in transformations for query 
     *          requests and responses.
     */
    public String getQueryOpcode() {
        return queryOpcode;
    }

    private AvailabilityStatus(String opcode, String queryOpcode) {        
        this.opcode = opcode;
        this.queryOpcode = queryOpcode;
    }

    /**
     * Returns the availability status represented by the given opcode.
     * This method takes standard opcodes and query opcodes into account.
     * @param opcode
     *          the opcode to look up. Can be <code>null</code>.
     * @return the status. <code>null</code> if the opcode was <code>null</code>
     *          or could not be found.
     *          <br>See ITI TF v.8.0 Vol. 2a Section 3.18.4.1.2.3.6.
     */
    public static AvailabilityStatus valueOfOpcode(String opcode) {
        if (opcode == null) {
            return null;
        }
        
        for (AvailabilityStatus status : AvailabilityStatus.values()) {
            if (opcode.equals(status.getOpcode()) || opcode.equals(status.getQueryOpcode())) {
                return status;
            }
        }
        
        return null;
    }

    /**
     * Retrieves the representation of a given status.
     * <p>
     * This is a <code>null</code>-safe version of {@link #getOpcode()}.
     * @param status
     *          the status. Can be <code>null</code>.
     * @return the representation or <code>null</code> if the status was <code>null</code>.
     */
    public static String toOpcode(AvailabilityStatus status) {
        return status != null ? status.getOpcode() : null;
    }

    /**
     * Retrieves the query representation of a given status.
     * <p>
     * This is a <code>null</code>-safe version of {@link #getQueryOpcode()}.
     * @param status
     *          the status. Can be <code>null</code>.
     * @return the representation or <code>null</code> if the status was <code>null</code>.
     */
    public static String toQueryOpcode(AvailabilityStatus status) {
        return status != null ? status.getQueryOpcode() : null;
    }
}
