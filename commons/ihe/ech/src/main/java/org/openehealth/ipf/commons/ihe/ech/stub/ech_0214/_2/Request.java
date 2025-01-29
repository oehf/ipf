
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
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PersonToUPIType;


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
 *         &lt;element name="content">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SPIDCategory" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdCategoryType"/>
 *                   &lt;element name="responseLanguage" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType"/>
 *                   &lt;choice>
 *                     &lt;element name="getInfoPersonRequest" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="getInfoPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
 *                               &lt;element name="detailLevelOfResponse">
 *                                 &lt;simpleType>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                                     &lt;minLength value="1"/>
 *                                     &lt;maxLength value="20"/>
 *                                   &lt;/restriction>
 *                                 &lt;/simpleType>
 *                               &lt;/element>
 *                               &lt;element name="pid">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;choice>
 *                                         &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
 *                                         &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
 *                                       &lt;/choice>
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="searchPersonRequest" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="searchPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
 *                               &lt;element name="algorithm" type="{http://www.ech.ch/xmlns/eCH-0214/2}algorithmType" minOccurs="0"/>
 *                               &lt;element name="searchedPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personToUPIType"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="compareDataRequest" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="compareDataRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
 *                               &lt;element name="pids">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;sequence>
 *                                         &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
 *                                         &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
 *                                       &lt;/sequence>
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "content"
})
@XmlRootElement(name = "request")
public class Request {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected Request.Content content;
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
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link Request.Content }
     *     
     */
    public Request.Content getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request.Content }
     *     
     */
    public void setContent(Request.Content value) {
        this.content = value;
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
     *         &lt;element name="responseLanguage" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType"/>
     *         &lt;choice>
     *           &lt;element name="getInfoPersonRequest" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="getInfoPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
     *                     &lt;element name="detailLevelOfResponse">
     *                       &lt;simpleType>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *                           &lt;minLength value="1"/>
     *                           &lt;maxLength value="20"/>
     *                         &lt;/restriction>
     *                       &lt;/simpleType>
     *                     &lt;/element>
     *                     &lt;element name="pid">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                             &lt;choice>
     *                               &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
     *                               &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
     *                             &lt;/choice>
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="searchPersonRequest" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="searchPersonRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
     *                     &lt;element name="algorithm" type="{http://www.ech.ch/xmlns/eCH-0214/2}algorithmType" minOccurs="0"/>
     *                     &lt;element name="searchedPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personToUPIType"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="compareDataRequest" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="compareDataRequestId" type="{http://www.ech.ch/xmlns/eCH-0214/2}subrequestIdType"/>
     *                     &lt;element name="pids">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                             &lt;sequence>
     *                               &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
     *                               &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
     *                             &lt;/sequence>
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
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
        "responseLanguage",
        "getInfoPersonRequest",
        "searchPersonRequest",
        "compareDataRequest"
    })
    public static class Content {

        @XmlElement(name = "SPIDCategory", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String spidCategory;
        @XmlElement(required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String responseLanguage;
        protected List<Request.Content.GetInfoPersonRequest> getInfoPersonRequest;
        protected List<Request.Content.SearchPersonRequest> searchPersonRequest;
        protected List<Request.Content.CompareDataRequest> compareDataRequest;

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
         * Gets the value of the responseLanguage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResponseLanguage() {
            return responseLanguage;
        }

        /**
         * Sets the value of the responseLanguage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResponseLanguage(String value) {
            this.responseLanguage = value;
        }

        /**
         * Gets the value of the getInfoPersonRequest property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the getInfoPersonRequest property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGetInfoPersonRequest().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Request.Content.GetInfoPersonRequest }
         * 
         * 
         */
        public List<Request.Content.GetInfoPersonRequest> getGetInfoPersonRequest() {
            if (getInfoPersonRequest == null) {
                getInfoPersonRequest = new ArrayList<Request.Content.GetInfoPersonRequest>();
            }
            return this.getInfoPersonRequest;
        }

        /**
         * Gets the value of the searchPersonRequest property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the searchPersonRequest property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSearchPersonRequest().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Request.Content.SearchPersonRequest }
         * 
         * 
         */
        public List<Request.Content.SearchPersonRequest> getSearchPersonRequest() {
            if (searchPersonRequest == null) {
                searchPersonRequest = new ArrayList<Request.Content.SearchPersonRequest>();
            }
            return this.searchPersonRequest;
        }

        /**
         * Gets the value of the compareDataRequest property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the compareDataRequest property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCompareDataRequest().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Request.Content.CompareDataRequest }
         * 
         * 
         */
        public List<Request.Content.CompareDataRequest> getCompareDataRequest() {
            if (compareDataRequest == null) {
                compareDataRequest = new ArrayList<Request.Content.CompareDataRequest>();
            }
            return this.compareDataRequest;
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
         *         &lt;element name="pids">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
         *                   &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
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
            "compareDataRequestId",
            "pids"
        })
        public static class CompareDataRequest {

            @XmlElement(required = true)
            protected BigInteger compareDataRequestId;
            @XmlElement(required = true)
            protected Request.Content.CompareDataRequest.Pids pids;

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
             * Gets the value of the pids property.
             * 
             * @return
             *     possible object is
             *     {@link Request.Content.CompareDataRequest.Pids }
             *     
             */
            public Request.Content.CompareDataRequest.Pids getPids() {
                return pids;
            }

            /**
             * Sets the value of the pids property.
             * 
             * @param value
             *     allowed object is
             *     {@link Request.Content.CompareDataRequest.Pids }
             *     
             */
            public void setPids(Request.Content.CompareDataRequest.Pids value) {
                this.pids = value;
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
            public static class Pids {

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
         *         &lt;element name="detailLevelOfResponse">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
         *               &lt;minLength value="1"/>
         *               &lt;maxLength value="20"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="pid">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;choice>
         *                   &lt;element name="vn" type="{http://www.ech.ch/xmlns/eCH-0044/4}vnType"/>
         *                   &lt;element name="SPID" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}SPIDType"/>
         *                 &lt;/choice>
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
            "getInfoPersonRequestId",
            "detailLevelOfResponse",
            "pid"
        })
        public static class GetInfoPersonRequest {

            @XmlElement(required = true)
            protected BigInteger getInfoPersonRequestId;
            @XmlElement(required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            protected String detailLevelOfResponse;
            @XmlElement(required = true)
            protected Request.Content.GetInfoPersonRequest.Pid pid;

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
             * Gets the value of the detailLevelOfResponse property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDetailLevelOfResponse() {
                return detailLevelOfResponse;
            }

            /**
             * Sets the value of the detailLevelOfResponse property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDetailLevelOfResponse(String value) {
                this.detailLevelOfResponse = value;
            }

            /**
             * Gets the value of the pid property.
             * 
             * @return
             *     possible object is
             *     {@link Request.Content.GetInfoPersonRequest.Pid }
             *     
             */
            public Request.Content.GetInfoPersonRequest.Pid getPid() {
                return pid;
            }

            /**
             * Sets the value of the pid property.
             * 
             * @param value
             *     allowed object is
             *     {@link Request.Content.GetInfoPersonRequest.Pid }
             *     
             */
            public void setPid(Request.Content.GetInfoPersonRequest.Pid value) {
                this.pid = value;
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
            public static class Pid {

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
         *         &lt;element name="algorithm" type="{http://www.ech.ch/xmlns/eCH-0214/2}algorithmType" minOccurs="0"/>
         *         &lt;element name="searchedPerson" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personToUPIType"/>
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
            "searchedPerson"
        })
        public static class SearchPersonRequest {

            @XmlElement(required = true)
            protected BigInteger searchPersonRequestId;
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "token")
            protected String algorithm;
            @XmlElement(required = true)
            protected PersonToUPIType searchedPerson;

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
             * Gets the value of the searchedPerson property.
             * 
             * @return
             *     possible object is
             *     {@link PersonToUPIType }
             *     
             */
            public PersonToUPIType getSearchedPerson() {
                return searchedPerson;
            }

            /**
             * Sets the value of the searchedPerson property.
             * 
             * @param value
             *     allowed object is
             *     {@link PersonToUPIType }
             *     
             */
            public void setSearchedPerson(PersonToUPIType value) {
                this.searchedPerson = value;
            }

        }

    }

}
