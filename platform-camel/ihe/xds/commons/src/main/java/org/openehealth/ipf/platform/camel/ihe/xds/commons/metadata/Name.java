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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
        result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime
                * result
                + ((secondAndFurtherGivenNames == null) ? 0 : secondAndFurtherGivenNames.hashCode());
        result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Name other = (Name) obj;
        if (familyName == null) {
            if (other.familyName != null)
                return false;
        } else if (!familyName.equals(other.familyName))
            return false;
        if (givenName == null) {
            if (other.givenName != null)
                return false;
        } else if (!givenName.equals(other.givenName))
            return false;
        if (prefix == null) {
            if (other.prefix != null)
                return false;
        } else if (!prefix.equals(other.prefix))
            return false;
        if (secondAndFurtherGivenNames == null) {
            if (other.secondAndFurtherGivenNames != null)
                return false;
        } else if (!secondAndFurtherGivenNames.equals(other.secondAndFurtherGivenNames))
            return false;
        if (suffix == null) {
            if (other.suffix != null)
                return false;
        } else if (!suffix.equals(other.suffix))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
