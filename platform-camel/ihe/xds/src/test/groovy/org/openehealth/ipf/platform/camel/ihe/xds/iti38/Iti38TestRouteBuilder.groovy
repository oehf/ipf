/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti38;

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*

import java.util.concurrent.atomic.AtomicInteger

import org.apache.camel.ExchangePattern
import org.apache.camel.Message
import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.slf4j.LoggerFactory

/**
 * Test routes for ITI-38.
 * @author Dmytro Rud
 */
class Iti38TestRouteBuilder extends SpringRouteBuilder {
    private static final transient LOG = LoggerFactory.getLogger(Iti38TestRouteBuilder.class)

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static final QueryResponse RESPONSE = SampleData.createQueryResponseWithObjRef()

    static final long ASYNC_DELAY = 10 * 1000L

    static boolean errorOccurred = false
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xca-iti38-async-response:iti38service-response?correlator=#correlator')
            .process(iti38ResponseValidator())
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
        from('xca-iti38:iti38service')
            .process(iti38RequestValidator())
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
            .process(iti38ResponseValidator())

    }

}
