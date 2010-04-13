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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti56;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.async.AsynchronousItiEndpoint;
import static org.openehealth.ipf.platform.camel.ihe.xcpd.XcpdTestUtils.*

/**
 * Test routes for ITI-56.
 * @author Dmytro Rud
 */
class Iti56TestRouteBuilder extends SpringRouteBuilder {

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static final String RESPONSE = readFile('iti56/iti56-sample-response.xml') 

    static final long ASYNC_DELAY = 10 * 1000L
    
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcpd-iti56-async-response:iti56service-response?correlator=#correlator')
            .validate().iti56Response()
            .process {
                assert it.pattern == ExchangePattern.InOnly
                assert it.in.headers[AsynchronousItiEndpoint.CORRELATION_KEY_HEADER_NAME] == 
                    "corr ${asyncResponseCount.getAndIncrement() * 2}"
            }
            .delay(ASYNC_DELAY)


        // responding route
        from('xcpd-iti56:iti56service')
            .validate().iti56Request()
            .process {
                Exchanges.resultMessage(it).body = RESPONSE
                responseCount.incrementAndGet()
            }
            .validate().iti56Response()
    }

}
