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

import javax.xml.bind.annotation.*;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class represents a name.
 * <p>
 * It is derived from the HL7v2 data types XPN and XCN. It only contains
 * naming related fields of these data types.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are 
 * removed from the HL7 string.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Name", propOrder = {"prefix", "givenName", "secondAndFurtherGivenNames", "familyName", "suffix"})
public class Name implements Serializable {
    private static final long serialVersionUID = -3455779057944896901L;

    @XmlElement(name = "family")
    private String familyName;                  // XCN.2.1, XPN.1.1
    @XmlElement(name = "given")
    private String givenName;                   // XCN.3, XPN.2
    @XmlElement(name = "secondAndFurtherGiven")
    private String secondAndFurtherGivenNames;  // XCN.4, XPN.3
    private String suffix;                      // XCN.5, XPN.4
    private String prefix;                      // XCN.6, XPN.5

    /**
     * Constructs a name.
     */
    public Name() {}
    
    /**
     * Constructs a name.
     * @param familyName
     *          the family name (XCN.2.1/XPN.1.1).
     */
    public Name(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Constructs a name.
     * @param familyName
     *          the family name (XCN.2.1/XPN.1.1).
     * @param givenName
     *          the given name (XCN.3/XPN.2).
     * @param secondAndFurtherGivenNames
     *          the second and further names (XCN.4/XPN.3).
     * @param suffix
     *          the suffix (XCN.5/XPN.4).
     * @param prefix
     *          the prefix (XCN.6/XPN.5).
     */
    public Name(String familyName, String givenName, String secondAndFurtherGivenNames, String suffix, String prefix) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.secondAndFurtherGivenNames = secondAndFurtherGivenNames;
        this.suffix = suffix;
        this.prefix = prefix;
    }

    /**
     * @return the family name (XCN.2.1/XPN.1.1).
     */
    public String getFamilyName() {
        return familyName;
    }
    
    /**
     * @param familyName
     *          the family name (XCN.2.1/XPN.1.1).
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    
    /**
     * @return the given name (XCN.3/XPN.2).
     */
    public String getGivenName() {
        return givenName;
    }
    
    /**
     * @param givenName
     *          the given name (XCN.3/XPN.2).
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    
    /**
     * @return the second and further names (XCN.4/XPN.3).
     */
    public String getSecondAndFurtherGivenNames() {
        return secondAndFurtherGivenNames;
    }

    /**
     * @param secondAndFurtherGivenNames
     *          the second and further names (XCN.4/XPN.3).
     */
    public void setSecondAndFurtherGivenNames(String secondAndFurtherGivenNames) {
        this.secondAndFurtherGivenNames = secondAndFurtherGivenNames;
    }

    /**
     * @return the suffix (XCN.5/XPN.4).
     */
    public String getSuffix() {
        return suffix;
    }
    
    /**
     * @param suffix
     *          the suffix (XCN.5/XPN.4).
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    /**
     * @return the prefix (XCN.6/XPN.5).
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *          the prefix (XCN.6/XPN.5).
     */
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
