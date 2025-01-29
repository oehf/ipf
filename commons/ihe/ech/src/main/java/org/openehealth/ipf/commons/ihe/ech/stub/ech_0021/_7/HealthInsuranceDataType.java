
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.OrganisationMailAddressType;


/**
 * <p>Java class for healthInsuranceDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="healthInsuranceDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="healthInsured" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType"/>
 *         &lt;element name="insurance" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="insuranceName" type="{http://www.ech.ch/xmlns/eCH-0021/7}baseNameType"/>
 *                   &lt;element name="insuranceAddress" type="{http://www.ech.ch/xmlns/eCH-0010/5}organisationMailAddressType"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="healthInsuranceValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "healthInsuranceDataType", propOrder = {
    "healthInsured",
    "insurance",
    "healthInsuranceValidFrom"
})
public class HealthInsuranceDataType {

    @XmlElement(required = true)
    protected String healthInsured;
    protected HealthInsuranceDataType.Insurance insurance;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar healthInsuranceValidFrom;

    /**
     * Gets the value of the healthInsured property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHealthInsured() {
        return healthInsured;
    }

    /**
     * Sets the value of the healthInsured property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHealthInsured(String value) {
        this.healthInsured = value;
    }

    /**
     * Gets the value of the insurance property.
     * 
     * @return
     *     possible object is
     *     {@link HealthInsuranceDataType.Insurance }
     *     
     */
    public HealthInsuranceDataType.Insurance getInsurance() {
        return insurance;
    }

    /**
     * Sets the value of the insurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link HealthInsuranceDataType.Insurance }
     *     
     */
    public void setInsurance(HealthInsuranceDataType.Insurance value) {
        this.insurance = value;
    }

    /**
     * Gets the value of the healthInsuranceValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHealthInsuranceValidFrom() {
        return healthInsuranceValidFrom;
    }

    /**
     * Sets the value of the healthInsuranceValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHealthInsuranceValidFrom(XMLGregorianCalendar value) {
        this.healthInsuranceValidFrom = value;
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
     *       &lt;choice>
     *         &lt;element name="insuranceName" type="{http://www.ech.ch/xmlns/eCH-0021/7}baseNameType"/>
     *         &lt;element name="insuranceAddress" type="{http://www.ech.ch/xmlns/eCH-0010/5}organisationMailAddressType"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "insuranceName",
        "insuranceAddress"
    })
    public static class Insurance {

        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String insuranceName;
        protected OrganisationMailAddressType insuranceAddress;

        /**
         * Gets the value of the insuranceName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInsuranceName() {
            return insuranceName;
        }

        /**
         * Sets the value of the insuranceName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInsuranceName(String value) {
            this.insuranceName = value;
        }

        /**
         * Gets the value of the insuranceAddress property.
         * 
         * @return
         *     possible object is
         *     {@link OrganisationMailAddressType }
         *     
         */
        public OrganisationMailAddressType getInsuranceAddress() {
            return insuranceAddress;
        }

        /**
         * Sets the value of the insuranceAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link OrganisationMailAddressType }
         *     
         */
        public void setInsuranceAddress(OrganisationMailAddressType value) {
            this.insuranceAddress = value;
        }

    }

}
