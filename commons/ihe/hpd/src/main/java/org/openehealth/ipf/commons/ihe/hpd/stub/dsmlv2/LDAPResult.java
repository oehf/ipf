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

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for LDAPResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LDAPResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage">
 *       &lt;sequence>
 *         &lt;element name="resultCode" type="{urn:oasis:names:tc:DSML:2:0:core}ResultCode"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referral" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="matchedDN" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlDN" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LDAPResult", propOrder = {
    "resultCode",
    "errorMessage",
    "referral"
})
@XmlSeeAlso({
    ExtendedResponse.class
})
public class LDAPResult
    extends DsmlMessage implements BatchResponseElement
{

    // this attribute is required only in JSON serialization to hold the actual element name
    @XmlTransient
    @JsonProperty("@e")
    @Getter @Setter
    private String elementName;

    @XmlElement(required = true)
    protected ResultCode resultCode;
    protected String errorMessage;
    @XmlSchemaType(name = "anyURI")
    protected List<String> referral;
    @XmlAttribute(name = "matchedDN")
    protected String matchedDN;

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link ResultCode }
     *     
     */
    public ResultCode getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultCode }
     *     
     */
    public void setResultCode(ResultCode value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the referral property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referral property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferral().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReferral() {
        if (referral == null) {
            referral = new ArrayList<>();
        }
        return this.referral;
    }

    /**
     * Gets the value of the matchedDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchedDN() {
        return matchedDN;
    }

    /**
     * Sets the value of the matchedDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchedDN(String value) {
        this.matchedDN = value;
    }

}
