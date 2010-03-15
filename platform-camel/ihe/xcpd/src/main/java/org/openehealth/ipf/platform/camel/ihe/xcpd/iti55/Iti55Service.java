/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55;

import java.util.Map;

import javax.xml.datatype.Duration;
import javax.xml.ws.handler.MessageContext;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.Iti55PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * Service implementation for the IHE ITI-55 transaction (XCPD).
 * @author Dmytro Rud
 */
public class Iti55Service extends DefaultItiWebService implements Iti55PortType {

    @Override
    public String respondingGatewayPRPAIN201305UV02(String request) {
        MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
        Map<String, Object> headers = TtlHeaderUtils.retrieveTtlHeaderAsMap(messageContext);
        
        Exchange result = process(request, headers, ExchangePattern.InOut);
        if(result.getException() != null) {
            return Hl7v3NakFactory.createNak(request, result.getException(), "PRPA_IN201306UV02", true);
        }
        
        Message resultMessage = Exchanges.resultMessage(result);
        Duration dura = resultMessage.getHeader(Iti55Component.XCPD_OUTPUT_TTL_HEADER_NAME, Duration.class);
        TtlHeaderUtils.addTtlHeader(dura, messageContext);
        return resultMessage.getBody(String.class);
    }
 

}
