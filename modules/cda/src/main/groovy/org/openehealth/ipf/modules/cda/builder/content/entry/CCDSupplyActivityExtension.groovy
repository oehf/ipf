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
import org.openehealth.ipf.modules.cda.builder.content.entry.*


/**
 * Chapter 3.9.2.1.2: "Supply activity"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.24 Supply Activity
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.47 Medication Status Observation
 * <li>2.16.840.1.113883.10.20.1.53 Product
 * <li>2.16.840.1.113883.10.20.1.52 Product Instance 
 * <li>2.16.840.1.113883.10.20.1.57 Status Observation
 * <li>                             Source
 * <li>2.16.840.1.113883.10.20.1.40 Comments 
 * </ul>
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDSupplyActivityExtension extends CompositeModelExtension {
	
     CCDSupplyActivityExtension() {
		super()
	}
	
     CCDSupplyActivityExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
	    super.register(registered)
	
		POCDMT000040Section.metaClass {
			
			setSupplyActivity  {POCDMT000040Supply supply ->
				POCDMT000040Entry entry = CDAR2Factory.eINSTANCE.createPOCDMT000040Entry()
				entry.supply = supply
				delegate.entry.add(entry)
			}
			
			getSupplyActivity { ->
				delegate.entry.findAll{ 
					templateId() in it.supply?.templateId?.root
				}?.supply
			}
		}
		
	    POCDMT000040Supply.metaClass {
			
			setFulfillmentInstruction{ POCDMT000040Act act ->           
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ',act:act)
				})
			}
			
			getFulfillmentInstruction { ->
				delegate.entryRelationship.find {
					'2.16.840.1.113883.10.20.1.43' in it.observation?.templateId.root
				}?.observation
			}
			
			setSupplyLocation { POCDMT000040ParticipantRole role ->				
				delegate.participant.add(builder.build {
					clinicalStatementParticipant(typeCode:'LOC',participantRole:role)
				})
			}
			
			getSupplyLocation { ->
				delegate.participant.findAll{ 'LOC' == it.typeCode }?.participantRole
			}
			
		}
				
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Supply Activity'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.34'
	}
	
	Collection modelExtensions() {
	    [ new CCDProductExtension(),
	      new CCDProductInstanceExtension(),
	      new CCDMedicationStatusExtension(),
          new CCDCommentsExtension(),
          new CCDSourceExtension()]
	}


}
