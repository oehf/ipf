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
/**
 * @author Martin Krasser
 */
class GroupAdapterTest extends GroovyTestCase {
    
    def message
    
    void setUp() {
        message = load('msg-02.hl7')
    }
    
    void testInvokeMethod() {
        // invoke method on adapter
        assert message.count('MSH') == 1
        
        // invoke method on target
        assert message.isRequired('MSH')
    }
    
    void testGet() {
        // property access on target
        assert message.version == '2.4'
    }
    
    void testGetAt() {
        assert message.MSH instanceof SegmentAdapter
        assert message.PATIENT_RESULT instanceof Closure
        assert message.PATIENT_RESULT() instanceof List
        assert message.PATIENT_RESULT(0) instanceof GroupAdapter
        assert message.PATIENT_RESULT(0).PATIENT.PID instanceof SegmentAdapter

        // alternative notation 
        assert message['MSH'] instanceof SegmentAdapter
        assert message['PATIENT_RESULT'] instanceof Closure
        assert message['PATIENT_RESULT']() instanceof List
        assert message['PATIENT_RESULT'](0) instanceof GroupAdapter
        assert message['PATIENT_RESULT'](0)['PATIENT']['PID'] instanceof SegmentAdapter
    }
    
    void testCount() {
        assert message.PATIENT_RESULT(0).count('ORDER_OBSERVATION') == 2
        
        // alternative notation
        assert message['PATIENT_RESULT'](0).count('ORDER_OBSERVATION') == 2
    }
    
    void testGetTarget() {
        assert message.target instanceof ca.uhn.hl7v2.model.Message
    }
    
    void testNrp() {
        def nteCount = observation(message).count('NTE')
        assert observation(message).nrp('NTE') instanceof SegmentAdapter
        assert observation(message).count('NTE') == nteCount + 1
    }
    
    private GroupAdapter observation(message) {
        message.PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(1)
    }
    
}