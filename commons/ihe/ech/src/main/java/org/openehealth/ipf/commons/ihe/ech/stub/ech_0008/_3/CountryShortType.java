
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0008._3;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for countryShortType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="countryShortType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="countryNameShort" type="{http://www.ech.ch/xmlns/eCH-0008/3}countryNameShortType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "countryShortType", propOrder = {
    "countryNameShort"
})
public class CountryShortType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String countryNameShort;

    /**
     * Gets the value of the countryNameShort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryNameShort() {
        return countryNameShort;
    }

    /**
     * Sets the value of the countryNameShort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryNameShort(String value) {
        this.countryNameShort = value;
    }

}
