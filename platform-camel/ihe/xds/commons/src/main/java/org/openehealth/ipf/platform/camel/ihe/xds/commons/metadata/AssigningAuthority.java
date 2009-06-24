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
 * Represents an authority that assigns IDs.
 * <p>
 * This class is based on the HL7 HD data type.
 * @author Jens Riemschneider
 */
public class AssigningAuthority {
    private String namespaceId;     // HD.1
    private String universalId;     // HD.2
    private String universalIdType; // HD.3
    
    public AssigningAuthority() {}

    public AssigningAuthority(String namespaceId, String universalId, String universalIdType) {
        this.namespaceId = namespaceId;
        this.universalId = universalId;
        this.universalIdType = universalIdType;
    }

    public String getNamespaceId() {
        return namespaceId;
    }
    
    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    public String getUniversalId() {
        return universalId;
    }
    
    public void setUniversalId(String universalId) {
        this.universalId = universalId;
    }
    
    public String getUniversalIdType() {
        return universalIdType;
    }

    public void setUniversalIdType(String universalIdType) {
        this.universalIdType = universalIdType;
    }
}
