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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Participant2
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ParticipantRole
import org.openehealth.ipf.modules.cda.AbstractValidator


/**
 * Validates the CCD Alerts content section module (2.16.840.1.113883.10.20.1.11)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDAlertsValidator extends AbstractValidator {

     CCDAlertsValidator() {
         super('/schematron/ccd/voc.xml')
     }     
     
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-256', POCDMT000040Section.class, section)
		assertContains('CONF-256', '2.16.840.1.113883.10.20.1.2', section.templateId.root)
		doValidateAlerts(section)		
	}
	
	void doValidateAlerts(POCDMT000040Section section) {
	    assertNotNull('CONF-258', section.code)
	    assertEquals('CONF-259', '48765-2', section.code.code)
        assertEquals('CONF-259', '2.16.840.1.113883.6.1', section.code.codeSystem)
        assertNotNull('CONF-261', section.title)
        assertMatches('CONF-262', '(?i).*(alert|allergies and adverse reactions)*.', section.title.mixed[0].value)
        section.entry.each{ entry ->
            assertNotNull('CONF-256', entry.act)
            assertInstanceOf('CONF-256', POCDMT000040Act.class, entry.act)
            doValidateProblemsAct(entry.act)
        }            
	}
	
	/**
	 * Implements set of CCD Alerts : problems act
	 * TODO ProblemAct needs its own validator as of it is used also in problmes section
	 */
	void doValidateProblemsAct(POCDMT000040Act act){
		assertContains('CONF-145', '2.16.840.1.113883.10.20.1.27', act.templateId.root)
		assertEquals('CONF-146', 'ACT', act.classCode.name)
		assertEquals('CONF-147', 'EVN', act.moodCode.name)
		assertMinSize('CONF-148', 1, act.id)
		assertEquals('CONF-149', 'NA', act.code.nullFlavor.name)
		assertMinSize('CONF-151', 1, act.entryRelationship)
        def alertObservationEntries = act.entryRelationship.findAll{
		    '2.16.840.1.113883.10.20.1.18' in it.observation?.templateId?.root 
		}
		alertObservationEntries.each{ entry ->
		    assertEquals('CONF-153', 'SUBJ', entry.typeCode.name)
		    assertNotNull('CONF-262', entry.observation)
		    assertInstanceOf('CONF-262', POCDMT000040Observation.class, entry.observation)
		    doValidateAlertObservation(entry.observation)
		} 
	}		
				
    void doValidateAlertObservation(POCDMT000040Observation obs){
        assertContains('CONF-262', '2.16.840.1.113883.10.20.1.18', obs.templateId.root) 
        assertEquals('CONF-263', 'EVN', obs.moodCode.name)
        assertNotNull('CONF-264', obs.statusCode)
        assertEquals('CONF-265', 'completed', obs.statusCode.code)
        /* CONF-269: An alert observation SHALL contain one or more sources of 
         *           information, as defined in section 5.2 Source.
         */
         //TODO check source
        def alertStatusObservations = obs.entryRelationship.findAll{
            '2.16.840.1.113883.10.20.1.39' in it.observation?.templateId?.root
        }
        assertMaxSize('CONF-270', 1, alertStatusObservations)
        alertStatusObservations.each{ entry ->
            assertNotNull('CONF-271', entry.observation)
            assertInstanceOf('CONF-271', POCDMT000040Observation.class, entry.observation)
            doValidateAlertStatusObservation(entry.observation)
        }
        def participantAgents = obs.participant.findAll{
            it.typeCode.name == 'CSM'
        }
        participantAgents.each{ entry ->
            assertNotNull('CONF-276', entry.participantRole)
            doValidateAlertsParticipantAgent(entry)
        }
        def alertReactionObservations = obs.entryRelationship.findAll{
            '2.16.840.1.113883.10.20.1.54' in it.observation?.templateId?.root
        }
        alertReactionObservations.each{ entry ->
            assertNotNull('CONF-282', entry.observation)
            new CCDReactionObservationValidator().validate(entry.observation, null)
        }

    }
	
	/**
	 * Implements set of CCD Alerts conformance rules : alert status observation
	 */
	void doValidateAlertStatusObservation(POCDMT000040Observation obs){
		assertContains('CONF-271', '2.16.840.1.113883.10.20.1.39', obs.templateId.root)
		new CCDStatusObservationValidator().validate(obs, null)
		/* CONF-272: The value for “Observation / value” in an alert status observation SHALL be
		 *           selected from ValueSet 2.16.840.1.113883.1.11.20.3 AlertStatusCode STATIC 20061017. 
		 */
		assertCode('CONF-272', '2.16.840.1.113883.1.11.20.3', obs.value[0])
	}
		
	/**
	 * Implements set of CCD Alerts conformance rules : reference
	 */
	void doValidateAlertsParticipantAgent(POCDMT000040Participant2 participant){
	    assertInstanceOf('CONF-274', POCDMT000040Participant2.class, participant)
	    assertNotNull('CONF-274', participant.participantRole.playingEntity)
	    assertEquals('CONF-275', 'CSM', participant.typeCode.name)
		assertEquals('CONF-276', 'MANU', participant.participantRole.classCode.name)
		assertEquals('CONF-277', 'MMAT', participant.participantRole.playingEntity.classCode.name)
		assertNotNull('CONF-278', participant.participantRole.playingEntity.code)
	}
	
}

