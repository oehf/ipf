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

import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

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
        Validate.notNull(factory, "ebXML factory must be not null");
        this.factory = factory;
    }


    public List<EbXMLRegistryError> toEbXML(List<ErrorInfo> errorInfoList) {
        notNull(errorInfoList, "error info list cannot be null");

        List<EbXMLRegistryError> result = new ArrayList<EbXMLRegistryError>();
        for (ErrorInfo errorInfo : errorInfoList) {
            result.add(toEbXML(errorInfo));
        }
        return result;
    }


    public List<ErrorInfo> fromEbXML(List<EbXMLRegistryError> registryErrorList) {
        notNull(registryErrorList, "registry error list cannot be null");

        List<ErrorInfo> result = new ArrayList<ErrorInfo>();
        for (EbXMLRegistryError registryError : registryErrorList) {
            result.add(fromEbXML(registryError));
        }
        return result;
    }


    public EbXMLRegistryError toEbXML(ErrorInfo errorInfo) {
        notNull(errorInfo, "error info cannot be null");

        EbXMLRegistryError regError = factory.createRegistryError();

        regError.setErrorCode((errorInfo.getErrorCode() == ErrorCode._USER_DEFINED)
                ? errorInfo.getCustomErrorCode()
                : ErrorCode.getOpcode(errorInfo.getErrorCode()));

        regError.setCodeContext(errorInfo.getCodeContext());
        regError.setSeverity(errorInfo.getSeverity());
        regError.setLocation(errorInfo.getLocation());
        return regError;
    }


    public ErrorInfo fromEbXML(EbXMLRegistryError ebXML) {
        ErrorInfo errorInfo = new ErrorInfo();

        ErrorCode errorCode = ErrorCode.valueOfOpcode(ebXML.getErrorCode());
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
