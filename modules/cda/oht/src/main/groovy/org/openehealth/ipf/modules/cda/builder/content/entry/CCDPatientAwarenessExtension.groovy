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
 * Chapter 3.5.2.4: "Patient awareness of a problem"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.48 Patient Awareness
 * </ul>
 * Dependent templates:
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDPatientAwarenessExtension extends CompositeModelExtension {
	
     CCDPatientAwarenessExtension() {
		super()
	 }
	
     CCDPatientAwarenessExtension(builder) {
		super(builder)
	 }
	
	def register(Collection registered) {
	    
	   super.register(registered)
	   
		// Extension required by ProblemAct (2.16.840.1.113883.10.20.1.11)
       POCDMT000040Act.metaClass {
	       
           setPatientAwareness{ POCDMT000040Participant2 participant ->
               delegate.participant.add(participant)                
           }
   
           getPatientAwareness { ->
               delegate.participant.find{ 
                   templateId() in it.templateId.root}
           }
	   
	    }//problem act extensions
		
	} 	
	
	String extensionName() {
		'CCD Patient Awareness'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.48'
	}
}
