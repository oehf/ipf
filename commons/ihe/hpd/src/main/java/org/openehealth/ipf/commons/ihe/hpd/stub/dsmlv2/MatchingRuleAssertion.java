
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchingRuleAssertion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchingRuleAssertion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlValue"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dnAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="matchingRule" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" type="{urn:oasis:names:tc:DSML:2:0:core}AttributeDescriptionValue" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchingRuleAssertion", propOrder = {
    "value"
})
public class MatchingRuleAssertion {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String value;
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
    public String getValue() {
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
    public void setValue(String value) {
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
        if (dnAttributes == null) {
            return false;
        } else {
            return dnAttributes;
        }
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
