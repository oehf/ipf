
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LDAPResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LDAPResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultCode" type="{urn:oasis:names:tc:DSML:2:0:core}ResultCode"/&gt;
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="referral" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="matchedDN" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlDN" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LDAPResult", propOrder = {
    "resultCode",
    "errorMessage",
    "referral"
})
@XmlSeeAlso({
    ExtendedResponse.class
})
public class LDAPResult
    extends DsmlMessage
{

    @XmlElement(required = true)
    protected ResultCode resultCode;
    protected String errorMessage;
    @XmlSchemaType(name = "anyURI")
    protected List<String> referral;
    @XmlAttribute(name = "matchedDN")
    protected String matchedDN;

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link ResultCode }
     *     
     */
    public ResultCode getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultCode }
     *     
     */
    public void setResultCode(ResultCode value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the referral property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referral property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferral().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReferral() {
        if (referral == null) {
            referral = new ArrayList<>();
        }
        return this.referral;
    }

    /**
     * Gets the value of the matchedDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchedDN() {
        return matchedDN;
    }

    /**
     * Sets the value of the matchedDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchedDN(String value) {
        this.matchedDN = value;
    }

}
