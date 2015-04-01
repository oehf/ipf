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
import org.junit.Ignore
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*

import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

/**
 * Unit test for validation DSL extensions.
 * @author Dmytro Rud
 */
class TestIti8Validation extends MllpTestContainer {

    private static final Logger LOG = LoggerFactory.getLogger(TestIti8Validation)

    def static CONTEXT_DESCRIPTOR = 'iti8/iti-8-validation.xml'
    
    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeClass
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }
    
    @Test
    void testHappyCase() {
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1')
        def msg = send(endpointUri, body)
        assertACK(msg)
    }

    @Ignore
    void testConcurrentMessages() {
        File f = File.createTempFile("tmp", "tmp");
        Writer w = new BufferedWriter(new FileWriter(f));
        LOG.info("Wrinting into {}", f.absolutePath)
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
        w.flush();
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