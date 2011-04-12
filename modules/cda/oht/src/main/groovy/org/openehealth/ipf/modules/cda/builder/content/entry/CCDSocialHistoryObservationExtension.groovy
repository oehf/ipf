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
 * Chapter 3.7.2.1 "Social History Observation"
 *  
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.33 Social History Observation
 * <li>2.16.840.1.113883.10.20.1.56 Social History Status Observation 
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.41 Episode Observation
 * </ul>
 * 
 * @author Christian Ohr
 */
public class CCDSocialHistoryObservationExtension extends CompositeModelExtension {
	
     CCDSocialHistoryObservationExtension() {
		super()
	 }
	
     CCDSocialHistoryObservationExtension(builder) {
		super(builder)
	 }
	
	 def register(Collection registered) {
	     
	    super.register(registered)

	    POCDMT000040Section.metaClass {		    
	        setSocialHistoryObservation {POCDMT000040Observation observation ->
	            delegate.entry.add(builder.build {
	                ccd_entry(observation:observation)
	            })
	        }	            
	        getSocialHistoryObservation { ->
	            delegate.entry?.observation?.findAll { 
	                templateId() in it.templateId?.root
	            }
	        }
	    }
	    
		POCDMT000040Observation.metaClass {		    
		    setSocialHistoryStatus { POCDMT000040Observation observation ->
		        delegate.entryRelationship.add(builder.build {
		            ccd_entryRelationship(typeCode:'REFR', observation:observation)
		        })
		    }		    
		    getSocialHistoryStatus { ->
			    delegate.entryRelationship.find {
			        '2.16.840.1.113883.10.20.1.56' in it.observation?.templateId?.root
			    }?.observation		        
		    }		    		    
		}
		
	} 
		
	String extensionName() {
		'CCD Social History Observation'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.33'
	}
	
	Collection modelExtensions() {
	    [ new CCDEpisodeObservationExtension(),
	      new CCDSourceExtension(),
	      new CCDCommentsExtension()	    
	    ]
	}	
}
