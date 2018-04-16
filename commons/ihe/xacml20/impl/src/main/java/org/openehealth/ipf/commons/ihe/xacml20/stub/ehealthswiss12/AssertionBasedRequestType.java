
package org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;


/**
 * <p>Java class for AssertionBasedRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssertionBasedRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Assertion"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssertionBasedRequestType", propOrder = {
    "assertion"
})
@XmlSeeAlso({
    AddPolicyRequest.class,
    UpdatePolicyRequest.class,
    DeletePolicyRequest.class
})
public class AssertionBasedRequestType {

    @XmlElement(name = "Assertion", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", required = true)
    protected AssertionType assertion;

    /**
     * Gets the value of the assertion property.
     * 
     * @return
     *     possible object is
     *     {@link AssertionType }
     *     
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * Sets the value of the assertion property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssertionType }
     *     
     */
    public void setAssertion(AssertionType value) {
        this.assertion = value;
    }

}
