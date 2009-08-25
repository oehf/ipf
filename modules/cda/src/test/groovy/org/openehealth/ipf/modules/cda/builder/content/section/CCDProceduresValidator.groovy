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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Procedure
import org.openehealth.ipf.modules.cda.AbstractValidator


/**
 * Validates the CCD Vital Signs content section module (2.16.840.1.113883.10.20.1.16)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProceduresValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-422', POCDMT000040Section.class, section)
		doValidateProcedures(section)
	}
	
	void doValidateProcedures(POCDMT000040Section section) {
	    assertContains('CONF-422', '2.16.840.1.113883.10.20.1.12', section.templateId.root)
	    assertNotNull('CONF-422', section.text)
        assertNotNull('CONF-423', section.code)
        assertEquals('CONF-424', '47519-4', section.code.code)
        assertEquals('CONF-424', '2.16.840.1.113883.6.1', section.code.codeSystem)
        assertNotNull('CONF-425', section.title)
        section.entry.each{ entry ->
            new CCDProcedureActivityValidator().validate(entry, null)
        }   
	}	
	
}

