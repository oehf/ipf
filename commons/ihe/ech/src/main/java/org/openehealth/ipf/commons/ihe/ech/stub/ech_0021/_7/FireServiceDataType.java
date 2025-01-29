
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for fireServiceDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fireServiceDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fireService" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType" minOccurs="0"/>
 *         &lt;element name="fireServiceLiability" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType" minOccurs="0"/>
 *         &lt;element name="fireServiceValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fireServiceDataType", propOrder = {
    "fireService",
    "fireServiceLiability",
    "fireServiceValidFrom"
})
public class FireServiceDataType {

    protected String fireService;
    protected String fireServiceLiability;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fireServiceValidFrom;

    /**
     * Gets the value of the fireService property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFireService() {
        return fireService;
    }

    /**
     * Sets the value of the fireService property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFireService(String value) {
        this.fireService = value;
    }

    /**
     * Gets the value of the fireServiceLiability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFireServiceLiability() {
        return fireServiceLiability;
    }

    /**
     * Sets the value of the fireServiceLiability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFireServiceLiability(String value) {
        this.fireServiceLiability = value;
    }

    /**
     * Gets the value of the fireServiceValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFireServiceValidFrom() {
        return fireServiceValidFrom;
    }

    /**
     * Sets the value of the fireServiceValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFireServiceValidFrom(XMLGregorianCalendar value) {
        this.fireServiceValidFrom = value;
    }

}
