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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Filter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Filter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{urn:oasis:names:tc:DSML:2:0:core}FilterGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Filter", propOrder = {
    "and",
    "or",
    "not",
    "equalityMatch",
    "substrings",
    "greaterOrEqual",
    "lessOrEqual",
    "present",
    "approxMatch",
    "extensibleMatch"
})
public class Filter {

    protected FilterSet and;
    protected FilterSet or;
    protected Filter not;
    protected AttributeValueAssertion equalityMatch;
    protected SubstringFilter substrings;
    protected AttributeValueAssertion greaterOrEqual;
    protected AttributeValueAssertion lessOrEqual;
    protected AttributeDescription present;
    protected AttributeValueAssertion approxMatch;
    protected MatchingRuleAssertion extensibleMatch;

    /**
     * Gets the value of the and property.
     * 
     * @return
     *     possible object is
     *     {@link FilterSet }
     *     
     */
    public FilterSet getAnd() {
        return and;
    }

    /**
     * Sets the value of the and property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterSet }
     *     
     */
    public void setAnd(FilterSet value) {
        this.and = value;
    }

    /**
     * Gets the value of the or property.
     * 
     * @return
     *     possible object is
     *     {@link FilterSet }
     *     
     */
    public FilterSet getOr() {
        return or;
    }

    /**
     * Sets the value of the or property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterSet }
     *     
     */
    public void setOr(FilterSet value) {
        this.or = value;
    }

    /**
     * Gets the value of the not property.
     * 
     * @return
     *     possible object is
     *     {@link Filter }
     *     
     */
    public Filter getNot() {
        return not;
    }

    /**
     * Sets the value of the not property.
     * 
     * @param value
     *     allowed object is
     *     {@link Filter }
     *     
     */
    public void setNot(Filter value) {
        this.not = value;
    }

    /**
     * Gets the value of the equalityMatch property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public AttributeValueAssertion getEqualityMatch() {
        return equalityMatch;
    }

    /**
     * Sets the value of the equalityMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public void setEqualityMatch(AttributeValueAssertion value) {
        this.equalityMatch = value;
    }

    /**
     * Gets the value of the substrings property.
     * 
     * @return
     *     possible object is
     *     {@link SubstringFilter }
     *     
     */
    public SubstringFilter getSubstrings() {
        return substrings;
    }

    /**
     * Sets the value of the substrings property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubstringFilter }
     *     
     */
    public void setSubstrings(SubstringFilter value) {
        this.substrings = value;
    }

    /**
     * Gets the value of the greaterOrEqual property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public AttributeValueAssertion getGreaterOrEqual() {
        return greaterOrEqual;
    }

    /**
     * Sets the value of the greaterOrEqual property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public void setGreaterOrEqual(AttributeValueAssertion value) {
        this.greaterOrEqual = value;
    }

    /**
     * Gets the value of the lessOrEqual property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public AttributeValueAssertion getLessOrEqual() {
        return lessOrEqual;
    }

    /**
     * Sets the value of the lessOrEqual property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public void setLessOrEqual(AttributeValueAssertion value) {
        this.lessOrEqual = value;
    }

    /**
     * Gets the value of the present property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeDescription }
     *     
     */
    public AttributeDescription getPresent() {
        return present;
    }

    /**
     * Sets the value of the present property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeDescription }
     *     
     */
    public void setPresent(AttributeDescription value) {
        this.present = value;
    }

    /**
     * Gets the value of the approxMatch property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public AttributeValueAssertion getApproxMatch() {
        return approxMatch;
    }

    /**
     * Sets the value of the approxMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public void setApproxMatch(AttributeValueAssertion value) {
        this.approxMatch = value;
    }

    /**
     * Gets the value of the extensibleMatch property.
     * 
     * @return
     *     possible object is
     *     {@link MatchingRuleAssertion }
     *     
     */
    public MatchingRuleAssertion getExtensibleMatch() {
        return extensibleMatch;
    }

    /**
     * Sets the value of the extensibleMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchingRuleAssertion }
     *     
     */
    public void setExtensibleMatch(MatchingRuleAssertion value) {
        this.extensibleMatch = value;
    }

}
