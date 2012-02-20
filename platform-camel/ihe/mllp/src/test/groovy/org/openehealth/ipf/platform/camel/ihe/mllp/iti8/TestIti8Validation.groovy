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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import static org.junit.Assert.*

import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

/**
 * Unit test for validation DSL extensions.
 * @author Dmytro Rud
 */
class TestIti8Validation extends MllpTestContainer {
    
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
    
    @Test
    void testUnknownSegments() {
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1')
        body = body + 'AAA|1|2|3\n'
        def msg = send(endpointUri, body)
        assertNAK(msg)
        assertTrue(msg.toString().contains('Unknown segment AAA'))
    }
    
    @Test
    void testMissingFields() {
        def endpointUri = 'pix-iti8://localhost:18080?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1', false)
        def msg = send(endpointUri, body)
        assertNAK(msg)
        assertTrue(msg.toString().contains('Missing patient ID'))
    }
    
    @Test
    void testHandledError() {
        def endpointUri = 'pix-iti8://localhost:18089?audit=false'
        def body = getMessageString('ADT^A01', '2.3.1', false)
        def msg = send(endpointUri, body)
        assertACK(msg)
    }
}