/**
 * 
 */
package org.openehealth.ipf.modules.hl7.mappings

import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource


/**
 * @author Christian Ohr
 *
 */
public class BidiMappingServiceTest extends GroovyTestCase {
	
	void testMappingService() {
		def hl7MappingService = new BidiMappingService()
		hl7MappingService.setMappingScript(new ClassPathResource("examplemap2.groovy"))
		assert hl7MappingService.mappingKeys().contains("encounterType")
		assert hl7MappingService.get("encounterType", "I") == "IMP"
		assert hl7MappingService.get("messageType", "ADT^A01") == "PRPA_IN402001"
		assert hl7MappingService.get("messageType", "ADT^A04") == null
		assert hl7MappingService.keys("encounterType").sort() == ['E','I','O']
		assert hl7MappingService.keys("vip") == ["Y"] as Set
		assert hl7MappingService.get("vip", "X") == "X"
		assert hl7MappingService.getKey("encounterType", "IMP") == "I"
		assert hl7MappingService.getKeySystem("encounterType") == "2.16.840.1.113883.12.4"		
		assert hl7MappingService.get("nullTest", null) == "not null"

	}

	// Tests that the second mapping file overrides the first one
	void testMappingService2() {
		def hl7MappingService = new BidiMappingService()
		def resources = [new ClassPathResource("examplemap2.groovy"),
		                 new ClassPathResource("examplemap3.groovy")] as Resource[]
		hl7MappingService.setMappingScripts(resources)
		assert hl7MappingService.mappingKeys().contains("encounterType")
		assert hl7MappingService.get("encounterType", "I") == "IMP"
		assert hl7MappingService.get("messageType", "ADT^A04") == "PRPA_IN401001"
		assert hl7MappingService.get("messageType", "ADT^A01") == null
		assert hl7MappingService.keys("encounterType").sort() == ['E','I','O']
		assert hl7MappingService.keys("vip") == ["Y"] as Set
		assert hl7MappingService.get("vip", "X") == "X"
		assert hl7MappingService.getKey("encounterType", "IMP") == "I"
		assert hl7MappingService.getKeySystem("encounterType") == "2.16.840.1.113883.12.4"		
		assert hl7MappingService.get("nullTest", null) == "not null"

	}

}
