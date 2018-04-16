
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.StatementAbstractType;


/**
 * <p>Java class for XACMLPolicyStatementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XACMLPolicyStatementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:assertion}StatementAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Policy"/&gt;
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySet"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:assertion}ReferencedPolicies" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XACMLPolicyStatementType", propOrder = {
    "policyOrPolicySet",
    "referencedPolicies"
})
public class XACMLPolicyStatementType
    extends StatementAbstractType
{

    @XmlElements({
        @XmlElement(name = "Policy", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = PolicyType.class),
        @XmlElement(name = "PolicySet", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = PolicySetType.class)
    })
    protected List<Object> policyOrPolicySet;
    @XmlElement(name = "ReferencedPolicies")
    protected ReferencedPoliciesType referencedPolicies;

    /**
     * Gets the value of the policyOrPolicySet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policyOrPolicySet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicyOrPolicySet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicyType }
     * {@link PolicySetType }
     * 
     * 
     */
    public List<Object> getPolicyOrPolicySet() {
        if (policyOrPolicySet == null) {
            policyOrPolicySet = new ArrayList<Object>();
        }
        return this.policyOrPolicySet;
    }

    /**
     * Gets the value of the referencedPolicies property.
     * 
     * @return
     *     possible object is
     *     {@link ReferencedPoliciesType }
     *     
     */
    public ReferencedPoliciesType getReferencedPolicies() {
        return referencedPolicies;
    }

    /**
     * Sets the value of the referencedPolicies property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferencedPoliciesType }
     *     
     */
    public void setReferencedPolicies(ReferencedPoliciesType value) {
        this.referencedPolicies = value;
    }

}
