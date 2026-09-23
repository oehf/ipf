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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
class BidiMappingServiceTest {
	
    BidiMappingService mappingService

    @BeforeEach
    void setUp() {
        mappingService = new BidiMappingService()
    }

    @Test
	void testMappingService() {
		mappingService.setMappingScript(getClass().getResource("/example2.map"))
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

    @Test
    void testMappingServiceURL() {
        mappingService.setMappingScript(getClass().getResource("/example2.map"))
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
    @Test
	void testMappingService2() {
		def resources = [getClass().getResource("/example2.map"),
                         getClass().getResource("/example3.map")] as URL[]
		mappingService.setMappingScripts(resources)
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

    @Test
    void testMappingServiceURL2() {
        def resources = [getClass().getResource("/example2.map"),
                         getClass().getResource("/example3.map")] as URL[]
        mappingService.setMappingScripts(resources)
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
     * The reverse fallback declared by the first mapping file used to be lost as soon as a
     * second file was loaded, because the reverse index of every mapping was rebuilt from a
     * forward map that the previous rebuild had mutated. Indices are now built once per
     * mapping, when it is registered.
     */
    @Test
    void testReverseElseSurvivesSecondResource() {
        def resources = [getClass().getResource("/example2.map"),
                         getClass().getResource("/example3.map")] as URL[]
        mappingService.setMappingScripts(resources)
        assert mappingService.get("reverseElseTest", "d") == "c"
        assert mappingService.getKey("reverseElseTest", "b") == "a"
        assert mappingService.getKey("reverseElseTest", "c") == "a"
    }

    /**
     * The tilde convention of IPF 5.x is gone: a composite value is one string, and a Collection
     * passed as a key is rejected rather than joined.
     */
    @Test
    void testCompositeValues() {
        mappingService.setMappingScript(getClass().getResource("/example2.map"))
        assert mappingService.get("listTest", "a~b") == "c~d"
        assert mappingService.get("listTest2", "anything") == "c~d"
        assert mappingService.getKey("listTest", "c~d") == "a~b"
        Assertions.assertThrows(IllegalArgumentException) {
            mappingService.get("listTest", ["a", "b"])
        }
    }

    /**
     * The typed API is the same content, answered the same way.
     */
    @Test
    void testTypedMappings() {
        mappingService.setMappingScript(getClass().getResource("/example2.map"))
        def mappings = mappingService.mappings
        assert mappings.map("encounterType", "I").get() == "IMP"
        assert mappings.map("encounterType", "X").empty
        assert mappings.mapReverse("encounterType", "IMP").get() == "I"
        assert mappings.map("listTest", "a~b").get() == "c~d"
    }

}
