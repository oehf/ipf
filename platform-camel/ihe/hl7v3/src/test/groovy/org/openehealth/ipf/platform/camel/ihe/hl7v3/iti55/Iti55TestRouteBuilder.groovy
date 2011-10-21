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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55;

import java.util.concurrent.atomic.AtomicInteger
import javax.xml.datatype.Duration
import org.apache.camel.ExchangePattern
import org.apache.camel.Message
import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti55RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti55ResponseValidator

/**
 * Test routes for ITI-55.
 * @author Dmytro Rud
 */
class Iti55TestRouteBuilder extends SpringRouteBuilder {
    private static final transient LOG = LogFactory.getLog(Iti55TestRouteBuilder.class)

    static final AtomicInteger responseCount         = new AtomicInteger()
    static final AtomicInteger asyncResponseCount    = new AtomicInteger()
    static final AtomicInteger deferredResponseCount = new AtomicInteger()

    static final String RESPONSE = StandardTestContainer.readFile('iti55/iti55-sample-response.xml')

    static final long ASYNC_DELAY = 10 * 1000L

    static boolean errorOccurred = false

    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcpd-iti55-async-response:iti55service-async-response?correlator=#correlator')
            .process(iti55ResponseValidator())
            .process {
                try {
                    def inHttpHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
                    assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')

                    assert it.pattern == ExchangePattern.InOnly
                    assert it.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] == "corr ${asyncResponseCount.getAndIncrement() * 3}"
                    XcpdTestUtils.testPositiveAckCode(it.in.body)
                } catch (Exception e) {
                    errorOccurred = true
                    LOG.error(e)
                }
            }
            .delay(ASYNC_DELAY)


        // receiver of deferred responses
        from('xcpd-iti55-deferred-response:iti55service-deferred-response?correlator=#correlator')
            .process(iti55ResponseValidator())
            .process {
                try {
                    def inHttpHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
                    //assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')

                    assert it.pattern == ExchangePattern.InOnly
                    assert it.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] == "corr ${2 + deferredResponseCount.getAndIncrement() * 3}"
                    XcpdTestUtils.testPositiveAckCode(it.in.body)
                } catch (Exception e) {
                    errorOccurred = true
                    LOG.error(e)
                }
            }
            .delay(ASYNC_DELAY)


        // responding route
        from('xcpd-iti55:iti55service?rejectionHandlingStrategy=#rejectionHandlingStrategy')
            .process(iti55RequestValidator())
            .process {
                // check incoming SOAP and HTTP headers
                Duration dura = TtlHeaderUtils.getTtl(it.in)
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
                if (dura) {
                    XcpdTestUtils.setTtl(message, dura.years * 2)
                }
                message.headers[AbstractWsEndpoint.OUTGOING_HTTP_HEADERS] =
                    ['MyResponseHeader' : ('Re: ' + inHttpHeaders['MyRequestHeader'])]
                
                responseCount.incrementAndGet()
            }
            .process(iti55ResponseValidator())


        // generates NAK
        from('xcpd-iti55:iti55service2')
            .process {
                throw new RuntimeException('NAK')
            }
        
    }

}
