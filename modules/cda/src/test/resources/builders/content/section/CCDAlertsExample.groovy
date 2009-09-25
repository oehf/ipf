package builders.content.section

ccd_alerts{
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
				reactionIntervention{
				    medicationActivity {
				        id('cdbd33f0-6cde-11db-9fe1-0800200c9a66')
				        effectiveTime(make {
				            pivlts { 
				                period('6 h') 
				            }
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
				    }//medication activity
				}//reaction intervention
				reactionIntervention{
                    procedureActivityProcedure{
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
                                assignedPerson{ name('Procedure Performers Name') }//assignedPerson
                            }//assignedEntity
                        }//performer
                        age {
                            value(make { _int(57) }
                            )
                        }
                        problemObservationReason{
                            id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
                            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                            value(make{
                                cd(code:'233604007',
                                codeSystem:'2.16.840.1.113883.6.96',
                                displayName:'Pneumonia')
                            }
                            )
                            patientAwareness{
                                awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
                                participantRole(classCode:'MANU'){ 
                                    id('996-756-495@2.16.840.1.113883.19.5')
                                }
                            }
                        }//problem observation
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
                }//reaction intervention 2
			}//reaction observation
		}//alert observation
	}//problem act
}//alerts section

