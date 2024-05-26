/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openehealth.ipf.commons.ihe.hpd.stub.json.JaxbElementListSerializer;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BatchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BatchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:oasis:names:tc:DSML:2:0:core}BatchResponses" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="requestID" type="{urn:oasis:names:tc:DSML:2:0:core}RequestID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchResponse", propOrder = {
    "batchResponses"
})
@XmlRootElement
public class BatchResponse {

    @XmlElementRefs({
        @XmlElementRef(name = "extendedResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "modDNResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "searchResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "compareResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "errorResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "delResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "modifyResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "authResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "addResponse", namespace = "urn:oasis:names:tc:DSML:2:0:core", type = JAXBElement.class, required = false)
    })
    @JsonSerialize(using = JaxbElementListSerializer.class)
    protected List<JAXBElement<?>> batchResponses;
    @XmlAttribute(name = "requestID")
    protected String requestID;

    /**
     * Gets the value of the batchResponses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the batchResponses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchResponses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ExtendedResponse }{@code >}
     * {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}
     * {@link JAXBElement }{@code <}{@link SearchResponse }{@code >}
     * {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}
     * {@link JAXBElement }{@code <}{@link ErrorResponse }{@code >}
     * {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}
     * {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}
     * {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}
     * {@link JAXBElement }{@code <}{@link LDAPResult }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getBatchResponses() {
        if (batchResponses == null) {
            batchResponses = new ArrayList<>();
        }
        return this.batchResponses;
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
