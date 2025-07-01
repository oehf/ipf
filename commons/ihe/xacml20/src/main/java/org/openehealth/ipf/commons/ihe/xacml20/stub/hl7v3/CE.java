/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import lombok.NoArgsConstructor;

import java.io.Serial;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CE")
@NoArgsConstructor
public class CE extends CV {
    @Serial
    private static final long serialVersionUID = -7367916055979182332L;

    @XmlAttribute(name = "xsi:type")
    public String getXsiType() {
        return "hl7:CE";
    }

    public CE(String code, String codeSystem, String codeSystemName, String displayName) {
        super();
        setCode(code);
        setCodeSystem(codeSystem);
        setCodeSystemName(codeSystemName);
        setDisplayName(displayName);
    }

}

