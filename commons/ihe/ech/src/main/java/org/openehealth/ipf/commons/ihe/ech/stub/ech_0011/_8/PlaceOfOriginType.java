
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5.CantonAbbreviationType;


/**
 * <p>Java class for placeOfOriginType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="placeOfOriginType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="originName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="canton" type="{http://www.ech.ch/xmlns/eCH-0007/5}cantonAbbreviationType"/>
 *         &lt;element name="placeOfOriginId" type="{http://www.ech.ch/xmlns/eCH-0135/1}placeOfOriginIdType" minOccurs="0"/>
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
@XmlType(name = "placeOfOriginType", propOrder = {
    "originName",
    "canton",
    "placeOfOriginId",
    "historyMunicipalityId"
})
public class PlaceOfOriginType {

    @XmlElement(required = true)
    protected String originName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected CantonAbbreviationType canton;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger placeOfOriginId;
    protected Integer historyMunicipalityId;

    /**
     * Gets the value of the originName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * Sets the value of the originName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginName(String value) {
        this.originName = value;
    }

    /**
     * Gets the value of the canton property.
     * 
     * @return
     *     possible object is
     *     {@link CantonAbbreviationType }
     *     
     */
    public CantonAbbreviationType getCanton() {
        return canton;
    }

    /**
     * Sets the value of the canton property.
     * 
     * @param value
     *     allowed object is
     *     {@link CantonAbbreviationType }
     *     
     */
    public void setCanton(CantonAbbreviationType value) {
        this.canton = value;
    }

    /**
     * Gets the value of the placeOfOriginId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPlaceOfOriginId() {
        return placeOfOriginId;
    }

    /**
     * Sets the value of the placeOfOriginId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPlaceOfOriginId(BigInteger value) {
        this.placeOfOriginId = value;
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
