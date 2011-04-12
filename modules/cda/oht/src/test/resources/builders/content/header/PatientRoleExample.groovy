package builders.content.header

patientRole {
	id("12345@2.16.840.1.113883.19.5")                 
	patient {
		name {
			given('Henry') 
			family('Levin')
			suffix('the 7th')
		}
		administrativeGenderCode("M")
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
	providerOrganization { 
	    id("2.16.840.1.113883.19.5") 
	}
}


