
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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _XACMLAuthzDecisionQuery_QNAME = new QName("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", "XACMLAuthzDecisionQuery");
    private final static QName _Extensions_QNAME = new QName("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", "Extensions");
    private final static QName _XACMLPolicyQuery_QNAME = new QName("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", "XACMLPolicyQuery");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XACMLAuthzDecisionQueryType }
     * 
     */
    public XACMLAuthzDecisionQueryType createXACMLAuthzDecisionQueryType() {
        return new XACMLAuthzDecisionQueryType();
    }

    /**
     * Create an instance of {@link ExtensionsType }
     * 
     */
    public ExtensionsType createExtensionsType() {
        return new ExtensionsType();
    }

    /**
     * Create an instance of {@link XACMLPolicyQueryType }
     * 
     */
    public XACMLPolicyQueryType createXACMLPolicyQueryType() {
        return new XACMLPolicyQueryType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XACMLAuthzDecisionQueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", name = "XACMLAuthzDecisionQuery")
    public JAXBElement<XACMLAuthzDecisionQueryType> createXACMLAuthzDecisionQuery(XACMLAuthzDecisionQueryType value) {
        return new JAXBElement<XACMLAuthzDecisionQueryType>(_XACMLAuthzDecisionQuery_QNAME, XACMLAuthzDecisionQueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", name = "Extensions")
    public JAXBElement<ExtensionsType> createExtensions(ExtensionsType value) {
        return new JAXBElement<ExtensionsType>(_Extensions_QNAME, ExtensionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XACMLPolicyQueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", name = "XACMLPolicyQuery")
    public JAXBElement<XACMLPolicyQueryType> createXACMLPolicyQuery(XACMLPolicyQueryType value) {
        return new JAXBElement<XACMLPolicyQueryType>(_XACMLPolicyQuery_QNAME, XACMLPolicyQueryType.class, null, value);
    }

}
