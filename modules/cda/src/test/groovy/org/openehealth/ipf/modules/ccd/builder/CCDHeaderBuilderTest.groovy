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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ServiceEvent
import org.junit.Ignore

/**
 * @author Christian Ohr
 */
public class CCDHeaderBuilderTest extends AbstractCCDBuilderTest {
	
	
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
				// participants
				nextOfKin{
					id(root:'4ac71514-6a10-4164-9715-f8d96af48e6d')
					code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biiological mother')
					telecom(value:'tel:(999)555-1212')
					associatedPerson{
						name{
							given('Henrietta')
							family('Levin')
						}
					}
				}//next of kin
				emergencyContact{
					id(root:'4ac71514-6a10-4164-9715-f8d96af48e6f')
					associatedPerson{
						name{
							given('Baba')
							family('John')
						}
					} 
				}//emergency contact
				caregiver{
					scopingOrganization{ name('Very Good Health Clinic') }
				}//patient caregiver
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
						id { nullFlavor('NI') }
						representedOrganization { id(root:"2.16.840.1.113883.19.5") }
					}
				}//legal authenticator
				component {
					structuredBody {
						problems{
							text('Patient Problems Acts')
							problemAct{
								id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
								problemObservation{
									id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
									code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
									value(
											builder.build{
												cd(code:'233604007',
												codeSystem:'2.16.840.1.113883.6.96',
												displayName:'Pneumonia')
											}
											) 
									problemStatus{
										value(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
									}
									problemHealthstatus{
										value(code:'162467007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Symptom Free')
									}
								}//problem observation
								episodeObservation{
									code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
									value(
											builder.build{
												cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding')
											}
											)
									entryRelationship(typeCode:'SAS'){
										act(classCode:'ACT', moodCode:'EVN'){
											id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
											code(nullFlavor:'NA')
										}//act
									}//entryRelationship
								}//episode observation
								patientAwareness{
									awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
									participantRole{  id(root:'c8a6ff8-ed4b-4f7e-82c3-e98e58b45de8')  }
								}
							}//problem act
						}//problems section
						/*family history section */
						familyHistory {
							text('skipped') 
							familyMember {
								familyPerson {
									code(code:'9947008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological father')
									subject {
										administrativeGenderCode('M')
										birthTime(value:'1912')
									}
								}
								causeOfDeath() {
									id('d42ebf70-5c89-11db-b0de-0800200c9a66')
									code('ASSERTION')
									// value(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI')                                       
									value(
											builder().build { 
												ce(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI') 
											}
											)
									cause {
										id('6898fae0-5c8a-11db-b0de-0800200c9a66')
										code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
										statusCode('completed')
										// value(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
										value(
												builder().build {
													ce(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
												}
												)
									}
									age {
										value(
										builder().build { _int(57) }                                    
										)
									}
								}
								observation {
									id('5bfe3ec0-5c8b-11db-b0de-0800200c9a66')
									code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
									value(
											builder.build {
												ce(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')
											}
											)
									// value(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')                                       
									age {
										value(
										builder().build { _int(40) }
										)
									}
								}
							}//family member                           
							familyMember {
								familyPerson {
									code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological mother')
									subject {
										administrativeGenderCode('F')
										birthTime(value:'1912')
									}
								}                               
								observation {
									id('a13c6160-5c8b-11db-b0de-0800200c9a66')
									code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
									value(
											builder().build {
												ce(code:'195967001',codeSystem:'2.16.840.1.113883.6.96',displayName:'Asthma')
											}
											)
									age {
										value(
										builder().build { _int(30) }
										)
									}
								}
							}//family member                           
						}//familiy history
						
					}//body
				}//component
			}//document
		}
		
		document
	}
	
	public void testDummy(){
	    assert true
	}
	
	@Ignore
	public void NOtestRenderCCDDocument() {
		POCDMT000040ClinicalDocument document = buildCCD()
		def opts = [:]
		opts[XMLResource.OPTION_DECLARE_XML] = true
		opts[XMLResource.OPTION_ENCODING] = 'utf-8'
		println(renderer.render(document, opts))
	}
	
	@Ignore
	public void NOtestValidateCCDDocument() {
		POCDMT000040ClinicalDocument document = buildCCD()
		new CCDHeaderValidator().validate(document, null)
	}
	
}
