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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ProvideAndRegisterDocumentSetRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProvideAndRegisterDocumentSetRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0}SubmitObjectsRequest"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="Document" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>base64Binary">
 *                   &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
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
@XmlRootElement(name = "ProvideAndRegisterDocumentSetRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvideAndRegisterDocumentSetRequestType", propOrder = {
    "submitObjectsRequest",
    "document"
})
public class ProvideAndRegisterDocumentSetRequestType {

    @XmlElement(name = "SubmitObjectsRequest", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0", required = true)
    private SubmitObjectsRequest submitObjectsRequest;
    @XmlElement(name = "Document")
    private List<ProvideAndRegisterDocumentSetRequestType.Document> document;

    /**
     * Gets the value of the submitObjectsRequest property.
     * 
     * @return
     *     possible object is
     *     {@link SubmitObjectsRequest }
     *     
     */
    public SubmitObjectsRequest getSubmitObjectsRequest() {
        return submitObjectsRequest;
    }

    /**
     * Sets the value of the submitObjectsRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubmitObjectsRequest }
     *     
     */
    public void setSubmitObjectsRequest(SubmitObjectsRequest value) {
        submitObjectsRequest = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the document property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProvideAndRegisterDocumentSetRequestType.Document }
     * 
     *
     * @return the list of documents.
     */
    public List<ProvideAndRegisterDocumentSetRequestType.Document> getDocument() {
        if (document == null) {
            document = new ArrayList<ProvideAndRegisterDocumentSetRequestType.Document>();
        }
        return document;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>base64Binary">
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Document {

        @XmlValue
        @XmlMimeType("application/octet-stream")
        private DataHandler value;
        @XmlAttribute(required = true)
        @XmlSchemaType(name = "anyURI")
        private String id;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link DataHandler }
         *     
         */
        public DataHandler getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataHandler }
         *     
         */
        public void setValue(DataHandler value) {
            this.value = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            id = value;
        }

    }

}
