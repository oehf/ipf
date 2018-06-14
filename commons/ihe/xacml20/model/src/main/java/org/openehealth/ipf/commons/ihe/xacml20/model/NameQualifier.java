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
package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
@XmlType(name = "NameQualifier", namespace = "http://swisscom.com/hlt/asd/xuagen")
@XmlEnum(String.class)
public enum NameQualifier {
    PATIENT("urn:e-health-suisse:2015:epr-spid"),
    PROFESSIONAL("urn:gs1:gln"),
    CUSTODIAN_OR_GUARDIAN("urn:e-health-suisse:custodian-id"),
    ;

    @Getter private final String qualifier;

    public static NameQualifier fromCode(String code) {
        for (NameQualifier nameQualifier : values()) {
            if (nameQualifier.qualifier.equals(code)) {
                return nameQualifier;
            }
        }
        return null;
    }
}
