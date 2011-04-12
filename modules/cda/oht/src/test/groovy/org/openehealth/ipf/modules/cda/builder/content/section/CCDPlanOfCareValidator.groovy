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
import org.openhealthtools.ihe.common.cdar2.*

/**
 * Validates the CCD Plan Of Care content section module (2.16.840.1.113883.10.20.1.10)
 * 
 * @author Christian Ohr
 */
public class CCDPlanOfCareValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-480', POCDMT000040Section.class, section)
		doValidatePlanOfCare(section)
	}
	
	void doValidatePlanOfCare(POCDMT000040Section section) {
	    assertContains('CONF-481', '2.16.840.1.113883.10.20.1.10', section.templateId.root)
	    assertNotNull('CONF-480', section.text)
        assertNotNull('CONF-482', section.code)
        assertEquals('CONF-482', '18776-5', section.code.code)
        assertEquals('CONF-482', '2.16.840.1.113883.6.1', section.code.codeSystem)
        assertNotNull('CONF-483', section.title)
        section.entry.each{ entry ->
            doValidatePlanOfCareActivity(entry)
        }   
	}	
	
	void doValidatePlanOfCareActivity(POCDMT000040Entry entry) {
	    def moodCodes
	    if (entry.act != null) {
	        doValidateClinicalStatement(entry.act, ['INT','ARQ','PRMS','PRP','RQO'])
	    } else if (entry.observation != null) {
	        doValidateClinicalStatement(entry.observation, ['INT','GOL','PRMS','PRP','RQO'])
	    } else if (entry.procedure != null) {
	        doValidateClinicalStatement(entry.procedure, ['INT','ARQ','PRMS','PRP','RQO'])
	    } else if (entry.encounter != null) {
	        doValidateClinicalStatement(entry.encounter, ['INT','ARQ','PRMS','PRP','RQO'])
	    } else if (entry.substanceAdministration != null) {
	        doValidateClinicalStatement(entry.substanceAdministration, ['INT','PRMS','PRP','RQO'])
	    } else if (entry.supply != null) {
	        doValidateClinicalStatement(entry.supply, ['INT','PRMS','PRP','RQO'])
	    }
	}
	
	
	void doValidateClinicalStatement(def cs, List moodCodes) {
	    assertContains('CONF-485', '2.16.840.1.113883.10.20.1.25', cs.templateId?.root)
	    assertMinSize('CONF-486', 1, cs.id)
	    assertNotNull('CONF-487', cs.moodCode)
	    assertOneOf('CONF-488', moodCodes, cs.moodCode.name)
	    
	}
	
}

