package builders.content.entry

entry{
	act(classCode:'ACT', moodCode:'INT', negationInd:'false'){
		id(nullFlavor:'NA')
		code(code:'23426006',
				codeSystem:'2.16.840.1.113883.6.96',
				codeSystemName:'SNOMED CT',
				displayName:'Pulmonary function test'
				)//code
		text('Complete PFTs with lung volumes.')
		subject{ relatedSubject() }//subject
		entryRelationship(typeCode:'COMP'){
			act(classCode:'ACT', moodCode:'INT'){
				code(
				code:'252472004',
						codeSystem:'2.16.840.1.113883.6.96',
						codeSystemName:'SNOMED CT',
						displayName:'Lung volume test'
						)//code    
			}
		}//entry relationship
		entryRelationship(typeCode:'COMP'){
			act(classCode:'ACT', moodCode:'INT'){
				code(
				code:'252472005',
						codeSystem:'2.16.840.1.113883.6.96',
						codeSystemName:'SNOMED CT',
						displayName:'Lung volume test additional'
						)//code    
			}
		}//entry relationship
	}//act
}//entry


