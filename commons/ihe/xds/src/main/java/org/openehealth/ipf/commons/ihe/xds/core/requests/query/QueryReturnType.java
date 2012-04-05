/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Return types for XDS queries (ITI-18, ITI-38, ITI-63).
 * @author Dmytro Rud
 */
@XmlType(name = "QueryReturnType")
@XmlEnum(String.class)
public enum QueryReturnType {
    // for ITI-18 and ITI-38
    @XmlEnumValue("LeafClass") LEAF_CLASS("LeafClass"),
    @XmlEnumValue("ObjectRef") OBJECT_REF("ObjectRef"),

    // for ITI-63
    @XmlEnumValue("LeafClassWithRepositoryItem") LEAF_CLASS_WITH_REPOSITORY_ITEM("LeafClassWithRepositoryItem");

    private final String code;

    QueryReturnType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static QueryReturnType valueOfCode(String code) {
        for (QueryReturnType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
