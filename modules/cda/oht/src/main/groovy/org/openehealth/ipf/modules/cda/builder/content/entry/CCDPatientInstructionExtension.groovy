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
 * Chapter 3.9.2.2.2: "Patient Instruction" (2.16.840.1.113883.10.20.1.49)
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDPatientInstructionExtension extends BaseModelExtension {
	
	CCDPatientInstructionExtension() {
		super()
	}
	
	CCDPatientInstructionExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
		super.register(registered)
		
		// Extension required by Medication Activity (templateId 2.16.840.1.113883.10.20.1.24)
		
		POCDMT000040SubstanceAdministration.metaClass {
			setPatientInstruction{ POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ',act:act)
				})
			}
			
			getPatientInstruction { ->
				delegate.entryRelationship.findAll{ 
					templateId() in it.act?.templateId?.root
				}?.act
			}
		}
		
		// Extension required by Encounter Activity (2.16.840.1.113883.10.20.1.33)
		POCDMT000040Encounter.metaClass {
			setPatientInstruction{ POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ',act:act)
				})
			}
			
			getPatientInstruction { ->
				delegate.entryRelationship.findAll{ 
					templateId() in it.act?.templateId?.root
				}?.act
			}
		}


        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
			setPatientInstruction{ POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ',act:act)
				})
			}

			getPatientInstruction { ->
				delegate.entryRelationship.findAll{
					templateId() in it.act?.templateId?.root
				}?.act
			}
		}

        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Observation.metaClass {
			setPatientInstruction{ POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ',act:act)
				})
			}

			getPatientInstruction { ->
				delegate.entryRelationship.findAll{
					templateId() in it.act?.templateId?.root
				}?.act
			}
        }

        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Procedure.metaClass {
			setPatientInstruction{ POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ',act:act)
				})
			}

			getPatientInstruction { ->
				delegate.entryRelationship.findAll{
					templateId() in it.act?.templateId?.root
				}?.act
			}
        }
	} 	
	
	String extensionName() {
		'CCD Patient Instruction'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.49'
	}	
}
