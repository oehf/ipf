
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.PersonIdentificationType;


/**
 * <p>Java class for personType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personIdentification" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationType"/>
 *         &lt;element name="nameData" type="{http://www.ech.ch/xmlns/eCH-0011/8}nameDataType"/>
 *         &lt;element name="birthData" type="{http://www.ech.ch/xmlns/eCH-0011/8}birthDataType"/>
 *         &lt;element name="religionData" type="{http://www.ech.ch/xmlns/eCH-0011/8}religionDataType"/>
 *         &lt;element name="maritalData" type="{http://www.ech.ch/xmlns/eCH-0011/8}maritalDataType"/>
 *         &lt;element name="nationalityData" type="{http://www.ech.ch/xmlns/eCH-0011/8}nationalityDataType"/>
 *         &lt;element name="deathData" type="{http://www.ech.ch/xmlns/eCH-0011/8}deathDataType" minOccurs="0"/>
 *         &lt;element name="contactData" type="{http://www.ech.ch/xmlns/eCH-0011/8}contactDataType" minOccurs="0"/>
 *         &lt;element name="languageOfCorrespondance" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType" minOccurs="0"/>
 *         &lt;element name="restrictedVotingAndElectionRightFederation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="placeOfOrigin" type="{http://www.ech.ch/xmlns/eCH-0011/8}placeOfOriginType" maxOccurs="unbounded"/>
 *           &lt;element name="residencePermit" type="{http://www.ech.ch/xmlns/eCH-0011/8}residencePermitDataType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personType", propOrder = {
    "personIdentification",
    "nameData",
    "birthData",
    "religionData",
    "maritalData",
    "nationalityData",
    "deathData",
    "contactData",
    "languageOfCorrespondance",
    "restrictedVotingAndElectionRightFederation",
    "placeOfOrigin",
    "residencePermit"
})
public class PersonType {

    @XmlElement(required = true)
    protected PersonIdentificationType personIdentification;
    @XmlElement(required = true)
    protected NameDataType nameData;
    @XmlElement(required = true)
    protected BirthDataType birthData;
    @XmlElement(required = true)
    protected ReligionDataType religionData;
    @XmlElement(required = true)
    protected MaritalDataType maritalData;
    @XmlElement(required = true)
    protected NationalityDataType nationalityData;
    protected DeathDataType deathData;
    protected ContactDataType contactData;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String languageOfCorrespondance;
    protected Boolean restrictedVotingAndElectionRightFederation;
    protected List<PlaceOfOriginType> placeOfOrigin;
    protected ResidencePermitDataType residencePermit;

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
     * Gets the value of the nameData property.
     * 
     * @return
     *     possible object is
     *     {@link NameDataType }
     *     
     */
    public NameDataType getNameData() {
        return nameData;
    }

    /**
     * Sets the value of the nameData property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameDataType }
     *     
     */
    public void setNameData(NameDataType value) {
        this.nameData = value;
    }

    /**
     * Gets the value of the birthData property.
     * 
     * @return
     *     possible object is
     *     {@link BirthDataType }
     *     
     */
    public BirthDataType getBirthData() {
        return birthData;
    }

    /**
     * Sets the value of the birthData property.
     * 
     * @param value
     *     allowed object is
     *     {@link BirthDataType }
     *     
     */
    public void setBirthData(BirthDataType value) {
        this.birthData = value;
    }

    /**
     * Gets the value of the religionData property.
     * 
     * @return
     *     possible object is
     *     {@link ReligionDataType }
     *     
     */
    public ReligionDataType getReligionData() {
        return religionData;
    }

    /**
     * Sets the value of the religionData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReligionDataType }
     *     
     */
    public void setReligionData(ReligionDataType value) {
        this.religionData = value;
    }

    /**
     * Gets the value of the maritalData property.
     * 
     * @return
     *     possible object is
     *     {@link MaritalDataType }
     *     
     */
    public MaritalDataType getMaritalData() {
        return maritalData;
    }

    /**
     * Sets the value of the maritalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaritalDataType }
     *     
     */
    public void setMaritalData(MaritalDataType value) {
        this.maritalData = value;
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

    /**
     * Gets the value of the deathData property.
     * 
     * @return
     *     possible object is
     *     {@link DeathDataType }
     *     
     */
    public DeathDataType getDeathData() {
        return deathData;
    }

    /**
     * Sets the value of the deathData property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeathDataType }
     *     
     */
    public void setDeathData(DeathDataType value) {
        this.deathData = value;
    }

    /**
     * Gets the value of the contactData property.
     * 
     * @return
     *     possible object is
     *     {@link ContactDataType }
     *     
     */
    public ContactDataType getContactData() {
        return contactData;
    }

    /**
     * Sets the value of the contactData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactDataType }
     *     
     */
    public void setContactData(ContactDataType value) {
        this.contactData = value;
    }

    /**
     * Gets the value of the languageOfCorrespondance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageOfCorrespondance() {
        return languageOfCorrespondance;
    }

    /**
     * Sets the value of the languageOfCorrespondance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageOfCorrespondance(String value) {
        this.languageOfCorrespondance = value;
    }

    /**
     * Gets the value of the restrictedVotingAndElectionRightFederation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRestrictedVotingAndElectionRightFederation() {
        return restrictedVotingAndElectionRightFederation;
    }

    /**
     * Sets the value of the restrictedVotingAndElectionRightFederation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRestrictedVotingAndElectionRightFederation(Boolean value) {
        this.restrictedVotingAndElectionRightFederation = value;
    }

    /**
     * Gets the value of the placeOfOrigin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the placeOfOrigin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlaceOfOrigin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PlaceOfOriginType }
     * 
     * 
     */
    public List<PlaceOfOriginType> getPlaceOfOrigin() {
        if (placeOfOrigin == null) {
            placeOfOrigin = new ArrayList<PlaceOfOriginType>();
        }
        return this.placeOfOrigin;
    }

    /**
     * Gets the value of the residencePermit property.
     * 
     * @return
     *     possible object is
     *     {@link ResidencePermitDataType }
     *     
     */
    public ResidencePermitDataType getResidencePermit() {
        return residencePermit;
    }

    /**
     * Sets the value of the residencePermit property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResidencePermitDataType }
     *     
     */
    public void setResidencePermit(ResidencePermitDataType value) {
        this.residencePermit = value;
    }

}
