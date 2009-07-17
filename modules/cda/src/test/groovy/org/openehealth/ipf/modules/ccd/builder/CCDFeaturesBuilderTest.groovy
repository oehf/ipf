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
public class CCDBuilderFeaturesTest extends AbstractCCDBuilderTest{

  /**
   *
   */
	public void testCCDHeader(){
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
		
		assertTrue CCDConformanceValidatorHelper.checkCCDHeaderConformance(document)
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
		assertTrue CCDConformanceValidatorHelper.checkCCDPurposeConformance(component)
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
		assertTrue CCDConformanceValidatorHelper.checkCCDPayersConformance(section)
	}
	
	/**
     * 
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
                                    value(code:'304253006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Do not resuscitate'){
                                        originalText{
                                            reference(value:'#AD1')
                                        }//originalText
                                    }//value
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
                                            value(code:'15240007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Current and verified')
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
        assertTrue component instanceof POCDMT000040Component2
        def section = component.structuredBody.component.get(0).section
        assertTrue CCDConformanceValidatorHelper.checkCCDAdvanceDirectivesConformance(section)
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
        
}