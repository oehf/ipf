
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8.GeneralPlaceType;


/**
 * <p>Java class for maritalDataAddonType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maritalDataAddonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="placeOfMarriage" type="{http://www.ech.ch/xmlns/eCH-0011/8}generalPlaceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maritalDataAddonType", propOrder = {
    "placeOfMarriage"
})
public class MaritalDataAddonType {

    protected GeneralPlaceType placeOfMarriage;

    /**
     * Gets the value of the placeOfMarriage property.
     * 
     * @return
     *     possible object is
     *     {@link GeneralPlaceType }
     *     
     */
    public GeneralPlaceType getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    /**
     * Sets the value of the placeOfMarriage property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneralPlaceType }
     *     
     */
    public void setPlaceOfMarriage(GeneralPlaceType value) {
        this.placeOfMarriage = value;
    }

}
