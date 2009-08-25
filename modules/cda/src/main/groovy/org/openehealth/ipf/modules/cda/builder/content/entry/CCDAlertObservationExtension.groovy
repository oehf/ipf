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
 * Chapter 3.5.2.3 "Alert Observation"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.18 Alert Observation
 * <li>2.16.840.1.113883.10.20.1.39 Alert Status Observation
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.54 Reaction Observation
 * <li>2.16.840.1.113883.10.20.1.57 Status Observation
 * <li>                             Source
 * <li>2.16.840.1.113883.10.20.1.40 Comments 
 * </ul>
 * 
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDAlertObservationExtension extends CompositeModelExtension {
	
     CCDAlertObservationExtension() {
		super()
	 }
	
     CCDAlertObservationExtension(builder) {
		super(builder)
	 }
	
	def register(Collection registered) {
	    
	   super.register(registered)
		
		// Extension required by Problems Act (2.16.840.1.113883.10.20.1.27)
       POCDMT000040Act.metaClass {
	       setAlertObservation{ POCDMT000040Observation observation ->
               delegate.entryRelationship.add(builder.build {
                   ccd_entryRelationship(typeCode:'SUBJ', observation:observation)
               })
	       }
     
	       getAlertObservation {
	           delegate.entryRelationship.findAll{ 
	               templateId() in it.observation.templateId.root 
	           }?.observation
	       }
       }//alerts act extensions
       
       
       //required by Alert Observation (2.16.840.1.113883.10.20.1.18)
       POCDMT000040Observation.metaClass {
           
           setAlertStatus{ POCDMT000040Observation observation ->           
               delegate.entryRelationship.add(builder.build {
                   ccd_entryRelationship(typeCode:'REFR', observation:observation)
               })
           }
           
           getAlertStatus { ->
               delegate.entryRelationship.find {
                   '2.16.840.1.113883.10.20.1.39' in it.observation.templateId.root
               }?.observation
           }
                   
           setParticipantAgent{ POCDMT000040ParticipantRole participantRole ->
               delegate.participant.add(builder.build{
                   clinicalStatementParticipant(typeCode:'CSM', 
                           participantRole:participantRole)
               })                
           }
   
           getParticipantAgent { ->
               delegate.participant.findAll {
                   it.typeCode.name == 'CSM'
               }?.participantRole
           }
           
       }//alerts observation extensions
		
	} 	
	
	String extensionName() {
		'CCD Alert Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.18'
	}
	
    Collection modelExtensions() {
        [ new CCDReactionObservationExtension(),
          new CCDSourceExtension(),
          new CCDCommentsExtension()]
    }
}
