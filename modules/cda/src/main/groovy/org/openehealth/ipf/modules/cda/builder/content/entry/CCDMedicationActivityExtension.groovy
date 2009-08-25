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
 * 3.9.2.1.1: "Medication activity"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.24 Medication Activity
 * <li>2.16.840.1.113883.10.20.1.46 Medication Series Number Observation
 * </ul>
 * Dependencies:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.47 Medication Status Observation
 * <li>2.16.840.1.113883.10.20.1.53 Product
 * <li>2.16.840.1.113883.10.20.1.52 Product Instance
 * <li>2.16.840.1.113883.10.20.1.27 Problem Act
 * <li>2.16.840.1.113883.10.20.1.57 Status Observation
 * <li>                             Source
 * <li>2.16.840.1.113883.10.20.1.40 Comments 
 * </ul>
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDMedicationActivityExtension extends CompositeModelExtension {
	
     CCDMedicationActivityExtension() {
		super()
	}
	
     CCDMedicationActivityExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
	    super.register(registered)
	
		POCDMT000040Section.metaClass {
			setMedicationActivity  {POCDMT000040SubstanceAdministration substAdmin ->
				delegate.entry.add(builder.build{
				    ccd_entry(substanceAdministration:substAdmin)
				})
			}
			
			getMedicationActivity { ->
				delegate.entry?.findAll{
					templateId() in it.substanceAdministration?.templateId?.root
				}?.substanceAdministration
			}
		}
		
		POCDMT000040SubstanceAdministration.metaClass {
			
			setIndication{ POCDMT000040EntryRelationship rel ->
				rel.typeCode = XActRelationshipEntryRelationship.RSON_LITERAL
				delegate.entryRelationship.add(rel)
			}
			
			getIndication{ ->
				delegate.entryRelationship.findAll {
					it.typeCode?.name == 'RSON'
				}
			}
			
			setSeriesNumber{ int number ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ') {
						observation(classCode:'OBS', moodCode:'EVN') {
							templateId('2.16.840.1.113883.10.20.1.46')
							statusCode('completed')
							code(code:'30973-2', 
									codeSystem:'2.16.840.1.113883.6.1',
									displayName:'Dose number')
							value(builder.build { _int(number) })
						}
					}
				})
			}
			
			getSeriesNumber{ ->
				delegate.entryRelationship.find{ 
					'2.16.840.1.113883.10.20.1.46' in it.observation?.templateId?.root
				}?.observation?.value?.value			    
			}
			
			setManufacturedProduct{ POCDMT000040ManufacturedProduct mp ->
				delegate.consumable = builder.build { consumable(manufacturedProduct:mp) }
			}
			
			getManufacturedProduct{ -> 
			    delegate.consumable?.manufacturedProduct 
			}
		}
		
		// required by Medication activity (2.16.840.1.113883.10.20.1.24)
		POCDMT000040EntryRelationship.metaClass {
			
			setProblemAct { POCDMT000040Act act ->
				delegate.act = act
			}
			
			getProblemAct { -> delegate.act }
			
			setProblemObservation{ POCDMT000040Observation observation ->
				delegate.observation = observation
			}
			
			getProblemObservation { -> delegate.observation }
			
		}

        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
			setMedicationActivity  {POCDMT000040SubstanceAdministration substAdmin ->
				delegate.entryRelationship.add(builder.build{
				    entryRelationship(typeCode:'COMP', substanceAdministration:substAdmin)
				})
			}

			getMedicationActivity { ->
				delegate.entryRelationship.findAll{
					templateId() in it.substanceAdministration?.templateId.root
				}?.observation
		    }
        }

        POCDMT000040Observation.metaClass {
			setMedicationActivity  {POCDMT000040SubstanceAdministration substAdmin ->
				delegate.entryRelationship.add(builder.build{
				    entryRelationship(typeCode:'COMP', substanceAdministration:substAdmin)
				})
			}

			getMedicationActivity { ->
				delegate.entryRelationship.findAll{
					templateId() in it.substanceAdministration?.templateId.root
				}?.observation
		    }
        }

        POCDMT000040Procedure.metaClass {
			setMedicationActivity  {POCDMT000040SubstanceAdministration substAdmin ->
				delegate.entryRelationship.add(builder.build{
				    entryRelationship(typeCode:'COMP', substanceAdministration:substAdmin)
				})
			}

			getMedicationActivity { ->
				delegate.entryRelationship.findAll{
					templateId() in it.substanceAdministration?.templateId.root
				}?.observation
		    }
        }
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Medication Activity'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.24'
	}
	
	Collection modelExtensions() {
	    [ new CCDMedicationStatusExtension(),
	      new CCDProblemActExtension(),
	      new CCDPatientInstructionExtension(),
	      new CCDProductExtension(),
          new CCDProductInstanceExtension(),
          new CCDReactionObservationExtension(),
          new CCDSourceExtension(),
          new CCDCommentsExtension()]
	}


}
