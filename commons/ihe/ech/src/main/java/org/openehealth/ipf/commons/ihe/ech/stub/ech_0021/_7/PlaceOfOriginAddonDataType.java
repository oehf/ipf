
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for placeOfOriginAddonDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="placeOfOriginAddonDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="naturalizationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="expatriationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "placeOfOriginAddonDataType", propOrder = {
    "naturalizationDate",
    "expatriationDate"
})
@XmlSeeAlso({
    PlaceOfOriginAddonRestrictedNaturalizeDataType.class,
    PlaceOfOriginAddonRestrictedUnDoDataType.class
})
public class PlaceOfOriginAddonDataType {

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar naturalizationDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar expatriationDate;

    /**
     * Gets the value of the naturalizationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNaturalizationDate() {
        return naturalizationDate;
    }

    /**
     * Sets the value of the naturalizationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNaturalizationDate(XMLGregorianCalendar value) {
        this.naturalizationDate = value;
    }

    /**
     * Gets the value of the expatriationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpatriationDate() {
        return expatriationDate;
    }

    /**
     * Sets the value of the expatriationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpatriationDate(XMLGregorianCalendar value) {
        this.expatriationDate = value;
    }

}
