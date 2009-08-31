/**
 * 
 */
package org.openehealth.ipf.commons.map

import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
public class BidiMappingServiceTest extends GroovyTestCase {
	
    def mappingService
    
    void setUp() {
        mappingService = new BidiMappingService()
    }
    
	void testMappingService() {
		mappingService.mappingScript = new ClassPathResource("example2.map")
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
		mappingService.mappingScripts = resources
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

}
