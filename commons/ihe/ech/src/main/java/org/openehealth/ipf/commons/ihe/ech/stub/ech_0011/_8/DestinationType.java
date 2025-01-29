
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.AddressInformationType;


/**
 * <p>Java class for destinationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="destinationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ech.ch/xmlns/eCH-0011/8}generalPlaceType">
 *       &lt;sequence>
 *         &lt;element name="mailAddress" type="{http://www.ech.ch/xmlns/eCH-0010/5}addressInformationType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "destinationType", propOrder = {
    "mailAddress"
})
public class DestinationType
    extends GeneralPlaceType
{

    protected AddressInformationType mailAddress;

    /**
     * Gets the value of the mailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressInformationType }
     *     
     */
    public AddressInformationType getMailAddress() {
        return mailAddress;
    }

    /**
     * Sets the value of the mailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressInformationType }
     *     
     */
    public void setMailAddress(AddressInformationType value) {
        this.mailAddress = value;
    }

}
