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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
@XmlType(name = "Name", propOrder = {"prefix", "givenName", "secondAndFurtherGivenNames",
        "familyName", "suffix", "degree"})
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
public class Name implements Serializable {
    private static final long serialVersionUID = -3455779057944896901L;

    @XmlElement(name = "family")
    @Getter @Setter private String familyName;                  // XCN.2.1, XPN.1.1
    @XmlElement(name = "given")
    @Getter @Setter private String givenName;                   // XCN.3, XPN.2
    @XmlElement(name = "secondAndFurtherGiven")
    @Getter @Setter private String secondAndFurtherGivenNames;  // XCN.4, XPN.3
    @Getter @Setter private String suffix;                      // XCN.5, XPN.4
    @Getter @Setter private String prefix;                      // XCN.6, XPN.5
    @Getter @Setter private String degree;                      // XCN.7, XPN.6

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
     * @param degree
     *          academical degree (XCN.7/XPN.6).
     */
    public Name(String familyName, String givenName, String secondAndFurtherGivenNames, String suffix, String prefix, String degree) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.secondAndFurtherGivenNames = secondAndFurtherGivenNames;
        this.suffix = suffix;
        this.prefix = prefix;
        this.degree = degree;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
