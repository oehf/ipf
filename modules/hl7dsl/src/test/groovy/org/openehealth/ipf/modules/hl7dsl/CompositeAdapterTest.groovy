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
package org.openehealth.ipf.modules.hl7dsl

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.*
import ca.uhn.hl7v2.model.v22.message.ADT_A01

/**
 * @author Martin Krasser
 */
class CompositeAdapterTest extends GroovyTestCase {
    
    def composite
    MessageAdapter<ADT_A01> msg
    
    void setUp() {
        msg = load('msg-01.hl7')
        composite = msg.NK1(0)[4]
    }
    
    void testGet() {
        // property access on target
        assert composite.components.size() == 8
    }

    void testGetAt() {
        assert composite[4].value == 'NW'
    }

    void testGetEmptyAt() {
    	assert msg.PID[9](0)[1].value == null
    	assert msg.NK1(10)[4].value == null
    	assert msg.PV2[2][1].value == null
    }
    
    void testFrom() {
        def compositeCopy = composite.message.copy().NK1(0)[4]
        composite[4].value = 'XY'
        assert compositeCopy[4].value == 'NW'
        compositeCopy.from(composite)
        assert compositeCopy[4].value == 'XY'
    }

    void testFromFailure() {
        def compositeCopy = composite.message.copy().NK1(0)[4]
        try {
            compositeCopy.from(composite.message)
        } catch (AdapterException e) {
            // test passed
        }
    }

    void testSetAt() {
        // set via string
        composite[4] = 'NX'
        assert composite[4].value == 'NX'

        // set via primitive adapter
        composite[4] = composite[5]
        assert composite[4].value == '11000'
    }
    
}