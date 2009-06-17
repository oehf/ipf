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
package org.openehealth.ipf.modules.cda.builder

import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*

/**
 * @author Stefan Ivanov
 */
public class CDAR2BuilderStructureObservationMediaTest extends AbstractCDAR2BuilderTest {
	
	
	/**
	 * Test simple ObservationMedia
	 */
	public void testObservationMedia() {
		def entry = builder.build {
			entry{
				observationMedia(classCode:'OBS', moodCode:'EVN'){
					id()
					value(mediaType:'image/gif'){ reference(value:'lefthand.gif') }
				}//observation media
			}//entry
		}
		assertEquals 'OBS',entry.observationMedia.classCode.name
	}
	
	/**
	 * Test encounter defaults
	 */
	public void testObservationMediaDefaultValues() {
		def entry = builder.build {
			entry{
				observationMedia(){
					value(mediaType:'image/gif'){ 
					    reference(value:'lefthand.gif') 
				    }//value
				}//observation media
			}//entry
		}
		
		assertEquals 'EVN',entry.observationMedia.moodCode.name
		assertEquals null,entry.observationMedia.classCode
		assertEquals 0, entry.observationMedia.entryRelationship.size
		assertEquals 'image/gif', entry.observationMedia.value.mediaType
	}
	
	
	public void testObservationMediaAsEntryRelationship(){
		def entry = builder.build {
			entry{
				observation(classCode:'OBS', moodCode:'EVN'){
					code( code:'271807003', codeSystem:'2.16.840.1.113883.6.96', codeSystemName:'SNOMED CT', displayName:'Rash')
					statusCode(code:'completed')
					methodCode(code:'32750006', codeSystem:'2.16.840.1.113883.6.96', codeSystemName:'SNOMED CT', displayName:'Inspection')
					targetSiteCode(code:'48856004', codeSystem:'2.16.840.1.113883.6.96', codeSystemName:'SNOMED CT', displayName:'Skin of palmer surface of index finger'){
						qualifier{
							name(code:'78615007', codeSystem:'2.16.840.1.113883.6.96', codeSystemName:'SNOMED CT', displayName:'with laterality')
							value(code:'7771000', codeSystem:'2.16.840.1.113883.6.96', codeSystemName:'SNOMED CT', displayName:'left')    
						}//qualifier
					}//target site code
					entryRelationship(typeCode:'SPRT'){
						regionOfInterest(classCode:'ROIOVL', moodCode:'EVN'){
							//, ID:'MM1'){
							id(root:'2.16.840.1.113883.19.3.1')
							code(code:'ELLIPSE')
							value(value:'3')
							value(value:'1')
							value(value:'3')
							value(value:'7')
							value(value:'2')
							value(value:'4')
							value(value:'4')
							value(value:'4')
							entryRelationship(typeCode:'SUBJ'){
								observationMedia(classCode:'OBS', moodCode:'EVN'){
									id(root:'2.16.840.1.113883.19.2.1')
									value(mediaType:'image/gif'){ reference(value:'lefthand.gif') }//value
								}//observation media
							}//entry relationship embedded
						}//region of interest
					}//entry relationship
				}//observation
			}//entry
		}
		
		// println entry
    // assertEquals 8, entry.observation.entryRelationship.regionOfInterest.value.size
	}
}
