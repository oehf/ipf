package builders.content.section

ccd_purpose {
	text('Transfer of Care!')
	purposeActivity {
	    act(moodCode:'EVN', classCode:'ACT') {
			code(
			code:'308292007',
					codeSystem:'2.16.840.1.113883.6.96',
					displayName:'Transfer of care')
			statusCode('completed')
		}
	}
}
