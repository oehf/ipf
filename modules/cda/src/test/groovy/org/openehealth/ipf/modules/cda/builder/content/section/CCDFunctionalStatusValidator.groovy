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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Organizer
import org.openehealth.ipf.modules.cda.AbstractValidator


/**
 * Validates the CCD Functional Status content section module (2.16.840.1.113883.10.20.1.5)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDFunctionalStatusValidator extends AbstractValidator {
	
     CCDFunctionalStatusValidator() {
         super('/schematron/ccd/voc.xml')
     }
     
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-123', POCDMT000040Section.class, section)
		doValidateFunctionalStatus(section)		
	}
	
	void doValidateFunctionalStatus(POCDMT000040Section section) {
	    assertContains('CONF-123', '2.16.840.1.113883.10.20.1.5', section.templateId.root)
		assertNotNull('CONF-124', section.code)
		assertEquals('CONF-125', '47420-5', section.code.code)
		assertEquals('CONF-125', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-126',section.title)
		assertMatches('CONF-127', '(?i).*functional status*.', section.title.mixed[0].value)        
		section.entry.each { entry ->
		    if (entry.act){
		        new CCDProblemsValidator().doValidateProblemAct(entry.act)
		        doValidateFunctionalStatusStatusObservation(entry.act)
		    } else 
		    if (entry.organizer) {
		        new CCDResultsValidator().doValidateResultOrganizer(entry.organizer)
		        doValidateFunctionalStatusStatusObservation(entry.organizer)
		    }
	    }
	}
	
	void doValidateFunctionalStatusStatusObservation(POCDMT000040Act act){
	    def problemObservations = act.entryRelationship.findAll{
            '2.16.840.1.113883.10.20.1.28' in it.observation.templateId.root 
        }?.observation
        problemObservations.each { obs ->
            def functionalStatusObservations = obs.entryRelationship.findAll{
                '2.16.840.1.113883.10.20.1.44' in it.observation.templateId.root
            }?.observation
            assertSize('CONF-136', 1, functionalStatusObservations)
            doValidateFunctionalStatusStatusObservationTemplate(functionalStatusObservations[0])
        }
	}
	
	void doValidateFunctionalStatusStatusObservation(POCDMT000040Organizer organizer){
	    def resultObservations = organizer.entryRelationship.findAll{
	        '2.16.840.1.113883.10.20.1.31' in it.observation.templateId.root 
	    }?.observation
	    resultObservations.each { obs ->
	        def functionalStatusObservations = obs.entryRelationship.findAll{
	            '2.16.840.1.113883.10.20.1.44' in it.observation.templateId.root
	        }?.observation
	        assertSize('CONF-137', 1, functionalStatusObservations)
	        doValidateFunctionalStatusStatusObservationTemplate(functionalStatusObservations[0])
	        }
	    }
	
	void doValidateFunctionalStatusStatusObservationTemplate(POCDMT000040Observation obsStatus){
	    assertContains('CONF-138', '2.16.840.1.113883.10.20.1.44', obsStatus.templateId.root)
	    //TODO validate CONF-139 ValueSet
	    new CCDStatusObservationValidator().validate(obsStatus, null)
	    assertCode('CONF-139', '2.16.840.1.113883.1.11.20.5', obsStatus.value[0])
	}
}

