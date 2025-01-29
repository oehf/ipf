
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for birthAddonDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="birthAddonDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nameOfFather" type="{http://www.ech.ch/xmlns/eCH-0021/7}nameOfParentType" minOccurs="0"/>
 *         &lt;element name="nameOfMother" type="{http://www.ech.ch/xmlns/eCH-0021/7}nameOfParentType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "birthAddonDataType", propOrder = {
    "nameOfFather",
    "nameOfMother"
})
public class BirthAddonDataType {

    protected NameOfParentType nameOfFather;
    protected NameOfParentType nameOfMother;

    /**
     * Gets the value of the nameOfFather property.
     * 
     * @return
     *     possible object is
     *     {@link NameOfParentType }
     *     
     */
    public NameOfParentType getNameOfFather() {
        return nameOfFather;
    }

    /**
     * Sets the value of the nameOfFather property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameOfParentType }
     *     
     */
    public void setNameOfFather(NameOfParentType value) {
        this.nameOfFather = value;
    }

    /**
     * Gets the value of the nameOfMother property.
     * 
     * @return
     *     possible object is
     *     {@link NameOfParentType }
     *     
     */
    public NameOfParentType getNameOfMother() {
        return nameOfMother;
    }

    /**
     * Sets the value of the nameOfMother property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameOfParentType }
     *     
     */
    public void setNameOfMother(NameOfParentType value) {
        this.nameOfMother = value;
    }

}
