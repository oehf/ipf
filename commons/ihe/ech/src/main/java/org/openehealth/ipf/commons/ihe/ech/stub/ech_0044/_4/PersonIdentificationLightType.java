
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for personIdentificationLightType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personIdentificationLightType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType" minOccurs="0"/>
 *         &lt;element name="localPersonId" type="{http://www.ech.ch/xmlns/eCH-0044/4}namedPersonIdType" minOccurs="0"/>
 *         &lt;element name="otherPersonId" type="{http://www.ech.ch/xmlns/eCH-0044/4}namedPersonIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="officialName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType"/>
 *         &lt;element name="firstName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType"/>
 *         &lt;element name="originalName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType" minOccurs="0"/>
 *         &lt;element name="sex" type="{http://www.ech.ch/xmlns/eCH-0044/4}sexType" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://www.ech.ch/xmlns/eCH-0044/4}datePartiallyKnownType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personIdentificationLightType", propOrder = {
    "vn",
    "localPersonId",
    "otherPersonId",
    "officialName",
    "firstName",
    "originalName",
    "sex",
    "dateOfBirth"
})
public class PersonIdentificationLightType {

    @XmlSchemaType(name = "unsignedLong")
    protected Long vn;
    protected NamedPersonIdType localPersonId;
    protected List<NamedPersonIdType> otherPersonId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String officialName;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String firstName;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String originalName;
    protected String sex;
    protected DatePartiallyKnownType dateOfBirth;

    /**
     * Gets the value of the vn property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getVn() {
        return vn;
    }

    /**
     * Sets the value of the vn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setVn(Long value) {
        this.vn = value;
    }

    /**
     * Gets the value of the localPersonId property.
     * 
     * @return
     *     possible object is
     *     {@link NamedPersonIdType }
     *     
     */
    public NamedPersonIdType getLocalPersonId() {
        return localPersonId;
    }

    /**
     * Sets the value of the localPersonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedPersonIdType }
     *     
     */
    public void setLocalPersonId(NamedPersonIdType value) {
        this.localPersonId = value;
    }

    /**
     * Gets the value of the otherPersonId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherPersonId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherPersonId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NamedPersonIdType }
     * 
     * 
     */
    public List<NamedPersonIdType> getOtherPersonId() {
        if (otherPersonId == null) {
            otherPersonId = new ArrayList<NamedPersonIdType>();
        }
        return this.otherPersonId;
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

}
