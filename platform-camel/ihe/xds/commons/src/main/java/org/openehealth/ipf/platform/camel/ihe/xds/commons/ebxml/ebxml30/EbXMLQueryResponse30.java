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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.ObjectReference;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorCode;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Severity;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryError;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryErrorList;

/**
 * Encapsulation of {@link AdhocQueryResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLQueryResponse30 extends EbXMLBaseObjectContainer30 implements EbXMLQueryResponse {
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
        notNull(response, "response cannot be null");
        this.response = response;
    }
    
    /**
     * Constructs the response wrapper using a new {@link EbXMLObjectLibrary}.
     * @param response
     *          the object to wrap. 
     */
    public EbXMLQueryResponse30(AdhocQueryResponse response) {
        this(response, new EbXMLObjectLibrary());
    }

    @Override
    List<JAXBElement<? extends IdentifiableType>> getContents() {
        RegistryObjectListType list = response.getRegistryObjectList();
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
    public List<ErrorInfo> getErrors() {
        RegistryErrorList list = response.getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }
        
        List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
        for (RegistryError regError : list.getRegistryError()) {
            ErrorInfo error = new ErrorInfo();
            error.setCodeContext(regError.getCodeContext());
            error.setLocation(regError.getLocation());
            error.setErrorCode(ErrorCode.valueOfOpcode(regError.getErrorCode()));
            error.setSeverity(Severity.valueOfOpcode30(regError.getSeverity()));
            errors.add(error);
        }
        
        return errors;
    }

    @Override
    public void setErrors(List<ErrorInfo> errors) {
        RegistryErrorList value = EbXMLFactory30.RS_FACTORY.createRegistryErrorList();
        response.setRegistryErrorList(value);
        List<RegistryError> list = value.getRegistryError();
        for (ErrorInfo error : errors) {
            RegistryError regError = EbXMLFactory30.RS_FACTORY.createRegistryError();
            regError.setErrorCode(ErrorCode.getOpcode(error.getErrorCode()));
            regError.setCodeContext(error.getCodeContext());
            regError.setSeverity(Severity.getOpcode30(error.getSeverity()));
            regError.setLocation(error.getLocation());
            list.add(regError);
        }
    }

    @Override
    public void addReference(ObjectReference ref) {
        if (ref != null) {
            ObjectRefType objectRef = EbXMLFactory30.RIM_FACTORY.createObjectRefType();
            objectRef.setId(ref.getId());
            objectRef.setHome(ref.getHome());
            getContents().add(EbXMLFactory30.RIM_FACTORY.createObjectRef(objectRef));
        }
    }

    @Override
    public AdhocQueryResponse getInternal() {
        return response;
    }
}
