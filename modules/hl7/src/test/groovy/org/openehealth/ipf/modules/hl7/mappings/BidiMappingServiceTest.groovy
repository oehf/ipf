/**
 * 
 */
package org.openehealth.ipf.modules.hl7.mappings

import org.springframework.core.io.ClassPathResource

/**
 * @author Christian Ohr
 *
 */
public class BidiMappingServiceTest extends GroovyTestCase {
	
	void testMappingService() {
		def hl7MappingService = new BidiMappingService()
		hl7MappingService.setMappingScript(new ClassPathResource("examplemap2.groovy"))
		assert hl7MappingService.mappingKeys()
		assert hl7MappingService.mappingKeys().contains("encounterType")
		assert hl7MappingService.get("encounterType", "I") == "IMP"
		assert hl7MappingService.get("vip", "X") == "X"
		assert hl7MappingService.getKey("encounterType", "IMP") == "I"
		assert hl7MappingService.getKeySystem("encounterType") == "2.16.840.1.113883.12.4"
		
		assert hl7MappingService.get("nullTest", null) == "not null"

	}
	
}
