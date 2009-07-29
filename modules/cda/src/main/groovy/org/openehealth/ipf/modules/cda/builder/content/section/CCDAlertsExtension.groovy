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
public class CCDAlertsExtension extends BaseModelExtension {
	
     CCDAlertsExtension() {
		super()
	}
	
	CCDAlertsExtension(builder) {
		super(builder)
	}
	
	def extensions = {
		// --------------------------------------------------------------------------------------------
		// Chapter 3.5 "Alerts"
		// --------------------------------------------------------------------------------------------
		POCDMT000040StructuredBody.metaClass {
			// We assume that this is a CCD Alerts section, enforced by the builder
			setAlerts  {POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			getAlerts  { ->
				delegate.component.find { 
				    it.section.code.code == '48765-2'
				} ?.section
			}
		}//alerts body extensions
		
		POCDMT000040Section.metaClass {
			setProblemAct  {POCDMT000040Act act ->
				POCDMT000040Entry entry = builder.build {
					entry (typeCode:'DRIV')
				}
				entry.act = act
				delegate.entry.add(entry)
			}
			
			getProblemAct { ->
				delegate.entry.findAll{ 
					'2.16.840.1.113883.10.20.1.27' in it.act.templateId.root
				}?.act
			}
		}//alerts section extensions
		
		POCDMT000040Act.metaClass {
			setAlertObservation{ POCDMT000040Observation observation ->
				
				POCDMT000040EntryRelationship entryRelation = builder.build {
					entryRelationship(typeCode:'SUBJ')
				}
				entryRelation.observation = observation
				delegate.entryRelationship.add(entryRelation)
			}
			
			getAlertObservation {
				delegate.entryRelationship.findAll{ 
					'2.16.840.1.113883.10.20.1.18' in it.observation.templateId.root 
				}?.observation
			}
			
		}//alerts act extensions
		
		POCDMT000040Observation.metaClass {
			
			setAlertStatus{ POCDMT000040Observation observation ->           
				POCDMT000040EntryRelationship entryRelation = builder.build {
					entryRelationship(typeCode:'REFR')
				}
				entryRelation.observation = observation
				delegate.entryRelationship.add(entryRelation)
			}
			
			getAlertStatus { ->
				delegate.entryRelationship.find {
					'2.16.840.1.113883.10.20.1.39' in it.observation.templateId.root
				}?.observation
			}
			
			setReactionObservation { POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship entryRelation = builder.build {
                    entryRelationship(typeCode:'MFST')
                }
                entryRelation.observation = observation
                delegate.entryRelationship.add(entryRelation)
			}
        
			getReactionObservation{ ->
                delegate.entryRelationship.findAll {
                    '2.16.840.1.113883.10.20.1.51' in it.observation.templateId.root
                }?.observation
			}
			
			setParticipantAgent{ POCDMT000040ParticipantRole participantRole ->
			    POCDMT000040Participant2 participant = builder.build{
			        clinicalStatementParticipant{
			            typeCode('CSM')
			        }
			    }
			    participant.participantRole = participantRole
			    delegate.participant.add(participant)                
			}
    
			getParticipantAgent { ->
			    delegate.participant.find {
			        it.typeCode == 'CSM'
			    }?.participantRole
			}
			
			setSeverityObservation{ POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship entryRelation = builder.build {
                    entryRelationship(typeCode:'SUBJ')
                }
                entryRelation.observation = observation
                delegate.entryRelationship.add(entryRelation)
			}
			
			getSeverityObservation { ->
			    delegate.entryRelationship.findAll {
			        '2.16.840.1.113883.10.20.1.55' in it.observation.templateId.root
			    }?.observation
			}

			setReactionIntervention{ POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship entryRelation = builder.build {
                    entryRelationship(typeCode:'SUBJ')
                }
                entryRelation.observation = observation
                delegate.entryRelationship.add(entryRelation)
			}

			getReactionIntervention { ->
			    delegate.entryRelationship.findAll {
			        '2.16.840.1.113883.10.20.1.55' in it.observation.templateId.root
			    }?.observation
			}
			
		}//alerts observation extensions
		
		
		return 1
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Alerts'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.2'
	}	
}
