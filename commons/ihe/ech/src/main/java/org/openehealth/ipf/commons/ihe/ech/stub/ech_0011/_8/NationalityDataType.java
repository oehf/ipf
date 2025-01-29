
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0008._3.CountryType;


/**
 * <p>Java class for nationalityDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nationalityDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nationalityStatus" type="{http://www.ech.ch/xmlns/eCH-0011/8}nationalityStatusType"/>
 *         &lt;element name="countryInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="country" type="{http://www.ech.ch/xmlns/eCH-0008/3}countryType"/>
 *                   &lt;element name="nationalityValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
@XmlType(name = "nationalityDataType", propOrder = {
    "nationalityStatus",
    "countryInfo"
})
public class NationalityDataType {

    @XmlElement(required = true)
    protected String nationalityStatus;
    protected List<NationalityDataType.CountryInfo> countryInfo;

    /**
     * Gets the value of the nationalityStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalityStatus() {
        return nationalityStatus;
    }

    /**
     * Sets the value of the nationalityStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalityStatus(String value) {
        this.nationalityStatus = value;
    }

    /**
     * Gets the value of the countryInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the countryInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCountryInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NationalityDataType.CountryInfo }
     * 
     * 
     */
    public List<NationalityDataType.CountryInfo> getCountryInfo() {
        if (countryInfo == null) {
            countryInfo = new ArrayList<NationalityDataType.CountryInfo>();
        }
        return this.countryInfo;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="country" type="{http://www.ech.ch/xmlns/eCH-0008/3}countryType"/>
     *         &lt;element name="nationalityValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "country",
        "nationalityValidFrom"
    })
    public static class CountryInfo {

        @XmlElement(required = true)
        protected CountryType country;
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar nationalityValidFrom;

        /**
         * Gets the value of the country property.
         * 
         * @return
         *     possible object is
         *     {@link CountryType }
         *     
         */
        public CountryType getCountry() {
            return country;
        }

        /**
         * Sets the value of the country property.
         * 
         * @param value
         *     allowed object is
         *     {@link CountryType }
         *     
         */
        public void setCountry(CountryType value) {
            this.country = value;
        }

        /**
         * Gets the value of the nationalityValidFrom property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getNationalityValidFrom() {
            return nationalityValidFrom;
        }

        /**
         * Sets the value of the nationalityValidFrom property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setNationalityValidFrom(XMLGregorianCalendar value) {
            this.nationalityValidFrom = value;
        }

    }

}
