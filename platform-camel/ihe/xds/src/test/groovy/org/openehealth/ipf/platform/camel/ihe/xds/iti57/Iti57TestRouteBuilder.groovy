/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti57

import org.apache.camel.builder.RouteBuilder
import org.apache.cxf.headers.Header
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint

import javax.xml.namespace.QName

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti57RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti57ResponseValidator

/**
 * @author Boris Stanojevic
 */
public class Iti57TestRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti57:xds-iti57-service1')
            .process(iti57RequestValidator())
            .process { checkValue(it, 'service 1')}
            .process(iti57ResponseValidator())

        from('xds-iti57:xds-iti57-service2')
            .process { checkValue(it, 'service 2')}

        // for testing SOAP headers
        from('xds-iti57:xds-iti57-service3')
            .process {
                Map<QName, Header> soapHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_SOAP_HEADERS]
                boolean correct = true
                correct = correct && (soapHeaders != null)

                Header header1 = soapHeaders[new QName("http://acme.org", "MyHeader1")]
                correct = correct && (header1 != null)
                correct = correct && (header1.object.firstChild.data == "header 1 contents")

                Header header2 = soapHeaders[new QName("http://openehealth.org", "MyHeader2")]
                correct = correct && (header2 != null)
                correct = correct && (header2.object.firstChild.data == "header 2 contents")

                it.out.body = new Response(correct ? SUCCESS : FAILURE)
            }
    }

    void checkValue(exchange, expected) {
        def value = exchange.in.getBody(RegisterDocumentSet.class).documentEntries[0].comments.value        
        def response = new Response(expected == value ? SUCCESS : FAILURE)
        exchange.message.body = response
    }
}
