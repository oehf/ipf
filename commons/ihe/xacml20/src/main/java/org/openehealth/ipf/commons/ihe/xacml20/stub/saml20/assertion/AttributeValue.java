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
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeValue")
@Getter
@Setter
public class AttributeValue {

    @XmlElementRef(namespace = "urn:hl7-org:v3", name = "PurposeOfUse", type = JAXBElement.class, required = false)
    protected JAXBElement<CE> purposeOfUse;

    @XmlElementRef(namespace = "urn:hl7-org:v3", name = "Role", type = JAXBElement.class, required = false)
    protected JAXBElement<CE> role;

    @XmlElementRef(namespace = "urn:hl7-org:v3", name = "CodedValue", type = JAXBElement.class, required = false)
    protected JAXBElement<CV> codedValue;

    @XmlElementRef(namespace = "urn:hl7-org:v3", name = "InstanceIdentifier", type = JAXBElement.class, required = false)
    protected JAXBElement<II> instanceIdentifier;

    @XmlMixed
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected List<String> stringValues = new ArrayList<>();

    @XmlTransient
    public String getStringValue() {
        return stringValues.get(0);
    }

}