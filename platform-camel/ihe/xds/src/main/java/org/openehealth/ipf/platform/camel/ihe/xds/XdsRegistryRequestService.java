/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.xds;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.InvalidPayloadException;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

/**
 * Base class for XDS Registry Request services
 *
 * @since 3.1
 */
@Slf4j
public abstract class XdsRegistryRequestService<T> extends AbstractWebService {

    @SneakyThrows(InvalidPayloadException.class)
    protected RegistryResponseType processRequest(T body) {
        var result = process(body, XdsJaxbDataBinding.getCamelHeaders(body), ExchangePattern.InOut);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug("{} service failed", getClass().getSimpleName(), exception);
            var errorResponse = new Response(
                    exception,
                    getDefaultMetadataError(),
                    ErrorCode.REGISTRY_ERROR, null);
            return EbXML30Converters.convert(errorResponse);
        }

        return result.getMessage().getMandatoryBody(RegistryResponseType.class);
    }

    /**
     * Define the ErrorCode used to indicate a error in the metadata (typically a client fault).
     * IHE profiles do not have one common error code to indicate the error.
     *
     * If the default {@link ErrorCode} can not be used, override this method to use a different code.
     *
     * @return {@link ErrorCode#REGISTRY_METADATA_ERROR}.
     */
    protected ErrorCode getDefaultMetadataError() {
        return ErrorCode.REGISTRY_METADATA_ERROR;
    }
}
