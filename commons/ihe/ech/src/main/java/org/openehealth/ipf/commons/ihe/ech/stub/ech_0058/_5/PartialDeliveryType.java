
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for partialDeliveryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="partialDeliveryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uniqueIdDelivery" type="{http://www.ech.ch/xmlns/eCH-0058/5}uniqueIdDeliveryType"/>
 *         &lt;element name="totalNumberOfPackages" type="{http://www.ech.ch/xmlns/eCH-0058/5}totalNumberOfPackagesType"/>
 *         &lt;element name="numberOfActualPackage" type="{http://www.ech.ch/xmlns/eCH-0058/5}numberOfActualPackageType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "partialDeliveryType", propOrder = {
    "uniqueIdDelivery",
    "totalNumberOfPackages",
    "numberOfActualPackage"
})
public class PartialDeliveryType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String uniqueIdDelivery;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected int totalNumberOfPackages;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected int numberOfActualPackage;

    /**
     * Gets the value of the uniqueIdDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueIdDelivery() {
        return uniqueIdDelivery;
    }

    /**
     * Sets the value of the uniqueIdDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueIdDelivery(String value) {
        this.uniqueIdDelivery = value;
    }

    /**
     * Gets the value of the totalNumberOfPackages property.
     * 
     */
    public int getTotalNumberOfPackages() {
        return totalNumberOfPackages;
    }

    /**
     * Sets the value of the totalNumberOfPackages property.
     * 
     */
    public void setTotalNumberOfPackages(int value) {
        this.totalNumberOfPackages = value;
    }

    /**
     * Gets the value of the numberOfActualPackage property.
     * 
     */
    public int getNumberOfActualPackage() {
        return numberOfActualPackage;
    }

    /**
     * Sets the value of the numberOfActualPackage property.
     * 
     */
    public void setNumberOfActualPackage(int value) {
        this.numberOfActualPackage = value;
    }

}
