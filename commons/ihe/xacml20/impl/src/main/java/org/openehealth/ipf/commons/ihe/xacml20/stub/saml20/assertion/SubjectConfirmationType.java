
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubjectConfirmationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectConfirmationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}BaseID"/&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/&gt;
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedID"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectConfirmationData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Method" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectConfirmationType", propOrder = {
    "baseID",
    "nameID",
    "encryptedID",
    "subjectConfirmationData"
})
public class SubjectConfirmationType {

    @XmlElement(name = "BaseID")
    protected BaseIDAbstractType baseID;
    @XmlElement(name = "NameID")
    protected NameIDType nameID;
    @XmlElement(name = "EncryptedID")
    protected EncryptedElementType encryptedID;
    @XmlElement(name = "SubjectConfirmationData")
    protected SubjectConfirmationDataType subjectConfirmationData;
    @XmlAttribute(name = "Method", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String method;

    /**
     * Gets the value of the baseID property.
     * 
     * @return
     *     possible object is
     *     {@link BaseIDAbstractType }
     *     
     */
    public BaseIDAbstractType getBaseID() {
        return baseID;
    }

    /**
     * Sets the value of the baseID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseIDAbstractType }
     *     
     */
    public void setBaseID(BaseIDAbstractType value) {
        this.baseID = value;
    }

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
     * Gets the value of the subjectConfirmationData property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectConfirmationDataType }
     *     
     */
    public SubjectConfirmationDataType getSubjectConfirmationData() {
        return subjectConfirmationData;
    }

    /**
     * Sets the value of the subjectConfirmationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectConfirmationDataType }
     *     
     */
    public void setSubjectConfirmationData(SubjectConfirmationDataType value) {
        this.subjectConfirmationData = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

}
