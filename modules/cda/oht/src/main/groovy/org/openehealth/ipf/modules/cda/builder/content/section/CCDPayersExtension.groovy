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

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.*

/**
 * Chapter 3.1 "Payers"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.9  Payers Section
 * <li>2.16.840.1.113883.10.20.1.20 Coverage Activity
 * <li>2.16.840.1.113883.10.20.1.26 Policy Activity
 * <li>2.16.840.1.113883.10.20.1.19 Authorization Activity
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>                             Information Source
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * </ul>
 *
 * @author Christian Ohr
 */
public class CCDPayersExtension extends CompositeModelExtension {
	
	CCDPayersExtension() {
		super()
	}
	
	CCDPayersExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)
	    
	    POCDMT000040ClinicalDocument.metaClass {
	        setPayers { POCDMT000040Section section ->
                if (delegate.component?.structuredBody){
                    delegate.component.structuredBody.component.add(builder.build {
                        sections(section:section)
                    })
                } else {
                    delegate.component = builder.build {
                        ccd_component{
                            structuredBody {
                                component(section:section)
                            }
                        }
                    }
                }
            }
            
	        getPayers{ ->
                delegate.component?.structuredBody?.component?.find { 
                    it.section?.code?.code == '48768-6'
                } ?.section
            }
            
        }
				
		POCDMT000040StructuredBody.metaClass {

			setPayers {POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			getPayers  { ->
				delegate.component.find { 
				    it.section?.code?.code == '48768-6'
				} ?.section
			}
			
		}// payers structured body extensions
		
		POCDMT000040Section.metaClass {
			setCoverageActivity  {POCDMT000040Act act ->
				delegate.entry.add(builder.build {
				    ccd_entry(act:act)
				})
			}
			getCoverageActivity  { ->
				delegate.entry.findAll { 
				    '2.16.840.1.113883.10.20.1.20' in it.act?.templateId?.root
				} ?.act
			}
		}// payers section extensions
		
		POCDMT000040Act.metaClass {
			
			setPolicyActivity {POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					ccd_entryRelationship(typeCode:'COMP', act:act)
				})
			}
			
			getPolicyActivity { ->
				delegate.entryRelationship.findAll {
				    '2.16.840.1.113883.10.20.1.26' in it.act?.templateId?.root
				}?.act
			}
			
			setPayer {POCDMT000040AssignedEntity assignedEntity ->
				delegate.performer.add(builder.build {
					clinicalStatementPerformer(typeCode:'PRF', assignedEntity:assignedEntity)
				})
			}
			
			getPayer  { ->
				delegate.performer.find {
					it.typeCode == ParticipationPhysicalPerformer.PRF_LITERAL
				}?.assignedEntity
			}
			
			setCoveredParty  {POCDMT000040ParticipantRole participantRole ->
				delegate.participant.add(builder.build {
					clinicalStatementParticipant (typeCode:'COV', 
					        participantRole:participantRole)
				})
			}
			
			getCoveredParty  { ->
				delegate.participant.find {
					it.typeCode == ParticipationIndirectTarget.COV_LITERAL
				}?.participantRole
			}
			
			setSubscriber  {POCDMT000040ParticipantRole participantRole ->
				delegate.participant.add(builder.build {
					clinicalStatementParticipant (typeCode:'HLD',
					        participantRole:participantRole)
				})
			}
			
			getSubscriber  { ->
				delegate.participant.find {
					it.typeCode == ParticipationIndirectTarget.HLD_LITERAL
				}?.participantRole
			}
			
			setAuthorizationActivity { POCDMT000040Act act ->
				// CONF-66: The value for “Act / entryRelationship / @typeCode”
				// in a policy activity SHALL be “REFR” 2.16.840.1.113883.5.1002
				// ActRelationshipType STATIC.
				delegate.entryRelationship.add(builder.build {
				    ccd_entryRelationship (typeCode:'REFR', act:act)
				})
			}
			
			getAuthorizationActivity { ->
				delegate.entryRelationship.findAll {
				    '2.16.840.1.113883.10.20.1.19' in it.act?.templateId?.root
				}?.act
			}
			
			setPromise { POCDMT000040EntryRelationship rel ->
                rel.typeCode = XActRelationshipEntryRelationship.SUBJ_LITERAL
				delegate.entryRelationship.add(rel)
			}
			
			getPromise { ->
				delegate.entryRelationship.find {
					it.typeCode == XActRelationshipEntryRelationship.SUBJ_LITERAL
				}
			}
		}//payers act extensions
		
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Payers'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.9'
	}	
	
	Collection modelExtensions() {
        [ new CCDSourceExtension(),
          new CCDCommentsExtension()]
	}	
}
