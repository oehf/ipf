
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for swissMunicipalityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="swissMunicipalityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="municipalityId" type="{http://www.ech.ch/xmlns/eCH-0007/5}municipalityIdType" minOccurs="0"/>
 *         &lt;element name="municipalityName" type="{http://www.ech.ch/xmlns/eCH-0007/5}municipalityNameType"/>
 *         &lt;element name="cantonAbbreviation" type="{http://www.ech.ch/xmlns/eCH-0007/5}cantonAbbreviationType" minOccurs="0"/>
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
@XmlType(name = "swissMunicipalityType", propOrder = {
    "municipalityId",
    "municipalityName",
    "cantonAbbreviation",
    "historyMunicipalityId"
})
public class SwissMunicipalityType {

    protected Integer municipalityId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String municipalityName;
    @XmlSchemaType(name = "token")
    protected CantonAbbreviationType cantonAbbreviation;
    protected Integer historyMunicipalityId;

    /**
     * Gets the value of the municipalityId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMunicipalityId() {
        return municipalityId;
    }

    /**
     * Sets the value of the municipalityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMunicipalityId(Integer value) {
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
     * Gets the value of the cantonAbbreviation property.
     * 
     * @return
     *     possible object is
     *     {@link CantonAbbreviationType }
     *     
     */
    public CantonAbbreviationType getCantonAbbreviation() {
        return cantonAbbreviation;
    }

    /**
     * Sets the value of the cantonAbbreviation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CantonAbbreviationType }
     *     
     */
    public void setCantonAbbreviation(CantonAbbreviationType value) {
        this.cantonAbbreviation = value;
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
