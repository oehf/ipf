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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55.asyncresponse;

import java.util.Map;

import javax.xml.ws.handler.MessageContext;

import org.apache.camel.ExchangePattern;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3CorrelationUtils;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.asyncresponse.Iti55AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.async.AsynchronousItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xcpd.iti55.TtlHeaderUtils;

/**
 * Service implementation for the IHE ITI-55 (XCPD) async response.
 * @author Dmytro Rud
 */
public class Iti55AsyncResponseService extends DefaultItiWebService implements Iti55AsyncResponsePortType {

    @Override
    public Object respondingGatewayPRPAIN201305UV02(String response) {
        AsynchronousItiEndpoint endpoint = (AsynchronousItiEndpoint) getConsumer().getEndpoint();
        
        String messageId = Hl7v3CorrelationUtils.getHl7v3MessageId(response, false);
        endpoint.getCorrelator().getServiceEndpoint(messageId);
        
        MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
        Map<String, Object> headers = TtlHeaderUtils.retrieveTtlHeaderAsMap(messageContext);
        process(response, headers, ExchangePattern.InOnly);
        return null;
    }
}
