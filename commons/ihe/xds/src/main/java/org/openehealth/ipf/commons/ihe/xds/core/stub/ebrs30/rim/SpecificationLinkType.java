//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.9-03/31/2009 04:14 PM(snajper)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.04.30 at 06:20:20 PM CEST 
//


package org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpecificationLinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecificationLinkType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}RegistryObjectType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}UsageDescription" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}UsageParameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="serviceBinding" use="required" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}referenceURI" />
 *       &lt;attribute name="specificationObject" use="required" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}referenceURI" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpecificationLinkType", propOrder = {
    "usageDescription",
    "usageParameter"
})
public class SpecificationLinkType
    extends RegistryObjectType
{

    @XmlElement(name = "UsageDescription")
    protected InternationalStringType usageDescription;
    @XmlElement(name = "UsageParameter")
    protected List<String> usageParameter;
    @XmlAttribute(required = true)
    protected String serviceBinding;
    @XmlAttribute(required = true)
    protected String specificationObject;

    /**
     * Gets the value of the usageDescription property.
     * 
     * @return
     *     possible object is
     *     {@link InternationalStringType }
     *     
     */
    public InternationalStringType getUsageDescription() {
        return usageDescription;
    }

    /**
     * Sets the value of the usageDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalStringType }
     *     
     */
    public void setUsageDescription(InternationalStringType value) {
        this.usageDescription = value;
    }

    /**
     * Gets the value of the usageParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the usageParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsageParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUsageParameter() {
        if (usageParameter == null) {
            usageParameter = new ArrayList<>();
        }
        return this.usageParameter;
    }

    /**
     * Gets the value of the serviceBinding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceBinding() {
        return serviceBinding;
    }

    /**
     * Sets the value of the serviceBinding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceBinding(String value) {
        this.serviceBinding = value;
    }

    /**
     * Gets the value of the specificationObject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecificationObject() {
        return specificationObject;
    }

    /**
     * Sets the value of the specificationObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecificationObject(String value) {
        this.specificationObject = value;
    }

}
