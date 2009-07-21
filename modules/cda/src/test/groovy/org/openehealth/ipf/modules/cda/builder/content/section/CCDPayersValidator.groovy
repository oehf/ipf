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

import org.openehealth.ipf.commons.core.modules.api.Validatorimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Sectionimport org.openehealth.ipf.modules.cda.AbstractValidator

/**
 * Validates the Payers content section module (2.16.840.1.113883.10.20.1.9)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDPayersValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-31', POCDMT000040Section.class, section)
	    assertContains('CONF-31', '2.16.840.1.113883.10.20.1.9', section.templateId.root)
		doValidatePayers(section)		
	}
	
	void doValidatePayers(POCDMT000040Section section) {
		assertNotNull('CONF-31', section.code)
		assertEquals('CONF-32', '48768-6',section.code.code)
		assertEquals('CONF-32', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-33',section.title)
		assertMatches('CONF-34', '(?i).*(payers|insurance)*.', section.title.mixed[0].value)	    
		section.entry.each { entry ->
		    def act = entry.act
		    assertInstanceOf('CONF-35 Failed', POCDMT000040Act.class, act)
		    doValidateCoverageActivity(act)            
		}
	}
	
	
	/**
	 * Implements set of CCD Payers conformance rules : coverage activity
	 */
	void doValidateCoverageActivity(POCDMT000040Act act){
		assertEquals('CONF-36', 'ACT', act.classCode.name)
		assertEquals('CONF-37', 'DEF', act.moodCode.name)
		assertNotNull('CONF-38', act.id)
		assertNotNull('CONF-39', act.statusCode)
		assertEquals('CONF-40', 'completed', act.statusCode.code)
		assertNotNull('CONF-41', act.code)
		assertEquals('CONF-42', '2.16.840.1.113883.6.1', act.code.codeSystem)
		assertEquals('CONF-42', '48768-6', act.code.code)
		assertMinSize('CONF-43 Failed', 1, act.entryRelationship)
		act.entryRelationship.eachWithIndex { rel, index ->
			assertEquals('CONF-45', 'COMP', rel.typeCode.name)
			assertInstanceOf('CONF-48', POCDMT000040Act.class, rel.act)
			doValidatePolicyActivity(rel.act)
		}
	}
	
	/**
	 * Implements a set of CCD Payers conformance rules : policy activity
	 */
	void doValidatePolicyActivity(POCDMT000040Act policyActivity){
		assertContains('CONF-46', '2.16.840.1.113883.10.20.1.26', policyActivity.templateId.root)
		assertEquals('CONF-49 Failed', 'ACT', policyActivity.classCode.name)
		assertEquals('CONF-50', 'EVN', policyActivity.moodCode.name)
		assertMinSize('CONF-51', 1, policyActivity.id)
		assertNotNull('CONF-52', policyActivity.statusCode)
		assertEquals('CONF-53', 'completed', policyActivity.statusCode.code)
		policyActivity.performer.each { performer ->
			assertEquals('CONF-56', 'PRF', performer.typeCode.name)
			assertMinSize('CONF-57', 1, performer.assignedEntity.id)
		}
		policyActivity.participant.each { participant ->
			assertEquals('CONF-58', 'COV', participant.typeCode.name)
		}
		/* CONF-66: Act / entryRelationship / @typeCode is 'REFR' */
		policyActivity.entryRelationship.eachWithIndex { rel, index->
			assertEquals('CONF-66', 'REFR', rel.typeCode.name)
			doValidateAuthorizationAcitivity(rel.act)
		}
	}
	
	/**
	 * Implements a set of CCD Payers conformance rules : authorization activity
	 */
	void doValidateAuthorizationAcitivity(POCDMT000040Act authActivity){
		/* CONF-67: Act / entryRelationship / @typeCode is 'REFR' SHALL be 
		 * an authorization activity (templateId 2.16.840.1.11CONF-67: 
		 * The target of a policy activity with 
		 * Act / entryRelationship / @typeCode=”REFR” SHALL be 
		 * an authorization activity (templateId 2.16.840.1.113883.10.20.1.19) 
		 * or an Act, with Act [@classCode = “ACT”] and Act [@moodCode = “DEF”], 
		 * representing a description of the coverage plan */
		//TODO
		/* CONF-68: A description of the coverage plan SHALL contain one 
		 * or more Act / Id, to represent the plan identifier */
		//TODO
		assertContains('CONF-69', '2.16.840.1.113883.10.20.1.19', authActivity.templateId.root)
		assertEquals('CONF-70', 'ACT', authActivity.classCode.name)
		assertMinSize('CONF-71', 1, authActivity.id)
		assertEquals('CONF-72', 'EVN', authActivity.moodCode.name)
		assertMinSize('CONF-73', 1, authActivity.entryRelationship)
		/* CONF-74: Act / entryRelationship / @typeCode is 'SUBJ' */
		authActivity.entryRelationship.eachWithIndex{ rel, index ->
			assertEquals('CONF-74', 'SUBJ', rel.typeCode.name)
		}
		/* CONF-75: The target of an authorization activity with 
		 * Act / entryRelationship / @typeCode=”SUBJ” SHALL be 
		 * a clinical statement with moodCode = “PRMS” (Promise).
		 */
		//TODO
		
	}	
}

