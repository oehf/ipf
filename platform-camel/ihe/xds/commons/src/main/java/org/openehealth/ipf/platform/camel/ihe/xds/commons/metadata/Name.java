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
 * This class represents a name.
 * <p>
 * It is derived from the HL7v2 data types XPN and XCN. It only contains
 * naming related fields of these data types.
 * @author Jens Riemschneider
 */
public class Name {
    private String familyName;                  // XCN.2.1, XPN 1.1
    private String givenName;                   // XCN.3, XPN.2
    private String secondAndFurtherGivenNames;  // XCN.4, XPN.3
    private String suffix;                      // XCN.5, XPN.4
    private String prefix;                      // XCN.6, XPN.5
    
    public Name() {}
    
    public Name(String familyName, String givenName, String secondAndFurtherGivenNames, String suffix, String prefix) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.secondAndFurtherGivenNames = secondAndFurtherGivenNames;
        this.suffix = suffix;
        this.prefix = prefix;
    }

    public String getFamilyName() {
        return familyName;
    }
    
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    
    public String getGivenName() {
        return givenName;
    }
    
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    
    public String getSecondAndFurtherGivenNames() {
        return secondAndFurtherGivenNames;
    }

    public void setSecondAndFurtherGivenNames(String secondAndFurtherGivenNames) {
        this.secondAndFurtherGivenNames = secondAndFurtherGivenNames;
    }

    public String getSuffix() {
        return suffix;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
