/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.svs.iti48;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapFault;
import org.openehealth.ipf.commons.ihe.svs.core.SvsException;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * Service implementation for the IHE ITI-48 transaction (Retrieve Value Set).
 * <p>
 * This implementation delegates to a Camel consumer by creating an exchange.
 *
 * @author Quentin Ligier
 */
@Slf4j
public class Iti48Service extends AbstractWebService implements Iti48PortType {

    @Override
    public RetrieveValueSetResponse valueSetRepositoryRetrieveValueSet(final RetrieveValueSetRequest body) {
        final Exchange result = this.process(body);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug("Iti-48 service failed", exception);
            if (exception instanceof SoapFault) {
                // Pass it through
                throw (SoapFault) exception;
            } else if (exception instanceof SvsException) {
                // Most probably thrown from the validator
                throw new SoapFault(exception.getMessage(), Soap12.getInstance().getSender());
            } else {
                // Wrap it in a SoapFault
                throw new SoapFault("An error occurred", Soap12.getInstance().getSender());
            }
        }
        return result.getMessage().getBody(RetrieveValueSetResponse.class);
    }
}