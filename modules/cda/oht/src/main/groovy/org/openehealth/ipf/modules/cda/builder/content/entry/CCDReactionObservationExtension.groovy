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
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension


/**
 * 3.8.2.4 "Reaction observations and interventions"
 * 
 * Template Definitions:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.54 Reaction Observation
 * <li>2.16.840.1.113883.10.20.1.55 Severity Observation
 * </ul>
 *
 * Dependencies:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.29 Procedure Activity
 * <li>2.16.840.1.113883.10.20.1.24 Medication Activity
 * </ul>
 * 
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDReactionObservationExtension extends CompositeModelExtension {
	
	CCDReactionObservationExtension() {
		super()
	}
	
	CCDReactionObservationExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
		super.register(registered)
		
		//required by Medication Activity (2.16.840.1.113883.10.20.1.24)
		POCDMT000040SubstanceAdministration.metaClass {
		    setReactionObservation { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'CAUS', observation:observation)
                })
		    }
        
		    getReactionObservation{ ->
		        delegate.entryRelationship.findAll {
		            templateId() in it.observation?.templateId?.root
		        }?.observation
		    }
		}
		
        //required by Alert Observation (2.16.840.1.113883.10.20.1.18)
		POCDMT000040Observation.metaClass {
			
			setReactionObservation { POCDMT000040Observation observation ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'MFST', observation:observation)
				})
			}
			
			getReactionObservation{ ->
				delegate.entryRelationship.findAll {
					templateId() in it.observation?.templateId?.root
				}?.observation
			}
			
			setSeverityObservation{ POCDMT000040Observation observation ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', observation:observation)
				})
			}
			
			getSeverityObservation { ->
				delegate.entryRelationship?.findAll {
					'2.16.840.1.113883.10.20.1.55' in it.observation?.templateId?.root
				}?.observation
			}
			
			setReactionIntervention{ POCDMT000040EntryRelationship entryRel ->
				delegate.entryRelationship.add(entryRel)
			}
			getReactionIntervention { ->
				delegate.participant.find { it.typeCode == 'RSON' }
			}
			
		}//reaction observation extensions
		
		POCDMT000040EntryRelationship.metaClass{
		    //medication activity reaction intervention
		    setMedicationActivity{POCDMT000040SubstanceAdministration sa ->
                delegate.substanceAdministration = sa
		    }
		    
		    getMedicationActivity{ ->
                delegate.substanceAdministration
		    }
		    //procedure activity act
		    setProcedureActivityAct{POCDMT000040Act act ->
		        delegate.act = act
		    }
        
		    setProcedureActivityAct{ ->
                delegate.act
		    }
		    //procedure activity observation
            setProcedureActivityObservation{POCDMT000040Observation obs ->
                delegate.observation = obs
            }
        
            setProcedureActivityObservation{ ->
                delegate.observation
            }
            //procedure activity procedure
            setProcedureActivityProcedure{POCDMT000040Procedure proc ->
                delegate.procedure = proc
            }
        
            getProcedureActivityProcedure{ ->
                delegate.procedure
            }
		}//reaction intervention extension
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Reaction Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.54'
	}
	
    Collection modelExtensions() {
        [ new CCDSourceExtension(),
          new CCDCommentsExtension()]
    }
}
