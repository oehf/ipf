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

import org.openehealth.ipf.modules.cda.AbstractValidator
import org.openhealthtools.ihe.common.cdar2.*

/**
 * Validates the CCD Immunizations content section module (2.16.840.1.113883.10.20.1.6)
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDImmunizationsValidator extends AbstractValidator {
	
	void validate(Object section, Object profile){
		assertInstanceOf('CONF-376', POCDMT000040Section.class, section)
		assertContains('CONF-376', '2.16.840.1.113883.10.20.1.6', section.templateId.root)
		assertNotNull('CONF-377', section.code)
		assertEquals('CONF-377', '11369-6',section.code.code)
		assertEquals('CONF-378', '2.16.840.1.113883.6.1', section.code.codeSystem)
		assertNotNull('CONF-379', section.title)
		assertMatches('CONF-380', '(?i).*immunization*.', section.title.mixed[0].value)
	}
	
	void doValidateImmunizations(POCDMT000040Section section) {
		section.entry.each { item ->
			if (item.substanceAdministration) {
				new CCDMedicationsValidator().doValidateMedicationActivity(item.substanceAdministration)
			} else if (item.supply) {
			    new CCDMedicationsValidator().doValidateSupplyActivity(item.supply)
			} else {
				fail('CONF-376', 'Immunizations section must have either MedicationActivity or SupplyActivity')
			}
		}
	}
	
}

