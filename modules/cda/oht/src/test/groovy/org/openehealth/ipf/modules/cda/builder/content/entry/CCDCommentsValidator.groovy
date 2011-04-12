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

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openehealth.ipf.modules.cda.AbstractValidator

/**
 * Validates the CCD Comments content
 * 
 * @author Stefan Ivanov
 */
public class CCDCommentsValidator extends AbstractValidator {
	
	void validate(Object clinicalStatement, Object profile){
	    //TODO
	}

    void doValidateComment(POCDMT000040Act comment){
        assertInstanceOf('CONF-503', POCDMT000040Act.class,  comment)
        assertContains('CONF-503', '2.16.840.1.113883.10.20.1.40', comment.templateId.root)
        assertEquals('CONF-504', 'ACT', comment.classCode.name)
        assertEquals('CONF-505', 'EVN', comment.moodCode.name)
        assertNotNull('CONF-506', comment.code)
        assertEquals('CONF-507', '48767-8', comment.code.code)
        assertEquals('CONF-507', '2.16.840.1.113883.6.1', comment.code.codeSystem)
    }

}
