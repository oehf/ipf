
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deathDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deathDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deathPeriod" type="{http://www.ech.ch/xmlns/eCH-0011/8}deathPeriodType"/>
 *         &lt;element name="placeOfDeath" type="{http://www.ech.ch/xmlns/eCH-0011/8}generalPlaceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deathDataType", propOrder = {
    "deathPeriod",
    "placeOfDeath"
})
public class DeathDataType {

    @XmlElement(required = true)
    protected DeathPeriodType deathPeriod;
    protected GeneralPlaceType placeOfDeath;

    /**
     * Gets the value of the deathPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link DeathPeriodType }
     *     
     */
    public DeathPeriodType getDeathPeriod() {
        return deathPeriod;
    }

    /**
     * Sets the value of the deathPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeathPeriodType }
     *     
     */
    public void setDeathPeriod(DeathPeriodType value) {
        this.deathPeriod = value;
    }

    /**
     * Gets the value of the placeOfDeath property.
     * 
     * @return
     *     possible object is
     *     {@link GeneralPlaceType }
     *     
     */
    public GeneralPlaceType getPlaceOfDeath() {
        return placeOfDeath;
    }

    /**
     * Sets the value of the placeOfDeath property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneralPlaceType }
     *     
     */
    public void setPlaceOfDeath(GeneralPlaceType value) {
        this.placeOfDeath = value;
    }

}
