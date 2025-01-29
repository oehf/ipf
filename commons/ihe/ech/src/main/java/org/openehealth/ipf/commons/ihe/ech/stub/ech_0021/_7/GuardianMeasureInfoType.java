
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for guardianMeasureInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="guardianMeasureInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="basedOnLaw" type="{http://www.ech.ch/xmlns/eCH-0021/7}basedOnLawType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="basedOnLawAddOn" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="guardianMeasureValidFrom" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guardianMeasureInfoType", propOrder = {
    "basedOnLaw",
    "basedOnLawAddOn",
    "guardianMeasureValidFrom"
})
public class GuardianMeasureInfoType {

    protected List<String> basedOnLaw;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String basedOnLawAddOn;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar guardianMeasureValidFrom;

    /**
     * Gets the value of the basedOnLaw property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the basedOnLaw property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBasedOnLaw().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBasedOnLaw() {
        if (basedOnLaw == null) {
            basedOnLaw = new ArrayList<String>();
        }
        return this.basedOnLaw;
    }

    /**
     * Gets the value of the basedOnLawAddOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasedOnLawAddOn() {
        return basedOnLawAddOn;
    }

    /**
     * Sets the value of the basedOnLawAddOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasedOnLawAddOn(String value) {
        this.basedOnLawAddOn = value;
    }

    /**
     * Gets the value of the guardianMeasureValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGuardianMeasureValidFrom() {
        return guardianMeasureValidFrom;
    }

    /**
     * Sets the value of the guardianMeasureValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGuardianMeasureValidFrom(XMLGregorianCalendar value) {
        this.guardianMeasureValidFrom = value;
    }

}
