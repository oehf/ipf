
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for jobDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jobDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="kindOfEmployment">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="jobTitle" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="occupationData" type="{http://www.ech.ch/xmlns/eCH-0021/7}occupationDataType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jobDataType", propOrder = {
    "kindOfEmployment",
    "jobTitle",
    "occupationData"
})
public class JobDataType {

    @XmlElement(required = true)
    protected String kindOfEmployment;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String jobTitle;
    protected List<OccupationDataType> occupationData;

    /**
     * Gets the value of the kindOfEmployment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKindOfEmployment() {
        return kindOfEmployment;
    }

    /**
     * Sets the value of the kindOfEmployment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKindOfEmployment(String value) {
        this.kindOfEmployment = value;
    }

    /**
     * Gets the value of the jobTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the value of the jobTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTitle(String value) {
        this.jobTitle = value;
    }

    /**
     * Gets the value of the occupationData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the occupationData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOccupationData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OccupationDataType }
     * 
     * 
     */
    public List<OccupationDataType> getOccupationData() {
        if (occupationData == null) {
            occupationData = new ArrayList<OccupationDataType>();
        }
        return this.occupationData;
    }

}
