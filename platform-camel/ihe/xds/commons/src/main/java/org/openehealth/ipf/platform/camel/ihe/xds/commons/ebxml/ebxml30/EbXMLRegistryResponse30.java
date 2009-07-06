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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorCode;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Severity;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryError;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryErrorList;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs.RegistryResponseType;

/**
 * The ebXML 3.0 version of the {@link EbXMLRegistryResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLRegistryResponse30 implements EbXMLRegistryResponse {
    private final RegistryResponseType regResponse;
    
    /**
     * Constructs the registry response by wrapping the given ebXML 3.0 object.
     * @param regResponse
     *          the object to wrap.
     */
    public EbXMLRegistryResponse30(RegistryResponseType regResponse) {
        notNull(regResponse, "regResponse cannot be null");
        this.regResponse = regResponse;
    }

    @Override
    public void setStatus(Status status) {
        regResponse.setStatus(Status.getOpcode30(status));
    }
    
    @Override
    public Status getStatus() {
        return Status.valueOfOpcode(regResponse.getStatus());
    }
    
    @Override
    public RegistryResponseType getInternal() {
        return regResponse;
    }

    @Override
    public List<ErrorInfo> getErrors() {
        RegistryErrorList list = regResponse.getRegistryErrorList();
        if (list == null) {
            return Collections.emptyList();
        }
        
        List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
        for (RegistryError regError : list.getRegistryError()) {
            ErrorInfo error = new ErrorInfo();
            error.setCodeContext(regError.getCodeContext());
            error.setLocation(regError.getLocation());
            error.setErrorCode(ErrorCode.valueOfOpcode(regError.getErrorCode()));
            error.setServerity(Severity.valueOfOpcode30(regError.getSeverity()));
            errors.add(error);
        }
        
        return errors;
    }

    @Override
    public void setErrors(List<ErrorInfo> errors) {
        RegistryErrorList value = EbXMLFactory30.RS_FACTORY.createRegistryErrorList();
        regResponse.setRegistryErrorList(value);
        List<RegistryError> list = value.getRegistryError();
        for (ErrorInfo error : errors) {
            RegistryError regError = EbXMLFactory30.RS_FACTORY.createRegistryError();
            regError.setErrorCode(ErrorCode.getOpcode(error.getErrorCode()));
            regError.setCodeContext(error.getCodeContext());
            regError.setSeverity(Severity.getOpcode30(error.getServerity()));
            regError.setLocation(error.getLocation());
            list.add(regError);
        }
    }
}
