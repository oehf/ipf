//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.9-03/31/2009 04:14 PM(snajper)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.04.30 at 06:20:20 PM CEST 
//


package org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceBindingQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceBindingQueryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}ServiceQuery" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}SpecificationLinkQuery" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TargetBindingQuery" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}ServiceBindingQueryType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceBindingQueryType", propOrder = {
    "serviceQuery",
    "specificationLinkQuery",
    "targetBindingQuery"
})
public class ServiceBindingQueryType
    extends RegistryObjectQueryType
{

    @XmlElement(name = "ServiceQuery")
    protected ServiceQueryType serviceQuery;
    @XmlElement(name = "SpecificationLinkQuery")
    protected List<SpecificationLinkQueryType> specificationLinkQuery;
    @XmlElement(name = "TargetBindingQuery")
    protected ServiceBindingQueryType targetBindingQuery;

    /**
     * Gets the value of the serviceQuery property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceQueryType }
     *     
     */
    public ServiceQueryType getServiceQuery() {
        return serviceQuery;
    }

    /**
     * Sets the value of the serviceQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceQueryType }
     *     
     */
    public void setServiceQuery(ServiceQueryType value) {
        this.serviceQuery = value;
    }

    /**
     * Gets the value of the specificationLinkQuery property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specificationLinkQuery property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecificationLinkQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpecificationLinkQueryType }
     * 
     * 
     */
    public List<SpecificationLinkQueryType> getSpecificationLinkQuery() {
        if (specificationLinkQuery == null) {
            specificationLinkQuery = new ArrayList<>();
        }
        return this.specificationLinkQuery;
    }

    /**
     * Gets the value of the targetBindingQuery property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceBindingQueryType }
     *     
     */
    public ServiceBindingQueryType getTargetBindingQuery() {
        return targetBindingQuery;
    }

    /**
     * Sets the value of the targetBindingQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceBindingQueryType }
     *     
     */
    public void setTargetBindingQuery(ServiceBindingQueryType value) {
        this.targetBindingQuery = value;
    }

}
