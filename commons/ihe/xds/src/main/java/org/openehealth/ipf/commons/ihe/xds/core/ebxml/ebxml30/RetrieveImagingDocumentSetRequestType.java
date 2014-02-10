//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.09 at 11:50:13 AM PDT 
//
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 * <p>Java class for RetrieveImagingDocumentSetRequestType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RetrieveImagingDocumentSetRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudyRequest" type="tns:StudyRequest" maxOccurs="unbounded"/>
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="studyInstanceUID" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                   &lt;element name="SeriesRequest type="tns:SeriesRequest" maxOccurs="unbounded"/>
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="seriesInstanceUID" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                             &lt;element name="DocumentRequest" type="tns:DocumentRequest" maxOccurs="unbounded"/>
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                                       &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                                       &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *
 *        &lt;xs:element name="TransferSyntaxUIDList">
 *          &lt;xs:complexType>
 *            &lt;xs:sequence>
 *              &lt;xs:element name="TransferSyntaxUID" type="rim:LongName" maxOccurs="unbounded">
 *                &lt;xs:annotation>
 *                   &lt;xs:documentation>This is the list of DICOM transfer styntax UIDs to be used when requesting retrieval of DICOM images</xs:documentation>
 *                &lt;/xs:annotation>
 *              &lt;/xs:element>
 *            &lt;/xs:sequence>
 *          &lt;/xs:complexType>
 *        &lt;/xs:element>
 *
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 */
@XmlRootElement(name = "RetrieveImagingDocumentSetRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveImagingDocumentSetRequestType", propOrder = {
    "studyRequest", "transferSyntaxUIDList"
})
public class RetrieveImagingDocumentSetRequestType {

    @XmlElement(name = "StudyRequest", required = true, namespace = "urn:ihe:rad:xdsi-b:2009")
    protected List<StudyRequest> studyRequest;

    @XmlElement(name = "TransferSyntaxUIDList", required = true, namespace = "urn:ihe:rad:xdsi-b:2009")
    protected List<TransferSyntaxUIDList> transferSyntaxUIDList;

    /**
     * Gets the value of the studyRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studyRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudyRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StudyRequest }
     * 
     *
     * @return the study request.
     */
    public List<RetrieveImagingDocumentSetRequestType.StudyRequest> getStudyRequest() {
        if (studyRequest == null) {
            studyRequest = new ArrayList<StudyRequest>();
        }
        return this.studyRequest;
    }

    /**
     * Gets the value of the transferSyntaxUIDList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studyRequest property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransferSyntaxUIDList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransferSyntaxUIDList }
     *
     *
     * @return the study request.
     */
    public List<RetrieveImagingDocumentSetRequestType.TransferSyntaxUIDList> getTransferSyntaxUIDList() {
        if (transferSyntaxUIDList == null) {
            transferSyntaxUIDList = new ArrayList<TransferSyntaxUIDList>();
        }
        return this.transferSyntaxUIDList;
    }

    /**
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="studyInstanceUID" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="StudyRequest" type="tns:StudyRequest" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"studyInstanceUID", "seriesRequest"})
    public static class StudyRequest {

        @XmlAttribute(name = "studyInstanceUID", required = true)
        protected String studyInstanceUID;

        @XmlElement(name = "SeriesRequest", required = true, namespace = "urn:ihe:rad:xdsi-b:2009")
        protected List<SeriesRequest> seriesRequest;

        /**
         * Gets the value of the studyInstanceUID property.
         *
         * @return studyInstanceUID     {@link String }
         *
         */
        public String getStudyInstanceUID() {
            return studyInstanceUID;
        }

        /**
         * @param studyInstanceUID
         *          the unique ID of the Study instance.
         */
        public void setStudyInstanceUID(String studyInstanceUID) {
            this.studyInstanceUID = studyInstanceUID;
        }

        /**
         * Gets the value of the seriesRequest property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the seriesRequest property.
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSeriesRequests().add(newItem);
         * </pre>
         * <p>
         * Objects of the following type(s) are allowed in the list {@link SeriesRequest }
         *
         */
        public List<SeriesRequest> getSeriesRequest() {
            if (seriesRequest == null) {
                seriesRequest = new ArrayList<SeriesRequest>();
            }
            return this.seriesRequest;
        }

    }

    /**
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;xs:element name="TransferSyntaxUIDList">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element name="TransferSyntaxUID" type="rim:LongName" maxOccurs="unbounded">
     *         &lt;xs:annotation>
     *           &lt;xs:documentation>This is the list of DICOM transfer styntax UIDs to be used when requesting retrieval of DICOM images</xs:documentation>
     *         &lt;/xs:annotation>
     *       &lt;/xs:element>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"transferSyntaxUID"})
    public static class TransferSyntaxUIDList {

        @XmlElement(name = "TransferSyntaxUID", required = true, namespace="urn:ihe:rad:xdsi-b:2009")
        protected List<String> transferSyntaxUID;

        /**
         * Gets the value of the seriesRequest property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the seriesRequest property.
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTransferSyntaxUID().add(newItem);
         * </pre>
         * <p>
         * Objects of the following type(s) are allowed in the list {@link TransferSyntaxUIDList }
         *
         */
        public List<String> getTransferSyntaxUID() {
            if (transferSyntaxUID == null) {
                transferSyntaxUID = new ArrayList<String>();
            }
            return this.transferSyntaxUID;
        }

    }

    /**
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="seriesInstanceUID" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="SeriesRequest" type="tns:SeriesRequest" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"seriesInstanceUID", "documentRequest"})
    public static class SeriesRequest {

        @XmlAttribute(name = "seriesInstanceUID", required = true)
        protected String seriesInstanceUID;

        @XmlElement(name = "DocumentRequest", required = true)
        private List<RetrieveDocumentSetRequestType.DocumentRequest> documentRequest;

        /**
         * Gets the value of the seriesInstanceUID property.
         *
         * @return seriesInstanceUID      {@link String }
         *
         */
        public String getSeriesInstanceUID() {
            return seriesInstanceUID;
        }

        /**
         * @param seriesInstanceUID
         *          the unique ID of the series instance.
         */
        public void setSeriesInstanceUID(String seriesInstanceUID) {
            this.seriesInstanceUID = seriesInstanceUID;
        }

        /**
         * Gets the value of the documentRequest property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the documentRequest property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDocumentRequest().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RetrieveDocumentSetRequestType.DocumentRequest }
         *
         *
         * @return the document request.
         */
        public List<RetrieveDocumentSetRequestType.DocumentRequest> getDocumentRequests() {
            if (documentRequest == null) {
                documentRequest = new ArrayList<RetrieveDocumentSetRequestType.DocumentRequest>();
            }
            return documentRequest;
        }
    }

}
