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

/**
 * Describes the availability of an entry.
 * 
 * @author Jens Riemschneider
 */
public enum AvailabilityStatus {
    APPROVED("Approved", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved"),
    DEPRECATED("Deprecated", "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated"),
    SUBMITTED("Submitted", "urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted");
    
    private final String opcode;
    private final String queryOpcode;

    public String getOpcode() {
        return opcode;
    }
    
    public String getQueryOpcode() {
        return queryOpcode;
    }

    private AvailabilityStatus(String opcode, String queryOpcode) {        
        this.opcode = opcode;
        this.queryOpcode = queryOpcode;
    }

    public static AvailabilityStatus valueOfOpcode(String opcode) {
        if (opcode == null) {
            return null;
        }
        
        for (AvailabilityStatus status : AvailabilityStatus.values()) {
            if (opcode.equals(status.getOpcode()) || opcode.equals(status.getQueryOpcode())) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("Unknown status opcode: " + opcode);
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
