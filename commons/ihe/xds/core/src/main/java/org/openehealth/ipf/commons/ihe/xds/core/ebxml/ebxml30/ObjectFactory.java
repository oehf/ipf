/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.platform.camel.xds.commons.stub.iti41 package. 
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

    private final static QName _ProvideAndRegisterDocumentSetRequest_QNAME = new QName("urn:ihe:iti:xds-b:2007", "ProvideAndRegisterDocumentSetRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.platform.camel.xds.commons.stub.iti41
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetRequestType }
     * @return the new instance.
     */
    public ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterDocumentSetRequestType() {
        return new ProvideAndRegisterDocumentSetRequestType();
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetRequestType.Document }
     * @return the new instance.
     */
    public ProvideAndRegisterDocumentSetRequestType.Document createProvideAndRegisterDocumentSetRequestTypeDocument() {
        return new ProvideAndRegisterDocumentSetRequestType.Document();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvideAndRegisterDocumentSetRequestType }{@code >}}
     * @param value
     *          a request type.
     * @return the new instance.
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:xds-b:2007", name = "ProvideAndRegisterDocumentSetRequest")
    public JAXBElement<ProvideAndRegisterDocumentSetRequestType> createProvideAndRegisterDocumentSetRequest(ProvideAndRegisterDocumentSetRequestType value) {
        return new JAXBElement<ProvideAndRegisterDocumentSetRequestType>(_ProvideAndRegisterDocumentSetRequest_QNAME, ProvideAndRegisterDocumentSetRequestType.class, null, value);
    }
}
