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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Participant2
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Reference
import org.openehealth.ipf.modules.cda.AbstractValidator


/**
 * Validates the CCD Advance Directive content section module (2.16.840.1.113883.10.20.1.1)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDAdvanceDirectivesValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-78', POCDMT000040Section.class, section)
		assertContains('CONF-78', '2.16.840.1.113883.10.20.1.1', section.templateId.root)
		doValidateAdvanceDirectives(section)		
	}
	
	void doValidateAdvanceDirectives(POCDMT000040Section section) {
		assertNotNull('CONF-78', section.code)
		assertEquals('CONF-79', '42348-3',section.code.code)
		assertEquals('CONF-79', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-80',section.title)
		assertMatches('CONF-81', '(?i).*advance directives*.', section.title.mixed[0].value)        
		section.entry.each { entry ->
			def observation = entry.observation
			assertInstanceOf('CONF-82', POCDMT000040Observation.class, observation)
			doValidateAdvanceDirectiveObservation(observation)            
		}
	}
	
	/**
	 * Implements set of CCD Advance Directives : observation
	 */
	void doValidateAdvanceDirectiveObservation(POCDMT000040Observation obs){
		assertContains('CONF-82', '2.16.840.1.113883.10.20.1.17', obs.templateId.root)
		assertEquals('CONF-83', 'OBS', obs.classCode.name)
		assertEquals('CONF-84', 'EVN', obs.moodCode.name)
		assertNotNull('CONF-85', obs.id)
		assertNotNull('CONF-86', obs.statusCode)
		assertEquals('CONF-87', 'completed', obs.statusCode.code)
		assertNotNull('CONF-89', obs.code)
		def verifiers = obs.participant.findAll{'2.16.840.1.113883.10.20.1.58' in it.templateId.root}
		verifiers.eachWithIndex { ver, index ->
			assertInstanceOf('CONF-48', POCDMT000040Participant2.class, ver)
			assertContains('CONF-82', '2.16.840.1.113883.10.20.1.58', ver.templateId.root)
			assertEquals('CONF-94', 'VRF', ver.typeCode.name)       
		}
		/* CONF-97: An advance directive observation SHALL contain one or more sources of information,
		 *          as defined in section 5.2 Source. 
		 */
		//TODO SIV add 5.2 validation rules
		
		def statusObservation = obs.entryRelationship.findAll{
			'2.16.840.1.113883.10.20.1.37' in it.observation.templateId.root
		}?.observation
		assertNotNull('CONF-98', statusObservation)
		assertMaxSize('CONF-98', 1, statusObservation)
		doValidateAdvanceDirecitveStatusObservation(statusObservation[0])
		
		def references = obs.reference.findAll{
			'2.16.840.1.113883.10.20.1.36' in it.externalDocument.templateId.root
		}
		references.each{ entry ->
		    assertInstanceOf('CONF-101',POCDMT000040Reference.class, entry)
			doValidateAdvanceDirectiveReference(entry)
		}
		
	}
	
	/**
	 * Implements set of CCD Advance Directives conformance rules : status observation
	 */
	void doValidateAdvanceDirecitveStatusObservation(POCDMT000040Observation obs){
		assertContains('CONF-99', '2.16.840.1.113883.10.20.1.37', obs.templateId.root)
		new CCDStatusObservationValidator().validate(obs, null)
		/* CONF-100: Observation / value” in an advance directive status observation 
		 *           SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.1 
		 *           AdvanceDirectiveStatusCode STATIC 20061017. 
		 */
	}
	
	/**
	 * Implements set of CCD Advance Directives conformance rules : reference
	 */
	void doValidateAdvanceDirectiveReference(POCDMT000040Reference reference){
		assertNotNull('CONF-101', reference.externalDocument)
		assertContains('CONF-101', '2.16.840.1.113883.10.20.1.36', reference.externalDocument.templateId.root)
		assertEquals('CONF-103', 'REFR', reference.typeCode.name)
		assertMinSize('CONF-104', 1, reference.externalDocument.id)	    
	}
	
}

