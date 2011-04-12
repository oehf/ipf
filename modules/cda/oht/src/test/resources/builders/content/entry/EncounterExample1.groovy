package builders.content.entry

entry {
	encounter(classCode: 'ENC', moodCode: 'RQO') {
		code(
		   code: '185389009',
		   codeSystem: '2.16.840.1.113883.6.96',
           codeSystemName: 'SNOMED CT',
           displayName: 'Follow-up visit')
		effectiveTime {
			low('20000412')
			high('20000417')
		}
	}
}//entry
