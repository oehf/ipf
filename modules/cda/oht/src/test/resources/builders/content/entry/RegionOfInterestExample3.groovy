package builders.content.entry

entry {
	regionOfInterest(classCode: 'ROIOVL', moodCode: 'EVN', ID: 'MM1') {
		id(root: '2.16.840.1.113883.19.3.1')
		code(code: 'ELLIPSE')
		value(value: '3', unsorted: 'true')
		value(value: '1')
		value(value: '3')
		value(value: '7')
		entryRelationship(typeCode: 'SUBJ') {
			observationMedia(classCode: 'OBS', moodCode: 'EVN') {
				id(root: '2.16.840.1.113883.19.2.1')
				value(mediaType: 'image/gif') { reference(value: 'lefthand.gif') }//value
			}//observation media
		}//entry relationship
	}//region of interest
}//entry}
