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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.openhealthtools.ihe.common.cdar2.*import org.openehealth.ipf.modules.cda.builder.BaseModelExtension

/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Christian Ohr
 */
public class CCDFamilyHistoryExtension extends BaseModelExtension {
	
	CCDFamilyHistoryExtension() {
		super()
	}
	
	CCDFamilyHistoryExtension(builder) {
		super(builder)
	}
	
	def extensions = {
		
	        // --------------------------------------------------------------------------------------------
	        // Chapter 3.6 "Family History"
	        // --------------------------------------------------------------------------------------------

	        POCDMT000040StructuredBody.metaClass {
	            // We assume that this is a CCD Family History section, enforced by the builder
	            setFamilyHistory  {POCDMT000040Section section ->
	                POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
	                component.section = section
	                delegate.component.add(component)
	            }
	            getFamilyHistory  { ->
	                delegate.component.find { it.section.code.code == '10157-6' } ?.section
	            }
	        }
	        
	        POCDMT000040Section.metaClass {
	            setObservation  {POCDMT000040Observation observation ->
	                POCDMT000040Entry entry = builder.build {
	                    entry {
	                        typeCode('DRIV')
	                    }
	                }
	                entry.observation = observation
	                delegate.entry.add(entry)
	            }

	            getObservation { ->
	                delegate.entry.observation
	            }
	            
	            setFamilyMember  {POCDMT000040Organizer organizer ->
	                POCDMT000040Entry entry = builder.build {
	                    entry {
	                        typeCode('DRIV')
	                    }
	                }
	                entry.organizer = organizer
	                delegate.entry.add(entry)
	            }

	            getFamilyMember { ->
	                delegate.entry.organizer
	            }
	            
	            setCauseOfDeath  {POCDMT000040Observation observation ->
	                POCDMT000040Entry entry = builder.build {
	                    entry {
	                        typeCode('DRIV')
	                    }
	                }
	                entry.observation = observation
	                delegate.entry.add(entry)
	            }

	            getCauseOfDeath { ->
	                delegate.entry.observation
	            }            
	        }
	        
	        POCDMT000040Observation.metaClass {
	            setFamilyMember  {POCDMT000040RelatedSubject relatedSubject ->
	                POCDMT000040Subject subject = CDAR2Factory.eINSTANCE.createPOCDMT000040Subject()
	                subject.relatedSubject = relatedSubject
	                delegate.subject = subject
	                relatedSubject
	            }
	            getFamilyMember { ->
	                delegate.subject.relatedSubject
	            } 
	            setCause  {POCDMT000040Observation observation ->
	                POCDMT000040EntryRelationship rel = builder.build {
	                    entryRelationship {
	                        typeCode('CAUS')
	                    }
	                }
	                rel.observation = observation
	                delegate.entryRelationship.add(rel)
	            }
	            getCause { ->
	            	delegate.entryRelationship.find { it.typeCode == 'CAUS' }.observation
	            }
	            
	            setAge {POCDMT000040Observation observation ->
	                POCDMT000040EntryRelationship rel = builder.build {
	                    entryRelationship(inversionInd:true, typeCode:'SUBJ')
	                }
	                rel.observation = observation
	                delegate.entryRelationship.add(rel)
	            }
	            getAge { ->
	        	    delegate.entryRelationship.find { it.typeCode == 'SUBJ' }.observation            
	            }
	        }
	        
	        POCDMT000040Organizer.metaClass {
	            setFamilyPerson  {POCDMT000040RelatedSubject relatedSubject ->
	                POCDMT000040Subject subject = CDAR2Factory.eINSTANCE.createPOCDMT000040Subject()
	                subject.relatedSubject = relatedSubject
	                delegate.subject = subject
	                relatedSubject
	            }
	            getFamilyPerson { ->
	                delegate.subject.relatedSubject
	            }   
	            
	            setCauseOfDeath { POCDMT000040Observation observation -> 
	                POCDMT000040Component4 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component4()
	                component.observation = observation
	                delegate.component.add(component)
	                observation                
	            }
	            getCauseOfDeath { ->
	                delegate.component.find { 
	                    it.observation?.templateId[0] == '2.16.840.1.113883.10.20.1.42' 
	                }.observation
	            }
	            
	            setObservation  {POCDMT000040Observation observation ->
	                POCDMT000040Component4 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component4()
	                component.observation = observation
	                delegate.component.add(component)
	                observation                
	            }

	        	getObservation { ->
	        	    delegate.component.find { 
	        	        it.observation?.templateId[0] == '2.16.840.1.113883.10.20.1.22' 
	        	    }.observation
	        	}
	        }
	        
	        return 1
	}
	
	
	String extensionName() {
		'CCD Family History'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.4'
	}	
}
