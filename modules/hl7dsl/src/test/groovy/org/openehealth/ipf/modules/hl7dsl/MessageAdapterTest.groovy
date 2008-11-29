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
class MessageAdapterTest extends GroovyTestCase {
    
    def msg
    
    void setUp() {
        msg = load('msg-01.hl7')
    }

    void testMakeStream() {
        def stream = getClass().classLoader.getResource('msg-01.hl7').openStream()
        def result = MessageAdapters.make(stream)
        assert result.MSH[4].value == 'HZL'
    }
    
    void testRender() {
        def writer
        def result

        writer = new StringWriter()
        writer << msg // msg is writable
        result = writer.buffer.toString()
        assert result.contains('4444')
        assert !result.contains('6666')

        // modify message
        msg.NK1(0).nrp(5).value = '333-6666'
        
        writer = new StringWriter()
        writer << msg // msg is writable
        result = writer.buffer.toString()
        assert result.contains('4444')
        assert result.contains('6666')
        
        result = msg.toString()
        assert result.contains('4444')
        assert result.contains('6666')
    }
    
    void testCopy() {
        msg.MSH[5] = 'X'
        def copy = msg.copy()
        assert copy.MSH[5].value == 'X'
        copy.MSH[5].value = 'Y'
        assert copy.MSH[5].value == 'Y'
        assert msg.MSH[5].value == 'X'
    }
        

}