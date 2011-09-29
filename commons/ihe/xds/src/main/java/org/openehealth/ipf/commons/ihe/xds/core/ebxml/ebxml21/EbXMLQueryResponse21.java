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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.RegistryObjectListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryErrorList;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;

/**
 * Encapsulation of {@link RegistryResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLQueryResponse21 extends EbXMLObjectContainer21 implements EbXMLQueryResponse {
    private final RegistryResponse regResponse;
    
    /**
     * Constructs the response wrapper.
     * @param regResponse
     *          the ebXML 2.1 response object to wrap. 
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLQueryResponse21(RegistryResponse regResponse, EbXMLObjectLibrary objectLibrary) {
        super(objectLibrary);
        notNull(regResponse, "regResponse cannot be null");
        this.regResponse = regResponse;
    }
    
    /**
     * Constructs the response wrapper using a new {@link EbXMLObjectLibrary}.
     * <p>
     * The object library is filled with the objects from the given response.
     * @param regResponse
     *          the ebXML 2.1 response object to wrap. 
     */
    public EbXMLQueryResponse21(RegistryResponse regResponse) {
        this(regResponse, new EbXMLObjectLibrary());
        fillObjectLibrary();        
    }

    @Override
    protected List<Object> getContents() {
        AdhocQueryResponse adhocQueryResponse = regResponse.getAdhocQueryResponse();
        if (adhocQueryResponse == null) {
            return Collections.emptyList();
        }
        RegistryObjectListType list = adhocQueryResponse.getSQLQueryResult();
        if (list == null) {
            return Collections.emptyList();
        }
        return list.getObjectRefOrAssociationOrAuditableEvent();
    }

    @Override
    public void setStatus(Status status) {
        regResponse.setStatus(Status.getOpcode21(status));
    }
    
    @Override
    public Status getStatus() {
        return Status.valueOfOpcode(regResponse.getStatus());
    }
    
    @Override
    public List<EbXMLRegistryError> getErrors() {
        RegistryErrorList list = regResponse.getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }
        
        List<EbXMLRegistryError> errors = new ArrayList<EbXMLRegistryError>();
        for (RegistryError regError : list.getRegistryError()) {
            errors.add(new EbXMLRegistryError21(regError));
        }
        
        return errors;
    }

    @Override
    public void setErrors(List<EbXMLRegistryError> errors) {
        RegistryErrorList value = EbXMLFactory21.RS_FACTORY.createRegistryErrorList();
        regResponse.setRegistryErrorList(value);
        List<RegistryError> list = value.getRegistryError();
        for (EbXMLRegistryError error : errors) {
            list.add(((EbXMLRegistryError21) error).getInternal());
        }
    }
    
    @Override
    public void addReference(ObjectReference ref) {
        if (ref != null) {
            ObjectRefType objectRef = EbXMLFactory21.RIM_FACTORY.createObjectRefType();
            objectRef.setId(ref.getId());
            // Home not supported in 2.1
            getContents().add(objectRef);
        }
    }
    
    @Override
    public List<ObjectReference> getReferences() {
        List<ObjectReference> results = new ArrayList<ObjectReference>();
        for (Object identifiable : getContents()) {
            ObjectRefType objRefEbXML = cast(identifiable, ObjectRefType.class);            
            if (objRefEbXML != null) {
                ObjectReference objRef = new ObjectReference();
                objRef.setId(objRefEbXML.getId());
                results.add(objRef);
            }
        }
        
        return results;
    }

    @Override
    public RegistryResponse getInternal() {
        return regResponse;
    }
}
