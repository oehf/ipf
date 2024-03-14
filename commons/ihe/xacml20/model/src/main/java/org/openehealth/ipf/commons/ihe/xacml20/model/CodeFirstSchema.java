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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * This class is used only to create the XML Schema.
 * @author Dmytro Rud
 */
@XmlType(name = "CodeFirstSchema", namespace = "codeFirstSchema", propOrder = {"cx", "nameQualifier", "purposeOfUse", "subjectRole"})
public class CodeFirstSchema {
    @XmlElement
    CX cx;

    @XmlElement
    NameQualifier nameQualifier;

    @XmlElement
    PurposeOfUse purposeOfUse;

    @XmlElement
    SubjectRole subjectRole;
}
