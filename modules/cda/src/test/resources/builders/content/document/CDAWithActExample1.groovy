package builders.content.document

clinicalDocument {
	typeId(root:'2.16.840.1.113883.1.3', extension:'POCD_HD000040')
	templateId(root:'2.16.840.1.113883.3.27.1776')
	id(root: '2.16.840.1.113883.19.4', extension: 'c266')
	code(
			code: '11488-4',
					codeSystem: '2.16.840.1.113883.6.1',
					codeSystemName: 'LOINC',
					displayName: 'Consultation note'
					)
	title('Good Health Clinic Consultation Note')
	effectiveTime('20000407')
	versionNumber(2)
	confidentialityCode(
			code: 'N',
					codeSystem: '2.16.840.1.113883.5.25')
	languageCode(code: 'en_US')
	setId(extension: "BB35", root: "2.16.840.1.113883.19.7")
	recordTarget {
		patientRole {
			id(extension: '12345', root: '2.16.840.1.113883.19.5')
		}
	}
	author()
	/* component with structured content*/
	component {
		structuredBody {
			component {
				section() {
					code(
					code: '10164-2',
							codeSystem: '2.16.840.1.113883.6.1',
							codeSystemName: 'LOINC'
							)
					title('History of Illness')
					text('a narrative content.')
					entry {
						act {
							id(root:'xxx')
							code(
									code: '23426006',
											codeSystem: '2.16.840.1.113883.6.96',
											codeSystemName: 'SNOMED CT',
											displayName: 'Pulmonary function test'
											)//code
						}//act
					}//entry one
					entry{
						act(classCode:'ACT', moodCode:'INT'){
							id(nullFlavor:'NA')
							code(
									code:'223468009',
											codeSystem:'2.16.840.1.113883.6.96',
											codeSystemName:'SNOMED CT',
											displayName:'Teaching of skills'
											) {
										qualifier{
											name(
											code:'363702006',
													codeSystem:'2.16.840.1.113883.6.96',
													codeSystemName:'SNOMED CT',
													displayName:'has focus'
													)
											value(
													code:'29893006',
															codeSystem:'2.16.840.1.113883.6.96',
															codeSystemName:'SNOMED CT',
															displayName:'Peak flow rate measurement'
															)
										}//code qualifier
									}//code
						}//act
					}//entry two
				}//section
			}//component
		}//structured body
	}
}


