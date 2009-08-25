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
}//document