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

import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*

/**
 * Test the schema definitions pass to build a CCD construct
 * @author Stefan Ivanov
 */
public class CCDFeaturesBuilderTest extends AbstractCCDBuilderTest{
	
	/**
	 *
	 */
	public void testCCDHeader(){
		def document = builder.build{
			clinicalDocument {
				templateId(root:'2.16.840.1.113883.10.20.1')
				id(root:'db734647-fc99-424c-a864-7e3cda82e703')
				code(code:'34133-9', 
				        codeSystem:'2.16.840.1.113883.6.1', 
						codeSystemName:'LOINC',
						displayName:'Summarization of episode note'
				)
				title('Good Health Clinic Continuity of Care Document')
				effectiveTime('20000407130000+0500')
				confidentialityCode(code:'N')
				languageCode(code:'en-US')
				recordTarget {
					patientRole{
						id(extension:'996-756-495', root:'2.16.840.1.113883.19.5') 
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
						}//provider organization
					}//patientRole
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
				author {
					time('20000407130000+0500')
					assignedAuthor {
						id('20cf14fb-b65c-4c8c-a54d-b0cca834c18d', nullFlavor:'NA')
						representedOrganization {
							id(root:"2.16.840.1.113883.19.5")
							name('Good Health Clinic')
						}
					}
				}//author 2
				documentationOf{
					serviceEvent(classCode:'PCPR'){
						effectiveTime{
							low(value:'19320924')
							high(value:'20000407')    
						}
					}
				}//documentationof
			}//ccd
		}
		
		assertNotNull document
	}
	
	/**
	 * 
	 */
	public void testCCDPurposeSection(){
		def component = builder.build{
			component {
				structuredBody {
					/* CCD Purpose Section (from samples) */
					component {
						section{
							/* Purpose section template */
							templateId(root:'2.16.840.1.113883.10.20.1.13') 
							code(code:'48764-5', codeSystem:'2.16.840.1.113883.6.1')
							title('Summary Purpose')
							text('Transfer of care')
							entry(typeCode:'DRIV'){
								act(classCode:'ACT', moodCode:'EVN'){
									/* Purpose activity template */
									templateId(root:'2.16.840.1.113883.10.20.1.30')
									code(code:'23745001', codeSystem:'2.16.840.1.113883.6.96', displayName:'Documentation procedure')
									statusCode(code:'completed')
									entryRelationship(typeCode:'RSON'){
										act(classCode:'ACT', moodCode:'EVN'){
											code(code:'23745001', codeSystem:'2.16.840.1.113883.6.96', displayName:'Transfer of care')
											statusCode(code:'completed')
										}//act
									}//entryRelationship
								}//act
							}//entry
						}//section>
					}//structured body component
				}//structuredBody
			}//component
		}
		
		assertNotNull component
	}

	/**
	 * 
	 */
	public void testCCDPayersSection(){
		def component = builder.build{
			component {
				structuredBody {
					/* CCD Payers Section (from samples) */
					component{
						section{
							templateId(root:'2.16.840.1.113883.10.20.1.9') 
							/* Payers section template */
							code(code:'48768-6', codeSystem:'2.16.840.1.113883.6.1')
							title('Payers')
							text{
								table(border:'1', width:'100%'){
									thead{
										tr{
											th('Payer name')
											th('Policy type / Coverage type')
											th('Covered party ID')
											th('Authorization(s)')
										}//tr
									}//thead
									tbody{
										tr{
											td('Good Health Insurance') 
											td('Extended healthcare / Self') 
											td('14d4a520-7aae-11db-9fe1-0800200c9a66')
											td('Colonoscopy')
										}//tr
									}//tbody
								}//table
							}//text
							entry(typeCode:'DRIV'){
								act(classCode:'ACT', moodCode:'DEF'){
									/* Coverage activity template */
									templateId(root:'2.16.840.1.113883.10.20.1.20')
									id(root:'1fe2cdd0-7aad-11db-9fe1-0800200c9a66')
									code(code:'48768-6', codeSystem:'2.16.840.1.113883.6.1', displayName:'Payment sources')
									statusCode(code:'completed')
									entryRelationship(typeCode:'COMP'){
										act(classCode:'ACT', moodCode:'EVN'){
											/* Policy activity template */
											templateId(root:'2.16.840.1.113883.10.20.1.26')
											id(root:'3e676a50-7aac-11db-9fe1-0800200c9a66')
											code(code:'EHCPOL', codeSystem:'2.16.840.1.113883.5.4', displayName:'Extended healthcare')
											statusCode(code:'completed')
											performer(typeCode:'PRF'){
												assignedEntity{
													id(root:'329fcdf0-7ab3-11db-9fe1-0800200c9a66')
													representedOrganization{
														name('Good Health Insurance')
													}//representedOrganization
												}//assignedEntity
											}//performer
											participant(typeCode:'COV'){
												participantRole{
													id(root:'14d4a520-7aae-11db-9fe1-0800200c9a66')
													code(code:'SELF', codeSystem:'2.16.840.1.113883.5.111', displayName:'Self')
												}//participantRole
											}//participant
											entryRelationship(typeCode:'REFR'){
												act(classCode:'ACT', moodCode:'EVN'){
													/* Authorization activity template */
													templateId(root:'2.16.840.1.113883.10.20.1.19')
													id(root:'f4dce790-8328-11db-9fe1-0800200c9a66')
													code(nullFlavor:'NA')
													entryRelationship(typeCode:'SUBJ'){
														procedure(classCode:'PROC', moodCode:'PRMS'){
															code(code:'73761001', codeSystem:'2.16.840.1.113883.6.96', displayName:'Colonoscopy')
														}//procedure
													}//entryRelationship
												}//act
											}//entryRelationship
										}//act
									}//entryRelationship
								}//act
							}//entry
						}//section
					}//component
				}//structuredBody
			}//component
		}
		assertTrue component instanceof POCDMT000040Component2
		def section = component.structuredBody.component.get(0).section
		assertNotNull section
	}

	/*
     * @deprecated move to separate test class
     */
    public void testCCDAdvanceDirectivesSection(){
        def component = builder.build{
            component {
                structuredBody {
                    /* CCD Advance Directives Section */
                    component{
                        section{
                            /*  Advance directives section template */
                            templateId(root:'2.16.840.1.113883.10.20.1.1')
                            code(code:'42348-3', codeSystem:'2.16.840.1.113883.6.1')
                            title('Advance Directives')
                            text{
                                table(border:'1', width:'100%'){
                                    thead{
                                    tr{
                                        th('Directive')
                                        th('Description')
                                        th('Verification')
                                        th('Supporting Document(s)')
                                    }//tr
                                    }//thead
                                    tbody{
                                        tr{
                                            td('Resuscitation status')
                                            td{
                                                content(ID:'AD1'){
                                                    'Do not resuscitate' 
                                                }
                                            }
                                            td('Dr. Robert Dolin, Nov 07, 1999')
                                            td{
                                                linkHtml(href:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf'){
                                                    'Advance directive'
                                                }
                                            }//td
                                        }//tr
                                    }//tbody
                                }//table
                            }//text
                            entry(typeCode:'DRIV'){
                                observation(classCode:'OBS', moodCode:'EVN'){
                                    /*  Advance directive observation template */
                                    templateId(root:'2.16.840.1.113883.10.20.1.17')
                                    id(root:'9b54c3c9-1673-49c7-aef9-b037ed72ed27')
                                    code(code:'304251008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resuscitation')
                                    statusCode(code:'completed')
                                    value(builder.build {
                                        ce(code:'304253006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Do not resuscitate')
                                        {
                                            originalText{
                                                reference(value:'#AD1')
                                            }//originalText
                                        }
                                    })//value
                                    participant(typeCode:'VRF'){
                                        /* Verification of an advance directive observation template */
                                        templateId(root:'2.16.840.1.113883.10.20.1.58')
                                        time(value:'19991107')
                                        participantRole{
                                            id(root:'20cf14fb-b65c-4c8c-a54d-b0cca834c18c')
                                        }//participantRole
                                    }//participant
                                    participant(typeCode:'VRF'){
                                        /* Verification of an advance directive observation template */
                                        templateId(root:'2.16.840.1.113883.10.20.1.58')
                                        time(value:'19991107')
                                        participantRole{
                                            id(root:'20cf14fb-b65c-4c8c-a54d-b0cca834c18c')
                                        }//participantRole
                                    }//participant 2
                                    entryRelationship(typeCode:'REFR'){
                                        observation(classCode:'OBS', moodCode:'EVN'){
                                            /* Advance directive status observation template */
                                            templateId(root:'2.16.840.1.113883.10.20.1.37')
                                            code(code:'33999-4', codeSystem:'2.16.840.1.113883.6.1', displayName:'Status')
                                            statusCode(code:'completed')
                                            value(builder.build {
                                                ce(code:'15240007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Current and verified')
                                            })
                                        }//observation
                                    }//entryRelationship
                                    reference(typeCode:'REFR'){
                                        externalDocument{
                                            /* Advance directive reference template */
                                            templateId(root:'2.16.840.1.113883.10.20.1.36')
                                            id(root:'b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3')
                                            code(code:'371538006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Advance directive')
                                            text(mediaType:'application/pdf'){
                                                reference(value:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf')
                                            }//text
                                        }//externalDocument
                                    }//reference
                                }//observation
                            }//entry
                        }//section
                    }//component
                }//structuredBody
            }//component
        }
        assertNotNull component
	}
	
	/**
	 * 
	 */
	public void testCCDSupport(){
		def document = builder.build{
			clinicalDocument {
				templateId(root:'2.16.840.1.113883.10.20.1')
				id(root:'db734647-fc99-424c-a864-7e3cda82e703')
				code(
						code:'34133-9', 
								codeSystem:'2.16.840.1.113883.6.1', 
								codeSystemName:'LOINC',
								displayName:'Summarization of episode note'
								)
				title('Good Health Clinic Continuity of Care Document')
				effectiveTime('20000407130000+0500')
				confidentialityCode(code:'N')
				languageCode(code:'en-US')
				recordTarget {
					patientRole {
						id(extension:'12345', root:'2.16.840.1.113883.19.5')
						patient {
							name {
								given('Henry') 
								family('Levin')
								suffix('the 7th')
							}
							administrativeGenderCode (code:'M', codeSystem:'2.16.840.1.113883.5.1')
							birthTime('19320924')
							guardian{
								code(code:'guardian code', displayName:'Guardian Entry')
								guardianPerson{
									name {
										given('Guardian') 
										family('Person')
									}
								}
							}//guardian
						}
						providerOrganization {
							id(root:'2.16.840.1.113883.19.5')
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
				author {
					time('20000407130000+0500')
					assignedAuthor {
						id('20cf14fb-b65c-4c8c-a54d-b0cca834c18d', nullFlavor:'NA')
						representedOrganization {
							id(root:"2.16.840.1.113883.19.5")
							name('Good Health Clinic')
						}
					}
				}//author 2
				participant(typeCode:'IND'){
					associatedEntity(classCode:'NOK'){
						id(root:'4ac71514-6a10-4164-9715-f8d96af48e6d')
						code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biiological mother')
						telecom(value:'tel:(999)555-1212')
						associatedPerson{
							name{
								given('Henrietta')
								family('Levin')
							}//name
						}//associatedPerson
					}//associatedEntity>
				}//participant
				documentationOf{
					serviceEvent(classCode:'PCPR'){
						effectiveTime{
							low(value:'19320924')
							high(value:'20000407')    
						}
					}
				}//documentationof
			}//ccd
		}
		assert document != null
	}
	
	/**
	 * Section 3.5 Problems
	 */
	public void testCCDProblems(){
		def component = builder.build{
			component {
				structuredBody {
					/* CCD Problems Section */
					component{
						section{
							/* Problem section template */
							templateId(root:'2.16.840.1.113883.10.20.1.11') 
							code(code:'11450-4', codeSystem:'2.16.840.1.113883.6.1')
							title('Problems') 
							text{
								table(border:'1', width:'100%'){
									thead{
										tr{
											th('Condition')
											th('Effective Dates')
											th('Condition Status')
										}//tr
									}//thead
									tbody{
										tr{
											td('Asthma')
											td('1950')
											td('Active')
										}//tr
										tr{
											td('Pneumonia')
											td('Jan 1997')
											td('Resolved')
										}//tr
										tr{
											td('')
											td('Mar 1999')
											td('Resolved')
										}//tr
										tr{
											td('Myocardial Infarction')
											td('Jan 1997')
											td('Resolved')
										}//tr
									}//tbody
								}//table
							}//text
							entry(typeCode:'DRIV'){
								act(classCode:'ACT', moodCode:'EVN'){
									/* Problem act template */
									templateId(root:'2.16.840.1.113883.10.20.1.27') 
									id(root:'6a2fa88d-4174-4909-aece-db44b60a3abb')
									code(nullFlavor:'NA')
									entryRelationship(typeCode:'SUBJ'){
										observation(classCode:'OBS',  moodCode:'EVN'){
											/* Problem observation template */
											templateId(root:'2.16.840.1.113883.10.20.1.28') 
											id(root:'d11275e7-67ae-11db-bd13-0800200c9a66')
											code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')                 
											statusCode(code:'completed') 
											effectiveTime{
												low(value:'1950')
											}//effectiveTime
											value(
											    builder.build {
													cd(code:'195967001', codeSystem:'2.16.840.1.113883.6.96', displayName:'Asthma')
											    }
											)//value
											entryRelationship(typeCode:'REFR'){
												observation(classCode:'OBS',  moodCode:'EVN'){
													/* Problem status observation template */
													templateId(root:'2.16.840.1.113883.10.20.1.50') 
													code(code:'33999-4', codeSystem:'2.16.840.1.113883.6.1', displayName:'Status')
													statusCode(code:'completed')
													value(
														builder.build {
															ce(code:'55561003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Active')
														}
													)//value
												}//observation
											}//entryRelationship
										}//observation
									}//entryRelationship
								}//act  
							}//entry 
							entry(typeCode:'DRIV'){
								act(classCode:'ACT', moodCode:'EVN'){
									/* Problem act template */
									templateId(root:'2.16.840.1.113883.10.20.1.27') 
									id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
									code(nullFlavor:'NA')
									entryRelationship(typeCode:'SUBJ'){
										observation(classCode:'OBS',  moodCode:'EVN'){
											/* Problem observation template */
											templateId(root:'2.16.840.1.113883.10.20.1.28') 
											id(root:'ab1791b0-5c71-11db-b0de-0800200c9a66')
											code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
											statusCode(code:'completed')
											effectiveTime{
												low(value:'199701')
											}//effectiveTime
											value(
												builder.build { 
													cd(code:'233604007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Pneumonia') 
												}
											)//value
											entryRelationship(typeCode:'REFR'){
												observation(classCode:'OBS',  moodCode:'EVN'){
													/* Problem status observation template */
													templateId(root:'2.16.840.1.113883.10.20.1.50') 
													code(code:'33999-4', codeSystem:'2.16.840.1.113883.6.1', displayName:'Status')
													statusCode(code:'completed')
													value(
														builder.build{
															ce(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
														}
													)//value
												}//observation
											}//entryRelationship
										}//observation
									}//entryRelationship
								}//act
							}//entry 
							entry(typeCode:'DRIV'){
								act(classCode:'ACT', moodCode:'EVN'){
									/* Problem act template */
									templateId(root:'2.16.840.1.113883.10.20.1.27') 
									id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
									code(nullFlavor:'NA')
									entryRelationship(typeCode:'SUBJ'){
										observation(classCode:'OBS',  moodCode:'EVN'){
											/* Problem observation template */
											templateId(root:'2.16.840.1.113883.10.20.1.28') 
											id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
											code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
											statusCode(code:'completed') 
											effectiveTime{
												low(value:'199903')
											}//effectiveTime
											value(
												builder.build {
													cd(code:'233604007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Pneumonia')    
												}
											)//value
											entryRelationship(typeCode:'REFR'){
												observation(classCode:'OBS',  moodCode:'EVN'){
													/* Problem status observation template */
													templateId(root:'2.16.840.1.113883.10.20.1.50') 
													code(code:'33999-4', codeSystem:'2.16.840.1.113883.6.1', displayName:'Status')
													statusCode(code:'completed')
													value(
														builder.build {
															ce(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
														}
													)//value
												}//observation
											}//entryRelationship
										}//observation
									}//entryRelationship
									entryRelationship(typeCode:'SUBJ', inversionInd:true){
										observation(classCode:'OBS',  moodCode:'EVN'){
											/* Episode observation template */
											templateId(root:'2.16.840.1.113883.10.20.1.41') 
											code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
											statusCode(code:'completed')
											value(
												builder.build{
												    cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding'){
			                                              qualifier{
			                                                  name(code:'246456000', displayName:'Episodicity')
			                                                  value(code:'288527008', displayName:'New episode')
			                                              }//qualifier
												    }//cd
												}
											)//value
											entryRelationship(typeCode:'SAS'){
												act(classCode:'ACT', moodCode:'EVN'){
													id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
													code(nullFlavor:'NA')
												}//act
											}//entryRelationship
										}//observation
									}//entryRelationship
								}//act  
							}//entry 
							entry(typeCode:'DRIV'){
								act(classCode:'ACT', moodCode:'EVN'){
									/* Problem act template */
									templateId(root:'2.16.840.1.113883.10.20.1.27') 
									id(root:'5a2c903c-bd77-4bd1-ad9d-452383fbfefa')
									code(nullFlavor:'NA')
									entryRelationship(typeCode:'SUBJ'){
										observation(classCode:'OBS', moodCode:'EVN'){
											/* Problem observation template */
											templateId(root:'2.16.840.1.113883.10.20.1.28') 
											code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
											statusCode(code:'completed') 
											effectiveTime{
												low(value:'199701')
											}//effectiveTime
											value(
												builder.build{
													cd(code:'22298006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Myocardial infarction')
												}
											)//value
											entryRelationship(typeCode:'REFR'){
												observation(classCode:'OBS', moodCode:'EVN'){
													/* Problem status observation template */
													templateId(root:'2.16.840.1.113883.10.20.1.50') 
													code(code:'33999-4', codeSystem:'2.16.840.1.113883.6.1', displayName:'Status')
													statusCode(code:'completed')
													value(
														builder.build{
															ce(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
														}
													)//value
												}//observation
											}//entryRelationship
										}//observation
									}//entryRelationship
								}//act  
							}//entry    
						}//section
					}//component
				}//structured body
			}//component
		}
		assertTrue component instanceof POCDMT000040Component2
		def section = component.structuredBody.component.get(0).section
		assertNotNull section
	}
	
}