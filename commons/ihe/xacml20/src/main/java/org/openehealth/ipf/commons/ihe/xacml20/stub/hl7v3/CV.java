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
package org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * <p>Java class for CV complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CV"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="originalText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="codeSystem" use="required" type="{urn:hl7-org:v3}oid" /&gt;
 *       &lt;attribute name="codeSystemName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="codeSystemVersion" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CV", propOrder = {
    "originalText"
})
@Getter
@Setter
public class CV implements Serializable {
    @Serial
    private static final long serialVersionUID = 4293231794649378796L;

    protected String originalText;
    @XmlAttribute(name = "code", required = true)
    protected String code;
    @XmlAttribute(name = "codeSystem", required = true)
    protected String codeSystem;
    @XmlAttribute(name = "codeSystemName")
    protected String codeSystemName;
    @XmlAttribute(name = "codeSystemVersion")
    protected String codeSystemVersion;
    @XmlAttribute(name = "displayName")
    protected String displayName;

    /**
     * Compares two CV values.  Two CV values are equal if their attributes <code>code</code>,
     * <code>codeSystem</code>, and <code>codeSystemVersion</code> are equal.
     * Attributes <code>originalText</code> and <code>displayName</code> are decorations
     * intended solely for humans, and do not have to be evaluated programmatically.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CV cv) &&
            Objects.equals(code, cv.code) &&
            Objects.equals(codeSystem, cv.codeSystem) &&
            Objects.equals(codeSystemVersion, cv.codeSystemVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, codeSystem, codeSystemVersion);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
