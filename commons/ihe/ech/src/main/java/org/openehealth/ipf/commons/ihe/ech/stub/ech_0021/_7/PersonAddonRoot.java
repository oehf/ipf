
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element name="personAddon" type="{http://www.ech.ch/xmlns/eCH-0021/7}personAddonType"/>
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
    "personAddon"
})
@XmlRootElement(name = "personAddonRoot")
public class PersonAddonRoot {

    @XmlElement(required = true)
    protected PersonAddonType personAddon;

    /**
     * Gets the value of the personAddon property.
     * 
     * @return
     *     possible object is
     *     {@link PersonAddonType }
     *     
     */
    public PersonAddonType getPersonAddon() {
        return personAddon;
    }

    /**
     * Sets the value of the personAddon property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonAddonType }
     *     
     */
    public void setPersonAddon(PersonAddonType value) {
        this.personAddon = value;
    }

}
