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
 * Chapter 3.15.2 "Encounter Activity"
 *  
 * Templates included:
 * <ul>
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.45 Encounter Location
 * <li>2.16.840.1.113883.10.20.1.49 Patient Instruction 
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * <li>                             Information Source
 * </ul>
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDEncounterActivityExtension extends CompositeModelExtension {
	
    CCDEncounterActivityExtension() {
		super()
	}
	
    CCDEncounterActivityExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)
	    
		POCDMT000040Section.metaClass {
			
			setEncounterActivity {POCDMT000040Encounter encounter ->
                delegate.entry.add(builder.build {
                    ccd_entry (typeCode:'DRIV', encounter:encounter)
                })
			}
			
			getEncounterActivity { ->
			    delegate.entry.findAll{ 
			        templateId() in it.encounter?.templateId?.root
			    }?.encounter
			}
		}//encoutner activity section extensions

		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Encounter Activity'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.21'
	}	
	
	Collection modelExtensions() {
		[ new CCDEncounterLocationExtension(),
		  new CCDPatientInstructionExtension(),
		  new CCDSourceExtension(),
		  new CCDCommentsExtension()]
	}	
}
