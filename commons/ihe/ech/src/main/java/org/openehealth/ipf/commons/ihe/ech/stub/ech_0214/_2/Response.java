
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2;

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
 *                       &lt;element name="globalNotice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
 *                     &lt;/sequence>
 *                     &lt;choice>
 *                       &lt;element name="getInfoPersonResponse" maxOccurs="unbounded">
 *                         &lt;complexType>
 *                           &lt;complexContent>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;sequence>
 *                                 &lt;element name="getInfoPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
 *                                 &lt;choice>
 *                                   &lt;sequence>
 *                                     &lt;element name="echoPidRequest">
 *                                       &lt;complexType>
 *                                         &lt;complexContent>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                             &lt;choice>
 *                                               &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
 *                                               &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
 *                                             &lt;/choice>
 *                                           &lt;/restriction>
 *                                         &lt;/complexContent>
 *                                       &lt;/complexType>
 *                                     &lt;/element>
 *                                     &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                                       &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
 *                                     &lt;/sequence>
 *                                     &lt;sequence>
 *                                       &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
 *                                       &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/sequence>
 *                                   &lt;element name="negativReportOnGetInfoPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
 *                                 &lt;/choice>
 *                               &lt;/sequence>
 *                             &lt;/restriction>
 *                           &lt;/complexContent>
 *                         &lt;/complexType>
 *                       &lt;/element>
 *                       &lt;element name="searchPersonResponse" maxOccurs="unbounded">
 *                         &lt;complexType>
 *                           &lt;complexContent>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;sequence>
 *                                 &lt;element name="searchPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
 *                                 &lt;choice>
 *                                   &lt;sequence>
 *                                     &lt;element name="algorithm" type="{http://www.ech.ch/xmlns/eCH-0214/2}algorithmType" minOccurs="0"/>
 *                                     &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                                       &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
 *                                     &lt;/sequence>
 *                                     &lt;choice>
 *                                       &lt;element name="found">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
 *                                                 &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="maybeFound">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="candidate" maxOccurs="unbounded">
 *                                                   &lt;complexType>
 *                                                     &lt;complexContent>
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                         &lt;sequence>
 *                                                           &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
 *                                                           &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
 *                                                           &lt;element name="historicalValuesPersonFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" maxOccurs="unbounded" minOccurs="0"/>
 *                                                           &lt;element name="candidateLikeliness" type="{http://www.ech.ch/xmlns/eCH-0214/2}candidateLikelinessType" minOccurs="0"/>
 *                                                         &lt;/sequence>
 *                                                       &lt;/restriction>
 *                                                     &lt;/complexContent>
 *                                                   &lt;/complexType>
 *                                                 &lt;/element>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="notFound" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                                     &lt;/choice>
 *                                   &lt;/sequence>
 *                                   &lt;element name="negativReportOnSearchPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
 *                                 &lt;/choice>
 *                               &lt;/sequence>
 *                             &lt;/restriction>
 *                           &lt;/complexContent>
 *                         &lt;/complexType>
 *                       &lt;/element>
 *                       &lt;element name="compareDataResponse" maxOccurs="unbounded">
 *                         &lt;complexType>
 *                           &lt;complexContent>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;sequence>
 *                                 &lt;element name="compareDataRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
 *                                 &lt;choice>
 *                                   &lt;sequence>
 *                                     &lt;element name="echoPidsRequest">
 *                                       &lt;complexType>
 *                                         &lt;complexContent>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                             &lt;sequence>
 *                                               &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
 *                                               &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
 *                                             &lt;/sequence>
 *                                           &lt;/restriction>
 *                                         &lt;/complexContent>
 *                                       &lt;/complexType>
 *                                     &lt;/element>
 *                                     &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                                       &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
 *                                     &lt;/sequence>
 *                                     &lt;choice>
 *                                       &lt;element name="identicalData" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                                       &lt;element name="differentData">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/choice>
 *                                   &lt;/sequence>
 *                                   &lt;element name="negativReportOnCompareData" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
 *                                 &lt;/choice>
 *                               &lt;/sequence>
 *                             &lt;/restriction>
 *                           &lt;/complexContent>
 *                         &lt;/complexType>
 *                       &lt;/element>
 *                     &lt;/choice>
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
     *           &lt;element name="globalNotice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
     *         &lt;/sequence>
     *         &lt;choice>
     *           &lt;element name="getInfoPersonResponse" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="getInfoPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
     *                     &lt;choice>
     *                       &lt;sequence>
     *                         &lt;element name="echoPidRequest">
     *                           &lt;complexType>
     *                             &lt;complexContent>
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                 &lt;choice>
     *                                   &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
     *                                   &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
     *                                 &lt;/choice>
     *                               &lt;/restriction>
     *                             &lt;/complexContent>
     *                           &lt;/complexType>
     *                         &lt;/element>
     *                         &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *                           &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
     *                         &lt;/sequence>
     *                         &lt;sequence>
     *                           &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
     *                           &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" minOccurs="0"/>
     *                         &lt;/sequence>
     *                       &lt;/sequence>
     *                       &lt;element name="negativReportOnGetInfoPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
     *                     &lt;/choice>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="searchPersonResponse" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="searchPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
     *                     &lt;choice>
     *                       &lt;sequence>
     *                         &lt;element name="algorithm" type="{http://www.ech.ch/xmlns/eCH-0214/2}algorithmType" minOccurs="0"/>
     *                         &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *                           &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
     *                         &lt;/sequence>
     *                         &lt;choice>
     *                           &lt;element name="found">
     *                             &lt;complexType>
     *                               &lt;complexContent>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                   &lt;sequence>
     *                                     &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
     *                                     &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
     *                                   &lt;/sequence>
     *                                 &lt;/restriction>
     *                               &lt;/complexContent>
     *                             &lt;/complexType>
     *                           &lt;/element>
     *                           &lt;element name="maybeFound">
     *                             &lt;complexType>
     *                               &lt;complexContent>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                   &lt;sequence>
     *                                     &lt;element name="candidate" maxOccurs="unbounded">
     *                                       &lt;complexType>
     *                                         &lt;complexContent>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                             &lt;sequence>
     *                                               &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
     *                                               &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
     *                                               &lt;element name="historicalValuesPersonFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" maxOccurs="unbounded" minOccurs="0"/>
     *                                               &lt;element name="candidateLikeliness" type="{http://www.ech.ch/xmlns/eCH-0214/2}candidateLikelinessType" minOccurs="0"/>
     *                                             &lt;/sequence>
     *                                           &lt;/restriction>
     *                                         &lt;/complexContent>
     *                                       &lt;/complexType>
     *                                     &lt;/element>
     *                                   &lt;/sequence>
     *                                 &lt;/restriction>
     *                               &lt;/complexContent>
     *                             &lt;/complexType>
     *                           &lt;/element>
     *                           &lt;element name="notFound" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                         &lt;/choice>
     *                       &lt;/sequence>
     *                       &lt;element name="negativReportOnSearchPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
     *                     &lt;/choice>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="compareDataResponse" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="compareDataRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
     *                     &lt;choice>
     *                       &lt;sequence>
     *                         &lt;element name="echoPidsRequest">
     *                           &lt;complexType>
     *                             &lt;complexContent>
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                 &lt;sequence>
     *                                   &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
     *                                   &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
     *                                 &lt;/sequence>
     *                               &lt;/restriction>
     *                             &lt;/complexContent>
     *                           &lt;/complexType>
     *                         &lt;/element>
     *                         &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *                           &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
     *                         &lt;/sequence>
     *                         &lt;choice>
     *                           &lt;element name="identicalData" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                           &lt;element name="differentData">
     *                             &lt;complexType>
     *                               &lt;complexContent>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                   &lt;sequence>
     *                                     &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
     *                                   &lt;/sequence>
     *                                 &lt;/restriction>
     *                               &lt;/complexContent>
     *                             &lt;/complexType>
     *                           &lt;/element>
     *                         &lt;/choice>
     *                       &lt;/sequence>
     *                       &lt;element name="negativReportOnCompareData" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
     *                     &lt;/choice>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/choice>
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
        "globalNotice",
        "getInfoPersonResponse",
        "searchPersonResponse",
        "compareDataResponse"
    })
    public static class PositiveResponse {

        @XmlElement(name = "SPIDCategory", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String spidCategory;
        protected List<NoticeType> globalNotice;
        protected List<Response.PositiveResponse.GetInfoPersonResponse> getInfoPersonResponse;
        protected List<Response.PositiveResponse.SearchPersonResponse> searchPersonResponse;
        protected List<Response.PositiveResponse.CompareDataResponse> compareDataResponse;

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
         * Gets the value of the globalNotice property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the globalNotice property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGlobalNotice().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NoticeType }
         * 
         * 
         */
        public List<NoticeType> getGlobalNotice() {
            if (globalNotice == null) {
                globalNotice = new ArrayList<NoticeType>();
            }
            return this.globalNotice;
        }

        /**
         * Gets the value of the getInfoPersonResponse property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the getInfoPersonResponse property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGetInfoPersonResponse().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Response.PositiveResponse.GetInfoPersonResponse }
         * 
         * 
         */
        public List<Response.PositiveResponse.GetInfoPersonResponse> getGetInfoPersonResponse() {
            if (getInfoPersonResponse == null) {
                getInfoPersonResponse = new ArrayList<Response.PositiveResponse.GetInfoPersonResponse>();
            }
            return this.getInfoPersonResponse;
        }

        /**
         * Gets the value of the searchPersonResponse property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the searchPersonResponse property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSearchPersonResponse().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Response.PositiveResponse.SearchPersonResponse }
         * 
         * 
         */
        public List<Response.PositiveResponse.SearchPersonResponse> getSearchPersonResponse() {
            if (searchPersonResponse == null) {
                searchPersonResponse = new ArrayList<Response.PositiveResponse.SearchPersonResponse>();
            }
            return this.searchPersonResponse;
        }

        /**
         * Gets the value of the compareDataResponse property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the compareDataResponse property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCompareDataResponse().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Response.PositiveResponse.CompareDataResponse }
         * 
         * 
         */
        public List<Response.PositiveResponse.CompareDataResponse> getCompareDataResponse() {
            if (compareDataResponse == null) {
                compareDataResponse = new ArrayList<Response.PositiveResponse.CompareDataResponse>();
            }
            return this.compareDataResponse;
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
         *         &lt;element name="compareDataRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
         *         &lt;choice>
         *           &lt;sequence>
         *             &lt;element name="echoPidsRequest">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;sequence>
         *                       &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
         *                       &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
         *                     &lt;/sequence>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *             &lt;sequence maxOccurs="unbounded" minOccurs="0">
         *               &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
         *             &lt;/sequence>
         *             &lt;choice>
         *               &lt;element name="identicalData" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
         *               &lt;element name="differentData">
         *                 &lt;complexType>
         *                   &lt;complexContent>
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                       &lt;sequence>
         *                         &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
         *                       &lt;/sequence>
         *                     &lt;/restriction>
         *                   &lt;/complexContent>
         *                 &lt;/complexType>
         *               &lt;/element>
         *             &lt;/choice>
         *           &lt;/sequence>
         *           &lt;element name="negativReportOnCompareData" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
         *         &lt;/choice>
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
            "compareDataRequestId",
            "echoPidsRequest",
            "notice",
            "identicalData",
            "differentData",
            "negativReportOnCompareData"
        })
        public static class CompareDataResponse {

            @XmlElement(required = true)
            protected BigInteger compareDataRequestId;
            protected Response.PositiveResponse.CompareDataResponse.EchoPidsRequest echoPidsRequest;
            protected List<NoticeType> notice;
            protected Object identicalData;
            protected Response.PositiveResponse.CompareDataResponse.DifferentData differentData;
            protected NegativeReportType negativReportOnCompareData;

            /**
             * Gets the value of the compareDataRequestId property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getCompareDataRequestId() {
                return compareDataRequestId;
            }

            /**
             * Sets the value of the compareDataRequestId property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setCompareDataRequestId(BigInteger value) {
                this.compareDataRequestId = value;
            }

            /**
             * Gets the value of the echoPidsRequest property.
             * 
             * @return
             *     possible object is
             *     {@link Response.PositiveResponse.CompareDataResponse.EchoPidsRequest }
             *     
             */
            public Response.PositiveResponse.CompareDataResponse.EchoPidsRequest getEchoPidsRequest() {
                return echoPidsRequest;
            }

            /**
             * Sets the value of the echoPidsRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.PositiveResponse.CompareDataResponse.EchoPidsRequest }
             *     
             */
            public void setEchoPidsRequest(Response.PositiveResponse.CompareDataResponse.EchoPidsRequest value) {
                this.echoPidsRequest = value;
            }

            /**
             * Gets the value of the notice property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the notice property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getNotice().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link NoticeType }
             * 
             * 
             */
            public List<NoticeType> getNotice() {
                if (notice == null) {
                    notice = new ArrayList<NoticeType>();
                }
                return this.notice;
            }

            /**
             * Gets the value of the identicalData property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getIdenticalData() {
                return identicalData;
            }

            /**
             * Sets the value of the identicalData property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setIdenticalData(Object value) {
                this.identicalData = value;
            }

            /**
             * Gets the value of the differentData property.
             * 
             * @return
             *     possible object is
             *     {@link Response.PositiveResponse.CompareDataResponse.DifferentData }
             *     
             */
            public Response.PositiveResponse.CompareDataResponse.DifferentData getDifferentData() {
                return differentData;
            }

            /**
             * Sets the value of the differentData property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.PositiveResponse.CompareDataResponse.DifferentData }
             *     
             */
            public void setDifferentData(Response.PositiveResponse.CompareDataResponse.DifferentData value) {
                this.differentData = value;
            }

            /**
             * Gets the value of the negativReportOnCompareData property.
             * 
             * @return
             *     possible object is
             *     {@link NegativeReportType }
             *     
             */
            public NegativeReportType getNegativReportOnCompareData() {
                return negativReportOnCompareData;
            }

            /**
             * Sets the value of the negativReportOnCompareData property.
             * 
             * @param value
             *     allowed object is
             *     {@link NegativeReportType }
             *     
             */
            public void setNegativReportOnCompareData(NegativeReportType value) {
                this.negativReportOnCompareData = value;
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
             *         &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
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
                "pids"
            })
            public static class DifferentData {

                @XmlElement(required = true)
                protected PidsFromUPIType pids;

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
             *         &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
             *         &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
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
                "vn",
                "spid"
            })
            public static class EchoPidsRequest {

                @XmlSchemaType(name = "unsignedLong")
                protected long vn;
                @XmlElement(name = "SPID", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "token")
                protected String spid;

                /**
                 * Gets the value of the vn property.
                 * 
                 */
                public long getVn() {
                    return vn;
                }

                /**
                 * Sets the value of the vn property.
                 * 
                 */
                public void setVn(long value) {
                    this.vn = value;
                }

                /**
                 * Gets the value of the spid property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSPID() {
                    return spid;
                }

                /**
                 * Sets the value of the spid property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSPID(String value) {
                    this.spid = value;
                }

            }

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
         *         &lt;element name="getInfoPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
         *         &lt;choice>
         *           &lt;sequence>
         *             &lt;element name="echoPidRequest">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;choice>
         *                       &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
         *                       &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
         *                     &lt;/choice>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *             &lt;sequence maxOccurs="unbounded" minOccurs="0">
         *               &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
         *             &lt;/sequence>
         *             &lt;sequence>
         *               &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
         *               &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" minOccurs="0"/>
         *             &lt;/sequence>
         *           &lt;/sequence>
         *           &lt;element name="negativReportOnGetInfoPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
         *         &lt;/choice>
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
            "getInfoPersonRequestId",
            "echoPidRequest",
            "notice",
            "pids",
            "personFromUPI",
            "negativReportOnGetInfoPerson"
        })
        public static class GetInfoPersonResponse {

            @XmlElement(required = true)
            protected BigInteger getInfoPersonRequestId;
            protected Response.PositiveResponse.GetInfoPersonResponse.EchoPidRequest echoPidRequest;
            protected List<NoticeType> notice;
            protected PidsFromUPIType pids;
            protected PersonFromUPIType personFromUPI;
            protected NegativeReportType negativReportOnGetInfoPerson;

            /**
             * Gets the value of the getInfoPersonRequestId property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getGetInfoPersonRequestId() {
                return getInfoPersonRequestId;
            }

            /**
             * Sets the value of the getInfoPersonRequestId property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setGetInfoPersonRequestId(BigInteger value) {
                this.getInfoPersonRequestId = value;
            }

            /**
             * Gets the value of the echoPidRequest property.
             * 
             * @return
             *     possible object is
             *     {@link Response.PositiveResponse.GetInfoPersonResponse.EchoPidRequest }
             *     
             */
            public Response.PositiveResponse.GetInfoPersonResponse.EchoPidRequest getEchoPidRequest() {
                return echoPidRequest;
            }

            /**
             * Sets the value of the echoPidRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.PositiveResponse.GetInfoPersonResponse.EchoPidRequest }
             *     
             */
            public void setEchoPidRequest(Response.PositiveResponse.GetInfoPersonResponse.EchoPidRequest value) {
                this.echoPidRequest = value;
            }

            /**
             * Gets the value of the notice property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the notice property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getNotice().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link NoticeType }
             * 
             * 
             */
            public List<NoticeType> getNotice() {
                if (notice == null) {
                    notice = new ArrayList<NoticeType>();
                }
                return this.notice;
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

            /**
             * Gets the value of the negativReportOnGetInfoPerson property.
             * 
             * @return
             *     possible object is
             *     {@link NegativeReportType }
             *     
             */
            public NegativeReportType getNegativReportOnGetInfoPerson() {
                return negativReportOnGetInfoPerson;
            }

            /**
             * Sets the value of the negativReportOnGetInfoPerson property.
             * 
             * @param value
             *     allowed object is
             *     {@link NegativeReportType }
             *     
             */
            public void setNegativReportOnGetInfoPerson(NegativeReportType value) {
                this.negativReportOnGetInfoPerson = value;
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
             *       &lt;choice>
             *         &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
             *         &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "vn",
                "spid"
            })
            public static class EchoPidRequest {

                @XmlSchemaType(name = "unsignedLong")
                protected Long vn;
                @XmlElement(name = "SPID")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "token")
                protected String spid;

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
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSPID() {
                    return spid;
                }

                /**
                 * Sets the value of the spid property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSPID(String value) {
                    this.spid = value;
                }

            }

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
         *         &lt;element name="searchPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
         *         &lt;choice>
         *           &lt;sequence>
         *             &lt;element name="algorithm" type="{http://www.ech.ch/xmlns/eCH-0214/2}algorithmType" minOccurs="0"/>
         *             &lt;sequence maxOccurs="unbounded" minOccurs="0">
         *               &lt;element name="notice" type="{http://www.ech.ch/xmlns/eCH-0214/2}noticeType"/>
         *             &lt;/sequence>
         *             &lt;choice>
         *               &lt;element name="found">
         *                 &lt;complexType>
         *                   &lt;complexContent>
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                       &lt;sequence>
         *                         &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
         *                         &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
         *                       &lt;/sequence>
         *                     &lt;/restriction>
         *                   &lt;/complexContent>
         *                 &lt;/complexType>
         *               &lt;/element>
         *               &lt;element name="maybeFound">
         *                 &lt;complexType>
         *                   &lt;complexContent>
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                       &lt;sequence>
         *                         &lt;element name="candidate" maxOccurs="unbounded">
         *                           &lt;complexType>
         *                             &lt;complexContent>
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                 &lt;sequence>
         *                                   &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
         *                                   &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
         *                                   &lt;element name="historicalValuesPersonFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" maxOccurs="unbounded" minOccurs="0"/>
         *                                   &lt;element name="candidateLikeliness" type="{http://www.ech.ch/xmlns/eCH-0214/2}candidateLikelinessType" minOccurs="0"/>
         *                                 &lt;/sequence>
         *                               &lt;/restriction>
         *                             &lt;/complexContent>
         *                           &lt;/complexType>
         *                         &lt;/element>
         *                       &lt;/sequence>
         *                     &lt;/restriction>
         *                   &lt;/complexContent>
         *                 &lt;/complexType>
         *               &lt;/element>
         *               &lt;element name="notFound" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
         *             &lt;/choice>
         *           &lt;/sequence>
         *           &lt;element name="negativReportOnSearchPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}negativeReportType"/>
         *         &lt;/choice>
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
            "searchPersonRequestId",
            "algorithm",
            "notice",
            "found",
            "maybeFound",
            "notFound",
            "negativReportOnSearchPerson"
        })
        public static class SearchPersonResponse {

            @XmlElement(required = true)
            protected BigInteger searchPersonRequestId;
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "token")
            protected String algorithm;
            protected List<NoticeType> notice;
            protected Response.PositiveResponse.SearchPersonResponse.Found found;
            protected Response.PositiveResponse.SearchPersonResponse.MaybeFound maybeFound;
            protected Object notFound;
            protected NegativeReportType negativReportOnSearchPerson;

            /**
             * Gets the value of the searchPersonRequestId property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getSearchPersonRequestId() {
                return searchPersonRequestId;
            }

            /**
             * Sets the value of the searchPersonRequestId property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setSearchPersonRequestId(BigInteger value) {
                this.searchPersonRequestId = value;
            }

            /**
             * Gets the value of the algorithm property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAlgorithm() {
                return algorithm;
            }

            /**
             * Sets the value of the algorithm property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAlgorithm(String value) {
                this.algorithm = value;
            }

            /**
             * Gets the value of the notice property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the notice property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getNotice().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link NoticeType }
             * 
             * 
             */
            public List<NoticeType> getNotice() {
                if (notice == null) {
                    notice = new ArrayList<NoticeType>();
                }
                return this.notice;
            }

            /**
             * Gets the value of the found property.
             * 
             * @return
             *     possible object is
             *     {@link Response.PositiveResponse.SearchPersonResponse.Found }
             *     
             */
            public Response.PositiveResponse.SearchPersonResponse.Found getFound() {
                return found;
            }

            /**
             * Sets the value of the found property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.PositiveResponse.SearchPersonResponse.Found }
             *     
             */
            public void setFound(Response.PositiveResponse.SearchPersonResponse.Found value) {
                this.found = value;
            }

            /**
             * Gets the value of the maybeFound property.
             * 
             * @return
             *     possible object is
             *     {@link Response.PositiveResponse.SearchPersonResponse.MaybeFound }
             *     
             */
            public Response.PositiveResponse.SearchPersonResponse.MaybeFound getMaybeFound() {
                return maybeFound;
            }

            /**
             * Sets the value of the maybeFound property.
             * 
             * @param value
             *     allowed object is
             *     {@link Response.PositiveResponse.SearchPersonResponse.MaybeFound }
             *     
             */
            public void setMaybeFound(Response.PositiveResponse.SearchPersonResponse.MaybeFound value) {
                this.maybeFound = value;
            }

            /**
             * Gets the value of the notFound property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getNotFound() {
                return notFound;
            }

            /**
             * Sets the value of the notFound property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setNotFound(Object value) {
                this.notFound = value;
            }

            /**
             * Gets the value of the negativReportOnSearchPerson property.
             * 
             * @return
             *     possible object is
             *     {@link NegativeReportType }
             *     
             */
            public NegativeReportType getNegativReportOnSearchPerson() {
                return negativReportOnSearchPerson;
            }

            /**
             * Sets the value of the negativReportOnSearchPerson property.
             * 
             * @param value
             *     allowed object is
             *     {@link NegativeReportType }
             *     
             */
            public void setNegativReportOnSearchPerson(NegativeReportType value) {
                this.negativReportOnSearchPerson = value;
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
                "pids",
                "personFromUPI"
            })
            public static class Found {

                @XmlElement(required = true)
                protected PidsFromUPIType pids;
                @XmlElement(required = true)
                protected PersonFromUPIType personFromUPI;

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
             *         &lt;element name="candidate" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
             *                   &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
             *                   &lt;element name="historicalValuesPersonFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" maxOccurs="unbounded" minOccurs="0"/>
             *                   &lt;element name="candidateLikeliness" type="{http://www.ech.ch/xmlns/eCH-0214/2}candidateLikelinessType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
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
                "candidate"
            })
            public static class MaybeFound {

                @XmlElement(required = true)
                protected List<Response.PositiveResponse.SearchPersonResponse.MaybeFound.Candidate> candidate;

                /**
                 * Gets the value of the candidate property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the candidate property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getCandidate().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Response.PositiveResponse.SearchPersonResponse.MaybeFound.Candidate }
                 * 
                 * 
                 */
                public List<Response.PositiveResponse.SearchPersonResponse.MaybeFound.Candidate> getCandidate() {
                    if (candidate == null) {
                        candidate = new ArrayList<Response.PositiveResponse.SearchPersonResponse.MaybeFound.Candidate>();
                    }
                    return this.candidate;
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
                 *         &lt;element name="pids" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsFromUPIType"/>
                 *         &lt;element name="personFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType"/>
                 *         &lt;element name="historicalValuesPersonFromUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personFromUPIType" maxOccurs="unbounded" minOccurs="0"/>
                 *         &lt;element name="candidateLikeliness" type="{http://www.ech.ch/xmlns/eCH-0214/2}candidateLikelinessType" minOccurs="0"/>
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
                    "pids",
                    "personFromUPI",
                    "historicalValuesPersonFromUPI",
                    "candidateLikeliness"
                })
                public static class Candidate {

                    @XmlElement(required = true)
                    protected PidsFromUPIType pids;
                    @XmlElement(required = true)
                    protected PersonFromUPIType personFromUPI;
                    protected List<PersonFromUPIType> historicalValuesPersonFromUPI;
                    @XmlSchemaType(name = "positiveInteger")
                    protected BigInteger candidateLikeliness;

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

                    /**
                     * Gets the value of the historicalValuesPersonFromUPI property.
                     * 
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the historicalValuesPersonFromUPI property.
                     * 
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     *    getHistoricalValuesPersonFromUPI().add(newItem);
                     * </pre>
                     * 
                     * 
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link PersonFromUPIType }
                     * 
                     * 
                     */
                    public List<PersonFromUPIType> getHistoricalValuesPersonFromUPI() {
                        if (historicalValuesPersonFromUPI == null) {
                            historicalValuesPersonFromUPI = new ArrayList<PersonFromUPIType>();
                        }
                        return this.historicalValuesPersonFromUPI;
                    }

                    /**
                     * Gets the value of the candidateLikeliness property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link BigInteger }
                     *     
                     */
                    public BigInteger getCandidateLikeliness() {
                        return candidateLikeliness;
                    }

                    /**
                     * Sets the value of the candidateLikeliness property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link BigInteger }
                     *     
                     */
                    public void setCandidateLikeliness(BigInteger value) {
                        this.candidateLikeliness = value;
                    }

                }

            }

        }

    }

}
