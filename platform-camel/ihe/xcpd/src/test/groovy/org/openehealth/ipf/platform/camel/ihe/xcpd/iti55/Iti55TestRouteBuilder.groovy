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
import static org.openehealth.ipf.platform.camel.ihe.xcpd.iti55.Iti55TestUtils.*

/**
 * Test routes for ITI-55.
 * @author Dmytro Rud
 */
class Iti55TestRouteBuilder extends SpringRouteBuilder {

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static final String RESPONSE = readFile('iti55-sample-response.xml') 

    static final long ASYNC_DELAY = 10 * 1000L
    
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcpd-iti55-async-response:iti55service-response?correlator=#correlator')
            .validate().iti55Response()
            .process {
                assert it.pattern == ExchangePattern.InOnly
                testPositiveAckCode(it.in.body)
                asyncResponseCount.incrementAndGet()
            }
            .delay(ASYNC_DELAY)


        // responding route
        from('xcpd-iti55:iti55service')
            .validate().iti55Request()
            .process {
                def dura = it.in.headers[Iti55Component.XCPD_INPUT_TTL_HEADER_NAME]
                assert dura instanceof Duration

                Message message = Exchanges.resultMessage(it)
                setOutgoingTTL(message, dura.years * 2)
                message.body = RESPONSE
                
                responseCount.incrementAndGet()
            }
            .validate().iti55Response()
    }

}
