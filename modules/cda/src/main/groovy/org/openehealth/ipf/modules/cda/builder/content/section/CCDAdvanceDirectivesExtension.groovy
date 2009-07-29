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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.BaseModelExtension


/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDAdvanceDirectivesExtension extends BaseModelExtension {
	
	CCDAdvanceDirectivesExtension() {
		super()
	}
	
	CCDAdvanceDirectivesExtension(builder) {
		super(builder)
	}
	
	def extensions = {
		
		// --------------------------------------------------------------------------------------------
		// Chapter 3.2 "Advance Directives"
		// --------------------------------------------------------------------------------------------
		
		POCDMT000040StructuredBody.metaClass {
			setAdvanceDirectives { POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			
			getAdvanceDirectives  { ->
				delegate.component.find { it.section.code.code == '42348-3'
				} ?.section
			}
		} //advance directives body extensions
		
		POCDMT000040Section.metaClass {
			setObservation { POCDMT000040Observation observation ->
				POCDMT000040Entry entry = builder.build {
					entry {
						typeCode('DRIV')
					}
				}
				entry.observation = observation
				delegate.entry.add(entry)
			}
			
			getObservation { ->
				delegate.entry.observation
			}
		}// advance directives extensions
		
		POCDMT000040Observation.metaClass {
			setVerifier  { POCDMT000040Participant2 participant ->
				delegate.participant.add(participant)
			}
			getVerifier { ->
				delegate.participant
			}
			
			// CONF-509: A status observation SHALL be the target of an entryRelationship whose value for
			//           “entryRelationship / @typeCode” SHALL be “REFR” 2.16.840.1.113883.5.1002
			//           ActRelationshipType STATIC.
			setAdvanceDirectiveStatus { POCDMT000040Observation observationStatus ->
				POCDMT000040EntryRelationship rell = builder.build {
					entryRelationship {
						typeCode('REFR')
					}
				}
				rell.observation = observationStatus
				delegate.entryRelationship.add(rell)
			}
			
			getAdvanceDirectiveStatus { ->
			    delegate.entryRelationship.find {
			        '2.16.840.1.113883.10.20.1.37' in it.observation.templateId.root
			    }?.observation
			}
			
			// CONF-101: An advance directive reference (templateId 2.16.840.1.113883.10.20.1.36) SHALL be
			//           represented with Observation / reference / ExternalDocument.
			// CONF-103: The value for “Observation / reference / @typeCode” in an advance directive reference SHALL be
			//           “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
			setReference{ POCDMT000040ExternalDocument document ->
				POCDMT000040Reference ref = builder.build {
					reference {
						typeCode('REFR')
					}
				} 
				ref.externalDocument = document
				delegate.reference.add(ref)
			}
			
			getReference { ->
				delegate.reference
			}
		}// advance directives observations extensions
		
		return 1
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Advance Directives'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.1'
	}	
}
