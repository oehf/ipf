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
package org.openehealth.ipf.commons.map

import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
public class BidiMappingServiceTest extends GroovyTestCase {
	
    BidiMappingService mappingService
    
    void setUp() {
        mappingService = new BidiMappingService()
    }
    
	void testMappingService() {
		mappingService.addMappingScript(new ClassPathResource("example2.map"))
		assert mappingService.mappingKeys().contains("encounterType")
		assert mappingService.get("encounterType", "I") == "IMP"
		assert mappingService.get("messageType", "ADT^A01") == "PRPA_IN402001"
		assert mappingService.get("messageType", "ADT^A04") == null
		assert mappingService.keys("encounterType").sort() == ['E','I','O']
		assert mappingService.keys("vip") == ["Y"] as Set
		assert mappingService.get("vip", "X") == "X"
		assert mappingService.getKey("encounterType", "IMP") == "I"
		assert mappingService.getKeySystem("encounterType") == "2.16.840.1.113883.12.4"		
		assert mappingService.get("nullTest", null) == "not null"
		assert mappingService.get("reverseElseTest", "a") == "b"
		assert mappingService.get("reverseElseTest", "d") == "c"
		assert mappingService.getKey("reverseElseTest", "b") == "a"
		assert mappingService.getKey("reverseElseTest", "c") == "a"		
	}
	
	/**
	 * Tests that the second mapping file overrides the first one 
	 */
	void testMappingService2() {
		def resources = [new ClassPathResource("example2.map"),
		                 new ClassPathResource("example3.map")] as Resource[]
		mappingService.addMappingScripts(resources)
		assert mappingService.mappingKeys().contains("encounterType")
		assert mappingService.get("encounterType", "I") == "IMP"
		assert mappingService.get("messageType", "ADT^A04") == "PRPA_IN401001"
		assert mappingService.get("messageType", "ADT^A01") == null
		assert mappingService.keys("encounterType").sort() == ['E','I','O']
		assert mappingService.keys("vip") == ["Y"] as Set
		assert mappingService.get("vip", "X") == "X"
		assert mappingService.getKey("encounterType", "IMP") == "I"
		assert mappingService.getKeySystem("encounterType") == "2.16.840.1.113883.12.4"		
		assert mappingService.get("nullTest", null) == "not null"
	}

    /**
     * Tests whether the "ignoreResourceNotFound" option works.
     */
    void testIgnoreResourceNotFound() {
        def resources = [new ClassPathResource("example2.map.NONEXISTENT"),
                         new ClassPathResource("example3.map")] as Resource[]
        mappingService.setMappingScripts(resources)
        mappingService.ignoreResourceNotFound = true
        mappingService.afterPropertiesSet()

        boolean failed = false
        assert mappingService.get('messageType', 'ADT^A04') == 'PRPA_IN401001'
        try {
            mappingService.get('O', 'encounterType')
        } catch (IllegalArgumentException e) {
            failed = (e.message == 'Unknown key O')
        }
        assert failed
    }
}
