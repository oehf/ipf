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

import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openehealth.ipf.modules.cda.AbstractValidatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Sectionimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Encounter
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Actimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Participant2

/**
 * Validates the Encounters content section module (2.16.840.1.113883.10.20.1.3)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDEncountersValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-453', POCDMT000040Section.class, section)
		assertContains('CONF-453', '2.16.840.1.113883.10.20.1.3', section.templateId.root)
		assertNotNull('CONF-453', section.text)
		assertNotNull('CONF-454', section.code)
		assertEquals('CONF-455', '46240-8',section.code.code)
		assertEquals('CONF-455', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-456', section.title)
		assertMatches('CONF-456', '(?i).*encounters*.', section.title.mixed[0].value)
		doValidateEncountersEncounterActivity(section)
	}
	
	/**
	 * Implements set of CCD Encounter Activities conformance rules
	 */
	void doValidateEncountersEncounterActivity(POCDMT000040Section section){		
		/* check encounter activities */
		section.entry.each { entry ->
		    assertNotNull('CONF-458', entry.encounter)
		    assertInstanceOf('CONF-458', POCDMT000040Encounter.class, entry.encounter)
		    doValidateEncounterActivity(entry.encounter)
		}
	}    
	
	/**
	 * Implements set of CCD Encounter Activity conformance rules
	 */
	void doValidateEncounterActivity(POCDMT000040Encounter encounter){
		assertContains('CONF-458', '2.16.840.1.113883.10.20.1.21', encounter.templateId.root)
		assertEquals('CONF-459', 'ENC', encounter.classCode.name)
		assertEquals('CONF-460', 'EVN', encounter.moodCode.name)
		assertSize('CONF-461', 1, encounter.id)
		//validate patient instructions
		def patientInstructionsEntries = encounter.entryRelationship.findAll{ 
		    '2.16.840.1.113883.10.20.1.49' in it.act?.templateId?.root
		}
	    patientInstructionsEntries.each{
		    new CCDMedicationsValidator().doValidatePatientInstructions(patientInstructionsEntries)
		}
		//validate encounter locations
		def encounterLocationEntries = encounter.participant.findAll{
		    '2.16.840.1.113883.10.20.1.45' in it.templateId.root
		}
		encounterLocationEntries.each { entry ->
		    assertInstanceOf('CONF-472', POCDMT000040Participant2.class, entry)
		    doValidateEncounterLocation(entry)
		}
	}
	
	
	/**
	 * Implements set of CCD Encounter Location conformance rules
	 */
	void doValidateEncounterLocation(POCDMT000040Participant2 participant){
	    assertInstanceOf('CONF-472', POCDMT000040Participant2.class, participant)
	    assertContains('CONF-472', '2.16.840.1.113883.10.20.1.45', participant.templateId.root)
	    assertEquals('CONF-473', 'LOC', participant.typeCode.name)
	    assertSize('CONF-474', 1, participant.participantRole.id)
	    assertEquals('CONF-475', 'SDLOC', participant.participantRole.classCode.name)
		assertNotNull('CONF-478', participant.participantRole.playingEntity)  
		assertEquals('CONF-479', 'PLC', participant.participantRole.playingEntity.classCode.name)    
	}
}

