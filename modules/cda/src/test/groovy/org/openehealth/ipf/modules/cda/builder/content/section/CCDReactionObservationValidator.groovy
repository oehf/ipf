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
import org.openehealth.ipf.modules.cda.AbstractValidator
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation


/**
 * Implements the general CCD Reaction Observation conformance rules
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDReactionObservationValidator extends AbstractValidator {
		
	public void validate(Object observation, Object profile){
	    assertInstanceOf('CONF-282', POCDMT000040Observation.class, observation)
	    assertContains('CONF-282', '2.16.840.1.113883.10.20.1.54', observation.templateId.root)
	    assertEquals('CONF-283', 'OBS', observation.classCode.name)
	    assertEquals('CONF-284', 'EVN', observation.moodCode.name)
	    assertNotNull('CONF-285', observation.statusCode)
		assertEquals('CONF-286', 'completed', observation.statusCode.code)
		def severityObservations = observation.entryRelationship.findAll{
	        '2.16.840.1.113883.10.20.1.55' in it.observation.templateId.root
	    }
	    severityObservations.each{ entry ->
            assertNotNull('CONF-287', entry.observation)
            assertEquals('CONF-288', 'SUBJ', entry.typeCode.name)
            doValidateReactionSeverityObservation(entry.observation)
	    }
	}
	
	public void doValidateReactionSeverityObservation(POCDMT000040Observation severityObs){
	    assertContains('CONF-287', '2.16.840.1.113883.10.20.1.55', severityObs.templateId.root)
	    assertEquals('CONF-289', 'OBS', severityObs.classCode.name)
        assertEquals('CONF-290', 'EVN', severityObs.moodCode.name)
        assertNotNull('CONF-291', severityObs.statusCode)
		assertEquals('CONF-292', 'completed', severityObs.statusCode.code)
        assertNotNull('CONF-293', severityObs.code)
        assertEquals('CONF-294', 'SEV', severityObs.code.code)
        assertSize('CONF-295', 1, severityObs.value)
	}
}

