
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for residencePermitDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="residencePermitDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="residencePermit" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitType"/>
 *         &lt;element name="residencePermitValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="residencePermitValidTill" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="entryDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "residencePermitDataType", propOrder = {
    "residencePermit",
    "residencePermitValidFrom",
    "residencePermitValidTill",
    "entryDate"
})
public class ResidencePermitDataType {

    @XmlElement(required = true, nillable = true)
    protected String residencePermit;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar residencePermitValidFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar residencePermitValidTill;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar entryDate;

    /**
     * Gets the value of the residencePermit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermit() {
        return residencePermit;
    }

    /**
     * Sets the value of the residencePermit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermit(String value) {
        this.residencePermit = value;
    }

    /**
     * Gets the value of the residencePermitValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResidencePermitValidFrom() {
        return residencePermitValidFrom;
    }

    /**
     * Sets the value of the residencePermitValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setResidencePermitValidFrom(XMLGregorianCalendar value) {
        this.residencePermitValidFrom = value;
    }

    /**
     * Gets the value of the residencePermitValidTill property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResidencePermitValidTill() {
        return residencePermitValidTill;
    }

    /**
     * Sets the value of the residencePermitValidTill property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setResidencePermitValidTill(XMLGregorianCalendar value) {
        this.residencePermitValidTill = value;
    }

    /**
     * Gets the value of the entryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEntryDate() {
        return entryDate;
    }

    /**
     * Sets the value of the entryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEntryDate(XMLGregorianCalendar value) {
        this.entryDate = value;
    }

}
