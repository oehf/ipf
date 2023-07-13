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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SearchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="searchResultEntry" type="{urn:oasis:names:tc:DSML:2:0:core}SearchResultEntry" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="searchResultReference" type="{urn:oasis:names:tc:DSML:2:0:core}SearchResultReference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="searchResultDone" type="{urn:oasis:names:tc:DSML:2:0:core}LDAPResult"/>
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
@XmlType(name = "SearchResponse", propOrder = {
    "searchResultEntry",
    "searchResultReference",
    "searchResultDone"
})
public class SearchResponse implements BatchResponseElement {

    protected List<SearchResultEntry> searchResultEntry;
    protected List<SearchResultReference> searchResultReference;
    @XmlElement(required = true)
    protected LDAPResult searchResultDone;
    @XmlAttribute(name = "requestID")
    protected String requestID;

    /**
     * Gets the value of the searchResultEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchResultEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchResultEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResultEntry }
     * 
     * 
     */
    public List<SearchResultEntry> getSearchResultEntry() {
        if (searchResultEntry == null) {
            searchResultEntry = new ArrayList<>();
        }
        return this.searchResultEntry;
    }

    /**
     * Gets the value of the searchResultReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchResultReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchResultReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResultReference }
     * 
     * 
     */
    public List<SearchResultReference> getSearchResultReference() {
        if (searchResultReference == null) {
            searchResultReference = new ArrayList<>();
        }
        return this.searchResultReference;
    }

    /**
     * Gets the value of the searchResultDone property.
     * 
     * @return
     *     possible object is
     *     {@link LDAPResult }
     *     
     */
    public LDAPResult getSearchResultDone() {
        return searchResultDone;
    }

    /**
     * Sets the value of the searchResultDone property.
     * 
     * @param value
     *     allowed object is
     *     {@link LDAPResult }
     *     
     */
    public void setSearchResultDone(LDAPResult value) {
        this.searchResultDone = value;
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
