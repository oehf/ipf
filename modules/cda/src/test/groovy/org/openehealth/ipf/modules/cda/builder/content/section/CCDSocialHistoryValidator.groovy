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
import org.openehealth.ipf.modules.cda.AbstractValidatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Sectionimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Organizerimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation

/**
 * Validates the Social History content section module (2.16.840.1.113883.10.20.1.15)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDSocialHistoryValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-232', POCDMT000040Section.class, section)
		assertContains('CONF-232', '2.16.840.1.113883.10.20.1.15', section.templateId.root)
		assertNotNull('CONF-233', section.code)
		assertEquals('CONF-234', '29762-2', section.code.code)
		assertEquals('CONF-234', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-235', section.title)
		assertMatches('CONF-236', '(?i).*social history*.', section.title.mixed[0].value)
		doValidateSocialHistory(section)
	}
	
	/**
	 * Implements set of CCD Social History conformance rules
	 */
	void doValidateSocialHistory(POCDMT000040Section section){	
		/* check social history */
		section.entry.each { entry ->
		    assertNotNull('CONF-237', entry.observation)
		    doValidateSocialHistoryObservation(entry.observation)
		}
	}    
	
	/**
	 * Implements set of CCD Social History Observation conformance rules
	 */
	void doValidateSocialHistoryObservation(POCDMT000040Observation observation){
		assertContains('CONF-237', '2.16.840.1.113883.10.20.1.33', observation.templateId.root)
		assertEquals('CONF-238', 'OBS', observation.classCode.name)
		assertEquals('CONF-239', 'EVN', observation.moodCode.name)
		assertMinSize('CONF-240', 1, observation.id)
		assertNotNull('CONF-241', observation.statusCode)   
		assertEquals('CONF-242', 'completed', observation.statusCode.code)
		//TODO validate status observation (2.16.840.1.113883.10.20.1.56)
	}

}

