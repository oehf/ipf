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
 * Lists all possible types of associations between two documents.
 * 
 * @author Jens Riemschneider
 */
public enum AssociationType {
    APPEND("APND"),
    REPLACE("RPLC"),
    TRANSFORM("XFRM"),
    TRANSFORM_AND_REPLACE("XFRM_RPLC"),
    HAS_MEMBER(null),
    SIGNS("signs");

    private final String representation;
    
    private AssociationType(String representation) {
        this.representation = representation;
    }

    /**
     * @return a string representation.
     */
    public String getRepresentation() {
        return representation;
    }
    
    /**
     * Returns the association type that is represented by the given string.
     * @param representation
     *          the string representation.
     * @return the association type.
     */
    public static AssociationType valueOfRepresentation(String representation) {
        if (representation == null) {
            return null;
        }
        
        for (AssociationType type : AssociationType.values()) {
            if (representation.equals(type.getRepresentation())) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Unsupported Association Type representation: " + representation);
    }
}
