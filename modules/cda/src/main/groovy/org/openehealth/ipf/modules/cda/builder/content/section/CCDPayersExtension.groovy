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
import org.openhealthtools.ihe.common.cdar2.*import org.openehealth.ipf.modules.cda.builder.BaseModelExtension

/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Christian Ohr
 */
public class CCDPayersExtension extends BaseModelExtension {
	
	CCDPayersExtension() {
		super()
	}
	
	CCDPayersExtension(builder) {
		super(builder)
	}
	
	def extensions = {
		
		// --------------------------------------------------------------------------------------------
		// Chapter 3.1 "Payers"
		// --------------------------------------------------------------------------------------------
		
		POCDMT000040StructuredBody.metaClass {
			// TODO: will be always the same
			setPayers {POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			getPayers  { ->
				delegate.component.find { it.section.code.code == '48768-6'
				} ?.section
			}
		}// payers structured body extensions
		
		POCDMT000040Section.metaClass {
			setCoverageActivity  {POCDMT000040Act act ->
				POCDMT000040Entry entry = CDAR2Factory.eINSTANCE.createPOCDMT000040Entry()
				delegate.entry.add(entry)
				entry.act = act
			}
			getCoverageActivity  { ->
				delegate.entry.find { it.act.code.code == '48768-6'
				} ?.act
			}
		}// payers section extensions
		
		POCDMT000040Act.metaClass {
			
			setPolicyActivity {POCDMT000040Act act ->
				POCDMT000040EntryRelationship rel = builder.build {
					entryRelationship {
						typeCode('COMP')
					}
				}
				delegate.entryRelationship.add(rel)
				rel.act = act
			}
			
			getPolicyActivity { ->
				delegate.entryRelationship.find {
					it.typeCode == XActRelationshipEntryRelationship.COMP_LITERAL
				}?.act
			}
			
			setPayer  {POCDMT000040AssignedEntity assignedEntity ->
				POCDMT000040Performer2 payer = builder.build {
					clinicalStatementPerformer {
						typeCode('PRF')
					}
				}
				delegate.performer.add(payer)
				payer.assignedEntity = assignedEntity
			}
			
			getPayer  { ->
				delegate.performer.find {
					it.typeCode == ParticipationPhysicalPerformer.PRF_LITERAL
				}?.assignedEntity
			}
			
			setCoveredParty  {POCDMT000040ParticipantRole participantRole ->
				POCDMT000040Participant2 coveredParty = builder.build {
					clinicalStatementParticipant {
						typeCode('COV')
					}
				}
				delegate.participant.add(coveredParty)
				coveredParty.participantRole = participantRole
			}
			
			getCoveredParty  { ->
				delegate.participant.find {
					it.typeCode == ParticipationIndirectTarget.COV_LITERAL
				}?.participantRole
			}
			
			setSubscriber  {POCDMT000040ParticipantRole participantRole ->
				POCDMT000040Participant2 subscriber = builder.build {
					clinicalStatementParticipant {
						typeCode('HLD')
					}
				}
				delegate.participant.add(coveredParty)
				coveredParty.participantRole = subscriber
			}
			
			getSubscriber  { ->
				delegate.participant.find {
					it.typeCode == ParticipationIndirectTarget.HLD_LITERAL
				}?.participantRole
			}
			
			setAuthorizationActivity { POCDMT000040Act act1 ->
				// CONF-66: The value for “Act / entryRelationship / @typeCode”
				// in a policy activity SHALL be “REFR” 2.16.840.1.113883.5.1002
				// ActRelationshipType STATIC.
				POCDMT000040EntryRelationship rel1 = CDAR2Factory.eINSTANCE.createPOCDMT000040EntryRelationship()
				rel1.typeCode = XActRelationshipEntryRelationship.REFR_LITERAL
				/*         POCDMT000040EntryRelationship rel1 = builder.build {
				 entryRelationship {
				 typeCode('REFR')
				 }
				 }
				 */         delegate.entryRelationship.add(rel1)
				rel1.act = act1
			}
			
			getAuthorizationActivity { ->
				delegate.entryRelationship.find {
					it.typeCode == XActRelationshipEntryRelationship.REFR_LITERAL
				}?.act
			}
			
			setPromise { POCDMT000040EntryRelationship rel ->
				delegate.entryRelationship.add(rel)
			}
			
			getPromise { ->
				delegate.entryRelationship.find {
					it.typeCode == XActRelationshipEntryRelationship.SUBJ_LITERAL
				}
			}
		}//payers act extensions
		
		return 1
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Payers'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.9'
	}	
}
