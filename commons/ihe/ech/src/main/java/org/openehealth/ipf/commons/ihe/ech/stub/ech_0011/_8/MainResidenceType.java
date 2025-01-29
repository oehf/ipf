
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5.SwissMunicipalityType;


/**
 * <p>Java class for mainResidenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mainResidenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mainResidence" type="{http://www.ech.ch/xmlns/eCH-0011/8}residenceDataType"/>
 *         &lt;element name="secondaryResidence" type="{http://www.ech.ch/xmlns/eCH-0007/5}swissMunicipalityType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mainResidenceType", propOrder = {
    "mainResidence",
    "secondaryResidence"
})
public class MainResidenceType {

    @XmlElement(required = true)
    protected ResidenceDataType mainResidence;
    protected List<SwissMunicipalityType> secondaryResidence;

    /**
     * Gets the value of the mainResidence property.
     * 
     * @return
     *     possible object is
     *     {@link ResidenceDataType }
     *     
     */
    public ResidenceDataType getMainResidence() {
        return mainResidence;
    }

    /**
     * Sets the value of the mainResidence property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResidenceDataType }
     *     
     */
    public void setMainResidence(ResidenceDataType value) {
        this.mainResidence = value;
    }

    /**
     * Gets the value of the secondaryResidence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the secondaryResidence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecondaryResidence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SwissMunicipalityType }
     * 
     * 
     */
    public List<SwissMunicipalityType> getSecondaryResidence() {
        if (secondaryResidence == null) {
            secondaryResidence = new ArrayList<SwissMunicipalityType>();
        }
        return this.secondaryResidence;
    }

}
