package builders.content.section

ccd_alerts{
	text('Patient Alerts')
	problemAct{
		id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
		alertObservation{
			id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
			code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
			effectiveTime('20000328')
			alertStatus{
				value(code:'55561003', 
				        codeSystem:'2.16.840.1.113883.6.96',
				        displayName:'Active')
			}
			participantAgent{
				playingEntity{
					code(code:'70618', 
					        codeSystem:'2.16.840.1.113883.6.88',
					        displayName:'Penicillin')
				}
			}//participant agent
			reactionObservation{
				code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
				value(make{
					cd(code:'247472004', 
					        codeSystem:'2.16.840.1.113883.6.96',
					        displayName:'Hives')
				}
				)
				severityObservation{
					value(make{
						cd(code:'247472004', 
						        codeSystem:'2.16.840.1.113883.6.96',
						        displayName:'Hives')
					})
				}
			}//reaction observation
		}//alert observation
	}//problem act
}//alerts section

