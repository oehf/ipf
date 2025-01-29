
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for swissAndFlMunicipalityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="swissAndFlMunicipalityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="municipalityId" type="{http://www.ech.ch/xmlns/eCH-0007/5}municipalityIdType"/>
 *         &lt;element name="municipalityName" type="{http://www.ech.ch/xmlns/eCH-0007/5}municipalityNameType"/>
 *         &lt;element name="cantonFlAbbreviation" type="{http://www.ech.ch/xmlns/eCH-0007/5}cantonFlAbbreviationType"/>
 *         &lt;element name="historyMunicipalityId" type="{http://www.ech.ch/xmlns/eCH-0007/5}historyMunicipalityId" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "swissAndFlMunicipalityType", propOrder = {
    "municipalityId",
    "municipalityName",
    "cantonFlAbbreviation",
    "historyMunicipalityId"
})
public class SwissAndFlMunicipalityType {

    protected int municipalityId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String municipalityName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected CantonFlAbbreviationType cantonFlAbbreviation;
    protected Integer historyMunicipalityId;

    /**
     * Gets the value of the municipalityId property.
     * 
     */
    public int getMunicipalityId() {
        return municipalityId;
    }

    /**
     * Sets the value of the municipalityId property.
     * 
     */
    public void setMunicipalityId(int value) {
        this.municipalityId = value;
    }

    /**
     * Gets the value of the municipalityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipalityName() {
        return municipalityName;
    }

    /**
     * Sets the value of the municipalityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipalityName(String value) {
        this.municipalityName = value;
    }

    /**
     * Gets the value of the cantonFlAbbreviation property.
     * 
     * @return
     *     possible object is
     *     {@link CantonFlAbbreviationType }
     *     
     */
    public CantonFlAbbreviationType getCantonFlAbbreviation() {
        return cantonFlAbbreviation;
    }

    /**
     * Sets the value of the cantonFlAbbreviation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CantonFlAbbreviationType }
     *     
     */
    public void setCantonFlAbbreviation(CantonFlAbbreviationType value) {
        this.cantonFlAbbreviation = value;
    }

    /**
     * Gets the value of the historyMunicipalityId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHistoryMunicipalityId() {
        return historyMunicipalityId;
    }

    /**
     * Sets the value of the historyMunicipalityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHistoryMunicipalityId(Integer value) {
        this.historyMunicipalityId = value;
    }

}
