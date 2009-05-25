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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.platform.camel.xds.commons.stub.iti43 package. 
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

    private final static QName _RetrieveDocumentSetRequest_QNAME = new QName("urn:ihe:iti:xds-b:2007", "RetrieveDocumentSetRequest");
    private final static QName _RetrieveDocumentSetResponse_QNAME = new QName("urn:ihe:iti:xds-b:2007", "RetrieveDocumentSetResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.platform.camel.xds.commons.stub.iti43
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetResponseType }
     * @return the created instance.
     */
    public RetrieveDocumentSetResponseType createRetrieveDocumentSetResponseType() {
        return new RetrieveDocumentSetResponseType();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetResponseType.DocumentResponse }
     * @return the created instance.
     */
    public RetrieveDocumentSetResponseType.DocumentResponse createRetrieveDocumentSetResponseTypeDocumentResponse() {
        return new RetrieveDocumentSetResponseType.DocumentResponse();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetRequestType.DocumentRequest }
     * @return the created instance.
     */
    public RetrieveDocumentSetRequestType.DocumentRequest createRetrieveDocumentSetRequestTypeDocumentRequest() {
        return new RetrieveDocumentSetRequestType.DocumentRequest();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetRequestType }
     * @return the created instance.
     */
    public RetrieveDocumentSetRequestType createRetrieveDocumentSetRequestType() {
        return new RetrieveDocumentSetRequestType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveDocumentSetRequestType }{@code >}}
     * @param value
     *          the request type.
     * @return the created instance.
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:xds-b:2007", name = "RetrieveDocumentSetRequest")
    public JAXBElement<RetrieveDocumentSetRequestType> createRetrieveDocumentSetRequest(RetrieveDocumentSetRequestType value) {
        return new JAXBElement<RetrieveDocumentSetRequestType>(_RetrieveDocumentSetRequest_QNAME, RetrieveDocumentSetRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveDocumentSetResponseType }{@code >}}
     * @param value
     *          the response type.
     * @return the created instance.
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:xds-b:2007", name = "RetrieveDocumentSetResponse")
    public JAXBElement<RetrieveDocumentSetResponseType> createRetrieveDocumentSetResponse(RetrieveDocumentSetResponseType value) {
        return new JAXBElement<RetrieveDocumentSetResponseType>(_RetrieveDocumentSetResponse_QNAME, RetrieveDocumentSetResponseType.class, null, value);
    }

}
