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
package org.openehealth.ipf.commons.ihe.hpd.stub.chpidd;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.AuthRequest;

import java.util.Objects;


/**
 * <p>Java class for DownloadRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DownloadRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="authRequest" type="{urn:oasis:names:tc:DSML:2:0:core}AuthRequest" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="fromDate" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}dateTime"&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="toDate"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}dateTime"&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="filterMyTransactions" default="true"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}boolean"&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;xsd:attribute name="pageNumber" use="optional" default="1"&gt;
 *         &lt;xsd:simpleType&gt;
 *           &lt;xsd:restriction base="xsd:unsignedInt"/&gt;
 *         &lt;/xsd:simpleType&gt;
 *       &lt;/xsd:attribute&gt;
 *       &lt;xsd:attribute name="pageSize" use="optional" default="1000"&gt;
 *         &lt;xsd:simpleType&gt;
 *           &lt;xsd:restriction base="xsd:unsignedInt"&gt;
 *             &lt;xsd:maxInclusive value="5000"/&gt;
 *           &lt;/xsd:restriction&gt;
 *         &lt;/xsd:simpleType&gt;
 *       &lt;/xsd:attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DownloadRequest", propOrder = {
    "authRequest"
})
@XmlRootElement(name = "downloadRequest")
public class DownloadRequest {

    protected AuthRequest authRequest;
    @XmlAttribute(name = "requestID")
    protected String requestID;
    @XmlAttribute(name = "fromDate", required = true)
    protected XMLGregorianCalendar fromDate;
    @XmlAttribute(name = "toDate")
    protected XMLGregorianCalendar toDate;
    @XmlAttribute(name = "filterMyTransactions")
    protected Boolean filterMyTransactions;

    @XmlAttribute(name = "pageNumber")
    @Getter @Setter protected int pageNumber;
    @XmlAttribute(name = "pageSize")
    @Getter @Setter protected int pageSize;

    /**
     * Gets the value of the authRequest property.
     * 
     * @return
     *     possible object is
     *     {@link AuthRequest }
     *     
     */
    public AuthRequest getAuthRequest() {
        return authRequest;
    }

    /**
     * Sets the value of the authRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthRequest }
     *     
     */
    public void setAuthRequest(AuthRequest value) {
        this.authRequest = value;
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

    /**
     * Gets the value of the fromDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFromDate() {
        return fromDate;
    }

    /**
     * Sets the value of the fromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFromDate(XMLGregorianCalendar value) {
        this.fromDate = value;
    }

    /**
     * Gets the value of the toDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setToDate(XMLGregorianCalendar value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the filterMyTransactions property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFilterMyTransactions() {
        return Objects.requireNonNullElse(filterMyTransactions, true);
    }

    /**
     * Sets the value of the filterMyTransactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFilterMyTransactions(Boolean value) {
        this.filterMyTransactions = value;
    }

}
