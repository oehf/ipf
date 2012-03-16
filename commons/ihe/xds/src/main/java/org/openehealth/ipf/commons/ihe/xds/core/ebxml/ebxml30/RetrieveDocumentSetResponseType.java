/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RetrieveDocumentSetResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetrieveDocumentSetResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0}RegistryResponse"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="DocumentResponse" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="NewRepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="NewDocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="mimeType" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "RetrieveDocumentSetResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveDocumentSetResponseType", propOrder = {
    "registryResponse",
    "documentResponse"
})
public class RetrieveDocumentSetResponseType {

    @XmlElement(name = "RegistryResponse", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", required = true)
    private RegistryResponseType registryResponse;
    @XmlElement(name = "DocumentResponse")
    private List<RetrieveDocumentSetResponseType.DocumentResponse> documentResponse;

    /**
     * Gets the value of the registryResponse property.
     * 
     * @return
     *     possible object is
     *     {@link RegistryResponseType }
     *     
     */
    public RegistryResponseType getRegistryResponse() {
        return registryResponse;
    }

    /**
     * Sets the value of the registryResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistryResponseType }
     *     
     */
    public void setRegistryResponse(RegistryResponseType value) {
        registryResponse = value;
    }

    /**
     * Gets the value of the documentResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RetrieveDocumentSetResponseType.DocumentResponse }
     * 
     *
     * @return the document response.
     */
    public List<RetrieveDocumentSetResponseType.DocumentResponse> getDocumentResponse() {
        if (documentResponse == null) {
            documentResponse = new ArrayList<RetrieveDocumentSetResponseType.DocumentResponse>();
        }
        return documentResponse;
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
     *         &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="mimeType" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
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
        "homeCommunityId",
        "repositoryUniqueId",
        "documentUniqueId",
        "newRepositoryUniqueId",
        "newDocumentUniqueId",
        "mimeType",
        "document"
    })
    public static class DocumentResponse {

        @XmlElement(name = "HomeCommunityId")
        private String homeCommunityId;
        @XmlElement(name = "RepositoryUniqueId", required = true)
        private String repositoryUniqueId;
        @XmlElement(name = "DocumentUniqueId", required = true)
        private String documentUniqueId;
        @XmlElement(name = "NewRepositoryUniqueId")
        private String newRepositoryUniqueId;
        @XmlElement(name = "NewDocumentUniqueId")
        private String newDocumentUniqueId;
        @XmlElement(required = true)
        private String mimeType;
        @XmlElement(name = "Document", required = true)
        @XmlMimeType("application/octet-stream")
        private DataHandler document;

        /**
         * Gets the value of the homeCommunityId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHomeCommunityId() {
            return homeCommunityId;
        }

        /**
         * Sets the value of the homeCommunityId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHomeCommunityId(String value) {
            homeCommunityId = value;
        }

        /**
         * Gets the value of the repositoryUniqueId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRepositoryUniqueId() {
            return repositoryUniqueId;
        }

        /**
         * Sets the value of the repositoryUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRepositoryUniqueId(String value) {
            repositoryUniqueId = value;
        }

        /**
         * Gets the value of the documentUniqueId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocumentUniqueId() {
            return documentUniqueId;
        }

        /**
         * Sets the value of the documentUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentUniqueId(String value) {
            documentUniqueId = value;
        }

        public String getNewRepositoryUniqueId() {
            return newRepositoryUniqueId;
        }

        public void setNewRepositoryUniqueId(String newRepositoryUniqueId) {
            this.newRepositoryUniqueId = newRepositoryUniqueId;
        }

        public String getNewDocumentUniqueId() {
            return newDocumentUniqueId;
        }

        public void setNewDocumentUniqueId(String newDocumentUniqueId) {
            this.newDocumentUniqueId = newDocumentUniqueId;
        }

        /**
         * Gets the value of the mimeType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMimeType() {
            return mimeType;
        }

        /**
         * Sets the value of the mimeType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMimeType(String value) {
            mimeType = value;
        }

        /**
         * Gets the value of the document property.
         * 
         * @return
         *     possible object is
         *     {@link DataHandler }
         *     
         */
        public DataHandler getDocument() {
            return document;
        }

        /**
         * Sets the value of the document property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataHandler }
         *     
         */
        public void setDocument(DataHandler value) {
            document = value;
        }

    }

}
