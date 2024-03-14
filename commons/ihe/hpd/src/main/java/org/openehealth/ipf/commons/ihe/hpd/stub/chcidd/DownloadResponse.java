/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.stub.chcidd;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.*;

import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;


/**
 * <p>Java class for DownloadResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DownloadResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:DSML:2:0:core}batchRequest" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DownloadResponse", propOrder = {
    "batchRequest"
})
@XmlRootElement(name = "downloadResponse")
public class DownloadResponse {

    @XmlElement(namespace = "urn:oasis:names:tc:DSML:2:0:core")
    protected List<BatchRequest> batchRequest;
    @XmlAttribute(name = "requestID")
    protected String requestID;

    /**
     * Gets the value of the batchRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the batchRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BatchRequest }
     * 
     * 
     */
    public List<BatchRequest> getBatchRequest() {
        if (batchRequest == null) {
            batchRequest = new ArrayList<>();
        }
        return this.batchRequest;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

}
