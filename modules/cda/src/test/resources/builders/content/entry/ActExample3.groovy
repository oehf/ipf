package builders.content.entry

entry{
	act {
		code(code:'223468009',
		  codeSystem:'2.16.840.1.113883.6.96',
		  codeSystemName:'SNOMED CT',
		  displayName:'Teaching of skills'
		) {  
			qualifier{
				name(code:'363702006',
				codeSystem:'2.16.840.1.113883.6.96',
				codeSystemName:'SNOMED CT',
				displayName:'has focus')
				value(code:'29893006',
						codeSystem:'2.16.840.1.113883.6.96',
						codeSystemName:'SNOMED CT',
						displayName:'Peak flow rate measurement')
			}//code qualifier
		}//code
	}//act
}//entry
