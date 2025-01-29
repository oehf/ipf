
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8.GeneralPlaceType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8.NationalityDataType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7.NameOfParentType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.DatePartiallyKnownType;


/**
 * <p>Java class for personToUPIType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personToUPIType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="firstName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType"/>
 *         &lt;element name="officialName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType"/>
 *         &lt;element name="originalName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType" minOccurs="0"/>
 *         &lt;element name="sex" type="{http://www.ech.ch/xmlns/eCH-0044/4}sexType" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://www.ech.ch/xmlns/eCH-0044/4}datePartiallyKnownType"/>
 *         &lt;element name="placeOfBirth" type="{http://www.ech.ch/xmlns/eCH-0011/8}generalPlaceType" minOccurs="0"/>
 *         &lt;sequence maxOccurs="2" minOccurs="0">
 *           &lt;element name="mothersName" type="{http://www.ech.ch/xmlns/eCH-0021/7}nameOfParentType"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="2" minOccurs="0">
 *           &lt;element name="fathersName" type="{http://www.ech.ch/xmlns/eCH-0021/7}nameOfParentType"/>
 *         &lt;/sequence>
 *         &lt;element name="nationalityData" type="{http://www.ech.ch/xmlns/eCH-0011/8}nationalityDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personToUPIType", propOrder = {
    "firstName",
    "officialName",
    "originalName",
    "sex",
    "dateOfBirth",
    "placeOfBirth",
    "mothersName",
    "fathersName",
    "nationalityData"
})
public class PersonToUPIType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String firstName;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String officialName;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String originalName;
    protected String sex;
    @XmlElement(required = true)
    protected DatePartiallyKnownType dateOfBirth;
    protected GeneralPlaceType placeOfBirth;
    protected List<NameOfParentType> mothersName;
    protected List<NameOfParentType> fathersName;
    protected NationalityDataType nationalityData;

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the officialName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficialName() {
        return officialName;
    }

    /**
     * Sets the value of the officialName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficialName(String value) {
        this.officialName = value;
    }

    /**
     * Gets the value of the originalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * Sets the value of the originalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalName(String value) {
        this.originalName = value;
    }

    /**
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSex(String value) {
        this.sex = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link DatePartiallyKnownType }
     *     
     */
    public DatePartiallyKnownType getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatePartiallyKnownType }
     *     
     */
    public void setDateOfBirth(DatePartiallyKnownType value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the placeOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link GeneralPlaceType }
     *     
     */
    public GeneralPlaceType getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the value of the placeOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneralPlaceType }
     *     
     */
    public void setPlaceOfBirth(GeneralPlaceType value) {
        this.placeOfBirth = value;
    }

    /**
     * Gets the value of the mothersName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mothersName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMothersName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameOfParentType }
     * 
     * 
     */
    public List<NameOfParentType> getMothersName() {
        if (mothersName == null) {
            mothersName = new ArrayList<NameOfParentType>();
        }
        return this.mothersName;
    }

    /**
     * Gets the value of the fathersName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fathersName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFathersName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameOfParentType }
     * 
     * 
     */
    public List<NameOfParentType> getFathersName() {
        if (fathersName == null) {
            fathersName = new ArrayList<NameOfParentType>();
        }
        return this.fathersName;
    }

    /**
     * Gets the value of the nationalityData property.
     * 
     * @return
     *     possible object is
     *     {@link NationalityDataType }
     *     
     */
    public NationalityDataType getNationalityData() {
        return nationalityData;
    }

    /**
     * Sets the value of the nationalityData property.
     * 
     * @param value
     *     allowed object is
     *     {@link NationalityDataType }
     *     
     */
    public void setNationalityData(NationalityDataType value) {
        this.nationalityData = value;
    }

}
