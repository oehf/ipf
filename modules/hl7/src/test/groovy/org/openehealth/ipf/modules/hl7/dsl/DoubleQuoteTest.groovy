/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.dsl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.*
import static org.junit.jupiter.api.Assertions.*

/**
 * @author Martin Krasser
 */
class DoubleQuoteTest extends groovy.test.GroovyAssert {
    
    def streetAddress
    def address
    def maidenName
    def msg

    @BeforeEach
    void setUp() {
        msg = load('dsl/msg-07.hl7')
        maidenName = msg.PID[6]
        streetAddress = msg.PID[11](0)[1]
        address = msg.PID[11]
    }

    @Test
    void testGet() {
        // property access on target
        assertEquals '', address.value2
        assertEquals '""', address.value
        
        assertEquals '', streetAddress.value2
        assertEquals '""', streetAddress.value
        
        assertEquals '', streetAddress[1].value2
        assertEquals '""', streetAddress[1].value
        
        assertTrue address.isNullValue()
        assertTrue streetAddress.isNullValue()
        assertTrue streetAddress[1].isNullValue()
    }

    @Test
    void testCopy() {
        def newStreetAddress = streetAddress
        assertEquals '""', streetAddress.value
        
    }


    
}