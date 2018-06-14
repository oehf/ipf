
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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.EncryptedElementType;


/**
 * <p>Java class for ResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:protocol}StatusResponseType"&gt;
 *       &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Assertion"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedAssertion"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseType", propOrder = {
    "assertionOrEncryptedAssertion"
})
@XmlRootElement(name = "Response")
public class ResponseType
    extends StatusResponseType
{

    @XmlElements({
        @XmlElement(name = "Assertion", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", type = AssertionType.class),
        @XmlElement(name = "EncryptedAssertion", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", type = EncryptedElementType.class)
    })
    protected List<Object> assertionOrEncryptedAssertion;

    /**
     * Gets the value of the assertionOrEncryptedAssertion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assertionOrEncryptedAssertion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssertionOrEncryptedAssertion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssertionType }
     * {@link EncryptedElementType }
     * 
     * 
     */
    public List<Object> getAssertionOrEncryptedAssertion() {
        if (assertionOrEncryptedAssertion == null) {
            assertionOrEncryptedAssertion = new ArrayList<Object>();
        }
        return this.assertionOrEncryptedAssertion;
    }

}
