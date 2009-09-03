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

package org.openehealth.ipf.platform.camel.ihe.xds.iti42.service;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.Iti42PortType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.core.DefaultItiWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

/**
 * Service implementation for the IHE ITI-42 transaction (Register Document Set).
 * <p>
 * This implementation delegates to a Camel consumer by creating an exchange.
 *
 * @author Jens Riemschneider
 */
public class Iti42Service extends DefaultItiWebService implements Iti42PortType {
    public RegistryResponseType documentRegistryRegisterDocumentSetB(SubmitObjectsRequest body) {
        Exchange result = process(body);
        if (result.getException() != null) {
            Response errorResponse = new Response();
            configureError(errorResponse, result.getException(), ErrorCode.REGISTRY_METADATA_ERROR, ErrorCode.REGISTRY_ERROR);
            RegistryResponseType ebXML = EbXML30Converters.convert(errorResponse);
            return ebXML;
        }
        
        return Exchanges.resultMessage(result).getBody(RegistryResponseType.class);            
    }
}
