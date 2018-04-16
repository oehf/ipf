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

import static org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.CodingSystemIds;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
@XmlType(name = "PurposeOfUse", namespace = "http://swisscom.com/hlt/asd/xuagen")
@XmlEnum
public enum PurposeOfUse {
    NORMAL   (new CE("NORM", CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Normal")),
    EMERGENCY(new CE("EMER", CodingSystemIds.SWISS_PURPOSE_OF_USE, "eHealth Suisse Verwendungszweck", "Emergency"));

    @Getter private final CE code;

    public static PurposeOfUse fromCode(CE code) {
        for (PurposeOfUse purposeOfUse : values()) {
            if (purposeOfUse.code.equals(code)) {
                return purposeOfUse;
            }
        }
        return null;
    }
}
