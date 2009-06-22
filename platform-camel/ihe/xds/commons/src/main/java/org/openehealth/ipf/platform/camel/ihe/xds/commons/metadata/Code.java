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
 * Represents a code.
 * 
 * @author Jens Riemschneider
 */
public class Code {
    private String code;
    private LocalizedString displayName;
    private String schemeName;
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public LocalizedString getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(LocalizedString displayName) {
        this.displayName = displayName;
    }
    
    public String getSchemeName() {
        return schemeName;
    }
    
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }
}
