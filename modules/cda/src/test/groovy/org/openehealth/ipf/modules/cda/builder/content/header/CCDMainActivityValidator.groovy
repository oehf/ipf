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
package org.openehealth.ipf.modules.cda.builder.content.header

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040ServiceEvent
import org.openehealth.ipf.modules.cda.AbstractValidator

/**
 * Validates the CCD Support content
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDMainActivityValidator extends AbstractValidator {
	
	void validate(Object document, Object profile){
		def POCDMT000040ServiceEvent serviceEvent = document.documentationOf.serviceEvent.find{ 
		    it.classCode.name == 'PCPR'
		}
		assertNotNull('CONF-2', serviceEvent)
		doValidateMainActivity(serviceEvent)
	}
	
	void doValidateMainActivity(POCDMT000040ServiceEvent event){
		assertEquals('CONF-3', 'PCPR', event.classCode.name)
		assertNotNull('CONF-4', event.effectiveTime)
		assertNotNull('CONF-4', event.effectiveTime.low)
		assertNotNull('CONF-4', event.effectiveTime.high)
	}
	
}
