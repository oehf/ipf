
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for personAdditionalData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personAdditionalData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mrMrs" type="{http://www.ech.ch/xmlns/eCH-0010/5}mrMrsType" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.ech.ch/xmlns/eCH-0010/5}titleType" minOccurs="0"/>
 *         &lt;element name="languageOfCorrespondance" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personAdditionalData", propOrder = {
    "mrMrs",
    "title",
    "languageOfCorrespondance"
})
public class PersonAdditionalData {

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String mrMrs;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String title;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String languageOfCorrespondance;

    /**
     * Gets the value of the mrMrs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMrMrs() {
        return mrMrs;
    }

    /**
     * Sets the value of the mrMrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMrMrs(String value) {
        this.mrMrs = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the languageOfCorrespondance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageOfCorrespondance() {
        return languageOfCorrespondance;
    }

    /**
     * Sets the value of the languageOfCorrespondance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageOfCorrespondance(String value) {
        this.languageOfCorrespondance = value;
    }

}
