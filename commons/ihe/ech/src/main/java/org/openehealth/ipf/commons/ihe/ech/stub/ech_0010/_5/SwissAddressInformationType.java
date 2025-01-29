
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for swissAddressInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="swissAddressInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addressLine1" type="{http://www.ech.ch/xmlns/eCH-0010/5}addressLineType" minOccurs="0"/>
 *         &lt;element name="addressLine2" type="{http://www.ech.ch/xmlns/eCH-0010/5}addressLineType" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="street" type="{http://www.ech.ch/xmlns/eCH-0010/5}streetType"/>
 *           &lt;element name="houseNumber" type="{http://www.ech.ch/xmlns/eCH-0010/5}houseNumberType" minOccurs="0"/>
 *           &lt;element name="dwellingNumber" type="{http://www.ech.ch/xmlns/eCH-0010/5}dwellingNumberType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="locality" type="{http://www.ech.ch/xmlns/eCH-0010/5}localityType" minOccurs="0"/>
 *         &lt;element name="town" type="{http://www.ech.ch/xmlns/eCH-0010/5}townType"/>
 *         &lt;sequence>
 *           &lt;element name="swissZipCode" type="{http://www.ech.ch/xmlns/eCH-0010/5}swissZipCodeType"/>
 *           &lt;element name="swissZipCodeAddOn" type="{http://www.ech.ch/xmlns/eCH-0010/5}swissZipCodeAddOnType" minOccurs="0"/>
 *           &lt;element name="swissZipCodeId" type="{http://www.ech.ch/xmlns/eCH-0010/5}swissZipCodeIdType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="country">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0010/5}countryIdIso2Type">
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
@XmlType(name = "swissAddressInformationType", propOrder = {
    "addressLine1",
    "addressLine2",
    "street",
    "houseNumber",
    "dwellingNumber",
    "locality",
    "town",
    "swissZipCode",
    "swissZipCodeAddOn",
    "swissZipCodeId",
    "country"
})
public class SwissAddressInformationType {

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String addressLine1;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String addressLine2;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String street;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String houseNumber;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String dwellingNumber;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String locality;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String town;
    @XmlSchemaType(name = "unsignedInt")
    protected long swissZipCode;
    protected String swissZipCodeAddOn;
    protected Integer swissZipCodeId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String country;

    /**
     * Gets the value of the addressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets the value of the addressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine1(String value) {
        this.addressLine1 = value;
    }

    /**
     * Gets the value of the addressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets the value of the addressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine2(String value) {
        this.addressLine2 = value;
    }

    /**
     * Gets the value of the street property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the value of the street property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * Gets the value of the houseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Sets the value of the houseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHouseNumber(String value) {
        this.houseNumber = value;
    }

    /**
     * Gets the value of the dwellingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDwellingNumber() {
        return dwellingNumber;
    }

    /**
     * Sets the value of the dwellingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDwellingNumber(String value) {
        this.dwellingNumber = value;
    }

    /**
     * Gets the value of the locality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocality() {
        return locality;
    }

    /**
     * Sets the value of the locality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocality(String value) {
        this.locality = value;
    }

    /**
     * Gets the value of the town property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTown() {
        return town;
    }

    /**
     * Sets the value of the town property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTown(String value) {
        this.town = value;
    }

    /**
     * Gets the value of the swissZipCode property.
     * 
     */
    public long getSwissZipCode() {
        return swissZipCode;
    }

    /**
     * Sets the value of the swissZipCode property.
     * 
     */
    public void setSwissZipCode(long value) {
        this.swissZipCode = value;
    }

    /**
     * Gets the value of the swissZipCodeAddOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSwissZipCodeAddOn() {
        return swissZipCodeAddOn;
    }

    /**
     * Sets the value of the swissZipCodeAddOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSwissZipCodeAddOn(String value) {
        this.swissZipCodeAddOn = value;
    }

    /**
     * Gets the value of the swissZipCodeId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSwissZipCodeId() {
        return swissZipCodeId;
    }

    /**
     * Sets the value of the swissZipCodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSwissZipCodeId(Integer value) {
        this.swissZipCodeId = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

}
