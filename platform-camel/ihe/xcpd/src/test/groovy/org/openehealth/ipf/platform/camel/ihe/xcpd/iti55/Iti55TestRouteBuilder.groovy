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

import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.datatype.Duration;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xcpd.XcpdTestUtils;

/**
 * Test routes for ITI-55.
 * @author Dmytro Rud
 */
class Iti55TestRouteBuilder extends SpringRouteBuilder {

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static final String RESPONSE = XcpdTestUtils.readFile('iti55/iti55-sample-response.xml') 

    static final long ASYNC_DELAY = 10 * 1000L
    
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcpd-iti55-async-response:iti55service-response?correlator=#correlator')
            .validate().iti55Response()
            .process {
                def inHttpHeaders = it.in.headers[DefaultItiEndpoint.INCOMING_HTTP_HEADERS]
                assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')
                
                assert it.pattern == ExchangePattern.InOnly
                assert it.in.headers[DefaultItiEndpoint.CORRELATION_KEY_HEADER_NAME] == 
                    "corr ${asyncResponseCount.getAndIncrement() * 2}"
                XcpdTestUtils.testPositiveAckCode(it.in.body)
            }
            .delay(ASYNC_DELAY)


        // responding route
        from('xcpd-iti55:iti55service')
            .validate().iti55Request()
            .process {
                // check incoming SOAP and HTTP headers
                Duration dura = TtlHeaderUtils.getTtl(it.in)
                def inHttpHeaders = it.in.headers[DefaultItiEndpoint.INCOMING_HTTP_HEADERS]
                assert inHttpHeaders['MyRequestHeader'].startsWith('Number')

                // create response, inclusive SOAP and HTTP headers
                Message message = Exchanges.resultMessage(it)
                message.body = RESPONSE
                if (dura) {
                    XcpdTestUtils.setTtl(message, dura.years * 2)
                }
                message.headers[DefaultItiEndpoint.OUTGOING_HTTP_HEADERS] = 
                    ['MyResponseHeader' : ('Re: ' + inHttpHeaders['MyRequestHeader'])]
                
                responseCount.incrementAndGet()
            }
            .validate().iti55Response()


        // generates NAK
        from('xcpd-iti55:iti55service2')
            .process {
                throw new RuntimeException('NAK')
            }
        
    }

}
