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

/**
 * @author Christian Ohr
 */
public class CDAR2BuilderTest extends AbstractCDAR2BuilderTest{

     
     public void testBuildPatientRole() {
         def role = builder.build {
             patientRole {
                 
                 id("12345@2.16.840.1.113883.19.5")                 
                 patient {
                     name {
                         given('Henry') 
                         family('Levin')
                         suffix('the 7th')
                     }
                     administrativeGenderCode("M")
                     birthTime('19320924')
                 }
                 
                 providerOrganization {
                     id(root:"2.16.840.1.113883.19.5")
                 }
             }
         }
         // println  role

     }
         
//     public void testBuildDataTypes() {
//         
//         def address = builder.build {
//             ad(isNotOrdered:true) {
//                 city('Walldorf')
//                 country('Germany')
//                 city('Heidelberg')
//             }
//         }
//         
//         assert address.isIsNotOrdered()
//         println address.getCity()
//     }
     

     
     public void testBuildClinicalDocumentMetaData() {
         
          def document = builder.build {
             clinicalDocument {
                // templateId(root:'2.16.840.1.113883.3.27.1776')
                id(root:'2.16.840.1.113883.19.4', extension:'c266')
                code(
                        code:'11488-4', 
                        codeSystem:'2.16.840.1.113883.6.1', 
                        codeSystemName:'LOINC',
                        displayName:'Consultation note'
                )
             	title('Good Health Clinic Consultation Note')
             	effectiveTime('20000407')
             	versionNumber(2)
             	confidentialityCode(
             	        code:'N', 
             	        codeSystem:'2.16.840.1.113883.5.25')
             	languageCode(code:'en_US')
             	setId (extension:"BB35", root:"2.16.840.1.113883.19.7")
//             	recordTarget {
//                    patientRole {
                patient {
                        id {
                            extension="12345" 
                            root="2.16.840.1.113883.19.5"
                        }
                        patient {
                            name {
                                given('Henry') 
                                family('Levin')
                                suffix('the 7th')
                            }
                            administrativeGenderCode {
                                code="M" 
                                codeSystem="2.16.840.1.113883.5.1"
                            }
                            birthTime('19320924')
                        }
                        providerOrganization {
                            id(root:"2.16.840.1.113883.19.5")
                        }
//                    }
                }
             	author {
             	    time('2000040714')
             	    assignedAuthor {
             	        id(extension:"KP00017",root:"2.16.840.1.113883.19.5")
             	        assignedPerson {
             	            name {
         						given('Robert')
         						family('Dolin')
         						suffix('MD')
             	            }
             	        }
             	        representedOrganization {
             	            id(root:"2.16.840.1.113883.19.5")
             	        }
             	    }
             	}
             	
             	custodian {
             	    assignedCustodian {
             	        representedCustodianOrganization {
             	           id(root:"2.16.840.1.113883.19.5")
             	           name('Good Health Clinic')
             	        }
             	    }
             	}
             	legalAuthenticator {
             	    time('20000408')
             	    signatureCode('S')
             	    assignedEntity {
             	        id(extension:"KP00017", root:"2.16.840.1.113883.19.5")
             	        assignedPerson {
             	            name {
         						given('Robert')
         						family('Dolin')
         						suffix('MD')
             	            }
             	        }
             	        representedOrganization {
             	            id(root:"2.16.840.1.113883.19.5")
             	        }
             	    }
             	}
             	relatedDocument(/*typeCode:"APND"*/) {
             	    typeCode('APND')
             	    parentDocument {
             	        id(extension:"a123", root:"2.16.840.1.113883.19.4")
             	        setId(extension:"BB35", root:"2.16.840.1.113883.19.7")
             	        versionNumber(1)
             	    }
             	}
             	componentOf {
             	    encompassingEncounter {
             	        id(extension:"KPENC1332", root:"2.16.840.1.113883.19.6")
             	        effectiveTime {
             	           low("20000407")
             	        }
             	        encounterParticipant(/*typeCode:"CON"*/) {
             	            time('20000407')
             	            assignedEntity {
             	                id(extension:"KP00017",root:"2.16.840.1.113883.19.5")
             	                assignedPerson {
             	                    name {
             	                        given('Robert')
             	                        family('Dolin')
             	                        suffix('MD')
             	                    }
             	                }
                     	        representedOrganization {
                     	            id(root:"2.16.840.1.113883.19.5")
                     	        }
             	            }
             	        }
             	        location {
             	            healthCareFacility {
             	               code(
             	                       code:"GIM",
             	                       codeSystem:"2.16.840.1.113883.5.10588",
             	                       displayName:"General internal medicine clinic"
             	               )
             	            }
             	        }
             	    }
             	}
            }
         }
          def renderer = new CDAR2Renderer()
          def opts = [:]
          opts[XMLResource.OPTION_DECLARE_XML] = true
          opts[XMLResource.OPTION_ENCODING] = 'utf-8'
          // println(renderer.render(document, opts))
     }
    
}
