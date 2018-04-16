
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
import javax.xml.bind.annotation.*;

import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.RequestAbstractType;


/**
 * <p>Java class for XACMLPolicyQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XACMLPolicyQueryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:protocol}RequestAbstractType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Request"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySetIdReference"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicyIdReference"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XACMLPolicyQueryType", propOrder = {
    "requestOrPolicySetIdReferenceOrPolicyIdReference"
})
@XmlRootElement(name = "XACMLPolicyQuery")
public class XACMLPolicyQueryType
    extends RequestAbstractType
{

    @XmlElementRefs({
        @XmlElementRef(name = "PolicyIdReference", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Request", namespace = "urn:oasis:names:tc:xacml:2.0:context:schema:os", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "PolicySetIdReference", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> requestOrPolicySetIdReferenceOrPolicyIdReference;

    /**
     * Gets the value of the requestOrPolicySetIdReferenceOrPolicyIdReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requestOrPolicySetIdReferenceOrPolicyIdReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequestOrPolicySetIdReferenceOrPolicyIdReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link IdReferenceType }{@code >}
     * {@link JAXBElement }{@code <}{@link RequestType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdReferenceType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRequestOrPolicySetIdReferenceOrPolicyIdReference() {
        if (requestOrPolicySetIdReferenceOrPolicyIdReference == null) {
            requestOrPolicySetIdReferenceOrPolicyIdReference = new ArrayList<JAXBElement<?>>();
        }
        return this.requestOrPolicySetIdReferenceOrPolicyIdReference;
    }

}
