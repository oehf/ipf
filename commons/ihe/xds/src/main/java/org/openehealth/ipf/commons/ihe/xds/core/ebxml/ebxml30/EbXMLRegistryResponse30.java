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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The ebXML 3.0 version of the {@link EbXMLRegistryResponse}.
 * @author Jens Riemschneider
 */
public class EbXMLRegistryResponse30 implements EbXMLRegistryResponse<RegistryResponseType> {
    private final RegistryResponseType regResponse;

    /**
     * Constructs the registry response by wrapping the given ebXML 3.0 object.
     * @param regResponse
     *          the object to wrap.
     */
    public EbXMLRegistryResponse30(RegistryResponseType regResponse) {
        this.regResponse = requireNonNull(regResponse, "regResponse cannot be null");
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
    public List<EbXMLRegistryError> getErrors() {
        var list = getInternal().getRegistryErrorList();
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
        getInternal().setRegistryErrorList(value);
        var list = value.getRegistryError();
        list.addAll(errors.stream()
                .map(error -> ((EbXMLRegistryError30) error).getInternal())
                .collect(Collectors.toList()));
    }
}
