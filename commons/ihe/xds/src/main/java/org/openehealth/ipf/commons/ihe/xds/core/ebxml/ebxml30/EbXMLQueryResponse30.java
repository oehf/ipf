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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectRefType;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Encapsulation of {@link AdhocQueryResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLQueryResponse30 extends EbXMLObjectContainer30 implements EbXMLQueryResponse {
    private final AdhocQueryResponse response;

    /**
     * Constructs a query response by wrapping the given ebXML 3.0 object.
     * @param response
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLQueryResponse30(AdhocQueryResponse response, EbXMLObjectLibrary objectLibrary) {
        super(objectLibrary);
        requireNonNull(response, "response cannot be null");
        this.response = response;
    }
    
    /**
     * Constructs the response wrapper using a new {@link EbXMLObjectLibrary}.
     * @param response
     *          the object to wrap. 
     */
    public EbXMLQueryResponse30(AdhocQueryResponse response) {
        this(response, new EbXMLObjectLibrary());
        fillObjectLibrary();
    }

    @Override
    List<JAXBElement<? extends IdentifiableType>> getContents() {
        var list = response.getRegistryObjectList();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getIdentifiable();
    }

    @Override
    public Status getStatus() {
        return Status.valueOfOpcode(response.getStatus());
    }

    @Override
    public void setStatus(Status status) {
        response.setStatus(Status.getOpcode30(status));
    }

    @Override
    public List<EbXMLRegistryError> getErrors() {
        var list = response.getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }
        
        return list.getRegistryError().stream()
                .map(EbXMLRegistryError30::new)
                .collect(Collectors.toList());
    }

    @Override
    public void setErrors(List<EbXMLRegistryError> errors) {
        var value = EbXMLFactory30.RS_FACTORY.createRegistryErrorList();
        response.setRegistryErrorList(value);
        var list = value.getRegistryError();
        for (var error : errors) {
            var regError = ((EbXMLRegistryError30) error).getInternal();
            list.add(regError);
        }
    }

    @Override
    public void addReference(ObjectReference ref) {
        if (ref != null) {
            var objectRef = EbXMLFactory30.RIM_FACTORY.createObjectRefType();
            objectRef.setId(ref.getId());
            objectRef.setHome(ref.getHome());
            getContents().add(EbXMLFactory30.RIM_FACTORY.createObjectRef(objectRef));
        }
    }

    @Override
    public List<ObjectReference> getReferences() {
        var results = new ArrayList<ObjectReference>();
        for (var identifiable : getContents()) {
            var objRefEbXML = cast(identifiable, ObjectRefType.class);
            if (objRefEbXML != null) {
                var objRef = new ObjectReference();
                objRef.setId(objRefEbXML.getId());
                objRef.setHome(objRefEbXML.getHome());
                results.add(objRef);
            }
        }
        
        return results;
    }

    @Override
    public AdhocQueryResponse getInternal() {
        return response;
    }
}
