package builders.content.section

ccd_problems{
	text('Patient Problems Acts')
	problemAct{
		id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
		problemObservation{
			id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
			code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
			value(make{
				cd(code:'233604007',
				codeSystem:'2.16.840.1.113883.6.96',
				displayName:'Pneumonia')
			}
			) 
			problemStatus{
				value(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
			}
			problemHealthstatus{
				value(code:'81323004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Alive and well')
			}
		}//problem observation
		episodeObservation{
			code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
			value(make {
				cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding')
			})
			entryRelationship(typeCode:'SAS'){
				act(classCode:'ACT', moodCode:'EVN'){
					id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
					code(nullFlavor:'NA')
				}//act
			}//entryRelationship
		}//episode observation
		patientAwareness{
			awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
			participantRole{ 
			    id('996-756-495@2.16.840.1.113883.19.5')
			}
		}
	}//problem act
	problemAct{
		id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
		problemObservation{
			id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
			code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
			value(make{
				cd(code:'233604007',
				codeSystem:'2.16.840.1.113883.6.96',
				displayName:'Pneumonia')
			}
			)
			patientAwareness{
				awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
				participantRole{ 
				    id('996-756-495@2.16.840.1.113883.19.5')
				}
			}
		}//problem observation
	}//problem act
}//problems section
