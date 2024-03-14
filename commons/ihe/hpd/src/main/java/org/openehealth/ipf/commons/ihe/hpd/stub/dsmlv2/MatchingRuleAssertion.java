/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import jakarta.xml.bind.annotation.*;
import java.util.Objects;


/**
 * <p>Java class for MatchingRuleAssertion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchingRuleAssertion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="value" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlValue"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dnAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="matchingRule" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{urn:oasis:names:tc:DSML:2:0:core}AttributeDescriptionValue" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchingRuleAssertion", propOrder = {
    "value"
})
public class MatchingRuleAssertion {

    @XmlElement(required = true, type = String.class)
    @XmlSchemaType(name = "anySimpleType")
    protected Object value;
    @XmlAttribute(name = "dnAttributes")
    protected Boolean dnAttributes;
    @XmlAttribute(name = "matchingRule")
    protected String matchingRule;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the value of the dnAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDnAttributes() {
        return Objects.requireNonNullElse(dnAttributes, false);
    }

    /**
     * Sets the value of the dnAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDnAttributes(Boolean value) {
        this.dnAttributes = value;
    }

    /**
     * Gets the value of the matchingRule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchingRule() {
        return matchingRule;
    }

    /**
     * Sets the value of the matchingRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchingRule(String value) {
        this.matchingRule = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
