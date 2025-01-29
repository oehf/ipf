
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for separationDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="separationDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="separation" type="{http://www.ech.ch/xmlns/eCH-0011/8}separationType" minOccurs="0"/>
 *         &lt;element name="separationValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="separationValidTill" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "separationDataType", propOrder = {
    "separation",
    "separationValidFrom",
    "separationValidTill"
})
public class SeparationDataType {

    protected String separation;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar separationValidFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar separationValidTill;

    /**
     * Gets the value of the separation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeparation() {
        return separation;
    }

    /**
     * Sets the value of the separation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeparation(String value) {
        this.separation = value;
    }

    /**
     * Gets the value of the separationValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSeparationValidFrom() {
        return separationValidFrom;
    }

    /**
     * Sets the value of the separationValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSeparationValidFrom(XMLGregorianCalendar value) {
        this.separationValidFrom = value;
    }

    /**
     * Gets the value of the separationValidTill property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSeparationValidTill() {
        return separationValidTill;
    }

    /**
     * Sets the value of the separationValidTill property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSeparationValidTill(XMLGregorianCalendar value) {
        this.separationValidTill = value;
    }

}
