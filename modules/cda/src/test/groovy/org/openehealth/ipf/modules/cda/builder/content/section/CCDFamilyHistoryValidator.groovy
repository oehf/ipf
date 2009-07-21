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
 * Validates the Family History content section module (2.16.840.1.113883.10.20.1.4)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDFamilyHistoryValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-184', POCDMT000040Section.class, section)
		assertContains('CONF-184', '2.16.840.1.113883.10.20.1.4', section.templateId.root)
		assertNotNull('CONF-185', section.code)
		assertEquals('CONF-186', '10157-6',section.code.code)
		assertEquals('CONF-186', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-187', section.title)
		assertMatches('CONF-188', '(?i).*family history*.', section.title.mixed[0].value)
		assertNull('CONF-189', section.subject)
		doValidateFamilyHistory(section)
	}
	
	/**
	 * Implements set of CCD Family History conformance rules
	 */
	void doValidateFamilyHistory(POCDMT000040Section section){		
		/* check family history */
		section.entry.each { item ->
			if (item.organizer) {
				doValidateFamilyHistoryOrganizer(item.organizer)
			} else if (item.observation) {
				if ('2.16.840.1.113883.10.20.1.22' in item.observation.templateId.root ) {
					doValidateFamilyHistoryObservation(obs)
				} else if ('2.16.840.1.113883.10.20.1.42' in item.observation.templateId.root) {
					doValidateFamilyHistoryObservationOfDeath(obs)
				} else {
					fail('CONF-184', "Wrong kind of observation ${item.observation.templateId.root} in Family History")
				}
			} else {
				fail('CONF-184', "Family History must have either Observations or Organizers")
			}
		}
	}    
	
	/**
	 * Implements set of CCD Family History Organizer conformance rules
	 */
	void doValidateFamilyHistoryOrganizer(POCDMT000040Organizer organizer){
		assertContains('CONF-200', '2.16.840.1.113883.10.20.1.23', organizer.templateId.root)
		assertEquals('CONF-201', 'CLUSTER', organizer.classCode.name)
		assertEquals('CONF-202', 'EVN', organizer.moodCode.name)
		assertNotNull('CONF-203', organizer.statusCode)   
		assertEquals('CONF-204', 'completed', organizer.statusCode.code)
		assertMinSize('CONF-205', 1, organizer.component)  
		organizer.component.each { comp ->
			def obs = comp.observation
			if ('2.16.840.1.113883.10.20.1.22' in obs?.templateId.root ) {
				doValidateFamilyHistoryObservation(obs)
			} else if ('2.16.840.1.113883.10.20.1.42' in obs?.templateId.root) {
				doValidateFamilyHistoryObservationOfDeath(obs)
			} else {
				fail('CONF-206', "Wrong kind of observation ${item.observation.templateId.root} in Family History Organizer")
			}
		}
		
		assertNotNull('CONF-208', organizer.subject)
		assertNotNull('CONF-212', organizer.subject.relatedSubject)
		assertEquals('CONF-213', 'PRS', organizer.subject.relatedSubject.classCode.name)
		assertNotNull('CONF-214', organizer.subject.relatedSubject.code)        
	}
	
	
	/**
	 * Implements set of CCD Family History Observation conformance rules
	 */
	void doValidateFamilyHistoryObservation(POCDMT000040Observation observation){
		assertContains('CONF-190', '2.16.840.1.113883.10.20.1.22', observation.templateId.root)    
		assertEquals('CONF-191', 'EVN', observation.moodCode.name)
		assertMinSize('CONF-192', 1, observation.id)
		assertNotNull('CONF-193', observation.statusCode)   
		assertEquals('CONF-194', 'completed', observation.statusCode.code)
		/* CONF-209: Observation has one subject if not in organizer */
		// TODO Assert.assertTrue('CONF-209', organizer.subject)    
	}
	
	/**
	 * Implements set of CCD Family History Observation conformance rules
	 */
	public boolean doValidateFamilyHistoryObservationOfDeath(POCDMT000040Observation observation){
		assertContains('CONF-196', '2.16.840.1.113883.10.20.1.42', observation.templateId.root)    
		assertContains('CONF-197', 'CAUS', observation.entryRelationship.typeCode.name)   
	}
}

