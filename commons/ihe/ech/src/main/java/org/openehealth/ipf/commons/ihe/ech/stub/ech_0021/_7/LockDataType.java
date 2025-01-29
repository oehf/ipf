
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for lockDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lockDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataLock" type="{http://www.ech.ch/xmlns/eCH-0021/7}dataLockType"/>
 *         &lt;element name="dataLockValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataLockValidTill" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="paperLock" type="{http://www.ech.ch/xmlns/eCH-0021/7}paperLockType"/>
 *         &lt;element name="paperLockValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="paperLockValidTill" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lockDataType", propOrder = {
    "dataLock",
    "dataLockValidFrom",
    "dataLockValidTill",
    "paperLock",
    "paperLockValidFrom",
    "paperLockValidTill"
})
public class LockDataType {

    @XmlElement(required = true)
    protected String dataLock;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataLockValidFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataLockValidTill;
    @XmlElement(required = true)
    protected String paperLock;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar paperLockValidFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar paperLockValidTill;

    /**
     * Gets the value of the dataLock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataLock() {
        return dataLock;
    }

    /**
     * Sets the value of the dataLock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataLock(String value) {
        this.dataLock = value;
    }

    /**
     * Gets the value of the dataLockValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataLockValidFrom() {
        return dataLockValidFrom;
    }

    /**
     * Sets the value of the dataLockValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataLockValidFrom(XMLGregorianCalendar value) {
        this.dataLockValidFrom = value;
    }

    /**
     * Gets the value of the dataLockValidTill property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataLockValidTill() {
        return dataLockValidTill;
    }

    /**
     * Sets the value of the dataLockValidTill property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataLockValidTill(XMLGregorianCalendar value) {
        this.dataLockValidTill = value;
    }

    /**
     * Gets the value of the paperLock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperLock() {
        return paperLock;
    }

    /**
     * Sets the value of the paperLock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperLock(String value) {
        this.paperLock = value;
    }

    /**
     * Gets the value of the paperLockValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaperLockValidFrom() {
        return paperLockValidFrom;
    }

    /**
     * Sets the value of the paperLockValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaperLockValidFrom(XMLGregorianCalendar value) {
        this.paperLockValidFrom = value;
    }

    /**
     * Gets the value of the paperLockValidTill property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaperLockValidTill() {
        return paperLockValidTill;
    }

    /**
     * Sets the value of the paperLockValidTill property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaperLockValidTill(XMLGregorianCalendar value) {
        this.paperLockValidTill = value;
    }

}
