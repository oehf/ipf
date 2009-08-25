package builders.content.entry

entry {
	observation(classCode: 'OBS', moodCode: 'EVN') {
		code(code: '271807003',
		codeSystem: '2.16.840.1.113883.6.96',
		codeSystemName: 'SNOMED CT',
		displayName: 'Rash')
		statusCode(code: 'completed')
		methodCode(code: '32750006',
				codeSystem: '2.16.840.1.113883.6.96',
				codeSystemName: 'SNOMED CT',
				displayName: 'Inspection')
		targetSiteCode(code: '48856004',
				codeSystem: '2.16.840.1.113883.6.96',
				codeSystemName: 'SNOMED CT',
				displayName: 'Skin of palmer surface of index finger') {
					qualifier {
						name(code: '78615007',
						codeSystem: '2.16.840.1.113883.6.96',
						codeSystemName: 'SNOMED CT',
						displayName: 'with laterality')
						value(code: '7771000',
								codeSystem: '2.16.840.1.113883.6.96',
								codeSystemName: 'SNOMED CT',
								displayName: 'left')
					}//qualifier
				}//target site code
		entryRelationship(typeCode: 'SPRT') {
			regionOfInterest(classCode: 'ROIOVL', moodCode: 'EVN', ID: 'MM1') {
				id(root: '2.16.840.1.113883.19.3.1')
				code(code: 'ELLIPSE')
				value(value: '3')
				value(value: '1')
				value(value: '3')
				value(value: '7')
				value(value: '2')
				value(value: '4')
				value(value: '4')
				value(value: '4')
				entryRelationship(typeCode: 'SUBJ') {
					observationMedia(classCode: 'OBS', moodCode: 'EVN') {
						id(root: '2.16.840.1.113883.19.2.1')
						value(mediaType: 'image/gif') { reference(value: 'lefthand.gif') }//value
					}//observation media
				}//entry relationship embedded
			}//region of interest
		}//entry relationship
	}//observation
}//entry



