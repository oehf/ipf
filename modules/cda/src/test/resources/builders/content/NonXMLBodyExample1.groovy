package builders.content

nonXMLBody(moodCode: 'EVN', classCode: 'DOCBODY') {
	typeId(
	root: '2.16.840.1.113883.1.3',
			extension: 'type'
			)
	text(
			mediaType: 'application/pdf',
					representation: 'B64',
					'this is the binary value as TXT/B64'
					)
}

