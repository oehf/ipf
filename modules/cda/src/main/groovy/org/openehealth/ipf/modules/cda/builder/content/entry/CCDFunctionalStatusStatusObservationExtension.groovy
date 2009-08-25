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
 * 3.4.2.1: "Representation of “status” values"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.44 Functional Status Status Observation
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.57 Status Observation
 * </ul>
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDFunctionalStatusStatusObservationExtension extends BaseModelExtension {
	
     CCDFunctionalStatusStatusObservationExtension() {
		super()
	}
	
     CCDFunctionalStatusStatusObservationExtension(builder) {
		super(builder)
	}
	
    def register(Collection registered) {
        
        super.register(registered)
		
		// required by Functional Status Problem Observation (2.16.840.1.113883.10.20.1.28)
        // required by Functional Status Result Observation (2.16.840.1.113883.10.20.1.31)
        POCDMT000040Observation.metaClass {
            
            setFunctionalStatusStatus { POCDMT000040Observation observation ->             
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR',observation:observation)
                })
            }
            
            getFunctionalStatusStatus { ->
                delegate.entryRelationship.find{
                    templateId() in it.observation?.templateId?.root
                }?.observation
            }
		}
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Functional Status Status Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.44'
	}	
}
