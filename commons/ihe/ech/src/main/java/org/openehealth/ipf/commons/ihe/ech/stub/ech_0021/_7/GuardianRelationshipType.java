
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.MailAddressType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8.PartnerIdOrganisationType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.PersonIdentificationLightType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.PersonIdentificationType;


/**
 * <p>Java class for guardianRelationshipType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="guardianRelationshipType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="guardianRelationshipId" type="{http://www.ech.ch/xmlns/eCH-0021/7}guardianRelationshipIdType"/>
 *         &lt;element name="partner" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice>
 *                     &lt;element name="personIdentification" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationType"/>
 *                     &lt;element name="personIdentificationPartner" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationLightType"/>
 *                     &lt;element name="partnerIdOrganisation" type="{http://www.ech.ch/xmlns/eCH-0011/8}partnerIdOrganisationType"/>
 *                   &lt;/choice>
 *                   &lt;element name="address" type="{http://www.ech.ch/xmlns/eCH-0010/5}mailAddressType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="typeOfRelationship">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0021/7}typeOfRelationshipType">
 *               &lt;enumeration value="7"/>
 *               &lt;enumeration value="8"/>
 *               &lt;enumeration value="9"/>
 *               &lt;enumeration value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="guardianMeasureInfo" type="{http://www.ech.ch/xmlns/eCH-0021/7}guardianMeasureInfoType"/>
 *         &lt;element name="care" type="{http://www.ech.ch/xmlns/eCH-0021/7}careType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guardianRelationshipType", propOrder = {
    "guardianRelationshipId",
    "partner",
    "typeOfRelationship",
    "guardianMeasureInfo",
    "care"
})
public class GuardianRelationshipType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String guardianRelationshipId;
    protected GuardianRelationshipType.Partner partner;
    @XmlElement(required = true)
    protected String typeOfRelationship;
    @XmlElement(required = true)
    protected GuardianMeasureInfoType guardianMeasureInfo;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger care;

    /**
     * Gets the value of the guardianRelationshipId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuardianRelationshipId() {
        return guardianRelationshipId;
    }

    /**
     * Sets the value of the guardianRelationshipId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuardianRelationshipId(String value) {
        this.guardianRelationshipId = value;
    }

    /**
     * Gets the value of the partner property.
     * 
     * @return
     *     possible object is
     *     {@link GuardianRelationshipType.Partner }
     *     
     */
    public GuardianRelationshipType.Partner getPartner() {
        return partner;
    }

    /**
     * Sets the value of the partner property.
     * 
     * @param value
     *     allowed object is
     *     {@link GuardianRelationshipType.Partner }
     *     
     */
    public void setPartner(GuardianRelationshipType.Partner value) {
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
     * Gets the value of the guardianMeasureInfo property.
     * 
     * @return
     *     possible object is
     *     {@link GuardianMeasureInfoType }
     *     
     */
    public GuardianMeasureInfoType getGuardianMeasureInfo() {
        return guardianMeasureInfo;
    }

    /**
     * Sets the value of the guardianMeasureInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link GuardianMeasureInfoType }
     *     
     */
    public void setGuardianMeasureInfo(GuardianMeasureInfoType value) {
        this.guardianMeasureInfo = value;
    }

    /**
     * Gets the value of the care property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCare() {
        return care;
    }

    /**
     * Sets the value of the care property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCare(BigInteger value) {
        this.care = value;
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
     *           &lt;element name="partnerIdOrganisation" type="{http://www.ech.ch/xmlns/eCH-0011/8}partnerIdOrganisationType"/>
     *         &lt;/choice>
     *         &lt;element name="address" type="{http://www.ech.ch/xmlns/eCH-0010/5}mailAddressType" minOccurs="0"/>
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
        "partnerIdOrganisation",
        "address"
    })
    public static class Partner {

        protected PersonIdentificationType personIdentification;
        protected PersonIdentificationLightType personIdentificationPartner;
        protected PartnerIdOrganisationType partnerIdOrganisation;
        protected MailAddressType address;

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
         * Gets the value of the partnerIdOrganisation property.
         * 
         * @return
         *     possible object is
         *     {@link PartnerIdOrganisationType }
         *     
         */
        public PartnerIdOrganisationType getPartnerIdOrganisation() {
            return partnerIdOrganisation;
        }

        /**
         * Sets the value of the partnerIdOrganisation property.
         * 
         * @param value
         *     allowed object is
         *     {@link PartnerIdOrganisationType }
         *     
         */
        public void setPartnerIdOrganisation(PartnerIdOrganisationType value) {
            this.partnerIdOrganisation = value;
        }

        /**
         * Gets the value of the address property.
         * 
         * @return
         *     possible object is
         *     {@link MailAddressType }
         *     
         */
        public MailAddressType getAddress() {
            return address;
        }

        /**
         * Sets the value of the address property.
         * 
         * @param value
         *     allowed object is
         *     {@link MailAddressType }
         *     
         */
        public void setAddress(MailAddressType value) {
            this.address = value;
        }

    }

}
