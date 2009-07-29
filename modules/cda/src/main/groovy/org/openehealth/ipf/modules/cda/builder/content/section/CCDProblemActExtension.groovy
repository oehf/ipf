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
public class CCDProblemActExtension extends BaseModelExtension {
	
	CCDProblemActExtension() {
		super()
	}
	
	CCDProblemActExtension(builder) {
		super(builder)
	}
	
	def extensions = {
		// --------------------------------------------------------------------------------------------
		// Chapter 3.5.2.1.1 "Problem act"
		// --------------------------------------------------------------------------------------------
		
		POCDMT000040Section.metaClass {
			setProblemAct  {POCDMT000040Act act ->
				POCDMT000040Entry entry = builder.build {
					entry {
						typeCode('DRIV')
					}
				}
				entry.act = act
				delegate.entry.add(entry)
			}
			
			getProblemAct { ->
				delegate.entry.findAll{ 
					'2.16.840.1.113883.10.20.1.27' in it.act.templateId.root
				}?.act
			}
		}//problems section extensions
		
		POCDMT000040Act.metaClass {
			setProblemObservation{ POCDMT000040Observation observation ->
				
				POCDMT000040EntryRelationship entryRelation = builder.build {
					entryRelationship {
						typeCode('SUBJ')
					}
				}
				entryRelation.observation = observation
				delegate.entryRelationship.add(entryRelation)
			}
			
			getProblemObservation {
				delegate.entryRelationship.findAll{ 
					'2.16.840.1.113883.10.20.1.28' in it.observation.templateId.root 
				}?.observation
			}
			
			setEpisodeObservation { POCDMT000040Observation observation ->
			    POCDMT000040EntryRelationship entryRelation = builder.build {
			        entryRelationship{
			            typeCode('SUBJ')
			        }
			    }
			    entryRelation.observation = observation
			    delegate.entryRelationship.add(entryRelation)
			}
        
			getEpisodeObservation{ ->
                delegate.entryRelationship.find {
                    '2.16.840.1.113883.10.20.1.41' in it.observation.templateId.root
                }?.observation
			}
			
			setPatientAwareness{ POCDMT000040Participant2 participant ->
                delegate.participant.add(participant)                
			}
        
			getPatientAwareness { ->
                delegate.participant.find{ '2.16.840.1.113883.10.20.1.48' in it.templateId.root}
			}
		}//problems act extensions
		
		POCDMT000040Observation.metaClass {
			
			setProblemStatus{ POCDMT000040Observation observation ->           
				POCDMT000040EntryRelationship entryRelation = builder.build {
					entryRelationship{
					    typeCode('REFR')
					}
				}
				entryRelation.observation = observation
				delegate.entryRelationship.add(entryRelation)
			}
			
			getProblemStatus { ->
				delegate.entryRelationship.find {
					'2.16.840.1.113883.10.20.1.50' in it.observation.templateId.root
				}?.observation
			}
			
			setProblemHealthstatus { POCDMT000040Observation observation ->
				POCDMT000040EntryRelationship entryRelation = builder.build {
					entryRelationship{
					}
				}
				entryRelation.observation = observation
				delegate.entryRelationship.add(entryRelation)
			}
			
			getProblemHealthstatus{ ->
				delegate.entryRelationship.findAll {
					'2.16.840.1.113883.10.20.1.51' in it.observation.templateId.root
				}?.observation
			}
			
			setPatientAwareness{ POCDMT000040Participant2 participant ->
			    delegate.participant.add(participant)                
			}
    
			getPatientAwareness { ->
                delegate.participant.find{ '2.16.840.1.113883.10.20.1.48' in it.templateId.root}
			}
			
		}//problems observation extensions
		
		
		return 1
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Problems Problem Act'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.11'
	}	
}
