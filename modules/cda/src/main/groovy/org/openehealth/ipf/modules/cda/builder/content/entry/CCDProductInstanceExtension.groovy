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
import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.BaseModelExtension


/**
 * Chapter 3.14.2.2: "Procedure related products" (2.16.840.1.113883.10.20.1.52)
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProductInstanceExtension extends BaseModelExtension {
	
	CCDProductInstanceExtension() {
		super()
	}
	
	CCDProductInstanceExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
		super.register(registered)
		
		// required by Medication Activity (2.16.840.1.113883.10.20.1.24) 
		POCDMT000040SubstanceAdministration.metaClass {
			
			setProductInstance{ POCDMT000040ParticipantRole role ->             
				delegate.participant.add(builder.build {
					clinicalStatementParticipant(typeCode:'DEV',participantRole:role)
				})
			}
			
			getProductInstance{ ->
				delegate.participant.participant.findAll{
					templateId() in it.participantRole?.templateId?.root
				}?.participantRole
			}
		}
		
		// required by Supply Activity (2.16.840.1.113883.10.20.1.34)
		POCDMT000040Supply.metaClass {
			setProductInstance{ POCDMT000040ParticipantRole role ->             
				delegate.participant.add(builder.build {
					clinicalStatementParticipant(typeCode:'DEV', participantRole:role)
				})
			}
			
			getProductInstance{ ->
				delegate.participant.findAll{
					templateId() in it.participantRole?.templateId?.root
				}?.participantRole
			}
		}
		
		// Extension required by Procedure Activity Act (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
			setProductInstance{ POCDMT000040ParticipantRole role ->             
				delegate.participant.add(builder.build {
					clinicalStatementParticipant(typeCode:'DEV', participantRole:role)
				})
			}
			
			getProductInstance{ ->
				delegate.participant.findAll{
					templateId() in it.participantRole?.templateId?.root
				}?.participantRole
			}
		}
		
		// Extension required by Procedure Activity Observation (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Observation.metaClass {
			setProductInstance{ POCDMT000040ParticipantRole role ->             
				delegate.participant.add(builder.build {
					clinicalStatementParticipant(typeCode:'DEV', participantRole:role)
				})
			}
			
			getProductInstance{ ->
				delegate.participant.findAll{
					templateId() in it.participantRole?.templateId?.root
				}?.participantRole
			}
		}
		
		// Extension required by Procedure Activity Procedure (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Procedure.metaClass {
			setProductInstance{ POCDMT000040ParticipantRole role ->             
				delegate.participant.add(builder.build {
					clinicalStatementParticipant(typeCode:'DEV', participantRole:role)
				})
			}
			
			getProductInstance{ ->
				delegate.participant.findAll{
					templateId() in it.participantRole?.templateId?.root
				}?.participantRole
			}
		}
		
	} 	
	
	String extensionName() {
		'CCD Product Instance'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.52'
	}	
}
