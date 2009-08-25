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
 * Chapter 3.5.2.3 "Episode Observation"
 * 
 * Template Definitions:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.41 Episode Observation
 * </ul>
 * 
 * Dependencies:
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDEpisodeObservationExtension extends BaseModelExtension {
	
     CCDEpisodeObservationExtension() {
		super()
	 }
	
     CCDEpisodeObservationExtension(builder) {
		super(builder)
	 }
	
	def register(Collection registered) {
	    
	   super.register(registered)
		
		// Extension required by Problems Act (2.16.840.1.113883.10.20.1.27)
		POCDMT000040Act.metaClass {
			setEpisodeObservation { POCDMT000040Observation observation ->
		        delegate.entryRelationship.add(builder.build {
		            ccd_entryRelationship (typeCode:'SUBJ', observation:observation)
		        })
			}    
			getEpisodeObservation{ ->
                delegate.entryRelationship.find {
                    templateId() in it.observation?.templateId?.root
                }?.observation
			}
		}

		// Extension required by Social History Observation (2.16.840.1.113883.10.20.1.33)
		POCDMT000040Observation.metaClass {
			setEpisodeObservation { POCDMT000040Observation observation ->
		        delegate.entryRelationship.add(builder.build {
		            ccd_entryRelationship (typeCode:'SUBJ', observation:observation)
		        })
			}    
			getEpisodeObservation{ ->
                delegate.entryRelationship.find {
                    templateId() in it.observation?.templateId?.root
                }?.observation
			}
		}		
		
	} 	
	
	String extensionName() {
		'CCD Episode Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.41'
	}	
}
