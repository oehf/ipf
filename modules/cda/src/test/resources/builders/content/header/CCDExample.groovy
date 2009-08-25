package builders.content.header

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
				guardian{
				    guardianOrganization{
				        id(root:"2.16.840.1.113883.19.5")
		                name('Good Health Clinic')
				    }
				}
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
		scopingOrganization{ 
		    name('Very Good Health Clinic') 
		}
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
	component{
	    structuredBody{
	        advanceDirectives{
	            text{
	                table(border:'1', width:'100%'){
	                   thead{
	                       tr{
	                           th('Directive')
	                           th('Description')
	                           th('Verification')
	                           th('Supporting Document(s)')
	                       }
	                   }
	                   tbody{
	                       tr{
	                           td('Resuscitation status')
	                           td('Do not resuscitate')
	                           td('Dr. Robert Dolin, Nov 07, 1999')
	                           td{
	                               linkHtml(href:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf','Advance directive')
	                           }
	                       }
	                   }
	                }
	            }//text
	            advanceDirectiveObservation{
	                id(root:'9b54c3c9-1673-49c7-aef9-b037ed72ed27')
	                code(code:'304251008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resuscitation')
	                value(make{
	                    cd(code:'304253006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Do not resuscitate'){
	                        originalText{
	                            reference(value:'#AD1')
	                        }
	                    }
	                })
	                verifier{
	                    time(value:'19991107')
	                    participantRole{ 
	                        id(root:'20cf14fb-b65c-4c8c-a54d-b0cca834c18c') 
	                    }
	                }//verifier
	                advanceDirectiveStatus{
	                    value(code:'15240007', displayName:'Current and verified')
	                }//advance directive observation status
	                advanceDirectiveReference{
	                    id(root:'b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3')
	                    code(code:'371538006',
	                            codeSystem:'2.16.840.1.113883.6.96',
	                            displayName:'Advance directive')
	                    text(mediaType:'application/pdf'){   
	                        reference(value:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf') 
	                    }
	                }//reference to external document
	            }//advance directive observation
	        }//advance directive section
	        alerts{
	            text('Patient Alerts')
	            problemAct{
	                id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
	                alertObservation{
	                    id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    effectiveTime('20000328')
	                    alertStatus{
	                        value(code:'55561003', 
	                                codeSystem:'2.16.840.1.113883.6.96',
	                                displayName:'Active')
	                    }
	                    participantAgent{
	                        playingEntity{
	                            code(code:'70618', 
	                                    codeSystem:'2.16.840.1.113883.6.88',
	                                    displayName:'Penicillin')
	                        }
	                    }//participant agent
	                    reactionObservation{
	                        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                        value(make{
	                            cd(code:'247472004', 
	                                    codeSystem:'2.16.840.1.113883.6.96',
	                                    displayName:'Hives')
	                        }
	                        )
	                        severityObservation{
	                            value(make{
	                                cd(code:'247472004', 
	                                        codeSystem:'2.16.840.1.113883.6.96',
	                                        displayName:'Hives')
	                            })
	                        }
	                    }//reaction observation
	                }//alert observation
	            }//problem act
	        }//alerts section
	        encounters {
	            text('Encounter Location: Very Good Health Clinic')
	            title('Encounters')
	            encounterActivity{
	                id(root:'2a620155-9d11-439e-92b3-5d9815ff4de8')
	                code(code:'GENRL', codeSystem:'2.16.840.1.113883.5.4', displayName:'General'){
	                    originalText('Checkup Examination')
	                }//code
	                encounterLocation{
	                    id(root:'2.16.840.1.113883.19.5')
	                    playingEntity{
	                        name('Very Good Health Clinic')
	                    }//playingEntity 
	                }
	            }//encounter        
	        }//encounter
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
	                    value(make { 
	                        ce(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI') 
	                    }
	                    )
	                    cause {
	                        id('6898fae0-5c8a-11db-b0de-0800200c9a66')
	                        code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                        statusCode('completed')
	                        // value(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
	                        value(make {
	                            ce(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
	                        }
	                        )
	                    }
	                    age {
	                        value(make { _int(57) }                                    
	                        )
	                    }
	                }
	                familyHistoryObservation {
	                    id('5bfe3ec0-5c8b-11db-b0de-0800200c9a66')
	                    code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    value(make {
	                        ce(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')
	                    }
	                    )
	                    // value(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')                                       
	                    age {
	                        value(make { _int(40) }
	                        )
	                    }
	                }
	            }                           
	            familyMember {
	                familyPerson {
	                    code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological mother')
	                    subject {
	                        administrativeGenderCode('F')
	                        birthTime(value:'1912')
	                    }
	                }                               
	                familyHistoryObservation {
	                    id('a13c6160-5c8b-11db-b0de-0800200c9a66')
	                    code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    value(make {
	                        ce(code:'195967001',codeSystem:'2.16.840.1.113883.6.96',displayName:'Asthma')
	                    }
	                    )
	                    age {
	                        value(make { _int(30) }
	                        )
	                    }
	                }
	            }                           
	        }//family history
	        functionalStatus{
	            text{
	                table(border:'1', width:'100%'){
	                    thead{
	                        tr{
	                            th('Functional Condition')
	                            th('Effective Dates')
	                            th('Condition Status')
	                        }
	                    }
	                    tbody{
	                        tr{
	                            td('Dependence on cane')
	                            td('1998')
	                            td('Active')
	                        }
	                        tr{
	                            td('Memory impairment')
	                            td('1999')
	                            td('Active')
	                        }
	                    }
	                }//table>
	            }
	            problemAct{
	                id(root:'6z2fa88d-4174-4909-aece-db44b60a3abb') 
	                code(nullFlavor:'NA')
	                problemObservation{
	                    id(root:'fd07111a-b15b-4dce-8518-1274d07f142a')
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4') 
	                    effectiveTime{low(value:'1998')}
	                    value( make{
	                            cd(code:'105504002', 
	                                    codeSystem:'2.16.840.1.113883.6.96', 
	                                    displayName:'Dependence on cane')
	                        }
	                    )
	                    functionalStatusStatus{
	                        value(code:'55561003',
	                                codeSystem:'2.16.840.1.113883.6.96',
	                                displayName:'Active') 
	                    }
	                }//problem observation
	            }//problem act
	            problemAct{
	               id(root:'64606e86-c080-11db-8314-0800200c9a66')
	               problemObservation{
	                   id(root:'64606e86-c080-11db-8314-0800200c9a66')
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    value( make{
	                            cd(code:'386807006',
	                                    codeSystem:'2.16.840.1.113883.6.96',
	                                    displayName:'Memory impairment')
	                        }
	                    )
	                    functionalStatusStatus{
	                        value(code:'55561003',
	                                codeSystem:'2.16.840.1.113883.6.96',
	                                displayName:'Active')
	                    }
	               }//problem observation
	            }//problem act
	        }//functional status section
	        immunizations{
	            text{
	                table(border:'1', width:'100%'){
	                   thead{
	                       tr{
	                           th('Vaccine')
	                           th('Date')
	                           th('Status')
	                       }
	                   }
	                   tbody{
	                       tr{
	                           td('Influenza virus vaccine, IM')
	                           td('Nov 1999')
	                           td('Completed')
	                       }
	                       tr{
	                           td('Influenza virus vaccine, IM')
	                           td('Dec 1998')
	                           td('Completed')
	                       }
	                       tr{
	                           td('Pneumococcal polysaccharide vaccine, IM')
	                           td('Dec 1998')
	                           td('Completed')
	                       }
	                       tr{
	                           td('Tetanus and diphtheria toxoids, IM')
	                           td('1997')
	                           td('Completed')
	                       }
	                   }
	                }
	            }//text
	            medicationActivity{
	                id(root:'e6f1ba43-c0ed-4b9b-9f12-f435d8ad8f92')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'199911')
	                    }
	                })
	                routeCode(code:'IM', displayName:"Intramuscular injection")
	                consumable {
	                    manufacturedProduct {
	                        manufacturedMaterial {
	                            code(code:'88',
	                                    codeSystem:'2.16.840.1.113883.6.59',
	                                    displayName:'Influenza virus vaccine') { 
	                                originalText('Influenza virus vaccine') 
	                            }
	                        }
	                    }//manufactured product
	                }
	            }//medication activity
	            medicationActivity{
	                id(root:'115f0f70-1343-4938-b62f-631de9749a0a')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'199812')
	                    }
	                })
	                routeCode(code:'IM', displayName:"Intramuscular injection")
	                consumable {
	                    manufacturedProduct {
	                        manufacturedMaterial {
	                            code(code:'88',
	                                    codeSystem:'2.16.840.1.113883.6.59',
	                                    displayName:'Influenza virus vaccine') { 
	                                originalText('Influenza virus vaccine') 
	                            }
	                        }
	                    }//manufactured product
	                }
	            }//medication activity
	            medicationActivity{
	                id(root:'78598407-9f16-42d5-8ffd-09281a60fe33')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'199812')
	                    }
	                })
	                routeCode(code:'IM', displayName:"Intramuscular injection")
	                consumable {
	                    manufacturedProduct {
	                        manufacturedMaterial {
	                            code(code:'33',
	                                    codeSystem:'2.16.840.1.113883.6.59',
	                                    displayName:'Pneumococcal polysaccharide vaccine') { 
	                                originalText('Pneumococcal polysaccharide vaccine') 
	                            }
	                        }
	                    }//manufactured product
	                }
	            }//medication activity
	            medicationActivity{
	                id(root:'261e94a0-95fb-4975-b5a5-c8e12c01c1bc')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'1997')
	                    }
	                })
	                routeCode(code:'IM', displayName:"Intramuscular injection")
	                consumable {
	                    manufacturedProduct {
	                        manufacturedMaterial {
	                            code(code:'09',
	                                    codeSystem:'2.16.840.1.113883.6.59',
	                                    displayName:'Tetanus and diphtheria toxoids') { 
	                                originalText('Tetanus and diphtheria toxoids') 
	                            }
	                        }
	                    }//manufactured product
	                }
	            }//medication activity                                  
	        }//immunizations section
	        medicalEquipment{
	            text{
	                table(border:'1', width:'100%'){
	                   thead{
	                       tr{
	                           th('Supply/Device')
	                           th('Date Supplied')
	                       }
	                   }
	                   tbody{
	                       tr{
	                           td('Automatic implantable cardioverter/defibrillator')
	                           td('Nov 1999')
	                       }
	                       tr{
	                           td('Total hip replacement prosthesis')
	                           td('1998')
	                       }
	                       tr{
	                           td('Wheelchair')
	                           td('1999')
	                       }
	                   }
	                }
	            }//text
	            supplyActivity{
	                id(root:'2413773c-2372-4299-bbe6-5b0f60664446')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'199911')
	                    }
	                })
	                productInstance{
	                    playingDevice{
	                        code(code:'72506001',
	                                codeSystem:'2.16.840.1.113883.6.96',
	                                displayName:'Automatic implantable cardioverter/defibrillator')
	                    }
	                }
	            }//suplly activity
	            supplyActivity{
	                id(root:'230b0ab7-206d-42d8-a947-ab4f63aad795')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'1998')
	                    }
	                })
	                productInstance{
	                    id(root:'03ca01b0-7be1-11db-9fe1-0800200c9a66')
	                    playingDevice{
	                        code(code:'304120007',
	                                codeSystem:'2.16.840.1.113883.6.96',
	                                displayName:'Total hip replacement prosthesis')
	                    }
	                    scopingEntity{
	                        id(root:'0abea950-5b40-4b7e-b8d9-2a5ea3ac5500')
	                        desc('Good Health Prostheses Company')
	                    }
	                }
	            }//suplly activity
	            supplyActivity{
	                id(root:'c4ffe98e-3cd3-4c54-b5bd-08ecb80379e0')
	                effectiveTime(make{
	                    ivlts{
	                        center(value:'1999')
	                    }
	                })
	                productInstance{
	                    playingDevice{
	                        code(code:'58938008',
	                                codeSystem:'2.16.840.1.113883.6.96',
	                                displayName:'Wheelchair')
	                    }//playingDevice
	                }
	            }//suplly activity
	        }
	        medications{
	            text {
	                table(border:'1',width:'100%') {
	                    thead {
	                        tr {
	                            th('Medication')
	                            th('Instructions')
	                            th('Start date')
	                            th('Status')
	                        }
	                    }
	                    tbody {
	                        tr {
	                            td('Albuterol inhalant')
	                            td('2 puffs QID PRN wheezing')
	                            td(' ')
	                            td('Active')
	                        }
	                        tr {
	                            td('Cephalexin (Keflex)')
	                            td('500mg PO QID x 7 days (for bronchitis)')
	                            td('Mar 28, 2000')
	                            td('No longer active')
	                        }
	                    }
	                }
	            }
	            // Informant: Standard CDA Builder
	            informant {
	                assignedEntity {
	                    id('996-756-495@2.16.840.1.113883.19.5')
	                    representedOrganization {
	                        id('2.16.840.1.113883.19.5')
	                        name('Good Health Clinic')
	                    }
	                }
	            }
	            medicationActivity {
	                id('cdbd33f0-6cde-11db-9fe1-0800200c9a66')
	                effectiveTime(make {
	                    pivlts { period('6 h') }
	                })
	                routeCode(code:'IPINHL', displayName:"Inhalation, oral")
	                doseQuantity(value:2.0)
	                administrationUnitCode(code:"415215001",
	                        codeSystem:"2.16.840.1.113883.6.96",
	                        displayName:"Puff")
	                consumable {
	                    manufacturedProduct {
	                        manufacturedMaterial {
	                            code(code:"307782",
	                                    codeSystem:"2.16.840.1.113883.6.88",
	                                    displayName:"Albuterol 0.09 MG/ACTUAT inhalant solution") { originalText('Albuterol inhalant') }
	                        }
	                    }
	                }
	                precondition {
	                    // TODO add to extension
	                    criterion {
	                        code(code:"ASSERTION",codeSystem:"2.16.840.1.113883.5.4")
	                        value(make {
	                            ce(code:'56018004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Wheezing')
	                        })

	                    }
	                }
	            }
	            medicationActivity {
	                id('cdbd5b07-6cde-11db-9fe1-0800200c9a66')
	                effectiveTime(make {
	                    ivlts {
	                        low('20000328')
	                        high('20000404')
	                    }
	                })
	                
	                effectiveTime(make {
	                    pivlts(operator:'A') { period('6 h') }
	                })
	                routeCode(code:'PO')
	                doseQuantity(value:1.0)
	                consumable {
	                    manufacturedProduct {
	                        manufacturedMaterial {
	                            code(code:"197454",
	                                    codeSystem:"2.16.840.1.113883.6.88",
	                                    displayName:"Cephalexin 500 MG oral tablet") { originalText('Cephalexin') }
	                        }
	                    }
	                }
	                indication {
	                    problemObservation(classCode:'COND') {
	                        id('cdbd5b08-6cde-11db-9fe1-0800200c9a66')
	                        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                        effectiveTime { low('20000328') }
	                        value(make {
	                            ce(code:'32398004',
	                                    codeSystem:'2.16.840.1.113883.6.96',
	                                    displayName:'Bronchitis')
	                        })
	                    }
	                }
	                
	            }
	        }//medications
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
	                        representedOrganization { name('Good Health Insurance') }
	                    }
	                    coveredParty {
	                        id('14d4a520-7aae-11db-9fe1-0800200c9a66')
	                        code('SELF')
	                    }
	                    subscriber {
	                        id('14d4a520-7aae-11db-9fe1-0800200c9a66')
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
	        }//payers
	        planOfCare {
	            text('Plan')
	            text{
	                table(border:'1', width:'100%'){
	                    thead{
	                        tr{
	                            th('Planned Activity')
	                            th('Planned Date')
	                        }
	                    }
	                    tbody{
	                        tr{
	                            td{
	                                content(ID:'Proc3', 'Total hip replacement, left')
	                            }
	                            td{content('1998')}
	                        }
	                    }
	                }
	            }
	            planOfCareActivity {
	                observation(moodCode:'RQO') {
	                    id('9a6d1bac-17d3-4195-89a4-1121bc809b4a')
	                    code(code:'23426006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Pulmonary function test')
	                    statusCode('new')
	                    effectiveTime {
	                        center('20000421')
	                    }
	                }
	                
	            }
	        }//plan of care
	        problems{
	            text('Patient Problems Acts')
	            problemAct{
	                id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
	                problemObservation{
	                    id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    value(make{
	                        cd(code:'233604007',
	                        codeSystem:'2.16.840.1.113883.6.96',
	                        displayName:'Pneumonia')
	                    }
	                    ) 
	                    problemStatus{
	                        value(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
	                    }
	                    problemHealthstatus{
	                        value(code:'81323004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
	                    }
	                }//problem observation
	                episodeObservation{
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    value(make {
	                        cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding')
	                    })
	                    entryRelationship(typeCode:'SAS'){
	                        act(classCode:'ACT', moodCode:'EVN'){
	                            id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
	                            code(nullFlavor:'NA')
	                        }//act
	                    }//entryRelationship
	                }//episode observation
	                //TODO: HERE IS SOMETHING IN SCHEMATRON WRONG?!
//	                patientAwareness{
//	                    awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
//	                    participantRole{ 
//	                        id('996-756-495@2.16.840.1.113883.19.5') 
//	                    }
//	                }
	            }//problem act
	            problemAct{
	                id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
	                problemObservation{
	                    id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    value(make{
	                        cd(code:'233604007',
	                        codeSystem:'2.16.840.1.113883.6.96',
	                        displayName:'Pneumonia')
	                    }
	                    )
//	                    patientAwareness{
//	                        awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
//	                        participantRole{ 
//	                            id('996-756-495@2.16.840.1.113883.19.5') 
//	                        }
//	                    }
	                }//problem observation
	            }//problem act
	        }//problems section
	        procedures{
	            text('Patient Procedures')
	            templateId(root:'2.16.840.1.113883.10.20.1.12')
	            templateId(root:'1.3.6.1.4.1.19376.1.5.3.1.3.11',
	                    assigningAuthorityName:'IHE PCC')
	            text{
	                table(border:'1', width:'100%'){
	                    thead{
	                        tr{
	                            th('Procedure')
	                            th('Date')
	                        }
	                    }//thead
	                    tbody{
	                        tr{
	                            td{
	                                content(ID:'Proc2', 'Total hip replacement, left')
	                            }//td
	                            td{content('1998')}
	                        }//tr
	                    }//tbody
	                }//table
	            }//text
	            procedureActivity{
	                procedure{
	                    id(root:'e401f340-7be2-11db-9fe1-0800200c9a66')
	                    code(code:'52734007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Total hip replacement'){
	                        originalText{ reference(value:'#Proc2') }//originalText
	                        qualifier{
	                            name(code:'272741003', displayName:'Laterality')
	                            value(code:'7771000', displayName:'Left')
	                        }//qualifier
	                    }//code
	                    text('IHE Requires reference to go here instead of originalText of code.<reference')
	                    statusCode('completed')
	                    effectiveTime('1998')
	                    performer{
	                        assignedEntity{
	                            id(root:'e401f340-7be2-11db-9fe1-0800200c9a66')
	                            assignedPerson{ name('Procedure Performers Name') }//assignedPerson
	                        }//assignedEntity
	                    }//performer
	                    age {
	                        value(make { _int(57) }
	                        )
	                    }
	                    encounterLocation{
	                        id(root:'2.16.840.1.113883.19.5')
	                        playingEntity{ name('Very Good Health Clinic') }//playingEntity
	                    }
	                    productInstance{
	                        id(root:'03ca01b0-7be1-11db-9fe1-0800200c9a66')
	                        playingDevice{
	                            code(code:'304120007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Total hip replacement prosthesis')
	                        }//playingDevice
	                        scopingEntity{
	                            id(root:'0abea950-5b40-4b7e-b8d9-2a5ea3ac5500')
	                            desc('Good Health Prostheses Company')
	                        }//scopingEntity
	                    }
	                    entryRelationship(typeCode:'REFR'){
	                        act(classCode:'ACT', moodCode:'EVN'){
	                            templateId(root:'1.3.6.1.4.1.19376.1.5.3.1.4.4', assigningAuthorityName:'IHE PCC')
	                            code(nullFlavor:'NA')
	                            text{ reference(value:'PtrToSectionText') }//text
	                            reference(typeCode:'REFR'){
	                                externalDocument(classCode:'DOC', moodCode:'EVN'){ text('Location of Documentation -  URL or other') }//externalDocument
	                            }//reference
	                        }//act
	                    }//entryRelationship
	                    informationSource{
	                        value(make{
	                            st('Unknown')
	                        })
	                    }
	                }//procedure activity procedure
	            }
	        }//procedures section
	        purpose {
	            text('Transfer of Care!')
	            purposeActivity {
	                act(moodCode:'EVN', classCode:'ACT') {
	                    code(code:'308292007',
	                            codeSystem:'2.16.840.1.113883.6.96',
	                            displayName:'Transfer of care')
	                    statusCode('completed')
	                }
	            }
	        }//purpose
	        results{
	            text('Patient Observation Results')
	            title('Other results')
	            resultOrganizer(classCode:'BATTERY'){
	                id(root:'7d5a02b0-67a4-11db-bd13-0800200c9a66')
	                code(code:'43789009',
	                        codeSystem:'2.16.840.1.113883.6.96',
	                        displayName:'CBC WO DIFFERENTIAL')
	                statusCode(code:'completed')
	                effectiveTime(value:'200003231430')
	                resultObservation{
	                    id(root:'107c2dc0-67a5-11db-bd13-0800200c9a66')
	                    code(code:'30313-1', codeSystem:'2.16.840.1.113883.6.1', displayName:'HGB')
	                    statusCode(code:'completed')
	                    effectiveTime(value:'200003231430')
	                    value(make {
	                        pq(value:13.2, unit:'g/dl')
	                    })
	                    interpretationCode(code:'N', codeSystem:'2.16.840.1.113883.5.83')
	                    referenceRange{
	                        observationRange{
	                            text('M 13-18 g/dl; F 12-16 g/dl')
	                        }//observationRange
	                    }//referenceRange
	                }//observation
	                resultObservation{
	                    id(root:'8b3fa370-67a5-11db-bd13-0800200c9a66')
	                    code(code:'33765-9', codeSystem:'2.16.840.1.113883.6.1', displayName:'WBC')
	                    statusCode(code:'completed')
	                    value(make{
	                        pq(value:6.7, unit:'10+3/ul')
	                    })
	                    interpretationCode(code:'N', codeSystem:'2.16.840.1.113883.5.83')
	                    referenceRange{
	                        observationRange{
	                            value( make {
	                                ivlpq{
	                                    low(value:4.3, unit:'10+3/ul')
	                                    high(value:10.8, unit:'10+3/ul')
	                                }
	                            })//value
	                        }//observationRange
	                    }//referenceRange
	                }//observation
	            }//result organizer
	        }//results section
	        socialHistory{
	            text{
	                table(border:'1', width:'100%'){
	                    thead{
	                        tr{
	                            th('Social History Element')
	                            th('Description')
	                            th('Effective Dates')
	                        }
	                    }   
	                    tbody{
	                        tr{
	                            td('Cigarette smoking')
	                            td('1 pack per day')
	                            td('1947 - 1972')
	                        }
	                        tr{
	                            td('')
	                            td('None')
	                            td('1973 -')
	                        }                            
	                    }
	                }//table
	            }//text 
	            socialHistoryObservation{
	                id(root:'9b56c25d-9104-45ee-9fa4-e0f3afaa01c1')
	                code(code:'230056004', codeSystem:'2.16.840.1.113883.6.96',  displayName:'Cigarette smoking')
	                effectiveTime{
	                    low(value:'1947')
	                    high(value:'1972')
	                }
	                value(make{
	                    st('1 pack per day')
	                }) 
	            }
	            socialHistoryObservation{
	                id(root:'45efb604-7049-4a2e-ad33-d38556c9636c')
	                code( code:'230056004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Cigarette smoking')
	                effectiveTime{
	                    low(value:'1973')
	                }
	                value(make{
	                    st('None')
	                })
	                episodeObservation{
	                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	                    entryRelationship(typeCode:'SAS'){
	                        observation(classCode:'OBS', moodCode:'EVN'){
	                            id(root:'9b56c25d-9104-45ee-9fa4-e0f3afaa01c1')
	                            code(code:'230056004', 
	                                    codeSystem:'2.16.840.1.113883.6.96',
	                                    displayName:'Cigarette smoking')
	                        }
	                    }//target social history observation
	                }
	            }
	        }//social history section
	        vitalSigns{
	            text('Patient Vital Signs')
	            vitalSignsOrganizer(classCode:'CLUSTER'){
	                id(root:'c6f88320-67ad-11db-bd13-0800200c9a66')
	                code(code:'46680005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Vital signs')
	                statusCode(code:'completed')
	                resultObservation{
	                    id(root:'c6f88321-67ad-11db-bd13-0800200c9a66')
	                    code(code:'50373000', codeSystem:'2.16.840.1.113883.6.96', displayName:'Body height')
	                    statusCode(code:'completed')
	                    value( make {
	                        pq(value:177.0, unit:'cm')
	                    })
	                }//result observation
	                resultObservation{
	                    id(root:'c6f88322-67ad-11db-bd13-0800200c9a66')
	                    code(code:'27113001', codeSystem:'2.16.840.1.113883.6.96', displayName:'Body weight')
	                    statusCode(code:'completed')
	                    effectiveTime(value:'19991114')
	                    value( make {
	                        pq('86 kg')
	                    })
	                }//result observation
	                resultObservation{
	                    id(root:'c6f88323-67ad-11db-bd13-0800200c9a66')
	                    code(code:'271649006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Systolic BP')
	                    statusCode(code:'completed')
	                    effectiveTime(value:'19991114')
	                    value( make{
	                        pq(value:132.0, unit:'mm[Hg]')
	                    })
	                }
	                resultObservation{
	                    id(root:'c6f88324-67ad-11db-bd13-0800200c9a66')
	                    code(code:'271650006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Diastolic BP')
	                    statusCode(code:'completed')
	                    effectiveTime(value:'19991114')
	                    value( make{
	                        pq(value:86.0, unit:'mm[Hg]')
	                    })
	                }
	            }//vital signs organizer
	        }//vital signs section
	    }//structured body
	}//component
}//document