/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti86;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RemoveDocumentsRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti86.Iti86PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

/**
 * Service implementation for the IHE ITI-86 transaction (Remove Documents).
 *
 * @since 3.3
 */
@Slf4j
@AllArgsConstructor
public class Iti86Service extends AbstractWebService implements Iti86PortType {

    @Override
    public RegistryResponseType documentRepositoryRemoveDocuments(RemoveDocumentsRequestType body) {
        var result = process(body);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug("ITI-86 service failed", exception);
            var errorResponse = new Response(
                    exception,
                    ErrorCode.REMOVE_DOCUMENTS_ERROR,
                    ErrorCode.REPOSITORY_ERROR,
                    null);
            return EbXML30Converters.convert(errorResponse);
        }
        return result.getMessage().getBody(RegistryResponseType.class);
    }

}
