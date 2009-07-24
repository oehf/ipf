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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.openhealthtools.ihe.common.cdar2.*import org.openehealth.ipf.modules.cda.builder.BaseModelExtension

/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDSupportExtension extends BaseModelExtension {
	
	CCDSupportExtension() {
		super()
	}
	
	CCDSupportExtension(builder) {
		super(builder)
	}
	
	def extensions = {
		
		// --------------------------------------------------------------------------------------------
		// Chapter 3.3 "Support"
		// --------------------------------------------------------------------------------------------
		POCDMT000040ClinicalDocument.metaClass {
			
			// CONF-112: The value for “ClinicalDocument / participant / @typeCode” 
			//           in a next of kin participant SHALL be “IND” “Indirect participant” 
			//           2.16.840.1.113883.5.90 ParticipationType STATIC.
			setNextOfKin {POCDMT000040AssociatedEntity kin ->
				POCDMT000040Participant1 kinParticipant = CDAR2Factory.eINSTANCE.createPOCDMT000040Participant1()
				kinParticipant.typeCode = ParticipationIndirectTarget.IND_LITERAL
				kinParticipant.associatedEntity = kin
				delegate.participant.add(kinParticipant)
			}
			
			getNextOfKin { ->
				delegate.participant.findAll { 
					it.associatedEntity.classCode.name == 'NOK'
				} ?.associatedEntity
			}
			
			// CONF-117: The value for “ClinicalDocument / participant / @typeCode”
			//           in an emergency contact participant SHALL be “IND” “Indirect participant”
			//           2.16.840.1.113883.5.90 ParticipationType STATIC.
			setEmergencyContact{POCDMT000040AssociatedEntity emergencyContact ->
				POCDMT000040Participant1 emergencyParticipant = CDAR2Factory.eINSTANCE.createPOCDMT000040Participant1()
				emergencyParticipant.typeCode = ParticipationIndirectTarget.IND_LITERAL
				emergencyParticipant.associatedEntity = emergencyContact
				delegate.participant.add(emergencyParticipant)
				
			}
			
			getEmergencyContact{ ->
				delegate.participant.findAll { 
					it.associatedEntity.classCode.name == 'ECON'
				} ?.associatedEntity
			}
			
			// CONF-121: The value for “ClinicalDocument / participant / @typeCode” in a 
			//           patient caregiver participant SHALL be “IND” “Indirect participant”
			//           2.16.840.1.113883.5.90 ParticipationType STATIC.
			setCaregiver{POCDMT000040AssociatedEntity caregiver ->
				POCDMT000040Participant1 caregiverParticipant = CDAR2Factory.eINSTANCE.createPOCDMT000040Participant1()
				caregiverParticipant.typeCode = ParticipationIndirectTarget.IND_LITERAL
				caregiverParticipant.associatedEntity = caregiver
				delegate.participant.add(caregiverParticipant)
				
			}
			
			getCaregiver{ ->
				delegate.participant.findAll { 
					it.associatedEntity.classCode.name == 'CAREGIVER'
				} ?.associatedEntity
			}
			
		}// ccd support extensions
		
		return 1
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Support'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1'
	}	
}
