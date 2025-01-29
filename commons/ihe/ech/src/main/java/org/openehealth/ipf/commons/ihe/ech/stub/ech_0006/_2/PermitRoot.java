
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0006._2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="residencePermitCategory" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitCategoryType" minOccurs="0"/>
 *         &lt;element name="residencePermitRuling" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitRulingType" minOccurs="0"/>
 *         &lt;element name="residencePermitBorder" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitBorderType" minOccurs="0"/>
 *         &lt;element name="residencePermitShortType" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitShortType" minOccurs="0"/>
 *         &lt;element name="residencePermit" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitType" minOccurs="0"/>
 *         &lt;element name="inhabitantControl" type="{http://www.ech.ch/xmlns/eCH-0006/2}inhabitantControlType" minOccurs="0"/>
 *         &lt;element name="residencePermitDetailedType" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitDetailedType" minOccurs="0"/>
 *         &lt;element name="residencePermitToBeRegisteredType" type="{http://www.ech.ch/xmlns/eCH-0006/2}residencePermitToBeRegisteredType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "residencePermitCategory",
    "residencePermitRuling",
    "residencePermitBorder",
    "residencePermitShortType",
    "residencePermit",
    "inhabitantControl",
    "residencePermitDetailedType",
    "residencePermitToBeRegisteredType"
})
@XmlRootElement(name = "permitRoot")
public class PermitRoot {

    protected String residencePermitCategory;
    protected String residencePermitRuling;
    protected String residencePermitBorder;
    protected String residencePermitShortType;
    protected String residencePermit;
    protected String inhabitantControl;
    protected String residencePermitDetailedType;
    protected String residencePermitToBeRegisteredType;

    /**
     * Gets the value of the residencePermitCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermitCategory() {
        return residencePermitCategory;
    }

    /**
     * Sets the value of the residencePermitCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermitCategory(String value) {
        this.residencePermitCategory = value;
    }

    /**
     * Gets the value of the residencePermitRuling property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermitRuling() {
        return residencePermitRuling;
    }

    /**
     * Sets the value of the residencePermitRuling property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermitRuling(String value) {
        this.residencePermitRuling = value;
    }

    /**
     * Gets the value of the residencePermitBorder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermitBorder() {
        return residencePermitBorder;
    }

    /**
     * Sets the value of the residencePermitBorder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermitBorder(String value) {
        this.residencePermitBorder = value;
    }

    /**
     * Gets the value of the residencePermitShortType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermitShortType() {
        return residencePermitShortType;
    }

    /**
     * Sets the value of the residencePermitShortType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermitShortType(String value) {
        this.residencePermitShortType = value;
    }

    /**
     * Gets the value of the residencePermit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermit() {
        return residencePermit;
    }

    /**
     * Sets the value of the residencePermit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermit(String value) {
        this.residencePermit = value;
    }

    /**
     * Gets the value of the inhabitantControl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInhabitantControl() {
        return inhabitantControl;
    }

    /**
     * Sets the value of the inhabitantControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInhabitantControl(String value) {
        this.inhabitantControl = value;
    }

    /**
     * Gets the value of the residencePermitDetailedType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermitDetailedType() {
        return residencePermitDetailedType;
    }

    /**
     * Sets the value of the residencePermitDetailedType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermitDetailedType(String value) {
        this.residencePermitDetailedType = value;
    }

    /**
     * Gets the value of the residencePermitToBeRegisteredType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencePermitToBeRegisteredType() {
        return residencePermitToBeRegisteredType;
    }

    /**
     * Sets the value of the residencePermitToBeRegisteredType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencePermitToBeRegisteredType(String value) {
        this.residencePermitToBeRegisteredType = value;
    }

}
