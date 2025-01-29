
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for nameOfParentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nameOfParentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="firstName" type="{http://www.ech.ch/xmlns/eCH-0044/4}officialFirstNameType"/>
 *             &lt;element name="officialName" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType"/>
 *           &lt;/sequence>
 *           &lt;element name="firstNameOnly" type="{http://www.ech.ch/xmlns/eCH-0044/4}officialFirstNameType"/>
 *           &lt;element name="officialNameOnly" type="{http://www.ech.ch/xmlns/eCH-0044/4}baseNameType"/>
 *         &lt;/choice>
 *         &lt;element name="officialProofOfNameOfParentsYesNo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nameOfParentType", propOrder = {
    "firstName",
    "officialName",
    "firstNameOnly",
    "officialNameOnly",
    "officialProofOfNameOfParentsYesNo"
})
public class NameOfParentType {

    protected String firstName;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String officialName;
    protected String firstNameOnly;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String officialNameOnly;
    protected Boolean officialProofOfNameOfParentsYesNo;

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
     * Gets the value of the firstNameOnly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstNameOnly() {
        return firstNameOnly;
    }

    /**
     * Sets the value of the firstNameOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstNameOnly(String value) {
        this.firstNameOnly = value;
    }

    /**
     * Gets the value of the officialNameOnly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficialNameOnly() {
        return officialNameOnly;
    }

    /**
     * Sets the value of the officialNameOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficialNameOnly(String value) {
        this.officialNameOnly = value;
    }

    /**
     * Gets the value of the officialProofOfNameOfParentsYesNo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOfficialProofOfNameOfParentsYesNo() {
        return officialProofOfNameOfParentsYesNo;
    }

    /**
     * Sets the value of the officialProofOfNameOfParentsYesNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOfficialProofOfNameOfParentsYesNo(Boolean value) {
        this.officialProofOfNameOfParentsYesNo = value;
    }

}
