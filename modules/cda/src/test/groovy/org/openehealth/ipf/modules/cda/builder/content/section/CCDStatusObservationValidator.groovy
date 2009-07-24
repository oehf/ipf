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

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openehealth.ipf.modules.cda.AbstractValidatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation

/**
 * Implements the general CCD Status Observation conformance rules
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDStatusObservationValidator extends AbstractValidator {
		
	public void validate(Object observation, Object profile){
	    assertInstanceOf('CONF-508', POCDMT000040Observation.class, observation)
	    assertEquals('CONF-510', 'OBS', observation.classCode.name)
	    assertEquals('CONF-511', 'EVN', observation.moodCode.name)
	    assertEquals('CONF-513', '33999-4', observation.code.code)
	    assertEquals('CONF-513', '2.16.840.1.113883.6.1', observation.code.codeSystem)
	    assertNotNull('CONF-515', observation.statusCode)
		assertEquals('CONF-515', 'completed', observation.statusCode.code)
		assertMinSize('CONF-516', 1, observation.value)
		assertMaxSize('CONF-516', 1, observation.value)
	}
}

