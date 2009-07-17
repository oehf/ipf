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
 * @author Stefan Ivanov
 */
public class CCDSupportBuilderTest extends AbstractCCDBuilderTest{
	
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
                            guardian{//support gardian
                                code(code:'guardian code', displayName:'Guardian Entry')
                                guardianPerson{
                                    name {
                                        given('Guardian') 
                                        family('Person')
                                    }
                                }
                            }//guardian
                        }//patient
                    	providerOrganization {
                            id('2.16.840.1.113883.19.5')
                            name('Good Health Clinic')
                        }
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
				}//author
				
				// informant
				
				// next of kin (participant)
				nextOfKin{
				    id(root:'4ac71514-6a10-4164-9715-f8d96af48e6d')
		            code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biiological mother')
		            telecom(value:'tel:(999)555-1212')
//		            associatedPerson{
//		                name(given:'Henrietta', family:'levin')
//		                    given>Henrietta</given>
//		                    <family>Levin</family>
//		                }//name>
//				    }//associatedPerson				    
				}//next of kin 1
                nextOfKin{
                    id(root:'4ac71514-6a10-4164-9715-f8d96af48e6e')
                    code(code:'65656006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biiological father')
                    telecom(value:'tel:(999)555-1212')
                    associatedPerson{
                      name{//TODO this could be easier
                          given('Kevin')
                          family('Levin')
                      }//name
				    }//associatedPerson                 
                }//next of kin 2
                // emergency contact (participant)
                emergencyContact{
                    id(root:'4ac71514-6a10-4164-9715-f8d96af48e6f')
                    associatedPerson{
                        name{
                            given('Baba')
                            family('John')
                        }//name
                    }//associatedPerson 
                }
                //patient caregiver (participant)
                caregiver{
                    scopingOrganization{
                        name('Very Good Health Clinic')
                    }//scopingOrganization>
                }//caregiver
				// mainActivity (documentationOf)
				mainActivity{
					effectiveTime{
						low(value:'19320924')
						high(value:'20000407')
					}
				}//main activity
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
						id(nullFlavor:'NI')
						representedOrganization {
							id(root:'2.16.840.1.113883.19.5')
						}
					}
				}
			}
			
		}
		
		document
	}
	
	public void testRenderCCDDocument() {
		POCDMT000040ClinicalDocument document = buildCCD()
		def opts = [:]
		opts[XMLResource.OPTION_DECLARE_XML] = true
		opts[XMLResource.OPTION_ENCODING] = 'utf-8'
		assert document != null
		//println(renderer.render(document, opts))
	}
	
	public void testExtractCCDDocument() {
		POCDMT000040ClinicalDocument document = buildCCD()
		assertTrue CCDConformanceValidatorHelper.checkCCDHeaderConformance(document)
		assert 2, document.nextOfKin.size
		assert 1, document.emergencyContact.size
		assert 1, document.caregiver.size
	}
}