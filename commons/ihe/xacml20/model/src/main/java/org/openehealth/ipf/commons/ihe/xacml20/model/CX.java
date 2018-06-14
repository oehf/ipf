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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dmytro Rud
 */
@XmlType(name = "CX", namespace = "http://swisscom.com/hlt/asd/xuagen")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CX {

    private String id;
    private String assigningAuthorityId;

    @XmlAttribute(required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(required = true)
    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    public void setAssigningAuthorityId(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    public String toHl7String() {
        return id + "^^^&" + assigningAuthorityId + "&ISO";
    }

    @Override
    public String toString() {
        return "CX{" +
                "id='" + id + '\'' +
                ", assigningAuthorityId='" + assigningAuthorityId + '\'' +
                '}';
    }
}
