package builders.content.entry

entry{
	procedure(classCode:'PROC', moodCode:'EVN'){
		code(
		code:'30549001', 
				codeSystem:'2.16.840.1.113883.6.96', 
				codeSystemName:'SNOMED CT' ,
				displayName:'Suture removal'
				)
		statusCode(code:'completed')
		effectiveTime(value:'200004071430')
		targetSiteCode(
				code:'66480008',
						codeSystem:'2.16.840.1.113883.6.96',
						codeSystemName:'SNOMED CT',
						displayName:'Left forearm'
						)
	}//procedure
}//entry
