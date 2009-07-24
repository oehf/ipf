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
package org.openehealth.ipf.modules.ccd.builder

import org.openehealth.ipf.modules.cda.AbstractValidator
import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument
import org.openehealth.ipf.modules.cda.builder.content.entry.CCDMainActivityValidator
/**
 * Implements a subset of validation rules for CCD instance documents.
 *
 * @author Stefan Ivanov
 */
public class CCDHeaderValidator extends AbstractValidator {
	
	/**
	 * Implements a set of CCD Header conformance rules
	 */
	void validate(Object document, Object profile){
		assertInstanceOf('CONF-0',  POCDMT000040ClinicalDocument.class, document )
		assertEquals('CONF-1', '34133-9', document.code.code)
		assertMinSize('CONF-2', 1, document.documentationOf)
		new CCDMainActivityValidator().validate(document, null)
		assertNotNull('CONF-5', document.languageCode.code)
		def locale = document.languageCode.code.split(/-/)
		assertTrue('CONF-6', locale[0] in Locale.getISOLanguages())        
		if (locale.length == 2)
			assertTrue('CONF-6', locale[1] in Locale.getISOCountries())
		def versions = document.templateId.findAll{  it.root == '2.16.840.1.113883.10.20.1'}
		assertMaxSize('CONF-7', 1, versions)
		assertTrue('CONF-8', versions[0].extension == null)
		assertMinSize('CONF-11', 1, document.recordTarget)
		assertMaxSize('CONF-11', 2, document.recordTarget)
		
		/* 2.6 From */
		/* CONF-12: ClinicalDocument / author / assignedAuthor / assignedPerson >= 1 and/or
		 *          ClinicalDocument / author / assignedAuthor / representedOrganization >= 1
		 */
		/* CONF-13: If author has an associated representedOrganization
		 * with no assignedPerson or assignedAuthoringDevice,
		 * then the value for “ClinicalDocument / author / assignedAuthor / id / @NullFlavor”
		 * SHALL be “NA” “Not applicable” 2.16.840.1.113883.5.1008 NullFlavor STATIC.
		 */
		//TODO
		/* 2.7 To */
		/* CONF-14: ClinicalDocument / informationRecipient >= 0 */
		//TODO
	}
	
	//    public static def retrievePayersPolicyActivity(POCDMT000040EntryRelationship entryRelationship){
	//        if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__ACT))
	//            return entryRelationship.act
	//        else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__ENCOUNTER))
	//            return entryRelationship.encounter
	//        else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__OBSERVATION))
	//                return entryRelationship.observation
	//            else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__ORGANIZER))
	//                    return entryRelationship.organizer
	//                else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__SUBSTANCE_ADMINISTRATION))
	//                        return entryRelationship.substanceAdministration
	//                    else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__SUPPLY))
	//                            return entryRelationship.supply
	//                        else
	//                            return null
	//    }
	//
}
