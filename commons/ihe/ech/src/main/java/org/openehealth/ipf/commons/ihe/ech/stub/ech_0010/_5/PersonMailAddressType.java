
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for personMailAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personMailAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="person" type="{http://www.ech.ch/xmlns/eCH-0010/5}personMailAddressInfoType"/>
 *         &lt;element name="addressInformation" type="{http://www.ech.ch/xmlns/eCH-0010/5}addressInformationType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personMailAddressType", propOrder = {
    "person",
    "addressInformation"
})
public class PersonMailAddressType {

    @XmlElement(required = true)
    protected PersonMailAddressInfoType person;
    @XmlElement(required = true)
    protected AddressInformationType addressInformation;

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link PersonMailAddressInfoType }
     *     
     */
    public PersonMailAddressInfoType getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonMailAddressInfoType }
     *     
     */
    public void setPerson(PersonMailAddressInfoType value) {
        this.person = value;
    }

    /**
     * Gets the value of the addressInformation property.
     * 
     * @return
     *     possible object is
     *     {@link AddressInformationType }
     *     
     */
    public AddressInformationType getAddressInformation() {
        return addressInformation;
    }

    /**
     * Sets the value of the addressInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressInformationType }
     *     
     */
    public void setAddressInformation(AddressInformationType value) {
        this.addressInformation = value;
    }

}
