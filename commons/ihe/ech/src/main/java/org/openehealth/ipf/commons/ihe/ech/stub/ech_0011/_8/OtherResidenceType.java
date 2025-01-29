
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for otherResidenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="otherResidenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="secondaryResidence">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0011/8}residenceDataType">
 *                 &lt;sequence>
 *                   &lt;element name="reportingMunicipality" type="{http://www.ech.ch/xmlns/eCH-0007/5}swissMunicipalityType"/>
 *                   &lt;element name="arrivalDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                   &lt;element name="comesFrom" type="{http://www.ech.ch/xmlns/eCH-0011/8}destinationType"/>
 *                   &lt;element name="dwellingAddress" type="{http://www.ech.ch/xmlns/eCH-0011/8}dwellingAddressType"/>
 *                   &lt;element name="departureDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   &lt;element name="goesTo" type="{http://www.ech.ch/xmlns/eCH-0011/8}destinationType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherResidenceType", propOrder = {
    "secondaryResidence"
})
public class OtherResidenceType {

    @XmlElement(required = true)
    protected OtherResidenceType.SecondaryResidence secondaryResidence;

    /**
     * Gets the value of the secondaryResidence property.
     * 
     * @return
     *     possible object is
     *     {@link OtherResidenceType.SecondaryResidence }
     *     
     */
    public OtherResidenceType.SecondaryResidence getSecondaryResidence() {
        return secondaryResidence;
    }

    /**
     * Sets the value of the secondaryResidence property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherResidenceType.SecondaryResidence }
     *     
     */
    public void setSecondaryResidence(OtherResidenceType.SecondaryResidence value) {
        this.secondaryResidence = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0011/8}residenceDataType">
     *       &lt;sequence>
     *         &lt;element name="reportingMunicipality" type="{http://www.ech.ch/xmlns/eCH-0007/5}swissMunicipalityType"/>
     *         &lt;element name="arrivalDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *         &lt;element name="comesFrom" type="{http://www.ech.ch/xmlns/eCH-0011/8}destinationType"/>
     *         &lt;element name="dwellingAddress" type="{http://www.ech.ch/xmlns/eCH-0011/8}dwellingAddressType"/>
     *         &lt;element name="departureDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="goesTo" type="{http://www.ech.ch/xmlns/eCH-0011/8}destinationType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SecondaryResidence
        extends ResidenceDataType
    {


    }

}
