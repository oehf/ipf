
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.DatePartiallyKnownType;


/**
 * <p>Java class for birthDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="birthDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateOfBirth" type="{http://www.ech.ch/xmlns/eCH-0044/4}datePartiallyKnownType"/>
 *         &lt;element name="placeOfBirth" type="{http://www.ech.ch/xmlns/eCH-0011/8}generalPlaceType"/>
 *         &lt;element name="sex" type="{http://www.ech.ch/xmlns/eCH-0044/4}sexType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "birthDataType", propOrder = {
    "dateOfBirth",
    "placeOfBirth",
    "sex"
})
public class BirthDataType {

    @XmlElement(required = true)
    protected DatePartiallyKnownType dateOfBirth;
    @XmlElement(required = true)
    protected GeneralPlaceType placeOfBirth;
    @XmlElement(required = true)
    protected String sex;

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

}
