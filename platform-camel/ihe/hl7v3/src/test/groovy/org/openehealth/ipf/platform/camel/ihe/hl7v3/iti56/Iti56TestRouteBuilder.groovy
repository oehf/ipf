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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti56

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.camel.ExchangePattern;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.*

/**
 * Test routes for ITI-56.
 * @author Dmytro Rud
 */
class Iti56TestRouteBuilder extends SpringRouteBuilder {

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static final String RESPONSE = StandardTestContainer.readFile('iti56/iti56-sample-response.xml')

    private final CountDownLatch countDownLatch, asyncCountDownLatch;

    static final int TASKS_COUNT = 5

    Iti56TestRouteBuilder(){
        countDownLatch      = new CountDownLatch(TASKS_COUNT)
        asyncCountDownLatch = new CountDownLatch(TASKS_COUNT)
    }
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcpd-iti56-async-response:iti56service-response' +
                '?correlator=#correlator' +
                '&inInterceptors=#inLogInterceptor' +
                '&outInterceptors=#outLogInterceptor')
            .process(iti56ResponseValidator())
            .process {
                if (! it.in.body.contains('<soap:Fault')) {
                    assert it.pattern == ExchangePattern.InOnly
                    assert it.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] ==
                        "corr ${asyncResponseCount.getAndIncrement() * 2}"
                }
                asyncCountDownLatch.countDown()
            }


        // responding route
        from('xcpd-iti56:iti56service' +
                '?inInterceptors=#inLogInterceptor' +
                '&outInterceptors=#outLogInterceptor')
            .process(iti56RequestValidator())
            .process {
                Exchanges.resultMessage(it).body = RESPONSE
                responseCount.incrementAndGet()
                countDownLatch.countDown()
            }
            .process(iti56ResponseValidator())


        // responding route for testing errors
        from('xcpd-iti56:iti56service-error' +
                '?inInterceptors=#inLogInterceptor' +
                '&outInterceptors=#outLogInterceptor')
            .throwException(new RuntimeException("abcd"))
    }

}
