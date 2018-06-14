
/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthnContextComparisonType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AuthnContextComparisonType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="exact"/&gt;
 *     &lt;enumeration value="minimum"/&gt;
 *     &lt;enumeration value="maximum"/&gt;
 *     &lt;enumeration value="better"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AuthnContextComparisonType")
@XmlEnum
public enum AuthnContextComparisonType {

    @XmlEnumValue("exact")
    EXACT("exact"),
    @XmlEnumValue("minimum")
    MINIMUM("minimum"),
    @XmlEnumValue("maximum")
    MAXIMUM("maximum"),
    @XmlEnumValue("better")
    BETTER("better");
    private final String value;

    AuthnContextComparisonType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthnContextComparisonType fromValue(String v) {
        for (AuthnContextComparisonType c: AuthnContextComparisonType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
