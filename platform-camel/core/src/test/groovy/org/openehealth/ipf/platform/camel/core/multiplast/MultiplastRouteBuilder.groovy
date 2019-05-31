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
package org.openehealth.ipf.platform.camel.core.multiplast

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.LinkedBlockingQueue
import java.security.AccessController
import javax.security.auth.SubjectDomainCombiner
import static org.junit.Assert.assertNotNull

class MultiplastRouteBuilder extends SpringRouteBuilder {

    void configure() throws Exception {

        ThreadPoolExecutor privilegedThreadPool = new PrivilegedTestExecutorService(3,
                3, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory())

        from('direct:start')
            .multiplast(
                this,
                body().tokenize(', '),
                header('recipients').tokenize(';'),
                new MultiplastAggregationStrategy(),
                privilegedThreadPool)

        from('mina2:tcp://localhost:10000?textline=true&sync=true')
            .process {
                assert it.in.body == 'abc'
                Exchanges.resultMessage(it).body = '123'
            }
            .delay(1000L)
        
        from('mina2:tcp://localhost:10001?textline=true&sync=true')
            .process {
                assert it.in.body == 'def'
                Exchanges.resultMessage(it).body = '456'
            }
            .delay(1000L)
        
        from('mina2:tcp://localhost:10002?textline=true&sync=true')
            .process {
                assert it.in.body == 'ghi'
                Exchanges.resultMessage(it).body = '789'
            }
            .delay(1000L)

        from('direct:abc')
            .process {
                assert it.in.body == 'ear'
                assertSubject()
                Exchanges.resultMessage(it).body = 'ijk'
            }
            .delay(1000L)

        from('direct:def')
            .process {
                assert it.in.body == 'war'
                assertSubject()
                Exchanges.resultMessage(it).body = 'lmn'
            }
            .delay(1000L)

        from('direct:ghi')
            .process {
                assert it.in.body == 'jar'
                assertSubject()
                Exchanges.resultMessage(it).body = 'opr'
            }
            .delay(1000L)
    }

    private void assertSubject(){
        SubjectDomainCombiner combiner = AccessController.getContext().domainCombiner
        assertNotNull combiner.getSubject()
        println "Subject: ${combiner.getSubject().toString()}"
    }

}
