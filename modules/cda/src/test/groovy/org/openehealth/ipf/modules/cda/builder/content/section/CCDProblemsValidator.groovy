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

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Section
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Participant2
import org.openehealth.ipf.modules.cda.AbstractValidator

/**
 * Validates the CCD Advance Directive content section module (2.16.840.1.113883.10.20.1.11)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProblemsValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-140', POCDMT000040Section.class, section)
		assertContains('CONF-140', '2.16.840.1.113883.10.20.1.11', section.templateId.root)
		doValidateProblems(section)		
	}
	
	void doValidateProblems(POCDMT000040Section section) {
        assertNotNull('CONF-141', section.code)
        assertEquals('CONF-142', '11450-4', section.code.code)
        assertEquals('CONF-142', '2.16.840.1.113883.6.1', section.code.codeSystem)
        assertNotNull('CONF-143', section.title)
        assertMatches('CONF-144', '(?i).*problems*.', section.title.mixed[0].value)
        section.entry.each{ entry ->
            assertNotNull('CONF-145', entry.act)
            assertInstanceOf('CONF-145', POCDMT000040Act.class, entry.act)
            doValidateProblemsAct(entry.act)
        }            
	}
	
	/**
	 * Implements set of CCD Problems : problems act
	 */
	void doValidateProblemsAct(POCDMT000040Act act){
		assertContains('CONF-145', '2.16.840.1.113883.10.20.1.27', act.templateId.root)
		assertEquals('CONF-146', 'ACT', act.classCode.name)
		assertEquals('CONF-147', 'EVN', act.moodCode.name)
		assertMinSize('CONF-148', 1, act.id)
		assertEquals('CONF-149', 'NA', act.code.nullFlavor.name)
		assertMinSize('CONF-151', 1, act.entryRelationship)
        def problemObservationEntries = act.entryRelationship.findAll{
		    '2.16.840.1.113883.10.20.1.28' in it.observation.templateId.root 
		}
		problemObservationEntries.each{ entry ->
		    assertEquals('CONF-153', 'SUBJ', entry.typeCode.name)
		    assertNotNull('CONF-154', entry.observation)
		    assertInstanceOf('CONF-154', POCDMT000040Observation.class, entry.observation)
		    doValidateProblemsProblemObservation(entry.observation)
		}

        def episodeObservationEntries = act.entryRelationship.findAll{
            '2.16.840.1.113883.10.20.1.41' in it.observation.templateId.root
		}
        assertMaxSize('CONF-168', 1, episodeObservationEntries)
        episodeObservationEntries.each{ entry ->
            assertNotNull('CONF-169', entry.observation)
            assertInstanceOf('CONF-169', POCDMT000040Observation.class, entry.observation)
            doValidateProblemsEpisodeObservation(entry.observation)
        }
        def patientAwarenessEntries = act.participant.findAll{
            '2.16.840.1.113883.10.20.1.48' in it.templateId.root
        }
        assertMaxSize('CONF-179', 1, patientAwarenessEntries)
        patientAwarenessEntries.each{ entry ->
            assertInstanceOf('CONF-178', POCDMT000040Participant2.class, entry)
            doValidateProblemsPatientAwareness(entry)
        }
	}		
				
    void doValidateProblemsProblemObservation(POCDMT000040Observation obs){
        assertContains('CONF-154', '2.16.840.1.113883.10.20.1.28', obs.templateId.root) 
        assertEquals('CONF-155', 'EVN', obs.moodCode.name)
        assertNotNull('CONF-156', obs.statusCode)
        assertEquals('CONF-157', 'completed', obs.statusCode.code)
        /* CONF-161: A problem observation SHALL contain one or more sources of information,
         *           as defined in section 5.2 Source. 
         */
         //TODO check source
        def statusObservations = obs.entryRelationship.findAll{
            '2.16.840.1.113883.10.20.1.50' in it.observation.templateId.root
        }
        assertMaxSize('CONF-162', 1, statusObservations)
        statusObservations.each{ entry ->
            assertNotNull('CONF-163', entry.observation)
            assertInstanceOf('CONF-163', POCDMT000040Observation.class, entry.observation)
            doValidateProblemsStatusObservation(entry.observation)
        }
        def healthStatusObservations = obs.entryRelationship.findAll{
            '2.16.840.1.113883.10.20.1.51' in it.observation.templateId.root
        }
        assertMaxSize('CONF-165', 1, healthStatusObservations)
        healthStatusObservations.each{ entry ->
            assertNotNull('CONF-166', entry.observation)
            assertInstanceOf('CONF-166', POCDMT000040Observation.class, entry.observation)
            doValidateProblemsHealthStatusObservation(entry.observation)
        }
    }
	
	/**
	 * Implements set of CCD Problems conformance rules : status observation
	 */
	void doValidateProblemsStatusObservation(POCDMT000040Observation obs){
		assertContains('CONF-163', '2.16.840.1.113883.10.20.1.50', obs.templateId.root)
		new CCDStatusObservationValidator().validate(obs, null)
		/* CONF-164: The value for “Observation / value” in a problem status observation
		 *           SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.13 
		 *           ProblemStatusCode STATIC 20061017. 
		 */
		 //TODO
	}
	
	   /**
     * Implements set of CCD Problems conformance rules : status observation
     */
    void doValidateProblemsHealthStatusObservation(POCDMT000040Observation obs){
        assertContains('CONF-99', '2.16.840.1.113883.10.20.1.51', obs.templateId.root)
        assertInstanceOf('CONF-508', POCDMT000040Observation.class, obs)
        assertEquals('CONF-510', 'OBS', obs.classCode.name)
        assertEquals('CONF-511', 'EVN', obs.moodCode.name)
        assertEquals('CONF-513', '11323-3', obs.code.code)
        assertEquals('CONF-513', '2.16.840.1.113883.6.1', obs.code.codeSystem)
        assertNotNull('CONF-515', obs.statusCode)
        assertEquals('CONF-515', 'completed', obs.statusCode.code)
        assertMinSize('CONF-516', 1, obs.value)
        assertMaxSize('CONF-516', 1, obs.value)
    }
		
    /**
     * Implements set of CCD Problems conformance rules : status observation
     */
    void doValidateProblemsEpisodeObservation(POCDMT000040Observation obs){
        assertContains('CONF-169', '2.16.840.1.113883.10.20.1.41', obs.templateId.root)
        assertEquals('CONF-170', 'OBS', obs.classCode.name)
        assertEquals('CONF-171', 'EVN', obs.moodCode.name)
        assertNotNull('CONF-172 Failed', obs.statusCode)
        assertEquals('CONF-173 Failed', 'completed', obs.statusCode.code)
        assertEquals('CONF-174', 'ASSERTION', obs.code.code)
        assertEquals('CONF-174', '2.16.840.1.113883.5.4', obs.code.codeSystem)
        def sbjSources = obs.entryRelationship.findAll{
             it.typeCode.name == 'SUBJ'
        }
        assertMaxSize('CONF-176', 1, sbjSources)
        def sasSources = obs.entryRelationship.findAll{
             it.typeCode.name == 'SAS'
        }
        assertMinSize('CONF-177', 1, sasSources)
        
    }
	
	/**
	 * Implements set of CCD Problems conformance rules : reference
	 */
	void doValidateProblemsPatientAwareness(POCDMT000040Participant2 participant){
	    assertContains('CONF-178', '2.16.840.1.113883.10.20.1.48', participant.templateId.root)
		assertEquals('CONF-181', 'SBJ', participant.typeCode.name)
		assertNotNull('CONF-182', participant.awarenessCode)	    
		assertNotNull('CONF-181', participant.participantRole.id)
		assertMaxSize('CONF-148', 1, participant.participantRole.id)
	}
	
}

