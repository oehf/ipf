
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for datePartiallyKnownType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="datePartiallyKnownType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="yearMonthDay" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="yearMonth" type="{http://www.w3.org/2001/XMLSchema}gYearMonth"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}gYear"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datePartiallyKnownType", propOrder = {
    "yearMonthDay",
    "yearMonth",
    "year"
})
public class DatePartiallyKnownType {

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar yearMonthDay;
    @XmlSchemaType(name = "gYearMonth")
    protected XMLGregorianCalendar yearMonth;
    @XmlSchemaType(name = "gYear")
    protected XMLGregorianCalendar year;

    /**
     * Gets the value of the yearMonthDay property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getYearMonthDay() {
        return yearMonthDay;
    }

    /**
     * Sets the value of the yearMonthDay property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setYearMonthDay(XMLGregorianCalendar value) {
        this.yearMonthDay = value;
    }

    /**
     * Gets the value of the yearMonth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getYearMonth() {
        return yearMonth;
    }

    /**
     * Sets the value of the yearMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setYearMonth(XMLGregorianCalendar value) {
        this.yearMonth = value;
    }

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setYear(XMLGregorianCalendar value) {
        this.year = value;
    }

}
