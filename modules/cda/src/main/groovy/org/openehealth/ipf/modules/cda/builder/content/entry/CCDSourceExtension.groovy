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
 * Chapter 5.2 "Source"
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDSourceExtension extends BaseModelExtension {
	
     CCDSourceExtension() {
		super()
	}
	
     CCDSourceExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	        
	    super.register(registered)
		
        POCDMT000040Act.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.findAll {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd act source extensions
		
        POCDMT000040Encounter.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd encounter source extensions
        
        POCDMT000040Observation.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd observation source extensions
		
        POCDMT000040ObservationMedia.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd observation media source extensions
        
        POCDMT000040Organizer.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd organizer source extensions
        
        POCDMT000040RegionOfInterest.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd region of interest source extensions
        
        POCDMT000040Procedure.metaClass {
		    setInformationSource { POCDMT000040Observation observation ->
		        delegate.entryRelationship.add(builder.build {
		            entryRelationship(typeCode:'REFR', observation:observation)
		        })	        
		    }
		    
		    getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
		    }
			
		}// ccd procedure source extensions
		
        POCDMT000040SubstanceAdministration.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd substance administration source extensions
		
        POCDMT000040Supply.metaClass {
            setInformationSource { POCDMT000040Observation observation ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship(typeCode:'REFR', observation:observation)
                })          
            }
            
            getInformationSource { ->
                delegate.entryRelationship?.find {
                    it.typeCode == 'REFR'
                }?.observation
            }
            
        }// ccd supply source extensions
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Source'
	}
	
	String templateId() {
		'CCD Source'
	}	
}
