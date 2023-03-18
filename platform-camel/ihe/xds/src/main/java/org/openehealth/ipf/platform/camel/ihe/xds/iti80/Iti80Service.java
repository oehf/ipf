/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti80;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.InvalidPayloadException;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

@Slf4j
public class Iti80Service extends AbstractWebService implements Iti80PortType {
    @SneakyThrows(InvalidPayloadException.class)
    @Override
    public RegistryResponseType documentCrossGatewayDocumentProvide(ProvideAndRegisterDocumentSetRequestType body) {
        var result = process(body, XdsJaxbDataBinding.getCamelHeaders(body.getSubmitObjectsRequest()), ExchangePattern.InOut);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug("ITI-80 service failed", exception);
            var errorResponse = new Response(
                    exception,
                    ErrorCode.REPOSITORY_METADATA_ERROR,
                    ErrorCode.REPOSITORY_ERROR, null);
            return EbXML30Converters.convert(errorResponse);
        }
        
        return result.getMessage().getMandatoryBody(RegistryResponseType.class);
    }
}