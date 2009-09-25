package builders.content.header

clinicalDocument {
	// templateId(root:'2.16.840.1.113883.3.27.1776')
	id(root:'2.16.840.1.113883.19.4', extension:'c266')
	code(code:'11488-4', 
			codeSystem:'2.16.840.1.113883.6.1', 
			codeSystemName:'LOINC',
			displayName:'Consultation note'
			)
	title('Good Health Clinic Consultation Note')
	effectiveTime('20000407')
	versionNumber(2)
	confidentialityCode('N')
	languageCode(code:'en_US')
	setId (extension:"BB35", root:"2.16.840.1.113883.19.7")
	recordTarget {
		patientRole {
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
				guardian{
					code(code:'guardian code', displayName:'Guardian Entry')
					guardianPerson{
						name {
							given('Guardian') 
							family('Person')
						}
					}
				}
			}
			providerOrganization { id(root:"2.16.840.1.113883.19.5") }
		}//patient role
	}//record target
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
	participant(typeCode:'CSM') {
	    associatedEntity (classCode:'GUAR'){
          id(root:'4ff51570-83a9-47b7-91f2-93ba30373141')
          addr{
              streetAddressLine('17 Daws Rd.')
              city('Blue Bell')
              state('MA')
              postalCode('02368')
          }
          telecom{
              value('tel:(888)555-1212')
          }
          associatedPerson{
              name{
                  given('Kenneth')
                  family('Ross')
              }//name
          }//associatedPerson
	    }//associatedEntity
	}
	relatedDocument(typeCode:"APND") {
		parentDocument {
			id(extension:"a123", root:"2.16.840.1.113883.19.4")
			setId(extension:"BB35", root:"2.16.840.1.113883.19.7")
			versionNumber(1)
		}
	}
	componentOf {
		encompassingEncounter {
			id(extension:"KPENC1332", root:"2.16.840.1.113883.19.6")
			effectiveTime { low("20000407") }
			encounterParticipant(typeCode:"CON") {
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
					representedOrganization { id(root:"2.16.840.1.113883.19.5") }
				}
			}
			location {
				healthCareFacility {
					code(code:"GIM",
					     codeSystem:"2.16.840.1.113883.5.10588",
						 displayName:"General internal medicine clinic"
					)
				}
			}
		}
	}
	/* component with structured content*/
    component {
        structuredBody {
            component {
                section {
                    code(
                    code: '10164-2',
                            codeSystem: '2.16.840.1.113883.6.1',
                            codeSystemName: 'LOINC'
                            )
                    title('History of Illness')
                    text('a narrative content.')
                    entry {
                        act {
                            id(nullFlavor:'NA')
                            code(code: '23426006',
                                            codeSystem: '2.16.840.1.113883.6.96',
                                            codeSystemName: 'SNOMED CT',
                                            displayName: 'Pulmonary function test')//code
                                            participant(typeCode:'CSM'){
                            participantRole(classCode:'MANU'){
                                playingEntity{
                                    code(code:'70618', 
                                            codeSystem:'2.16.840.1.113883.6.88', 
                                            displayName:'default class code')
                                    }//playingEntity
                                }//participantRole
                            }//participant
                            participant(typeCode:'CSM'){
                                participantRole(classCode:'MANU'){
                                    playingEntity(classCode:'MMAT'){
                                        code(code:'70618', 
                                                codeSystem:'2.16.840.1.113883.6.88', 
                                                displayName:'mmat class code')
                                    }//playingEntity
                                }//participantRole
                            }//participant
                            participant(typeCode:'CSM'){
                                participantRole(classCode:'MANU'){
                                    playingDevice{
                                        code(code:'70618', 
                                                codeSystem:'2.16.840.1.113883.6.88', 
                                                displayName:'default device class code')
                                    }//playingEntity
                                }//participantRole
                            }//participant
                            participant(typeCode:'CSM'){
                                participantRole(classCode:'MANU'){
                                    playingDevice(classCode:'DEV'){
                                        code(code:'70618', 
                                                codeSystem:'2.16.840.1.113883.6.88', 
                                                displayName:'set device class code')
                                    }//playingEntity
                                }//participantRole
                            }//participant
                        }//act
                    }//entry
                }//section
            }//component
        }//structured body
    }
}



