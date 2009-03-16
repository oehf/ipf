
package org.openehealth.ipf.platform.camel.lbs.cxf.process.types;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attachinfo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="onewayattach" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "attachinfo",
    "onewayattach"
})
@XmlRootElement(name = "postMe")
public class PostMe {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler attachinfo;
    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler onewayattach;

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

    /**
     * Gets the value of the attachinfo property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getAttachinfo() {
        return attachinfo;
    }

    /**
     * Sets the value of the attachinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setAttachinfo(DataHandler value) {
        this.attachinfo = value;
    }

    /**
     * Gets the value of the onewayattach property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getOnewayattach() {
        return onewayattach;
    }

    /**
     * Sets the value of the onewayattach property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setOnewayattach(DataHandler value) {
        this.onewayattach = value;
    }

}
