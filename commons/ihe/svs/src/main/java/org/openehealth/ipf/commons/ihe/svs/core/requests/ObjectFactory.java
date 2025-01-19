/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.requests;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openehealth.ipf.commons.ihe.svs.core.requests package.
 * <p>An ObjectFactory allows you to programmatically
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

    private final static QName _RetrieveValueSetRequest_QNAME = new QName("urn:ihe:iti:svs:2008", "RetrieveValueSetRequest");
    private final static QName _ValueSetRequest_QNAME = new QName("urn:ihe:iti:svs:2008", "ValueSetRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.svs.core.requests
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RetrieveValueSetRequest }
     *
     */
    public RetrieveValueSetRequest createRetrieveValueSetRequest() {
        return new RetrieveValueSetRequest();
    }

    /**
     * Create an instance of {@link ValueSetRequest }
     *
     */
    public ValueSetRequest createValueSetRequest() {
        return new ValueSetRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveValueSetRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:svs:2008", name = "downloadRequest")
    public JAXBElement<RetrieveValueSetRequest> createRetrieveValueSetRequest(RetrieveValueSetRequest value) {
        return new JAXBElement<>(_RetrieveValueSetRequest_QNAME, RetrieveValueSetRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSetRequest }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:svs:2008", name = "downloadResponse")
    public JAXBElement<ValueSetRequest> createValueSetRequest(ValueSetRequest value) {
        return new JAXBElement<>(_ValueSetRequest_QNAME, ValueSetRequest.class, null, value);
    }

}
