/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import org.apache.camel.Exchange
import org.apache.camel.spi.Synchronization
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AbstractMllpTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.context.ContextConfiguration

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * Unit test for validation DSL extensions.
 * @author Dmytro Rud
 */
@ContextConfiguration('/iti8/iti-8-validation.xml')
class TestIti8Validation extends AbstractMllpTest {

    private static final Logger log = LoggerFactory.getLogger(TestIti8Validation)
    
    @Test
    void testHappyCase() {
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1')
        def msg = send(endpointUri, body)
        assertACK(msg)
    }

    @Disabled
    void testConcurrentMessages() {
        File f = File.createTempFile("tmp", "tmp")
        Writer w = new BufferedWriter(new FileWriter(f))
        log.info("Wrinting into {}", f.absolutePath)
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1')
        int n = 100000000
        CountDownLatch latch = new CountDownLatch(n)
        for (int i = 0; i < n; i++) {
            sendAsync(endpointUri, body, new Synchronization() {
                @Override
                void onComplete(Exchange exchange) {
                    latch.countDown()
                    long end = System.currentTimeMillis()
                    w.write(Long.toString(end - exchange.getProperty(Exchange.CREATED_TIMESTAMP, Long.class)) + "\n")
                }

                @Override
                void onFailure(Exchange exchange) {
                    latch.countDown()
                }
            })
        }
        latch.await(30, TimeUnit.MINUTES)
        w.flush()
    }
    
    @Test
    void testUnknownSegments() {
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1')
        body = body + 'AAA|1|2|3\n'
        def msg = send(endpointUri, body)
        assertNAK(msg)
        assertTrue(msg.toString().contains('The structure \'AAA\' appears in the message but not in the profile'))
    }
    
    @Test
    void testMissingFields() {
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1', false)
        def msg = send(endpointUri, body)
        assertNAK(msg)
        assertTrue(msg.toString().contains('PID must have at least  1 repetitions'))
    }
    
    @Test
    void testHandledError() {
        def endpointUri = 'pix-iti8://localhost:18089?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1', false)
        def msg = send(endpointUri, body)
        assertNAK(msg)
    }
}