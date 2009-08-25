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
import org.openehealth.ipf.modules.cda.builder.content.entry.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension


/**
 * Chapter 3.13.2.1.1 "Result organizer"
 * 
 * Dependent templates:
 * <ul>
 * <li>                             Source
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * </ul>
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDResultOrganizerExtension extends CompositeModelExtension {
	
     CCDResultOrganizerExtension() {
		super()
	}
	
     CCDResultOrganizerExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)
	    
		POCDMT000040Section.metaClass {
            setResultOrganizer  {POCDMT000040Organizer organizer ->
                delegate.entry.add(builder.build {
                    ccd_entry (typeCode:'DRIV', organizer:organizer)
                })
            }
            
            getResultOrganizer { ->
                delegate.entry.findAll{ 
                    templateId() in it.organizer?.templateId?.root
                }?.organizer
            }
        }//result section extensions
        
        POCDMT000040Organizer.metaClass {
            setResultObservation{ POCDMT000040Observation observation ->
                POCDMT000040Component4 organizerComponent = builder.build{
                    ccd_organizerComponents(observation:observation)
                }
                delegate.component.add(organizerComponent)
            }
            
            getResultObservation{ ->
                delegate.component?.observation
            }
        }//result organizer extensions
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Result Organizer'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.32'
	}
	
	Collection modelExtensions() {
		[ new CCDSourceExtension(),
		  new CCDCommentsExtension()
		]
	}	
}
