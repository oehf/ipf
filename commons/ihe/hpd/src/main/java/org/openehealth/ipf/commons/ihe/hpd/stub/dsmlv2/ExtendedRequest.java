
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requestName" type="{urn:oasis:names:tc:DSML:2:0:core}NumericOID"/&gt;
 *         &lt;element name="requestValue" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedRequest", propOrder = {
    "requestName",
    "requestValue"
})
public class ExtendedRequest
    extends DsmlMessage
{

    @XmlElement(required = true)
    protected String requestName;
    protected Object requestValue;

    /**
     * Gets the value of the requestName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestName() {
        return requestName;
    }

    /**
     * Sets the value of the requestName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestName(String value) {
        this.requestName = value;
    }

    /**
     * Gets the value of the requestValue property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRequestValue() {
        return requestValue;
    }

    /**
     * Sets the value of the requestValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRequestValue(Object value) {
        this.requestValue = value;
    }

}
