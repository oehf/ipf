
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for uidStructureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="uidStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uidOrganisationIdCategorie" type="{http://www.ech.ch/xmlns/eCH-0021/7}uidOrganisationIdCategorieType"/>
 *         &lt;element name="uidOrganisationId" type="{http://www.ech.ch/xmlns/eCH-0021/7}uidOrganisationIdType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "uidStructureType", propOrder = {
    "uidOrganisationIdCategorie",
    "uidOrganisationId"
})
public class UidStructureType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected UidOrganisationIdCategorieType uidOrganisationIdCategorie;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected int uidOrganisationId;

    /**
     * Gets the value of the uidOrganisationIdCategorie property.
     * 
     * @return
     *     possible object is
     *     {@link UidOrganisationIdCategorieType }
     *     
     */
    public UidOrganisationIdCategorieType getUidOrganisationIdCategorie() {
        return uidOrganisationIdCategorie;
    }

    /**
     * Sets the value of the uidOrganisationIdCategorie property.
     * 
     * @param value
     *     allowed object is
     *     {@link UidOrganisationIdCategorieType }
     *     
     */
    public void setUidOrganisationIdCategorie(UidOrganisationIdCategorieType value) {
        this.uidOrganisationIdCategorie = value;
    }

    /**
     * Gets the value of the uidOrganisationId property.
     * 
     */
    public int getUidOrganisationId() {
        return uidOrganisationId;
    }

    /**
     * Sets the value of the uidOrganisationId property.
     * 
     */
    public void setUidOrganisationId(int value) {
        this.uidOrganisationId = value;
    }

}
