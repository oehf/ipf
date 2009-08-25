package builders.content.entry

entry {
	observation(classCode: 'COND', moodCode: 'EVN') {
		code(code: '14657009',
		codeSystem: '2.16.840.1.113883.6.96',
		codeSystemName: 'SNOMED CT',
		displayName: 'Established diagnosis'
		)
		statusCode(code: 'completed')
		effectiveTime(value: '200004071530')
		value(
				make {
					ce(code: '59621000',
					codeSystem: '2.16.840.1.113883.6.96',
					codeSystemName: 'SNOMED CT',
					displayName: 'Essential hypertension'){
						translation(code: '4019',
						codeSystem: '2.16.840.1.113883.6.2',
						codeSystemName: 'ICD9CM',
						displayName: 'HYPERTENSION NOS')
					}
				}
				)//value
		reference(typeCode: 'ELNK') {
			externalObservation(classCode: 'CNOD') { id(root: '2.16.840.1.113883.19.1.37') }//externalObservation
		}//reference one
		reference(typeCode: 'SPRT') {
			externalObservation(classCode: 'DGIMG') {
				id(root: '2.16.840.1.113883.19.1.14')
				code(
						code: '56350004',
								codeSystem: '2.16.840.1.113883.6.96',
								codeSystemName: 'SNOMED CT',
								displayName: 'Chest-X-ray'
								)
			}
		}//reference two
		reference(typeCode: 'SPRT') {
			seperatableInd(value: 'false')
			externalDocument {
				id(root: '2.16.840.1.113883.19.4.789')
				text(mediaType: 'multipart/related') { reference(value: 'HTN.cda') }
				setId(root: '2.16.840.1.113883.19.7.2465')
				versionNumber(value: '1')
			}//external document
		}//reference three
	}//observation
}//entry
