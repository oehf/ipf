
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0135._1;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="placeOfOriginId" type="{http://www.ech.ch/xmlns/eCH-0135/1}placeOfOriginIdType"/>
 *         &lt;element name="historyMunicipalityId" type="{http://www.ech.ch/xmlns/eCH-0007/5}historyMunicipalityId" minOccurs="0"/>
 *         &lt;element name="placeOfOriginName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cantonAbbreviation" type="{http://www.ech.ch/xmlns/eCH-0007/5}cantonAbbreviationType"/>
 *         &lt;element name="successorId" type="{http://www.ech.ch/xmlns/eCH-0135/1}successorIdType" minOccurs="0"/>
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
    "validFrom",
    "validTo",
    "placeOfOriginId",
    "historyMunicipalityId",
    "placeOfOriginName",
    "cantonAbbreviation",
    "successorId"
})
public class PlaceOfOriginType {

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar validFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar validTo;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger placeOfOriginId;
    protected Integer historyMunicipalityId;
    @XmlElement(required = true)
    protected String placeOfOriginName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected CantonAbbreviationType cantonAbbreviation;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger successorId;

    /**
     * Gets the value of the validFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the value of the validFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidFrom(XMLGregorianCalendar value) {
        this.validFrom = value;
    }

    /**
     * Gets the value of the validTo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidTo() {
        return validTo;
    }

    /**
     * Sets the value of the validTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidTo(XMLGregorianCalendar value) {
        this.validTo = value;
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

    /**
     * Gets the value of the placeOfOriginName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceOfOriginName() {
        return placeOfOriginName;
    }

    /**
     * Sets the value of the placeOfOriginName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceOfOriginName(String value) {
        this.placeOfOriginName = value;
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
     * Gets the value of the successorId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSuccessorId() {
        return successorId;
    }

    /**
     * Sets the value of the successorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSuccessorId(BigInteger value) {
        this.successorId = value;
    }

}
