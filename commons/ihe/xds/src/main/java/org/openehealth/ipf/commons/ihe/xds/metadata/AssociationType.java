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
package org.openehealth.ipf.commons.ihe.xds.metadata;

/**
 * Lists all possible types of associations between two documents.
 * 
 * @author Jens Riemschneider
 */
public enum AssociationType {
    APPEND("APND", "urn:ihe:iti:2007:AssociationType:APND"),
    REPLACE("RPLC", "urn:ihe:iti:2007:AssociationType:RPLC"),
    TRANSFORM("XFRM", "urn:ihe:iti:2007:AssociationType:XFRM"),
    TRANSFORM_AND_REPLACE("XFRM_RPLC", "urn:ihe:iti:2007:AssociationType:XFRM_RPLC"),
    HAS_MEMBER("HasMember", "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember"),
    SIGNS("signs", "urn:ihe:iti:2007:AssociationType:signs");

    private final String opcode21;
    private final String opcode30;
    
    private AssociationType(String opcode21, String opcode30) {
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
     * @return a string representation in ebXML 3.0.
     */
    public String getOpcode30() {
        return opcode30;
    }
    
    /**
     * <code>null</code>-safe version of {@link #getOpcode21()}.
     * @param type
     *          the type for which to get the opcode. Can be <code>null</code>.
     * @return the opcode or <code>null</code> if type was <code>null</code>.
     */
    public static String getOpcode21(AssociationType type) {
        return type != null ? type.getOpcode21() : null;
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
     * This method looks up the opcode via the ebXML 2.1 representations.
     * @param opcode
     *          the string representation. Can be <code>null</code>.
     * @return the association type or <code>null</code> if the opcode was <code>null</code>
     *          or not supported.
     */
    public static AssociationType valueOfOpcode21(String opcode) {
        if (opcode == null) {
            return null;
        }
        
        for (AssociationType type : AssociationType.values()) {
            if (opcode.equals(type.getOpcode21())) {
                return type;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the association type that is represented by the given opcode.
     * <p>
     * This method looks up the opcode via the ebXML 3.0 representations.
     * @param opcode
     *          the string representation. Can be <code>null</code>.
     * @return the association type or <code>null</code> if the opcode was <code>null</code>
     *          or not supported.
     */
    public static AssociationType valueOfOpcode30(String opcode) {
        if (opcode == null) {
            return null;
        }
        
        for (AssociationType type : AssociationType.values()) {
            if (opcode.equals(type.getOpcode30())) {
                return type;
            }
        }
        
        return null;
    }
    
    /**
     * @return <code>true</code> if the association contains a replacement.
     */
    public boolean isReplace() {
        return this == REPLACE || this == TRANSFORM_AND_REPLACE;
    }
}
