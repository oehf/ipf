
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.PersonMailAddressType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.PersonIdentificationLightType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.PersonIdentificationType;


/**
 * <p>Java class for maritalRelationshipType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maritalRelationshipType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="partner">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice>
 *                     &lt;element name="personIdentification" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationType"/>
 *                     &lt;element name="personIdentificationPartner" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationLightType"/>
 *                   &lt;/choice>
 *                   &lt;element name="address" type="{http://www.ech.ch/xmlns/eCH-0010/5}personMailAddressType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="typeOfRelationship">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0021/7}typeOfRelationshipType">
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
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
@XmlType(name = "maritalRelationshipType", propOrder = {
    "partner",
    "typeOfRelationship"
})
public class MaritalRelationshipType {

    @XmlElement(required = true)
    protected MaritalRelationshipType.Partner partner;
    @XmlElement(required = true)
    protected String typeOfRelationship;

    /**
     * Gets the value of the partner property.
     * 
     * @return
     *     possible object is
     *     {@link MaritalRelationshipType.Partner }
     *     
     */
    public MaritalRelationshipType.Partner getPartner() {
        return partner;
    }

    /**
     * Sets the value of the partner property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaritalRelationshipType.Partner }
     *     
     */
    public void setPartner(MaritalRelationshipType.Partner value) {
        this.partner = value;
    }

    /**
     * Gets the value of the typeOfRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfRelationship() {
        return typeOfRelationship;
    }

    /**
     * Sets the value of the typeOfRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfRelationship(String value) {
        this.typeOfRelationship = value;
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
     *         &lt;choice>
     *           &lt;element name="personIdentification" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationType"/>
     *           &lt;element name="personIdentificationPartner" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationLightType"/>
     *         &lt;/choice>
     *         &lt;element name="address" type="{http://www.ech.ch/xmlns/eCH-0010/5}personMailAddressType" minOccurs="0"/>
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
        "personIdentification",
        "personIdentificationPartner",
        "address"
    })
    public static class Partner {

        protected PersonIdentificationType personIdentification;
        protected PersonIdentificationLightType personIdentificationPartner;
        protected PersonMailAddressType address;

        /**
         * Gets the value of the personIdentification property.
         * 
         * @return
         *     possible object is
         *     {@link PersonIdentificationType }
         *     
         */
        public PersonIdentificationType getPersonIdentification() {
            return personIdentification;
        }

        /**
         * Sets the value of the personIdentification property.
         * 
         * @param value
         *     allowed object is
         *     {@link PersonIdentificationType }
         *     
         */
        public void setPersonIdentification(PersonIdentificationType value) {
            this.personIdentification = value;
        }

        /**
         * Gets the value of the personIdentificationPartner property.
         * 
         * @return
         *     possible object is
         *     {@link PersonIdentificationLightType }
         *     
         */
        public PersonIdentificationLightType getPersonIdentificationPartner() {
            return personIdentificationPartner;
        }

        /**
         * Sets the value of the personIdentificationPartner property.
         * 
         * @param value
         *     allowed object is
         *     {@link PersonIdentificationLightType }
         *     
         */
        public void setPersonIdentificationPartner(PersonIdentificationLightType value) {
            this.personIdentificationPartner = value;
        }

        /**
         * Gets the value of the address property.
         * 
         * @return
         *     possible object is
         *     {@link PersonMailAddressType }
         *     
         */
        public PersonMailAddressType getAddress() {
            return address;
        }

        /**
         * Sets the value of the address property.
         * 
         * @param value
         *     allowed object is
         *     {@link PersonMailAddressType }
         *     
         */
        public void setAddress(PersonMailAddressType value) {
            this.address = value;
        }

    }

}
