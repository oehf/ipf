package builders.content.section

ccd_payers {
	text {
		paragraph('Payer information')
		table(border:'1',width:'100%') {
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
						linkHtml(href:'Colonoscopy.pdf', 'Colonoscopy')
					}
				}
			}
			
		}                              
	}
	coverageActivity {
		id('1fe2cdd0-7aad-11db-9fe1-0800200c9a66')
		policyActivity {
			id('3e676a50-7aac-11db-9fe1-0800200c9a66')
			code('EHCPOL')
			payer {
				id('329fcdf0-7ab3-11db-9fe1-0800200c9a66')
				representedOrganization { name('Good Health Insurance') }
			}
			coveredParty {
				id('14d4a520-7aae-11db-9fe1-0800200c9a66')
				code('SELF')
			}
			subscriber {
			    id('14d4a520-7aae-11db-9fe1-0800200c9a66')
			}
			authorizationActivity {
				id('f4dce790-8328-11db-9fe1-0800200c9a66')
				code(nullFlavor:'NA')
				promise {
					procedure(moodCode:'PRMS') {
						code(
						code:'73761001',
								codeSystem:'2.16.840.1.113883.6.96',
								displayName:'Colonoscopy'
								)
					}
				}
			}
		}
	}
}
