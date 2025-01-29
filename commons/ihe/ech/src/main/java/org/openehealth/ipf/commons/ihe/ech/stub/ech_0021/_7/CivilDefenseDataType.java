
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for civilDefenseDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="civilDefenseDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="civilDefense" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType" minOccurs="0"/>
 *         &lt;element name="civilDefenseValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "civilDefenseDataType", propOrder = {
    "civilDefense",
    "civilDefenseValidFrom"
})
public class CivilDefenseDataType {

    protected String civilDefense;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar civilDefenseValidFrom;

    /**
     * Gets the value of the civilDefense property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivilDefense() {
        return civilDefense;
    }

    /**
     * Sets the value of the civilDefense property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivilDefense(String value) {
        this.civilDefense = value;
    }

    /**
     * Gets the value of the civilDefenseValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCivilDefenseValidFrom() {
        return civilDefenseValidFrom;
    }

    /**
     * Sets the value of the civilDefenseValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCivilDefenseValidFrom(XMLGregorianCalendar value) {
        this.civilDefenseValidFrom = value;
    }

}
