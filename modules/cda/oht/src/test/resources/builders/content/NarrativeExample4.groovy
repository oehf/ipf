package builders.content

strucDocText {
	paragraph('Payer information')
	table(border: '1', width: '100%') {
		thead {
			tr {
				th('Payer name')
				th('Policy type')
				th('Covered Party ID')
				th('Authorizations')
			}
		}
		tbody {
			tr {
				td('Good Health Insurance')
				td('Extended healthcare / Self')
				td('14d4a520-7aae-11db-9fe1-0800200c9a66')
				td {
					linkHtml(href: 'Colonoscopy.pdf', 'Colonoscopy')
				}
			}
		}
		
	}
}


