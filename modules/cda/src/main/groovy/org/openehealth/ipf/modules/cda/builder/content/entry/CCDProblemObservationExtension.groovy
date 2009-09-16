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
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension


/**
 * Chapter 3.5.2.1.2 "Problem observation"
 * 
 * Template Definitions:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.28 Problem Observation
 * <li>2.16.840.1.113883.10.20.1.50 Problem Status Observation
 * <li>2.16.840.1.113883.10.20.1.51 Problem Healthstatus Observation
 * </ul>
 * Dependencies:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.38 Age Observation
 * <li>                             Source
 * <li>2.16.840.1.113883.10.20.1.40 Comments
 * </ul>
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProblemObservationExtension extends CompositeModelExtension {
	
     CCDProblemObservationExtension() {
		super()
	}
	
     CCDProblemObservationExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)
	    
	    // Extension required by Medication Activity (2.16.840.1.113883.10.20.1.24)
	    POCDMT000040SubstanceAdministration.metaClass {
            setProblemObservation{ POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship (typeCode:'SUBJ', observation:observation)
                })
            }

            getProblemObservation {
                delegate.entryRelationship.findAll{
                    templateId() in it.observation.templateId.root
                }?.observation
            }
        }//problem act extensions

        // Extension required by Problems Act (2.16.840.1.113883.10.20.1.27)
	    POCDMT000040Act.metaClass {
            setProblemObservation{ POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship (typeCode:'SUBJ', observation:observation)
                })
            }
            
            getProblemObservation {
                delegate.entryRelationship.findAll{ 
                    templateId() in it.observation.templateId.root 
                }?.observation
            }    
        }//problem act extensions
        
        // Extension problem status required also by Family History Observation(2.16.840.1.113883.10.20.1.4)
		POCDMT000040Observation.metaClass {
			
			setProblemStatus{ POCDMT000040Observation observation ->           
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'REFR', observation:observation)
				})
			}
			
			getProblemStatus { ->
				delegate.entryRelationship.find {
					'2.16.840.1.113883.10.20.1.50' in it.observation.templateId.root
				}?.observation
			}
			
			setProblemHealthstatus { POCDMT000040Observation observation ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', observation:observation)
				})
			}
			
			getProblemHealthstatus{ ->
				delegate.entryRelationship.find {
					'2.16.840.1.113883.10.20.1.51' in it.observation.templateId.root
				}?.observation
			}
			
			setPatientAwareness{ POCDMT000040Participant2 participant ->
			    delegate.participant.add(participant)                
			}
    
			getPatientAwareness { ->
                delegate.participant.find{ '2.16.840.1.113883.10.20.1.48' in it.templateId.root}
			}
			
		}//problem observation extensions

        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
			setProblemObservationReason  {POCDMT000040Observation observation ->
				delegate.entryRelationship.add(builder.build {
				    entryRelationship (typeCode:'RSON', observation:observation)
				})
			}

			getProblemObservationReason { ->
				delegate.entryRelationship.findAll{
					templateId() in it.observation?.templateId.root
				}?.observation
		    }
        }

        POCDMT000040Observation.metaClass {
            setProblemObservationReason  {POCDMT000040Observation observation ->
				delegate.entryRelationship.add(builder.build {
				    entryRelationship (typeCode:'RSON', observation:observation)
				})
			}

            getProblemObservationReason { ->
				delegate.entryRelationship.findAll{
					templateId() in it.observation?.templateId.root
				}?.observation
		    }
        }

        POCDMT000040Procedure.metaClass {
            setProblemObservationReason  {POCDMT000040Observation observation ->
				delegate.entryRelationship.add(builder.build {
				    entryRelationship (typeCode:'RSON', observation:observation)
				})
			}

            getProblemObservationReason { ->
				delegate.entryRelationship.findAll{
					templateId() in it.observation?.templateId.root
				}?.observation
		    }
        }
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Problem Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.28'
	}
	
    Collection modelExtensions() {
        [ new CCDAgeObservationExtension(),
          new CCDSourceExtension(),
          new CCDCommentsExtension()]
    }
}
