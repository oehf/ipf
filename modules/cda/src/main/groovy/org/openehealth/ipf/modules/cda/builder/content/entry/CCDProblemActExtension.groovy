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
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension


/**
 * Chapter 3.5.2.1.1 "Problem act"
 *
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.27 Problem Act
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.28 Problem Observation
 * <li>2.16.840.1.113883.10.20.1.18 Alert Observation
 * <li>2.16.840.1.113883.10.20.1.41 Episode Observation
 * <li>2.16.840.1.113883.10.20.1.48 Patient Awareness
 * <li>2.16.840.1.113883.10.20.1.40 Comments
 * </ul>
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProblemActExtension extends CompositeModelExtension {
	
	CCDProblemActExtension() {
		super()
	}
	
	CCDProblemActExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)

		// Extension required by Problems Section (2.16.840.1.113883.10.20.1.11)
		POCDMT000040Section.metaClass {
			setProblemAct  {POCDMT000040Act act ->
				delegate.entry.add(builder.build {
				    ccd_entry (typeCode:'DRIV', act:act)
				})
			}
			
			getProblemAct { ->
				delegate.entry.findAll{ 
					templateId() in it.act.templateId.root
				}?.act
		    }
        }//problems section extensions

        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
		POCDMT000040Act.metaClass {
			setProblemActReason  {POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
				    entryRelationship (typeCode:'RSON', act:act)
				})
			}

			getProblemActReason { ->
				delegate.entryRelationship.findAll{
					templateId() in it.act.templateId.root
				}?.act
		    }
        }

        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
        POCDMT000040Procedure.metaClass {
			setProblemActReason  {POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
				    entryRelationship (typeCode:'RSON', act:act)
				})
			}

			getProblemActReason { ->
				delegate.entryRelationship.findAll{
					templateId() in it.act.templateId.root
				}?.act
		    }
        }
        // Extension required by Procedure Activity (2.16.840.1.113883.10.20.1.29)
        POCDMT000040Observation.metaClass {
			setProblemActReason  {POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
				    entryRelationship (typeCode:'RSON', act:act)
				})
			}

			getProblemActReason { ->
				delegate.entryRelationship.findAll{
					templateId() in it.act.templateId.root
				}?.act
		    }
        }
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Problem Act'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.27'
	}
	
	Collection modelExtensions() {
	    [ new CCDProblemObservationExtension(),
	      new CCDAlertObservationExtension(),
	      new CCDEpisodeObservationExtension(),
	      new CCDPatientAwarenessExtension(),
	      new CCDCommentsExtension()]
	}
}
