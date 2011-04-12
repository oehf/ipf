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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Organizer
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Participant2
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ParticipantRole
import org.openehealth.ipf.modules.cda.AbstractValidator


/**
 * Validates the CCD Results content section module (2.16.840.1.113883.10.20.1.14)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDResultsValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-388', POCDMT000040Section.class, section)
		assertContains('CONF-388', '2.16.840.1.113883.10.20.1.14', section.templateId.root)
		doValidateResults(section)
	}
	
	void doValidateResults(POCDMT000040Section section) {
		assertNotNull('CONF-388', section.text)
		assertNotNull('CONF-389', section.code)
		assertEquals('CONF-390', '30954-2', section.code.code)
		assertEquals('CONF-390', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-391', section.title)
		section.entry.each{ entry ->
			assertNotNull('CONF-388', entry.organizer)
			assertInstanceOf('CONF-388', POCDMT000040Organizer.class, entry.organizer)
			doValidateResultOrganizer(entry.organizer)
		}   
	}
	
	/**
	 * Implements set of CCD Results : result organizer
	 */
	void doValidateResultOrganizer(POCDMT000040Organizer organizer){
		assertContains('CONF-393', '2.16.840.1.113883.10.20.1.32', organizer.templateId.root)
		doValidateResultOrganizerTemplate(organizer)
	}
	
	void doValidateResultOrganizerTemplate(POCDMT000040Organizer organizer){
	    assertEquals('CONF-394', 'EVN', organizer.moodCode.name)
        assertMinSize('CONF-395', 1, organizer.id)
        assertNotNull('CONF-396', organizer.statusCode)
        assertNotNull('CONF-397', organizer.code)
        organizer.component.each{ entry ->
        assertNotNull('CONF-407', entry.observation)
        assertInstanceOf('CONF-407', POCDMT000040Observation.class, entry.observation)
        doValidateResultObservation(entry.observation)
    } 
	}
	
	/**
	 * Implements set of CCD Results : result observation
	 */
	void doValidateResultObservation(POCDMT000040Observation obs){
		assertContains('CONF-407', '2.16.840.1.113883.10.20.1.31', obs.templateId.root)
		assertEquals('CONF-408', 'EVN', obs.moodCode.name)
		assertMinSize('CONF-409', 1, obs.id)
		assertNotNull('CONF-410', obs.statusCode)
		assertNotNull('CONF-412', obs.code)
		assertSize('CONF-416', 1, obs.value)
	}
}

