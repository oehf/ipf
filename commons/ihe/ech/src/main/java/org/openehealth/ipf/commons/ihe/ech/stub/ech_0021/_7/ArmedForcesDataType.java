
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for armedForcesDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="armedForcesDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="armedForcesService" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType" minOccurs="0"/>
 *         &lt;element name="armedForcesLiability" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType" minOccurs="0"/>
 *         &lt;element name="armedForcesValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "armedForcesDataType", propOrder = {
    "armedForcesService",
    "armedForcesLiability",
    "armedForcesValidFrom"
})
public class ArmedForcesDataType {

    protected String armedForcesService;
    protected String armedForcesLiability;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar armedForcesValidFrom;

    /**
     * Gets the value of the armedForcesService property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArmedForcesService() {
        return armedForcesService;
    }

    /**
     * Sets the value of the armedForcesService property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArmedForcesService(String value) {
        this.armedForcesService = value;
    }

    /**
     * Gets the value of the armedForcesLiability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArmedForcesLiability() {
        return armedForcesLiability;
    }

    /**
     * Sets the value of the armedForcesLiability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArmedForcesLiability(String value) {
        this.armedForcesLiability = value;
    }

    /**
     * Gets the value of the armedForcesValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getArmedForcesValidFrom() {
        return armedForcesValidFrom;
    }

    /**
     * Sets the value of the armedForcesValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setArmedForcesValidFrom(XMLGregorianCalendar value) {
        this.armedForcesValidFrom = value;
    }

}
