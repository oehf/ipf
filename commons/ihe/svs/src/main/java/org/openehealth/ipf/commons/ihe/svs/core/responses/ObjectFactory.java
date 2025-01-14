/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.core.responses;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openehealth.ipf.commons.ihe.hpd.stub.chpidd package.
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

    private final static QName _Concept_QNAME = new QName("urn:ihe:iti:svs:2008", "Concept");
    private final static QName _ConceptList_QNAME = new QName("urn:ihe:iti:svs:2008", "ConceptList");
    private final static QName _RetrieveValueSetResponse_QNAME = new QName("urn:ihe:iti:svs:2008", "RetrieveValueSetResponse");
    private final static QName _ValueSetResponse_QNAME = new QName("urn:ihe:iti:svs:2008", "ValueSetResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.svs.core.responses
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Concept }
     *
     */
    public Concept createConcept() {
        return new Concept();
    }

    /**
     * Create an instance of {@link ConceptList }
     *
     */
    public ConceptList createConceptList() {
        return new ConceptList();
    }

    /**
     * Create an instance of {@link RetrieveValueSetResponse }
     *
     */
    public RetrieveValueSetResponse createRetrieveValueSetResponse() {
        return new RetrieveValueSetResponse();
    }

    /**
     * Create an instance of {@link ValueSetResponse }
     *
     */
    public ValueSetResponse createValueSetResponse() {
        return new ValueSetResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Concept }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:svs:2008", name = "Concept")
    public JAXBElement<Concept> createConcept(Concept value) {
        return new JAXBElement<>(_Concept_QNAME, Concept.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptList }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:svs:2008", name = "ConceptList")
    public JAXBElement<ConceptList> createConceptList(ConceptList value) {
        return new JAXBElement<>(_ConceptList_QNAME, ConceptList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveValueSetResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:svs:2008", name = "RetrieveValueSetResponse")
    public JAXBElement<RetrieveValueSetResponse> createRetrieveValueSetResponse(RetrieveValueSetResponse value) {
        return new JAXBElement<>(_RetrieveValueSetResponse_QNAME, RetrieveValueSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueSetResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:svs:2008", name = "ValueSetResponse")
    public JAXBElement<ValueSetResponse> createValueSetResponse(ValueSetResponse value) {
        return new JAXBElement<>(_ValueSetResponse_QNAME, ValueSetResponse.class, null, value);
    }

}
