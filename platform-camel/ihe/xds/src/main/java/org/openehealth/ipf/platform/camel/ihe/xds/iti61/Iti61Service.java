/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti61;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML30Converters;

/**
 * Service implementation for the IHE ITI-61 transaction (Register On-Demand Document Entry).
 */
public class Iti61Service extends AbstractWebService implements Iti61PortType {

    @Override
    public RegistryResponseType documentRegistryRegisterOnDemandDocumentEntry(SubmitObjectsRequest body) {
        Exchange result = process(body, XdsJaxbDataBinding.getMap(body), ExchangePattern.InOut);
        if (result.getException() != null) {
            Response errorResponse = new Response(result.getException(), ErrorCode.REGISTRY_METADATA_ERROR, ErrorCode.REGISTRY_ERROR, null);
            return EbXML30Converters.convert(errorResponse);
        }
        
        return Exchanges.resultMessage(result).getBody(RegistryResponseType.class);            
    }
}
