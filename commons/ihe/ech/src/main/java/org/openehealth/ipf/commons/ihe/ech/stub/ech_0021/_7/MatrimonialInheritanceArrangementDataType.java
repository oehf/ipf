
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for matrimonialInheritanceArrangementDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="matrimonialInheritanceArrangementDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="matrimonialInheritanceArrangement" type="{http://www.ech.ch/xmlns/eCH-0011/8}yesNoType"/>
 *         &lt;element name="matrimonialInheritanceArrangementValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "matrimonialInheritanceArrangementDataType", propOrder = {
    "matrimonialInheritanceArrangement",
    "matrimonialInheritanceArrangementValidFrom"
})
public class MatrimonialInheritanceArrangementDataType {

    @XmlElement(required = true)
    protected String matrimonialInheritanceArrangement;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar matrimonialInheritanceArrangementValidFrom;

    /**
     * Gets the value of the matrimonialInheritanceArrangement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatrimonialInheritanceArrangement() {
        return matrimonialInheritanceArrangement;
    }

    /**
     * Sets the value of the matrimonialInheritanceArrangement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatrimonialInheritanceArrangement(String value) {
        this.matrimonialInheritanceArrangement = value;
    }

    /**
     * Gets the value of the matrimonialInheritanceArrangementValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMatrimonialInheritanceArrangementValidFrom() {
        return matrimonialInheritanceArrangementValidFrom;
    }

    /**
     * Sets the value of the matrimonialInheritanceArrangementValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMatrimonialInheritanceArrangementValidFrom(XMLGregorianCalendar value) {
        this.matrimonialInheritanceArrangementValidFrom = value;
    }

}
