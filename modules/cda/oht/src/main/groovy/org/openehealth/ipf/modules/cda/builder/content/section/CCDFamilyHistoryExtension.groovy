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
package org.openehealth.ipf.modules.cda.builder.content.section

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.*


/**
 * Chapter 3.6 "Family History"
 *  
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.22 Family History Observation
 * <li>2.16.840.1.113883.10.20.1.23 Family History Organizer
 * <li>2.16.840.1.113883.10.20.1.42 Family History Cause Of Death Observation
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.38 Age Observation
 * <li>2.16.840.1.113883.10.20.1.50 Problem Status Observation
 * <li>2.16.840.1.113883.10.20.1.40 Comment
 * <li>                             Information Source 
 * </ul>
 * 
 * @author Christian Ohr
 */
public class CCDFamilyHistoryExtension extends CompositeModelExtension {
	
	CCDFamilyHistoryExtension() {
		super()
	}
	
	CCDFamilyHistoryExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
		super.register(registered)

        POCDMT000040ClinicalDocument.metaClass {
            setFamilyHistory { POCDMT000040Section section ->
                if (delegate.component?.structuredBody){
                    delegate.component.structuredBody.component.add(builder.build {
                        sections(section:section)
                    })
                } else {
                    delegate.component = builder.build {
                        ccd_component{
                            structuredBody {
                                component(section:section)
                            }
                        }
                    }
                }
            }
            getFamilyHistory { ->
                delegate.component?.structuredBody?.component?.find {
                    it.section?.code?.code == '10157-6'
                } ?.section
            }
        }
		
		POCDMT000040StructuredBody.metaClass {
			setFamilyHistory  {POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			getFamilyHistory  { ->
				delegate.component.find { 
				    it.section?.code?.code == '10157-6' 
				} ?.section
			}
		}
		
		POCDMT000040Section.metaClass {
			setFamilyHistoryObservation  {POCDMT000040Observation observation ->
			    delegate.entry.add(builder.build {
					ccd_entry(typeCode:'DRIV', observation:observation)
				})
			}
			
			getFamilyHistoryObservation { ->
				delegate.entry?.observation?.findAll{
                    '2.16.840.1.113883.10.20.1.22' in it.templateId.root
                }?.observation

			}
			
			setFamilyMember  {POCDMT000040Organizer organizer ->
			    delegate.entry.add(builder.build {
					ccd_entry(typeCode:'DRIV', organizer:organizer)
				})
			}
			
			getFamilyMember { ->
				delegate.entry?.organizer?.findAll{
                    '2.16.840.1.113883.10.20.1.23' in  it.templateId.root
                }
			}
			
			setCauseOfDeath  {POCDMT000040Observation observation ->
			    delegate.entry.add(builder.build {
					ccd_entry (typeCode:'DRIV', observation:observation)
				})
			}
			
			getCauseOfDeath { ->
				delegate.entry?.observation
			}            
		}
		
		POCDMT000040Observation.metaClass {
			setFamilyMember  {POCDMT000040RelatedSubject relatedSubject ->
			    delegate.subject = builder.build {
			        subject(relatedSubject:relatedSubject)
			    }
			}
			getFamilyMember { ->
				delegate.subject?.relatedSubject
			} 
			setCause  {POCDMT000040Observation observation ->
			    delegate.entryRelationship.add(builder.build {
					ccd_entryRelationship (typeCode:'CAUS', observation:observation)
				})
			}
			getCause { ->
				delegate.entryRelationship.findAll {
				    it.typeCode.name == 'CAUS'
				}?.observation
			}
		}
		
		POCDMT000040Organizer.metaClass {
			setFamilyPerson  {POCDMT000040RelatedSubject relatedSubject ->
		        delegate.subject = builder.build {
		            subject(relatedSubject:relatedSubject)
		    	}
			}
			getFamilyPerson { ->
				delegate.subject?.relatedSubject
			}   
			
			setCauseOfDeath { POCDMT000040Observation observation -> 
				delegate.component.add(builder.build {
				    ccd_organizerComponents(observation:observation)				
				})               
			}
			getCauseOfDeath { ->
				delegate.component.findAll {
					'2.16.840.1.113883.10.20.1.42' in it.observation?.templateId?.root
				}?.observation
			}
			
			setFamilyHistoryObservation  {POCDMT000040Observation observation ->
				delegate.component.add(builder.build {
				    ccd_organizerComponents(observation:observation)				
				})               
			}
			
			getFamilyHistoryObservation { ->
				delegate.component.find { 
					'2.16.840.1.113883.10.20.1.22' in it.observation?.templateId?.root  
				}?.observation
			}
		}
		
	}
	
	
	String extensionName() {
		'CCD Family History'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.4'
	}
	
	Collection modelExtensions() {
		[ new CCDAgeObservationExtension(),
		  new CCDProblemObservationExtension(),
		  new CCDCommentsExtension(),
		  new CCDSourceExtension()
		]
	}
}
