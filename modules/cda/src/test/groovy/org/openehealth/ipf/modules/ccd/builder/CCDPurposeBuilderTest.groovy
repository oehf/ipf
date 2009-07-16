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
package org.openehealth.ipf.modules.ccd.builder

import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section

/**
 * @author Christian Ohr
 */
public class CCDPurposeBuilderTest extends AbstractCCDBuilderTest {


     private POCDMT000040ClinicalDocument buildCCD() {
         def document = builder.build {
             continuityOfCareDocument {
                id('db734647-fc99-424c-a864-7e3cda82e703')
             	title('Good Health Clinic Continuity of Care Document')
             	effectiveTime('20000407130000+0500')
             	confidentialityCode('N')
             	languageCode('en-US')
               	recordTarget {
                    patientRole {
                        id('996-756-495@2.16.840.1.113883.19.5') 
                        patient {
                            name {
                                given('Henry') 
                                family('Levin')
                                suffix('the 7th')
                            }
                            administrativeGenderCode('M')
                            birthTime('19320924')
                        }
                        providerOrganization {
                            id('2.16.840.1.113883.19.5')
                            name('Good Health Clinic')
                        }//patient
                    }//patient role
                }//record target
             	author {
             	    time('20000407130000+0500')
             	    assignedAuthor {
             	        id('20cf14fb-b65c-4c8c-a54d-b0cca834c18c')
             	        assignedPerson {
             	            name {
                                prefix('Dr.')
         						given('Robert')
         						family('Dolin')
             	            }
             	        }
             	        representedOrganization {
             	            id(root:"2.16.840.1.113883.19.5")
                            name('Good Health Clinic')
             	        }
             	    }
             	}

                // informant
                // participants
             	// mainActivity (documentationOf)
                mainActivity{
                    effectiveTime{
                        low(value:'19320924')
                        high(value:'20000407')
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
             	    time('20000407130000+0500')
             	    signatureCode('S')
             	    assignedEntity {
             	        id {
                           nullFlavor('NI')
                       }
             	        representedOrganization {
             	            id(root:"2.16.840.1.113883.19.5")
             	        }
             	    }
             	}

                component {
                   structuredBody {
                       // CCD Purpose (Chapter 2.8)
                       purpose {
                          text('Transfer of Care!')
                          purposeActivity {
                              act {
                                 code(code:'308292007',
                                      codeSystem:'2.16.840.1.113883.6.96',
                                      displayName:'Transfer of care')
                                 statusCode('completed')
                              }
                          }
                       }
                   }
                }
            }

         }
         
         document
     }
     
     public void testRenderCCDPurpose() {
         POCDMT000040ClinicalDocument document = buildCCD()
         def opts = [:]
         opts[XMLResource.OPTION_DECLARE_XML] = true
         opts[XMLResource.OPTION_ENCODING] = 'utf-8'
//         println(renderer.render(document, opts))
     }
     
     public void testExtractCCDPurpose() {
         POCDMT000040ClinicalDocument document = buildCCD()
         def body = document.component.structuredBody
         assert POCDMT000040Section.class.isAssignableFrom(body.purpose.class)
         assertTrue CCDConformanceValidatorHelper.checkCCDHeaderConformance(document)
         //assertTrue CCDConformanceValidatorHelper.checkCCDPurposeConformance(document.component)
         assert body.purpose.title.text == 'Summary purpose'
         assert body.purpose.text.text == 'Transfer of Care!'
         assert body.purpose.purposeActivity.act.code.code == '308292007'
     }
    
}
