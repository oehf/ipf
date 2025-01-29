
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for addressInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addressInformationType">
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
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="postOfficeBoxNumber" type="{http://www.ech.ch/xmlns/eCH-0010/5}postOfficeBoxNumberType" minOccurs="0"/>
 *           &lt;element name="postOfficeBoxText" type="{http://www.ech.ch/xmlns/eCH-0010/5}postOfficeBoxTextType"/>
 *         &lt;/sequence>
 *         &lt;element name="locality" type="{http://www.ech.ch/xmlns/eCH-0010/5}localityType" minOccurs="0"/>
 *         &lt;element name="town" type="{http://www.ech.ch/xmlns/eCH-0010/5}townType"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="swissZipCode" type="{http://www.ech.ch/xmlns/eCH-0010/5}swissZipCodeType"/>
 *             &lt;element name="swissZipCodeAddOn" type="{http://www.ech.ch/xmlns/eCH-0010/5}swissZipCodeAddOnType" minOccurs="0"/>
 *             &lt;element name="swissZipCodeId" type="{http://www.ech.ch/xmlns/eCH-0010/5}swissZipCodeIdType" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;element name="foreignZipCode" type="{http://www.ech.ch/xmlns/eCH-0010/5}foreignZipCodeType" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="country" type="{http://www.ech.ch/xmlns/eCH-0010/5}countryIdIso2Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addressInformationType", propOrder = {
    "addressLine1",
    "addressLine2",
    "street",
    "houseNumber",
    "dwellingNumber",
    "postOfficeBoxNumber",
    "postOfficeBoxText",
    "locality",
    "town",
    "swissZipCode",
    "swissZipCodeAddOn",
    "swissZipCodeId",
    "foreignZipCode",
    "country"
})
public class AddressInformationType {

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
    @XmlSchemaType(name = "unsignedInt")
    protected Long postOfficeBoxNumber;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String postOfficeBoxText;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String locality;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String town;
    @XmlSchemaType(name = "unsignedInt")
    protected Long swissZipCode;
    protected String swissZipCodeAddOn;
    protected Integer swissZipCodeId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String foreignZipCode;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
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
     * Gets the value of the postOfficeBoxNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    /**
     * Sets the value of the postOfficeBoxNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPostOfficeBoxNumber(Long value) {
        this.postOfficeBoxNumber = value;
    }

    /**
     * Gets the value of the postOfficeBoxText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostOfficeBoxText() {
        return postOfficeBoxText;
    }

    /**
     * Sets the value of the postOfficeBoxText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostOfficeBoxText(String value) {
        this.postOfficeBoxText = value;
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
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSwissZipCode() {
        return swissZipCode;
    }

    /**
     * Sets the value of the swissZipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSwissZipCode(Long value) {
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
     * Gets the value of the foreignZipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignZipCode() {
        return foreignZipCode;
    }

    /**
     * Sets the value of the foreignZipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignZipCode(String value) {
        this.foreignZipCode = value;
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
