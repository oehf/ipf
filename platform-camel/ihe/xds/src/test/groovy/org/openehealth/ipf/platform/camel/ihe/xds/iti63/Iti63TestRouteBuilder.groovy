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
package org.openehealth.ipf.platform.camel.ihe.xds.iti63;

import java.util.concurrent.atomic.AtomicInteger
import javax.activation.DataHandler
import org.apache.camel.ExchangePattern
import org.apache.camel.Message
import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti63RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti63ResponseValidator

/**
 * Test routes for ITI-63.
 * @author Dmytro Rud
 */
class Iti63TestRouteBuilder extends SpringRouteBuilder {
    private static final transient LOG = LogFactory.getLog(Iti63TestRouteBuilder.class)

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()

    static final QueryResponse RESPONSE = SampleData.createQueryResponseWithLeafClass()
    static {
        RESPONSE.status = Status.SUCCESS
        RESPONSE.documents << new Document(
                RESPONSE.documentEntries.get(0),
                new DataHandler('abcd ' * 1500, "text/plain"));
    }

    static final long ASYNC_DELAY = 10 * 1000L

    static boolean errorOccurred = false
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcf-iti63-async-response:iti63service-response?correlator=#correlator')
            .process(iti63ResponseValidator())
            .process {
                try {
                    def inHttpHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
                    assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')

                    assert it.pattern == ExchangePattern.InOnly
                    assert it.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] ==
                        "corr ${asyncResponseCount.getAndIncrement() * 2}"

                    assert it.in.getBody(QueryResponse.class).status == Status.SUCCESS
                } catch (Exception e) {
                    errorOccurred = true
                    LOG.error(e)
                }
            }
            .delay(ASYNC_DELAY)


        // responding route
        from('xcf-iti63:iti63service')
            .process(iti63RequestValidator())
            .process {
                // check incoming SOAP and HTTP headers
                def inHttpHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]

                try {
                    assert inHttpHeaders['MyRequestHeader'].startsWith('Number')
                } catch (Exception e) {
                    errorOccurred = true
                    LOG.error(e)
                }

                // create response, inclusive SOAP and HTTP headers
                Message message = Exchanges.resultMessage(it)
                message.body = RESPONSE
                message.headers[AbstractWsEndpoint.OUTGOING_HTTP_HEADERS] =
                    ['MyResponseHeader' : ('Re: ' + inHttpHeaders['MyRequestHeader'])]
                
                responseCount.incrementAndGet()
            }
            .process(iti63ResponseValidator())

    }

}
