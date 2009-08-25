package builders.content.section

ccd_familyHistory {
	text('skipped') 
	familyMember {
		familyPerson {
			code(code:'9947008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological father')
			subject {
				administrativeGenderCode('M')
				birthTime(value:'1912')
			}
		}
		causeOfDeath() {
			id('d42ebf70-5c89-11db-b0de-0800200c9a66')
			code('ASSERTION')
			// value(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI')                                       
			value(make { 
				ce(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI') 
			}
			)
			cause {
				id('6898fae0-5c8a-11db-b0de-0800200c9a66')
				code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
				statusCode('completed')
				// value(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
				value(make {
					ce(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
				}
				)
			}
			age {
				value(make { _int(57) }                                    
				)
			}
		}
		familyHistoryObservation {
			id('5bfe3ec0-5c8b-11db-b0de-0800200c9a66')
			code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
			value(make {
				ce(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')
			}
			)
			// value(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')                                       
			age {
				value(make { _int(40) }
				)
			}
		}
	}                           
	familyMember {
		familyPerson {
			code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological mother')
			subject {
				administrativeGenderCode('F')
				birthTime(value:'1912')
			}
		}                               
		familyHistoryObservation {
			id('a13c6160-5c8b-11db-b0de-0800200c9a66')
			code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
			value(make {
				ce(code:'195967001',codeSystem:'2.16.840.1.113883.6.96',displayName:'Asthma')
			}
			)
			age {
				value(make { _int(30) }
				)
			}
		}
	}                           
}


