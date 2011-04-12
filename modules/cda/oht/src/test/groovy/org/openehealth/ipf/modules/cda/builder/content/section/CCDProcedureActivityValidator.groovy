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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Entry
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Procedure
import org.openehealth.ipf.modules.cda.AbstractValidator


/**
 * Validates the CCD Procedure Activity template (2.16.840.1.113883.10.20.1.29)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProcedureActivityValidator extends AbstractValidator {
	
	void validate(Object entry, Object profile){
		assertInstanceOf('CONF-427', POCDMT000040Entry.class, entry)
		if (entry.act) {
            doValidateProcedureActivity(entry.act)
        } else 
        if (entry.observation) {
           doValidateProcedureActivity(entry.observation)
        } else
        if (entry.procedure){
            doValidateProcedureActivity(entry.procedure)
        } else {
            fail('CONF-427', 'Procedure Activity SHALL be represented with Act, Observation, or Procedure.')
        }	
	}
	
	/**
     * Implements validation rules for CCD Procedure Activity : act
     */
	void doValidateProcedureActivity(POCDMT000040Act act) {
	    assertInstanceOf('CONF-427', POCDMT000040Act.class, act)
	    doValidateProcedureActivityTemplate(act)
	}
	
	   /**
     * Implements validation rules for CCD Procedure Activity : observation
     */
    void doValidateProcedureActivity(POCDMT000040Observation observation) {
        assertInstanceOf('CONF-427', POCDMT000040Observation.class, observation)
        doValidateProcedureActivityTemplate(observation)
    }
	
	/**
	 * Implements validation rules for CCD Procedure Activity : procedure
	 */
	void doValidateProcedureActivity(POCDMT000040Procedure procedure){
	    assertInstanceOf('CONF-427', POCDMT000040Procedure.class, procedure)
	    doValidateProcedureActivityTemplate(procedure)
	}
	
	void doValidateProcedureActivityTemplate(Object object){
	    assertContains('CONF-427', '2.16.840.1.113883.10.20.1.29', object.templateId.root)
	    assertEquals('CONF-428', 'EVN', object.moodCode.name)
        assertMinSize('CONF-429', 1, object.id)
        assertNotNull('CONF-430', object.statusCode)
        assertNotNull('CONF-433', object.code)
	}
	
}

