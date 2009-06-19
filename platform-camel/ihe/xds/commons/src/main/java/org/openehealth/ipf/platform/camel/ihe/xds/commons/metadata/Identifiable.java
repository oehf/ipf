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
 * Represents an ID.
 * <p>
 * This class is derived from an HL7v2 CX data type. The XDS profile
 * limits the data type to two of its components (CX.1 and CX.4).
 * @author Jens Riemschneider
 */
public class Identifiable {
    private String id;                                // CX.1
    private AssigningAuthority assigningAuthority;    // CX.4
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public AssigningAuthority getAssigningAuthority() {
        return assigningAuthority;
    }
    
    public void setAssigningAuthority(AssigningAuthority assigningAuthority) {
        this.assigningAuthority = assigningAuthority;
    }
}
