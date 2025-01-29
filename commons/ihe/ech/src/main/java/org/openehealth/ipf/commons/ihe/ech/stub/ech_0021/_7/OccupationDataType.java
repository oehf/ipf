
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.AddressInformationType;


/**
 * <p>Java class for occupationDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="occupationDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UID" type="{http://www.ech.ch/xmlns/eCH-0021/7}uidStructureType" minOccurs="0"/>
 *         &lt;element name="employer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="placeOfWork" type="{http://www.ech.ch/xmlns/eCH-0010/5}addressInformationType" minOccurs="0"/>
 *         &lt;element name="placeOfEmployer" type="{http://www.ech.ch/xmlns/eCH-0010/5}addressInformationType" minOccurs="0"/>
 *         &lt;element name="occupationValidFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="occupationValidTill" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "occupationDataType", propOrder = {
    "uid",
    "employer",
    "placeOfWork",
    "placeOfEmployer",
    "occupationValidFrom",
    "occupationValidTill"
})
public class OccupationDataType {

    @XmlElement(name = "UID")
    protected UidStructureType uid;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String employer;
    protected AddressInformationType placeOfWork;
    protected AddressInformationType placeOfEmployer;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar occupationValidFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar occupationValidTill;

    /**
     * Gets the value of the uid property.
     * 
     * @return
     *     possible object is
     *     {@link UidStructureType }
     *     
     */
    public UidStructureType getUID() {
        return uid;
    }

    /**
     * Sets the value of the uid property.
     * 
     * @param value
     *     allowed object is
     *     {@link UidStructureType }
     *     
     */
    public void setUID(UidStructureType value) {
        this.uid = value;
    }

    /**
     * Gets the value of the employer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployer() {
        return employer;
    }

    /**
     * Sets the value of the employer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployer(String value) {
        this.employer = value;
    }

    /**
     * Gets the value of the placeOfWork property.
     * 
     * @return
     *     possible object is
     *     {@link AddressInformationType }
     *     
     */
    public AddressInformationType getPlaceOfWork() {
        return placeOfWork;
    }

    /**
     * Sets the value of the placeOfWork property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressInformationType }
     *     
     */
    public void setPlaceOfWork(AddressInformationType value) {
        this.placeOfWork = value;
    }

    /**
     * Gets the value of the placeOfEmployer property.
     * 
     * @return
     *     possible object is
     *     {@link AddressInformationType }
     *     
     */
    public AddressInformationType getPlaceOfEmployer() {
        return placeOfEmployer;
    }

    /**
     * Sets the value of the placeOfEmployer property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressInformationType }
     *     
     */
    public void setPlaceOfEmployer(AddressInformationType value) {
        this.placeOfEmployer = value;
    }

    /**
     * Gets the value of the occupationValidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOccupationValidFrom() {
        return occupationValidFrom;
    }

    /**
     * Sets the value of the occupationValidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOccupationValidFrom(XMLGregorianCalendar value) {
        this.occupationValidFrom = value;
    }

    /**
     * Gets the value of the occupationValidTill property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOccupationValidTill() {
        return occupationValidTill;
    }

    /**
     * Sets the value of the occupationValidTill property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOccupationValidTill(XMLGregorianCalendar value) {
        this.occupationValidTill = value;
    }

}
