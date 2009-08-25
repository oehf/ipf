package builders.content.entry

entry {
	observation(classCode: 'CNOD', moodCode: 'EVN', negationInd: true) {
		id(root:'uuid')
		code(
				code: '14657009',
						codeSystem: '2.16.840.1.113883.6.96',
						codeSystemName: 'SNOMED CT',
						displayName: 'Established diagnosis'
						)//code
		statusCode(code: 'completed')
		effectiveTime(value: '200004071530')
		value(make { ce(nullFlavor: 'NP') })
		value(make {
			ce(
			code: '40275004',
					codeSystem: '2.16.840.1.113883.6.96',
					codeSystemName: 'SNOMED CT',
					displayName: 'Contact dermatitis') {
				translation(
				code: '692.9',
						codeSystem: '2.16.840.1.113883.6.2', codeSystemName: 'ICD9CM',
						displayName: 'Contact Dermatitis, NOS'
						)
			}//translation
		})//value
		methodCode(
				code: '37931006',
						codeSystem: '2.16.840.1.113883.6.96',
						codeSystemName: 'SNOMED CT',
						displayName: 'Auscultation'
						)//method code
		targetSiteCode(
				code: '48856004',
						codeSystem: '2.16.840.1.113883.6.96',
						codeSystemName: 'SNOMED CT',
						displayName: 'Skin of palmer surface of index finger'
						) {
					qualifier {
						name(code: '78615007',
						codeSystem: '2.16.840.1.113883.6.96',
						codeSystemName: 'SNOMED CT',
						displayName: 'with laterality'
						)//name
						value(code: '7771000',
								codeSystem: '2.16.840.1.113883.6.96',
								codeSystemName: 'SNOMED CT',
								displayName: 'left')//value
					}//qualifier
				}//targetSiteCode
	}//observation
}//entry




