package builders.content.section

ccd_planOfCare {
	text('Plan')
	text{
		table(border:'1', width:'100%'){
			thead{
				tr{
					th('Planned Activity')
					th('Planned Date')
				}
			}
			tbody{
				tr{
					td{
						content(ID:'Proc3', 'Total hip replacement, left')
					}
					td{content('1998')}
				}
			}
		}
	}
	planOfCareActivity {
	    observation(moodCode:'RQO') {
	        id('9a6d1bac-17d3-4195-89a4-1121bc809b4a')
	        code(code:'23426006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Pulmonary function test')
	        statusCode('new')
	        effectiveTime {
	            center('20000421')
	        }
	    }
	    
	}
}
