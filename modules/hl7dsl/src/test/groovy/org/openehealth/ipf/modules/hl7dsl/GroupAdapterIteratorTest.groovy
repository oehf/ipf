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
package org.openehealth.ipf.modules.hl7dsl

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.*
import ca.uhn.hl7v2.model.v24.message.ORU_R01

/**
 * @author Christian Ohr
 */
class GroupAdapterIteratorTest extends GroovyTestCase {
    
    MessageAdapter<ORU_R01> message
    
    void setUp() {
        message = load('msg-02.hl7')
    }
    
    void testIterate() {
		def iterator = GroupAdapterIterator.iterator(message)
		def structures = []
		while (iterator.hasNext()) {
			structures += iterator.next().path
		}
		assertEquals(34, structures.size())
    }
    
    
}