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
			addr{
				streetAddressLine('21 North Ave')
	            city('Burlington')
	            state('MA')
	            postalCode('01803')
	            country('USA')
			}//addr	  
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
}



