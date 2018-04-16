
/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.EncryptedElementType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.NameIDType;


/**
 * <p>Java class for ManageNameIDRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManageNameIDRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:protocol}RequestAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedID"/&gt;
 *         &lt;/choice&gt;
 *         &lt;choice&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}NewID"/&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}NewEncryptedID"/&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}Terminate"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManageNameIDRequestType", propOrder = {
    "nameID",
    "encryptedID",
    "newID",
    "newEncryptedID",
    "terminate"
})
public class ManageNameIDRequestType
    extends RequestAbstractType
{

    @XmlElement(name = "NameID", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected NameIDType nameID;
    @XmlElement(name = "EncryptedID", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected EncryptedElementType encryptedID;
    @XmlElement(name = "NewID")
    protected String newID;
    @XmlElement(name = "NewEncryptedID")
    protected EncryptedElementType newEncryptedID;
    @XmlElement(name = "Terminate")
    protected TerminateType terminate;

    /**
     * Gets the value of the nameID property.
     * 
     * @return
     *     possible object is
     *     {@link NameIDType }
     *     
     */
    public NameIDType getNameID() {
        return nameID;
    }

    /**
     * Sets the value of the nameID property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIDType }
     *     
     */
    public void setNameID(NameIDType value) {
        this.nameID = value;
    }

    /**
     * Gets the value of the encryptedID property.
     * 
     * @return
     *     possible object is
     *     {@link EncryptedElementType }
     *     
     */
    public EncryptedElementType getEncryptedID() {
        return encryptedID;
    }

    /**
     * Sets the value of the encryptedID property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncryptedElementType }
     *     
     */
    public void setEncryptedID(EncryptedElementType value) {
        this.encryptedID = value;
    }

    /**
     * Gets the value of the newID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewID() {
        return newID;
    }

    /**
     * Sets the value of the newID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewID(String value) {
        this.newID = value;
    }

    /**
     * Gets the value of the newEncryptedID property.
     * 
     * @return
     *     possible object is
     *     {@link EncryptedElementType }
     *     
     */
    public EncryptedElementType getNewEncryptedID() {
        return newEncryptedID;
    }

    /**
     * Sets the value of the newEncryptedID property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncryptedElementType }
     *     
     */
    public void setNewEncryptedID(EncryptedElementType value) {
        this.newEncryptedID = value;
    }

    /**
     * Gets the value of the terminate property.
     * 
     * @return
     *     possible object is
     *     {@link TerminateType }
     *     
     */
    public TerminateType getTerminate() {
        return terminate;
    }

    /**
     * Sets the value of the terminate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminateType }
     *     
     */
    public void setTerminate(TerminateType value) {
        this.terminate = value;
    }

}
