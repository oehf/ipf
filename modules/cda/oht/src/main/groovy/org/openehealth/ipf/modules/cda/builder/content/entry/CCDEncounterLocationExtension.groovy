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

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.BaseModelExtension


/**
 * Chapter 3.15.2.2 "Encounter location"
 *
 * Template Definitions
 *      Encounter Location ((2.16.840.1.113883.10.20.1.45)
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDEncounterLocationExtension extends BaseModelExtension {
	
	CCDEncounterLocationExtension() {
		super()
	}
	
	CCDEncounterLocationExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)
	    
		// required by Encounter Activity (2.16.840.1.113883.10.20.1.3)
		POCDMT000040Encounter.metaClass {
			
			setEncounterLocation {POCDMT000040ParticipantRole role ->
				POCDMT000040Participant2 participant = builder.build{ 
				    ccd_encounterLocationParticipant(participantRole:role)
				}
				delegate.participant.add(participant)
			}
			
			getEncounterLocation { ->
				delegate.participant.findAll{ 
					templateId() in it.templateId?.root
				}?.participantRole
			}
		}//encoutner encounter location extensions

		// required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
			
			setEncounterLocation {POCDMT000040ParticipantRole role ->
				POCDMT000040Participant2 participant = builder.build{ 
				    ccd_encounterLocationParticipant(participantRole:role)
				}
				delegate.participant.add(participant)
			}
			
			getEncounterLocation { ->
				delegate.participant.findAll{ 
					templateId() in it.templateId?.root
				}?.participantRole
			}
		}//act encounter location extensions
		
		// required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Observation.metaClass {
			
			setEncounterLocation {POCDMT000040ParticipantRole role ->
				POCDMT000040Participant2 participant = builder.build{ 
				    ccd_encounterLocationParticipant(participantRole:role)
				}
				delegate.participant.add(participant)
			}
			
			getEncounterLocation { ->
				delegate.participant.findAll{ 
					templateId() in it.templateId?.root
				}?.participantRole
			}
		}//observation encounter location extensions
		
		// required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Procedure.metaClass {
			
			setEncounterLocation {POCDMT000040ParticipantRole role ->
				POCDMT000040Participant2 participant = builder.build{ 
				    ccd_encounterLocationParticipant(participantRole:role) 
				}
				delegate.participant.add(participant)
			}
			
			getEncounterLocation { ->
				delegate.participant.findAll{ 
					templateId() in it.templateId?.root
				}?.participantRole
			}
		}//observation encounter location extensions
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Encounter Location'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.45'
	}	
}
