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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*

/**
 * @author Christian Ohr
 */
public class CCDAdvanceDirectivesBuilderTest extends AbstractCCDBuilderTest{

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
                        }
                    }
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
                       // CCD Advance Directives (Chapter 3.2)
                       advanceDirectives{
                           text('Hey directives')
                           observation{
                               id(root:'9b54c3c9-1673-49c7-aef9-b037ed72ed27')
                               code(code:'304251008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resuscitation')
                               observationStatus{
                                   value(code:'15240007',
                                           codeSystem:'2.16.840.1.113883.6.96',
                                           displayName:'Current and verified')
                               }
                               verifier{
                                    time('19991107')
				                    participantRole{
					                    id(root:'20cf14fb-b65c-4c8c-a54d-b0cca834c18c')
                                    }//participantRole
                               }
                           }//observation instance first
                       }
                   }
                }
            }

         }

         document
     }


    public void testAdvanceDirectiveSchema(){
        assert builder.getSchema('ccd_advanceDirectives') != null
        assert builder.getSchema('ccd_advanceDirectiveObservationStatus') != null

        def advanceDirective = builder.build{
            ccd_advanceDirectives{
                text('Simple Advance Directives')
                observation{
                    id(root:'9b54c3c9-1673-49c7-aef9-b037ed72ed27')
                    code(code:'304251008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resuscitation')
                    observationStatus{
                        value(code:'15240007',
                                           codeSystem:'2.16.840.1.113883.6.96',
                                           displayName:'Current and verified')
                    }//observation status
                    verifier{
                        time('19991107')
                    }//verifier
                    reference{
    					id(root:'b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3')
					    code(code:'371538006',
                                codeSystem:'2.16.840.1.113883.6.96',
                                displayName:'Advance directive')
					    text(mediaType:'application/pdf'){
                            reference(value:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf')
                        }
                    }//reference to external document
                }//advance directive observation
            }//advance directive
        }
        CCDConformanceValidatorHelper.checkCCDAdvanceDirectivesConformance(advanceDirective)
    }

    public void testRenderCCDAdvanceDirectives() {
         POCDMT000040ClinicalDocument document = buildCCD()
         def opts = [:]
         opts[XMLResource.OPTION_DECLARE_XML] = true
         opts[XMLResource.OPTION_ENCODING] = 'utf-8'
         //println(renderer.render(document, opts))
     }

}