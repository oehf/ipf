
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}LDAPResult"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="responseName" type="{urn:oasis:names:tc:DSML:2:0:core}NumericOID" minOccurs="0"/&gt;
 *         &lt;element name="response" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedResponse", propOrder = {
    "responseName",
    "response"
})
public class ExtendedResponse
    extends LDAPResult
{

    protected String responseName;
    protected Object response;

    /**
     * Gets the value of the responseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseName() {
        return responseName;
    }

    /**
     * Sets the value of the responseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseName(String value) {
        this.responseName = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setResponse(Object value) {
        this.response = value;
    }

}
