
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for maritalDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maritalDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maritalStatus" type="{http://www.ech.ch/xmlns/eCH-0011/8}maritalStatusType"/>
 *         &lt;element name="dateOfMaritalStatus" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="cancelationReason" type="{http://www.ech.ch/xmlns/eCH-0011/8}partnershipAbolitionType" minOccurs="0"/>
 *         &lt;element name="officialProofOfMaritalStatusYesNo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="separationData" type="{http://www.ech.ch/xmlns/eCH-0011/8}separationDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maritalDataType", propOrder = {
    "maritalStatus",
    "dateOfMaritalStatus",
    "cancelationReason",
    "officialProofOfMaritalStatusYesNo",
    "separationData"
})
@XmlSeeAlso({
    MaritalDataRestrictedMaritalStatusPartnerType.class,
    MaritalDataRestrictedDivorceType.class,
    MaritalDataRestrictedPartnershipType.class,
    MaritalDataRestrictedUndoMarriedType.class,
    MaritalDataRestrictedUndoPartnershipType.class,
    MaritalDataRestrictedMarriageType.class
})
public class MaritalDataType {

    @XmlElement(required = true)
    protected String maritalStatus;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfMaritalStatus;
    protected String cancelationReason;
    protected Boolean officialProofOfMaritalStatusYesNo;
    protected SeparationDataType separationData;

    /**
     * Gets the value of the maritalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the dateOfMaritalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfMaritalStatus() {
        return dateOfMaritalStatus;
    }

    /**
     * Sets the value of the dateOfMaritalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfMaritalStatus(XMLGregorianCalendar value) {
        this.dateOfMaritalStatus = value;
    }

    /**
     * Gets the value of the cancelationReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelationReason() {
        return cancelationReason;
    }

    /**
     * Sets the value of the cancelationReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelationReason(String value) {
        this.cancelationReason = value;
    }

    /**
     * Gets the value of the officialProofOfMaritalStatusYesNo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOfficialProofOfMaritalStatusYesNo() {
        return officialProofOfMaritalStatusYesNo;
    }

    /**
     * Sets the value of the officialProofOfMaritalStatusYesNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOfficialProofOfMaritalStatusYesNo(Boolean value) {
        this.officialProofOfMaritalStatusYesNo = value;
    }

    /**
     * Gets the value of the separationData property.
     * 
     * @return
     *     possible object is
     *     {@link SeparationDataType }
     *     
     */
    public SeparationDataType getSeparationData() {
        return separationData;
    }

    /**
     * Sets the value of the separationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeparationDataType }
     *     
     */
    public void setSeparationData(SeparationDataType value) {
        this.separationData = value;
    }

}
