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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55

import groovy.util.slurpersupport.GPathResult
import javax.xml.ws.BindingProvider
import org.apache.camel.Exchange
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55PortType
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator
import org.openehealth.ipf.commons.xml.XmlUtils
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer

/**
 * Camel producer for the ITI-55 XCPD transaction
 * with support of the Deferred Response option.
 * @author Dmytro Rud
 */
class Iti55Producer extends DefaultItiProducer {
    private static final transient Log LOG = LogFactory.getLog(Iti55Producer.class)
    private static final String PROCESSING_MODE_PROPERTY = Iti55Producer.class.name + '.MODE'


    Iti55Producer(DefaultItiEndpoint endpoint, JaxWsClientFactory clientFactory) {
        super(endpoint, clientFactory, Object.class)
    }


    @Override
    protected void enrichRequestExchange(Exchange exchange, Map<String, Object> requestContext) {
        String encoding = exchange.getProperty(Exchange.CHARSET_NAME, String.class)
        String requestString = XmlUtils.toString(exchange.in.body, encoding)
        GPathResult requestXml = Hl7v3Utils.slurp(requestString)
        String responsePriorityCode = requestXml.controlActProcess.queryByParameter.responsePriorityCode.@code.text()

        if (responsePriorityCode == 'D') {
            if (! requestXml.respondTo[0].telecom.@value.text()) {
                throw new RuntimeException('missing deferred response URI in the request')
            }
            requestContext[AsynchronyCorrelator.FORCE_CORRELATION] = Boolean.TRUE
        }
        requestContext[PROCESSING_MODE_PROPERTY] = responsePriorityCode
    }


    @Override
    protected Object callService(Object client, Object request) {
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.requestContext

        return (requestContext[PROCESSING_MODE_PROPERTY] == 'D') ?
            ((Iti55PortType) client).discoverPatientsDeferred(request) :
            ((Iti55PortType) client).discoverPatients(request)
    }

}
