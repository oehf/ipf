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

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Actimport org.openehealth.ipf.modules.cda.AbstractValidatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Section

/**
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDPurposeValidator extends AbstractValidator {
		
	public void validate(Object section, Object profile){
	    assertInstanceOf('CONF-15', POCDMT000040Section.class, section)
	    assertContains('CONF-15', '2.16.840.1.113883.10.20.1.13', section.templateId.root)
	    assertEquals('CONF-16', '48764-5', section.code.code)
	    assertEquals('CONF-17', '2.16.840.1.113883.6.1', section.code.codeSystem)
	    assertNotNull('CONF-18', section.title)
		assertMatches('CONF-19', '(?i).*purpose*.', section.title.mixed[0].value)
		
		section.entry.each { entry ->		
		    def act = entry.act
		    assertInstanceOf('CONF-20', POCDMT000040Act.class, act)
		    assertEquals('CONF-21', 'ACT', act.classCode.name)
		    assertEquals('CONF-22', 'EVN', act.moodCode.name)
		    assertNotNull('CONF-23', act.statusCode)
		    assertEquals('CONF-24', 'completed', act.statusCode.code)
		    assertEquals('CONF-25', '2.16.840.1.113883.6.96', act.code.codeSystem)
		    assertEquals('CONF-25', '23745001', act.code.code)
		    assertEquals('CONF-26 Failed', 'RSON', act.entryRelationship.get(0).typeCode.name)
	    }
		
		/* CONF-27: Act / entryrelationship /@typeCode target is
		 *             in [Act, Observation, Procedure, Substance Administration, Supply]
		 */
		//assertNotNull act.entryRelationship.get(0).act
	}
}

