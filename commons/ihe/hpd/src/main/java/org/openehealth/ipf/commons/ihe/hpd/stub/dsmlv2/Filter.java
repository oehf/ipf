
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Filter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Filter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;group ref="{urn:oasis:names:tc:DSML:2:0:core}FilterGroup"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
