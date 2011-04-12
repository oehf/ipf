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
 * Chapter 3.6.2.4: "Representation of age" (2.16.840.1.113883.10.20.1.38)
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDAgeObservationExtension extends BaseModelExtension {
	
	CCDAgeObservationExtension() {
		super()
	}
	
	CCDAgeObservationExtension(builder) {
		super(builder)
	}
	
    def register(Collection registered) {
        
        super.register(registered)
		
		// required by Family History (2.16.840.1.113883.10.20.1.22)
        // required by Problem Observation (2.16.840.1.113883.10.20.1.28)
		// required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Observation.metaClass {
		    
			setAge {POCDMT000040Observation observation ->
				POCDMT000040EntryRelationship rel = builder.build {
					entryRelationship(inversionInd:true, 
					                    typeCode:'SUBJ', 
					                    observation:observation)
				}
				delegate.entryRelationship.add(rel)
			}
			
			getAge { ->
				delegate.entryRelationship.find {
				    templateId() in it.observation?.templateId?.root
				}?.observation            
			}
		}
		
		// required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
            
            setAge {POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship rel = builder.build {
                    entryRelationship(inversionInd:true, 
                                        typeCode:'SUBJ', 
                                        observation:observation)
                }
                delegate.entryRelationship.add(rel)
            }
            
            getAge { ->
                delegate.entryRelationship.find { 
                    templateId() in it.observation?.templateId?.root
                }?.observation            
            }
        }
		
		
		// required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
        POCDMT000040Procedure.metaClass {
            
            setAge {POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship rel = builder.build {
                    entryRelationship(inversionInd:true, 
                                        typeCode:'SUBJ', 
                                        observation:observation)
                }
                delegate.entryRelationship.add(rel)
            }
            
            getAge { ->
                delegate.entryRelationship.find { 
                    templateId() in it.observation?.templateId?.root
                }?.observation            
            }
        }
		
		// required by Encounter Activity (2.16.840.1.113883.10.20.1.21)
        POCDMT000040Encounter.metaClass {
            
            setAge {POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship rel = builder.build {
                    entryRelationship(inversionInd:true, 
                                        typeCode:'SUBJ', 
                                        observation:observation)
                }
                delegate.entryRelationship.add(rel)
            }
            
            getAge { ->
                delegate.entryRelationship.find { 
                    templateId() in it.observation?.templateId?.root
                }?.observation            
            }
        }
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Age Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.38'
	}	
}
