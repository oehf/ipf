/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.responses;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;

/**
 * Transforms between lists of {@link ErrorInfo} objects and their ebXML representations.
 * @author Dmytro Rud
 */
public class ErrorInfoListTransformer {
    private final EbXMLFactory factory;

    /**
     * Constructs the transformer.
     * @param factory
     *          the factory for ebXML objects.
     */
    public ErrorInfoListTransformer(EbXMLFactory factory) {
        this.factory = requireNonNull(factory, "ebXML factory must be not null");
    }


    public List<EbXMLRegistryError> toEbXML(List<ErrorInfo> errorInfoList) {
        requireNonNull(errorInfoList, "error info list cannot be null");
        return errorInfoList.stream()
                .map(this::toEbXML)
                .collect(Collectors.toList());
    }


    public List<ErrorInfo> fromEbXML(List<EbXMLRegistryError> registryErrorList) {
        requireNonNull(registryErrorList, "registry error list cannot be null");

        return registryErrorList.stream()
                .map(this::fromEbXML)
                .collect(Collectors.toList());
    }


    public EbXMLRegistryError toEbXML(ErrorInfo errorInfo) {
        requireNonNull(errorInfo, "error info cannot be null");

        var regError = factory.createRegistryError();

        regError.setErrorCode((errorInfo.getErrorCode() == ErrorCode._USER_DEFINED)
                ? errorInfo.getCustomErrorCode()
                : ErrorCode.getOpcode(errorInfo.getErrorCode()));

        regError.setCodeContext(errorInfo.getCodeContext());
        regError.setSeverity(errorInfo.getSeverity());
        regError.setLocation(errorInfo.getLocation());
        return regError;
    }


    public ErrorInfo fromEbXML(EbXMLRegistryError ebXML) {
        var errorInfo = new ErrorInfo();

        var errorCode = ErrorCode.valueOfOpcode(ebXML.getErrorCode());
        errorInfo.setErrorCode(errorCode);
        if (errorCode == ErrorCode._USER_DEFINED) {
            errorInfo.setCustomErrorCode(ebXML.getErrorCode());
        }

        errorInfo.setCodeContext(ebXML.getCodeContext());
        errorInfo.setLocation(ebXML.getLocation());
        errorInfo.setSeverity(ebXML.getSeverity());
        return errorInfo;
    }

}
