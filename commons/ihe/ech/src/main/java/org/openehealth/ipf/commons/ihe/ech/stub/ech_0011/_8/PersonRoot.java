
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reportedPerson" type="{http://www.ech.ch/xmlns/eCH-0011/8}reportedPersonType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "reportedPerson"
})
@XmlRootElement(name = "personRoot")
public class PersonRoot {

    @XmlElement(required = true)
    protected ReportedPersonType reportedPerson;

    /**
     * Gets the value of the reportedPerson property.
     * 
     * @return
     *     possible object is
     *     {@link ReportedPersonType }
     *     
     */
    public ReportedPersonType getReportedPerson() {
        return reportedPerson;
    }

    /**
     * Sets the value of the reportedPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportedPersonType }
     *     
     */
    public void setReportedPerson(ReportedPersonType value) {
        this.reportedPerson = value;
    }

}
