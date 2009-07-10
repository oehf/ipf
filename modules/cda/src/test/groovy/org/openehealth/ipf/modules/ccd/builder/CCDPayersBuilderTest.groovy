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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section

/**
 * @author Christian Ohr
 */
public class CCDPayersBuilderTest extends AbstractCCDBuilderTest {


     private POCDMT000040ClinicalDocument buildCCD() {
         def document = builder.build {
             continuityOfCareDocument {
                id('db734647-fc99-424c-a864-7e3cda82e703')
                title('Good Health Clinic Continuity of Care Document')
                effectiveTime('20000407130000+0500')
                confidentialityCode('N')
                languageCode('en-US')
                //recordTarget {
                    patient {
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
                // }
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
                       // CCD Payers (Chapter 3.1)
                       payers {
                          text {
                              paragraph('Payer information')
                              table(border:'1',width:'100%') {
                                 thead {
                                    tr {
                                       th('Payer name')
                                       th('Policy type')
                                       th('Covered Party ID')
                                       th('Authorizations')
                                    }
                                 }
                                 tbody {
                                    tr {
                                       td('Good Health Insurance')
                                       td('Extended healthcare / Self')
                                       td('14d4a520-7aae-11db-9fe1-0800200c9a66')
                                       td {
                                          linkHtml(href:'Colonoscopy.pdf', 'Colonoscopy')
                                       }
                                    }
                                 }

                              }                              
                          }
                          coverageActivity {
                              id('1fe2cdd0-7aad-11db-9fe1-0800200c9a66')
                              policyActivity {
                                  id('3e676a50-7aac-11db-9fe1-0800200c9a66')
                                  code('EHCPOL')
                                  payer {
                                      id('329fcdf0-7ab3-11db-9fe1-0800200c9a66')
                                      representedOrganization {
                                          name('Good Health Insurance')
                                      }
                                  }
                                  coveredParty {
                                      id('14d4a520-7aae-11db-9fe1-0800200c9a66')
                                      code('SELF')
                                  }
                                  authorizationActivity {
                                      id('f4dce790-8328-11db-9fe1-0800200c9a66')
                                      code(nullFlavor:'NA')
                                      promise {
                                          procedure(moodCode:'PRMS') {
                                              code(
                                                code:'73761001',
                                                codeSystem:'2.16.840.1.113883.6.96',
                                                displayName:'Colonoscopy'
                                              )
                                          }
                                      }
                                  }
                              }
                          }
                       }
                   }
                }
            }

         }
         
         document
     }

    public void testCoverageActivitySchemata(){
        def ccd_coverageActivity = builder.getSchema('ccd_coverageActivity')
        def ccd_policyActivity = builder.getSchema('ccd_policyActivity')
        def payer =  builder.build {
            ccd_payers{
                text('empty')
                coverageActivity(moodCode:'DEF'){
                    id('1fe2cdd0-7aad-11db-9fe1-0800200c9a66')
                    policyActivity{
                        id('3e676a50-7aac-11db-9fe1-0800200c9a66')
                        payer {
                            id('329fcdf0-7ab3-11db-9fe1-0800200c9a66')
                        }
                        coveredParty {
                            id('14d4a520-7aae-11db-9fe1-0800200c9a66')
                            code('SELF')
                        }
                        authorizationActivity {
                            id('f4dce790-8328-11db-9fe1-0800200c9a66')
                            code(nullFlavor:'NA')
                            promise()
                        }
                    }
                }
            }//payer section
        }
        CCDConformanceValidatorHelper.checkCCDPayersConformance(payer)
    }

     public void testRenderCCDPayers() {
         POCDMT000040ClinicalDocument document = buildCCD()
         def opts = [:]
         opts[XMLResource.OPTION_DECLARE_XML] = true
         opts[XMLResource.OPTION_ENCODING] = 'utf-8'
         //println(renderer.render(document, opts))
     }
     
     public void testExtractCCDPayers() {
         POCDMT000040ClinicalDocument document = buildCCD()
         def body = document.component.structuredBody
         assertTrue CCDConformanceValidatorHelper.checkCCDHeaderConformance(document)
         assertTrue CCDConformanceValidatorHelper.checkCCDPayersConformance(body.payers)
         assert POCDMT000040Section.class.isAssignableFrom(body.payers.class)
         assert body.payers.title.text == 'Payers'
         // TODO check coverageactivity mood code, should be DEF
         //assert body.payers.coverageActivity.moodCode.code == 'DEF'
      }
    
}
