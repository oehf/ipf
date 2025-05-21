package org.openehealth.ipf.commons.ihe.ech.stub.ech_0072._2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for countryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="countryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.ech.ch/xmlns/eCH-0072/2}countryIdType"/>
 *         &lt;element name="unId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="iso2Id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;minLength value="1"/>
 *               &lt;pattern value="[A-Z]{2}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="iso3Id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *               &lt;minLength value="1"/>
 *               &lt;pattern value="[A-Z]{3}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="shortNameDe" type="{http://www.ech.ch/xmlns/eCH-0072/2}string60Type"/>
 *         &lt;element name="shortNameFr" type="{http://www.ech.ch/xmlns/eCH-0072/2}string60Type"/>
 *         &lt;element name="shortNameIt" type="{http://www.ech.ch/xmlns/eCH-0072/2}string60Type"/>
 *         &lt;element name="shortNameEn" type="{http://www.ech.ch/xmlns/eCH-0072/2}string60Type" minOccurs="0"/>
 *         &lt;element name="officialNameDe" type="{http://www.ech.ch/xmlns/eCH-0072/2}string255Type" minOccurs="0"/>
 *         &lt;element name="officialNameFr" type="{http://www.ech.ch/xmlns/eCH-0072/2}string255Type" minOccurs="0"/>
 *         &lt;element name="officialNameIt" type="{http://www.ech.ch/xmlns/eCH-0072/2}string255Type" minOccurs="0"/>
 *         &lt;element name="continent">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="region">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="areaState" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="unMember" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="unEntryDate" type="{http://www.ech.ch/xmlns/eCH-0072/2}dateType" minOccurs="0"/>
 *         &lt;element name="recognizedCh" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="recognizedDate" type="{http://www.ech.ch/xmlns/eCH-0072/2}dateType" minOccurs="0"/>
 *         &lt;element name="remarkDe" type="{http://www.ech.ch/xmlns/eCH-0072/2}token255Type" minOccurs="0"/>
 *         &lt;element name="remarkFr" type="{http://www.ech.ch/xmlns/eCH-0072/2}token255Type" minOccurs="0"/>
 *         &lt;element name="remarkIt" type="{http://www.ech.ch/xmlns/eCH-0072/2}token255Type" minOccurs="0"/>
 *         &lt;element name="entryValid" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dateOfChange" type="{http://www.ech.ch/xmlns/eCH-0072/2}dateType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "countryType", propOrder = {
    "id",
    "unId",
    "iso2Id",
    "iso3Id",
    "shortNameDe",
    "shortNameFr",
    "shortNameIt",
    "shortNameEn",
    "officialNameDe",
    "officialNameFr",
    "officialNameIt",
    "continent",
    "region",
    "state",
    "areaState",
    "unMember",
    "unEntryDate",
    "recognizedCh",
    "recognizedDate",
    "remarkDe",
    "remarkFr",
    "remarkIt",
    "entryValid",
    "dateOfChange"
})
public class CountryType {

    @XmlSchemaType(name = "integer")
    protected int id;
    protected Integer unId;
    protected String iso2Id;
    protected String iso3Id;
    @XmlElement(required = true)
    protected String shortNameDe;
    @XmlElement(required = true)
    protected String shortNameFr;
    @XmlElement(required = true)
    protected String shortNameIt;
    protected String shortNameEn;
    protected String officialNameDe;
    protected String officialNameFr;
    protected String officialNameIt;
    protected int continent;
    protected int region;
    protected boolean state;
    protected Integer areaState;
    protected boolean unMember;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar unEntryDate;
    protected boolean recognizedCh;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar recognizedDate;
    protected String remarkDe;
    protected String remarkFr;
    protected String remarkIt;
    protected boolean entryValid;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfChange;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the unId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnId() {
        return unId;
    }

    /**
     * Sets the value of the unId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnId(Integer value) {
        this.unId = value;
    }

    /**
     * Gets the value of the iso2Id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIso2Id() {
        return iso2Id;
    }

    /**
     * Sets the value of the iso2Id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIso2Id(String value) {
        this.iso2Id = value;
    }

    /**
     * Gets the value of the iso3Id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIso3Id() {
        return iso3Id;
    }

    /**
     * Sets the value of the iso3Id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIso3Id(String value) {
        this.iso3Id = value;
    }

    /**
     * Gets the value of the shortNameDe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortNameDe() {
        return shortNameDe;
    }

    /**
     * Sets the value of the shortNameDe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortNameDe(String value) {
        this.shortNameDe = value;
    }

    /**
     * Gets the value of the shortNameFr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortNameFr() {
        return shortNameFr;
    }

    /**
     * Sets the value of the shortNameFr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortNameFr(String value) {
        this.shortNameFr = value;
    }

    /**
     * Gets the value of the shortNameIt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortNameIt() {
        return shortNameIt;
    }

    /**
     * Sets the value of the shortNameIt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortNameIt(String value) {
        this.shortNameIt = value;
    }

    /**
     * Gets the value of the shortNameEn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortNameEn() {
        return shortNameEn;
    }

    /**
     * Sets the value of the shortNameEn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortNameEn(String value) {
        this.shortNameEn = value;
    }

    /**
     * Gets the value of the officialNameDe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficialNameDe() {
        return officialNameDe;
    }

    /**
     * Sets the value of the officialNameDe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficialNameDe(String value) {
        this.officialNameDe = value;
    }

    /**
     * Gets the value of the officialNameFr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficialNameFr() {
        return officialNameFr;
    }

    /**
     * Sets the value of the officialNameFr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficialNameFr(String value) {
        this.officialNameFr = value;
    }

    /**
     * Gets the value of the officialNameIt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficialNameIt() {
        return officialNameIt;
    }

    /**
     * Sets the value of the officialNameIt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficialNameIt(String value) {
        this.officialNameIt = value;
    }

    /**
     * Gets the value of the continent property.
     * 
     */
    public int getContinent() {
        return continent;
    }

    /**
     * Sets the value of the continent property.
     * 
     */
    public void setContinent(int value) {
        this.continent = value;
    }

    /**
     * Gets the value of the region property.
     * 
     */
    public int getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     */
    public void setRegion(int value) {
        this.region = value;
    }

    /**
     * Gets the value of the state property.
     * 
     */
    public boolean isState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     */
    public void setState(boolean value) {
        this.state = value;
    }

    /**
     * Gets the value of the areaState property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAreaState() {
        return areaState;
    }

    /**
     * Sets the value of the areaState property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAreaState(Integer value) {
        this.areaState = value;
    }

    /**
     * Gets the value of the unMember property.
     * 
     */
    public boolean isUnMember() {
        return unMember;
    }

    /**
     * Sets the value of the unMember property.
     * 
     */
    public void setUnMember(boolean value) {
        this.unMember = value;
    }

    /**
     * Gets the value of the unEntryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUnEntryDate() {
        return unEntryDate;
    }

    /**
     * Sets the value of the unEntryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUnEntryDate(XMLGregorianCalendar value) {
        this.unEntryDate = value;
    }

    /**
     * Gets the value of the recognizedCh property.
     * 
     */
    public boolean isRecognizedCh() {
        return recognizedCh;
    }

    /**
     * Sets the value of the recognizedCh property.
     * 
     */
    public void setRecognizedCh(boolean value) {
        this.recognizedCh = value;
    }

    /**
     * Gets the value of the recognizedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRecognizedDate() {
        return recognizedDate;
    }

    /**
     * Sets the value of the recognizedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRecognizedDate(XMLGregorianCalendar value) {
        this.recognizedDate = value;
    }

    /**
     * Gets the value of the remarkDe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarkDe() {
        return remarkDe;
    }

    /**
     * Sets the value of the remarkDe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarkDe(String value) {
        this.remarkDe = value;
    }

    /**
     * Gets the value of the remarkFr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarkFr() {
        return remarkFr;
    }

    /**
     * Sets the value of the remarkFr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarkFr(String value) {
        this.remarkFr = value;
    }

    /**
     * Gets the value of the remarkIt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarkIt() {
        return remarkIt;
    }

    /**
     * Sets the value of the remarkIt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarkIt(String value) {
        this.remarkIt = value;
    }

    /**
     * Gets the value of the entryValid property.
     * 
     */
    public boolean isEntryValid() {
        return entryValid;
    }

    /**
     * Sets the value of the entryValid property.
     * 
     */
    public void setEntryValid(boolean value) {
        this.entryValid = value;
    }

    /**
     * Gets the value of the dateOfChange property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfChange() {
        return dateOfChange;
    }

    /**
     * Sets the value of the dateOfChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfChange(XMLGregorianCalendar value) {
        this.dateOfChange = value;
    }

}
