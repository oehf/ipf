
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.RequestAbstractType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.ReferencedPoliciesType;


/**
 * <p>Java class for XACMLAuthzDecisionQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XACMLAuthzDecisionQueryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:protocol}RequestAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Request"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Policy" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySet" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:assertion}ReferencedPolicies" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol}Extensions" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="InputContextOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="ReturnContext" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="CombinePolicies" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XACMLAuthzDecisionQueryType", propOrder = {
    "rest"
})
public class XACMLAuthzDecisionQueryType
    extends RequestAbstractType
{

    @XmlElementRefs({
        @XmlElementRef(name = "ReferencedPolicies", namespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:assertion", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Policy", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Extensions", namespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Request", namespace = "urn:oasis:names:tc:xacml:2.0:context:schema:os", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "PolicySet", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> rest;
    @XmlAttribute(name = "InputContextOnly")
    protected Boolean inputContextOnly;
    @XmlAttribute(name = "ReturnContext")
    protected Boolean returnContext;
    @XmlAttribute(name = "CombinePolicies")
    protected Boolean combinePolicies;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Extensions" is used by two different parts of a schema. See: 
     * line 46 of file:/C:/dev/ipf/commons/ihe/xacml20/impl/src/main/resources/wsdl/../schema/xacml-2.0-profile-saml2.0-v2-schema-protocol-wd-14.xsd
     * line 33 of file:/C:/dev/ipf/commons/ihe/xacml20/impl/src/main/resources/wsdl/../schema/sstc-saml-schema-protocol-2.0.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ReferencedPoliciesType }{@code >}
     * {@link JAXBElement }{@code <}{@link PolicyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExtensionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     * {@link JAXBElement }{@code <}{@link PolicySetType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<?>>();
        }
        return this.rest;
    }

    /**
     * Gets the value of the inputContextOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInputContextOnly() {
        if (inputContextOnly == null) {
            return false;
        } else {
            return inputContextOnly;
        }
    }

    /**
     * Sets the value of the inputContextOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInputContextOnly(Boolean value) {
        this.inputContextOnly = value;
    }

    /**
     * Gets the value of the returnContext property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isReturnContext() {
        if (returnContext == null) {
            return false;
        } else {
            return returnContext;
        }
    }

    /**
     * Sets the value of the returnContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnContext(Boolean value) {
        this.returnContext = value;
    }

    /**
     * Gets the value of the combinePolicies property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCombinePolicies() {
        if (combinePolicies == null) {
            return true;
        } else {
            return combinePolicies;
        }
    }

    /**
     * Sets the value of the combinePolicies property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCombinePolicies(Boolean value) {
        this.combinePolicies = value;
    }

}
