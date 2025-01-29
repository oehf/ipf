
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for pidsFromUPIType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pidsFromUPIType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pidsFromUPIType", propOrder = {
    "vn",
    "spid"
})
public class PidsFromUPIType {

    @XmlSchemaType(name = "unsignedLong")
    protected Long vn;
    @XmlElement(name = "SPID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected List<String> spid;

    /**
     * Gets the value of the vn property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getVn() {
        return vn;
    }

    /**
     * Sets the value of the vn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setVn(Long value) {
        this.vn = value;
    }

    /**
     * Gets the value of the spid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSPID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSPID() {
        if (spid == null) {
            spid = new ArrayList<String>();
        }
        return this.spid;
    }

}
