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

import org.openhealthtools.ihe.common.cdar2.*import org.openehealth.ipf.modules.cda.builder.BaseModelExtension

/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDCommentsExtension extends BaseModelExtension {
	
	CCDCommentsExtension() {
		super()
	}
	
	CCDCommentsExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
		super.register(registered)
		
		// --------------------------------------------------------------------------------------------
		// Chapter 4.3 "Comments"
		// --------------------------------------------------------------------------------------------
		POCDMT000040Section.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entry.add(builder.build {
					entry(act:act)
				})          
			}
			
			getComment { ->
				delegate.entry.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd act comment extensions
		
		POCDMT000040Act.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd act comment extensions
		
		POCDMT000040Encounter.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd encounter comment extensions
		
		POCDMT000040Observation.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd observation comment extensions
		
		POCDMT000040ObservationMedia.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd observation media comment extensions
		
		POCDMT000040Procedure.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd organizer comment extensions
		
		POCDMT000040RegionOfInterest.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd region of interest comment extensions
		
		POCDMT000040Procedure.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd procedure comment extensions
		
		POCDMT000040SubstanceAdministration.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd substance administration comment extensions
		
		POCDMT000040Supply.metaClass {
			setComment { POCDMT000040Act act ->
				delegate.entryRelationship.add(builder.build {
					entryRelationship(typeCode:'SUBJ', inversionInd:true, act:act)
				})          
			}
			
			getComment { ->
				delegate.entryRelationship.findAll {
					templateId() in it.act?.templateId?.root
				}?.act
			}
			
		}// ccd supply comment extensions
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Comments'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.40'
	}	
}
