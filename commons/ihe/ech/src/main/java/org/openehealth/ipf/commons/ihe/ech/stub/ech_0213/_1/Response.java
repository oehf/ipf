
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5.HeaderType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.NegativeReportType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PersonFromUPIType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PidsFromUPIType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.WarningType;


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
 *         &lt;element name="header" type="{http://www.ech.ch/xmlns/eCH-0058/5}headerType"/>
 *         &lt;choice>
 *           &lt;element name="positiveResponse">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="SPIDCategory" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdCategoryType"/>
 *                     &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                       &lt;element name="warning" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}warningType"/>
 *                     &lt;/sequence>
 *                     &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
 *                     &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="negativeReport" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="minorVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "positiveResponse",
    "negativeReport"
})
@XmlRootElement(name = "response")
public class Response {

    @XmlElement(required = true)
    protected HeaderType header;
    protected Response.PositiveResponse positiveResponse;
    protected NegativeReportType negativeReport;
    @XmlAttribute(name = "minorVersion", required = true)
    protected BigInteger minorVersion;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the positiveResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Response.PositiveResponse }
     *     
     */
    public Response.PositiveResponse getPositiveResponse() {
        return positiveResponse;
    }

    /**
     * Sets the value of the positiveResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Response.PositiveResponse }
     *     
     */
    public void setPositiveResponse(Response.PositiveResponse value) {
        this.positiveResponse = value;
    }

    /**
     * Gets the value of the negativeReport property.
     * 
     * @return
     *     possible object is
     *     {@link NegativeReportType }
     *     
     */
    public NegativeReportType getNegativeReport() {
        return negativeReport;
    }

    /**
     * Sets the value of the negativeReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link NegativeReportType }
     *     
     */
    public void setNegativeReport(NegativeReportType value) {
        this.negativeReport = value;
    }

    /**
     * Gets the value of the minorVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the value of the minorVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinorVersion(BigInteger value) {
        this.minorVersion = value;
    }


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
     *         &lt;element name="SPIDCategory" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdCategoryType"/>
     *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *           &lt;element name="warning" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}warningType"/>
     *         &lt;/sequence>
     *         &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
     *         &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
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
        "spidCategory",
        "warning",
        "pids",
        "personFromUPI"
    })
    public static class PositiveResponse {

        @XmlElement(name = "SPIDCategory", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String spidCategory;
        protected List<WarningType> warning;
        @XmlElement(required = true)
        protected PidsFromUPIType pids;
        @XmlElement(required = true)
        protected PersonFromUPIType personFromUPI;

        /**
         * Gets the value of the spidCategory property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPIDCategory() {
            return spidCategory;
        }

        /**
         * Sets the value of the spidCategory property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPIDCategory(String value) {
            this.spidCategory = value;
        }

        /**
         * Gets the value of the warning property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the warning property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getWarning().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link WarningType }
         * 
         * 
         */
        public List<WarningType> getWarning() {
            if (warning == null) {
                warning = new ArrayList<WarningType>();
            }
            return this.warning;
        }

        /**
         * Gets the value of the pids property.
         * 
         * @return
         *     possible object is
         *     {@link PidsFromUPIType }
         *     
         */
        public PidsFromUPIType getPids() {
            return pids;
        }

        /**
         * Sets the value of the pids property.
         * 
         * @param value
         *     allowed object is
         *     {@link PidsFromUPIType }
         *     
         */
        public void setPids(PidsFromUPIType value) {
            this.pids = value;
        }

        /**
         * Gets the value of the personFromUPI property.
         * 
         * @return
         *     possible object is
         *     {@link PersonFromUPIType }
         *     
         */
        public PersonFromUPIType getPersonFromUPI() {
            return personFromUPI;
        }

        /**
         * Sets the value of the personFromUPI property.
         * 
         * @param value
         *     allowed object is
         *     {@link PersonFromUPIType }
         *     
         */
        public void setPersonFromUPI(PersonFromUPIType value) {
            this.personFromUPI = value;
        }

    }

}
