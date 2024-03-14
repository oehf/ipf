/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.chcidd;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.hpd.stub.chcidd package.
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

    private final static QName _DownloadResponse_QNAME = new QName("urn:ch:admin:bag:epr:2017", "downloadResponse");
    private final static QName _DownloadRequest_QNAME = new QName("urn:ch:admin:bag:epr:2017", "downloadRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.hpd.stub.chcidd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DownloadResponse }
     * 
     */
    public DownloadResponse createDownloadResponse() {
        return new DownloadResponse();
    }

    /**
     * Create an instance of {@link DownloadRequest }
     * 
     */
    public DownloadRequest createDownloadRequest() {
        return new DownloadRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ch:admin:bag:epr:2017", name = "downloadResponse")
    public JAXBElement<DownloadResponse> createDownloadResponse(DownloadResponse value) {
        return new JAXBElement<>(_DownloadResponse_QNAME, DownloadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ch:admin:bag:epr:2017", name = "downloadRequest")
    public JAXBElement<DownloadRequest> createDownloadRequest(DownloadRequest value) {
        return new JAXBElement<>(_DownloadRequest_QNAME, DownloadRequest.class, null, value);
    }

}
