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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryErrorList;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;

/**
 * The ebXML 2.1 version of the {@link RegistryResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLRegistryResponse21 implements EbXMLRegistryResponse {
    private final RegistryResponse regResponse;
    
    /**
     * Constructs a response by wrapping the given ebXML object.
     * @param regResponse
     *          the object to wrap. 
     */
    public EbXMLRegistryResponse21(RegistryResponse regResponse) {
        notNull(regResponse, "regResponse cannot be null");
        this.regResponse = regResponse;
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
    public RegistryResponse getInternal() {
        return regResponse;
    }
}
