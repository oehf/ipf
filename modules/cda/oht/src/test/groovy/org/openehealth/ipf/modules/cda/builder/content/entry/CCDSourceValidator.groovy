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
package org.openehealth.ipf.modules.cda.builder.content.entry

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Reference
import org.openehealth.ipf.modules.cda.AbstractValidator

/**
 * Validates the CCD Support content
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDSourceValidator extends AbstractValidator {
	
	void validate(Object source, Object profile){
	}

    void doValidateInformationSource(POCDMT000040Observation infoSource){
        assertInstanceOf('CONF-525', POCDMT000040Observation.class,  infoSource)
        assertEquals('CONF-526', 'OBS', infoSource.classCode.name)
        assertEquals('CONF-527', 'EVN', infoSource.moodCode.name)
        assertNotNull('CONF-528', infoSource.statusCode)
        assertEquals('CONF-529', 'completed', infoSource.statusCode.code)
        assertNotNull('CONF-530', infoSource.code)
        assertEquals('CONF-531', '48766-0', infoSource.code.code)
        assertEquals('CONF-531', '2.16.840.1.113883.6.1', infoSource.code.codeSystem)
        assertSize('CONF-532', 1, infoSource.value)
    }
    
    void doValidateReferenceSource(POCDMT000040Reference refSource){
        assertInstanceOf('CONF-522', POCDMT000040Reference.class,  refSource)
        assertEquals('CONF-522', 'XCRPT', refSource.typeCode.name)
    }

}
