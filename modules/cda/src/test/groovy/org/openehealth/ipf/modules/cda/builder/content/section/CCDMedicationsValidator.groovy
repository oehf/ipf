/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.modules.cda.builder.content.section

import org.openehealth.ipf.modules.cda.AbstractValidator
import org.openhealthtools.ihe.common.cdar2.*

/**
 * Validates the CCD Medication content section module (2.16.840.1.113883.10.20.1.8)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDMedicationsValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-298', POCDMT000040Section.class, section)
		assertContains('CONF-298', '2.16.840.1.113883.10.20.1.8', section.templateId.root)
		assertNotNull('CONF-300', section.code)
		assertEquals('CONF-301', '10160-0',section.code.code)
		assertEquals('CONF-301', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-302', section.title)
		assertMatches('CONF-302', '(?i).*medication*.', section.title.mixed[0].value)
		doValidateMedications(section)
	}
	
	void doValidateMedications(POCDMT000040Section section) {
		section.entry.each { item ->
			if (item.substanceAdministration) {
				doValidateMedicationActivity(item.substanceAdministration)
			} else if (item.supply) {
				doValidateMedicationActivity(item.supply)
			} else {
				fail('CONF-298', 'Medication must have either MedicationActivity or SupplyActivity')
			}
		}
	}
	
	void doValidateMedicationActivity(POCDMT000040SubstanceAdministration sa) {
		assertContains('CONF-304', '2.16.840.1.113883.10.20.1.24', sa.templateId.root)
		assertEquals('CONF-305', 'EVN', sa.moodCode.name)
		assertMinSize('CONF-306', 1, sa.id)
		assertNotNull('CONF-307', sa.statusCode)
		assertMinSize('CONF-308', 1, sa.effectiveTime) // TODO check according to ch. 5.4.1
		assertNotNull('CONF-309', sa.routeCode)
		assertEquals('CONF-310', '2.16.840.1.113883.5.112', sa.routeCode.codeSystem)
		// check MedicationStatus if present
		def statusObservation = sa.entryRelationship.findAll{
			'2.16.840.1.113883.10.20.1.47' in it.observation.templateId.root
		}?.observation
		if (statusObservation) {
		    doValidateMedicationStatusObservation(statusObservation[0])
		}		
	}
	
	void doValidateSupplyActivity(POCDMT000040Supply supply) {
	    assertContains('CONF-316', '2.16.840.1.113883.10.20.1.34', supply.templateId.root)
        assertContains('CONF-317', supply.moodCode.name, ['EVN', 'INT'])
        assertMinSize('CONF-318', 1, supply.id)
        assertNotNull('CONF-319', supply.statusCode)
		// check MedicationStatus if present
		def statusObservation = supply.entryRelationship.findAll{
			'2.16.840.1.113883.10.20.1.47' in it.observation.templateId.root
		}?.observation
		if (statusObservation) {
		    doValidateMedicationStatusObservation(statusObservation[0])
		}	
	}
	
	void doValidatePatientInstructions(POCDMT000040EntryRelationship entry){
	    assertNotNull('CONF-331', entry.act)
	    assertContains('CONF-331', '2.16.840.1.113883.10.20.1.49', entry.act.templateId.root)
	    assertEquals('CONF-332', 'INT', entry.act.moodCode.name)
	    assertEquals('CONF-333', 'SUBJ', entry.typeCode.name)
	}
	
	void doValidateMedicationStatusObservation(POCDMT000040Observation obs){
		assertContains('CONF-352', '2.16.840.1.113883.10.20.1.47', obs.templateId.root)
		new CCDStatusObservationValidator().validate(obs, null)
		/* CONF-353: Observation / value” in an medication status observation 
		 *           SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.7 
		 */
		 assertCode('CONF-353', '2.16.840.1.113883.1.11.20.7', obs.value[0])
	}
	
}

