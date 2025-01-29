
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.PersonIdentificationType;


/**
 * <p>Java class for personAddonType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personAddonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personidentification" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdentificationType"/>
 *         &lt;element name="personAdditionalData" type="{http://www.ech.ch/xmlns/eCH-0021/7}personAdditionalData" minOccurs="0"/>
 *         &lt;element name="politicalRightData" type="{http://www.ech.ch/xmlns/eCH-0021/7}politicalRightDataType" minOccurs="0"/>
 *         &lt;element name="birthAddonData" type="{http://www.ech.ch/xmlns/eCH-0021/7}birthAddonDataType" minOccurs="0"/>
 *         &lt;element name="lockData" type="{http://www.ech.ch/xmlns/eCH-0021/7}lockDataType" minOccurs="0"/>
 *         &lt;element name="placeOfOriginAddonData" type="{http://www.ech.ch/xmlns/eCH-0021/7}placeOfOriginAddonDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="jobData" type="{http://www.ech.ch/xmlns/eCH-0021/7}jobDataType" minOccurs="0"/>
 *         &lt;element name="maritalRelationship" type="{http://www.ech.ch/xmlns/eCH-0021/7}maritalRelationshipType" minOccurs="0"/>
 *         &lt;element name="parentalRelationship" type="{http://www.ech.ch/xmlns/eCH-0021/7}parentalRelationshipType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="guardianRelationship" type="{http://www.ech.ch/xmlns/eCH-0021/7}guardianRelationshipType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="armedForcesData" type="{http://www.ech.ch/xmlns/eCH-0021/7}armedForcesDataType" minOccurs="0"/>
 *         &lt;element name="civilDefenseData" type="{http://www.ech.ch/xmlns/eCH-0021/7}civilDefenseDataType" minOccurs="0"/>
 *         &lt;element name="fireServiceData" type="{http://www.ech.ch/xmlns/eCH-0021/7}fireServiceDataType" minOccurs="0"/>
 *         &lt;element name="healthInsuranceData" type="{http://www.ech.ch/xmlns/eCH-0021/7}healthInsuranceDataType" minOccurs="0"/>
 *         &lt;element name="matrimonialInheritanceArrangementData" type="{http://www.ech.ch/xmlns/eCH-0021/7}matrimonialInheritanceArrangementDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personAddonType", propOrder = {
    "personidentification",
    "personAdditionalData",
    "politicalRightData",
    "birthAddonData",
    "lockData",
    "placeOfOriginAddonData",
    "jobData",
    "maritalRelationship",
    "parentalRelationship",
    "guardianRelationship",
    "armedForcesData",
    "civilDefenseData",
    "fireServiceData",
    "healthInsuranceData",
    "matrimonialInheritanceArrangementData"
})
public class PersonAddonType {

    @XmlElement(required = true)
    protected PersonIdentificationType personidentification;
    protected PersonAdditionalData personAdditionalData;
    protected PoliticalRightDataType politicalRightData;
    protected BirthAddonDataType birthAddonData;
    protected LockDataType lockData;
    protected List<PlaceOfOriginAddonDataType> placeOfOriginAddonData;
    protected JobDataType jobData;
    protected MaritalRelationshipType maritalRelationship;
    protected List<ParentalRelationshipType> parentalRelationship;
    protected List<GuardianRelationshipType> guardianRelationship;
    protected ArmedForcesDataType armedForcesData;
    protected CivilDefenseDataType civilDefenseData;
    protected FireServiceDataType fireServiceData;
    protected HealthInsuranceDataType healthInsuranceData;
    protected MatrimonialInheritanceArrangementDataType matrimonialInheritanceArrangementData;

    /**
     * Gets the value of the personidentification property.
     * 
     * @return
     *     possible object is
     *     {@link PersonIdentificationType }
     *     
     */
    public PersonIdentificationType getPersonidentification() {
        return personidentification;
    }

    /**
     * Sets the value of the personidentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonIdentificationType }
     *     
     */
    public void setPersonidentification(PersonIdentificationType value) {
        this.personidentification = value;
    }

    /**
     * Gets the value of the personAdditionalData property.
     * 
     * @return
     *     possible object is
     *     {@link PersonAdditionalData }
     *     
     */
    public PersonAdditionalData getPersonAdditionalData() {
        return personAdditionalData;
    }

    /**
     * Sets the value of the personAdditionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonAdditionalData }
     *     
     */
    public void setPersonAdditionalData(PersonAdditionalData value) {
        this.personAdditionalData = value;
    }

    /**
     * Gets the value of the politicalRightData property.
     * 
     * @return
     *     possible object is
     *     {@link PoliticalRightDataType }
     *     
     */
    public PoliticalRightDataType getPoliticalRightData() {
        return politicalRightData;
    }

    /**
     * Sets the value of the politicalRightData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoliticalRightDataType }
     *     
     */
    public void setPoliticalRightData(PoliticalRightDataType value) {
        this.politicalRightData = value;
    }

    /**
     * Gets the value of the birthAddonData property.
     * 
     * @return
     *     possible object is
     *     {@link BirthAddonDataType }
     *     
     */
    public BirthAddonDataType getBirthAddonData() {
        return birthAddonData;
    }

    /**
     * Sets the value of the birthAddonData property.
     * 
     * @param value
     *     allowed object is
     *     {@link BirthAddonDataType }
     *     
     */
    public void setBirthAddonData(BirthAddonDataType value) {
        this.birthAddonData = value;
    }

    /**
     * Gets the value of the lockData property.
     * 
     * @return
     *     possible object is
     *     {@link LockDataType }
     *     
     */
    public LockDataType getLockData() {
        return lockData;
    }

    /**
     * Sets the value of the lockData property.
     * 
     * @param value
     *     allowed object is
     *     {@link LockDataType }
     *     
     */
    public void setLockData(LockDataType value) {
        this.lockData = value;
    }

    /**
     * Gets the value of the placeOfOriginAddonData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the placeOfOriginAddonData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlaceOfOriginAddonData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PlaceOfOriginAddonDataType }
     * 
     * 
     */
    public List<PlaceOfOriginAddonDataType> getPlaceOfOriginAddonData() {
        if (placeOfOriginAddonData == null) {
            placeOfOriginAddonData = new ArrayList<PlaceOfOriginAddonDataType>();
        }
        return this.placeOfOriginAddonData;
    }

    /**
     * Gets the value of the jobData property.
     * 
     * @return
     *     possible object is
     *     {@link JobDataType }
     *     
     */
    public JobDataType getJobData() {
        return jobData;
    }

    /**
     * Sets the value of the jobData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JobDataType }
     *     
     */
    public void setJobData(JobDataType value) {
        this.jobData = value;
    }

    /**
     * Gets the value of the maritalRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link MaritalRelationshipType }
     *     
     */
    public MaritalRelationshipType getMaritalRelationship() {
        return maritalRelationship;
    }

    /**
     * Sets the value of the maritalRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaritalRelationshipType }
     *     
     */
    public void setMaritalRelationship(MaritalRelationshipType value) {
        this.maritalRelationship = value;
    }

    /**
     * Gets the value of the parentalRelationship property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parentalRelationship property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParentalRelationship().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParentalRelationshipType }
     * 
     * 
     */
    public List<ParentalRelationshipType> getParentalRelationship() {
        if (parentalRelationship == null) {
            parentalRelationship = new ArrayList<ParentalRelationshipType>();
        }
        return this.parentalRelationship;
    }

    /**
     * Gets the value of the guardianRelationship property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guardianRelationship property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuardianRelationship().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GuardianRelationshipType }
     * 
     * 
     */
    public List<GuardianRelationshipType> getGuardianRelationship() {
        if (guardianRelationship == null) {
            guardianRelationship = new ArrayList<GuardianRelationshipType>();
        }
        return this.guardianRelationship;
    }

    /**
     * Gets the value of the armedForcesData property.
     * 
     * @return
     *     possible object is
     *     {@link ArmedForcesDataType }
     *     
     */
    public ArmedForcesDataType getArmedForcesData() {
        return armedForcesData;
    }

    /**
     * Sets the value of the armedForcesData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArmedForcesDataType }
     *     
     */
    public void setArmedForcesData(ArmedForcesDataType value) {
        this.armedForcesData = value;
    }

    /**
     * Gets the value of the civilDefenseData property.
     * 
     * @return
     *     possible object is
     *     {@link CivilDefenseDataType }
     *     
     */
    public CivilDefenseDataType getCivilDefenseData() {
        return civilDefenseData;
    }

    /**
     * Sets the value of the civilDefenseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link CivilDefenseDataType }
     *     
     */
    public void setCivilDefenseData(CivilDefenseDataType value) {
        this.civilDefenseData = value;
    }

    /**
     * Gets the value of the fireServiceData property.
     * 
     * @return
     *     possible object is
     *     {@link FireServiceDataType }
     *     
     */
    public FireServiceDataType getFireServiceData() {
        return fireServiceData;
    }

    /**
     * Sets the value of the fireServiceData property.
     * 
     * @param value
     *     allowed object is
     *     {@link FireServiceDataType }
     *     
     */
    public void setFireServiceData(FireServiceDataType value) {
        this.fireServiceData = value;
    }

    /**
     * Gets the value of the healthInsuranceData property.
     * 
     * @return
     *     possible object is
     *     {@link HealthInsuranceDataType }
     *     
     */
    public HealthInsuranceDataType getHealthInsuranceData() {
        return healthInsuranceData;
    }

    /**
     * Sets the value of the healthInsuranceData property.
     * 
     * @param value
     *     allowed object is
     *     {@link HealthInsuranceDataType }
     *     
     */
    public void setHealthInsuranceData(HealthInsuranceDataType value) {
        this.healthInsuranceData = value;
    }

    /**
     * Gets the value of the matrimonialInheritanceArrangementData property.
     * 
     * @return
     *     possible object is
     *     {@link MatrimonialInheritanceArrangementDataType }
     *     
     */
    public MatrimonialInheritanceArrangementDataType getMatrimonialInheritanceArrangementData() {
        return matrimonialInheritanceArrangementData;
    }

    /**
     * Sets the value of the matrimonialInheritanceArrangementData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatrimonialInheritanceArrangementDataType }
     *     
     */
    public void setMatrimonialInheritanceArrangementData(MatrimonialInheritanceArrangementDataType value) {
        this.matrimonialInheritanceArrangementData = value;
    }

}
