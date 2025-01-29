
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pidsToUPIType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pidsToUPIType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
 *           &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
 *           &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pidsToUPIType", propOrder = {
    "content"
})
public class PidsToUPIType {

    @XmlElementRefs({
        @XmlElementRef(name = "vn", namespace = "http://www.ech.ch/xmlns/eCH-0213-commons/1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SPID", namespace = "http://www.ech.ch/xmlns/eCH-0213-commons/1", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends Serializable>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "SPID" is used by two different parts of a schema. See: 
     * line 109 of file:/C:/dev/ipf/commons/ihe/ech/src/main/resources/schema/eCH-0213-commons/1/eCH-0213-commons-1-0.xsd
     * line 106 of file:/C:/dev/ipf/commons/ihe/ech/src/main/resources/schema/eCH-0213-commons/1/eCH-0213-commons-1-0.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Long }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Serializable>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<? extends Serializable>>();
        }
        return this.content;
    }

}
