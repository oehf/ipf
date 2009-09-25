package builders.content.section

ccd_medications{
	text {
		table(border:'1',width:'100%') {
			thead {
				tr {
					th('Medication')
					th('Instructions')
					th('Start date')
					th('Status')
				}
			}
			tbody {
				tr {
					td('Albuterol inhalant')
					td('2 puffs QID PRN wheezing')
					td(' ')
					td('Active')
				}
				tr {
					td('Cephalexin (Keflex)')
					td('500mg PO QID x 7 days (for bronchitis)')
					td('Mar 28, 2000')
					td('No longer active')
				}
			}
		}
	}
	// Informant: Standard CDA Builder
	informant {
		assignedEntity {
			id('996-756-495@2.16.840.1.113883.19.5')
			representedOrganization {
				id('2.16.840.1.113883.19.5')
				name('Good Health Clinic')
			}
		}
	}
	medicationActivity {
		id('cdbd33f0-6cde-11db-9fe1-0800200c9a66')
		effectiveTime(make {
			pivlts { period('6 h') }
		})
		routeCode(code:'IPINHL', displayName:"Inhalation, oral")
		doseQuantity(value:2.0)
		maxDoseQuantity{
		    numerator(value:3.0)
		    denominator(value:5.0)
		}
		administrationUnitCode(code:"415215001",
				codeSystem:"2.16.840.1.113883.6.96",
				displayName:"Puff")
        consumable {
		    manufacturedProduct {
			    manufacturedMaterial {
				    code(code:"307782",
					        codeSystem:"2.16.840.1.113883.6.88",
					        displayName:"Albuterol 0.09 MG/ACTUAT inhalant solution") { originalText('Albuterol inhalant') }
			    }
            }
		}
        patientInstruction{
            id('cdbd5b08-6cde-11db-9fe1-0800200b8a66')
            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
            effectiveTime{
                low('20000338')
            }
            text('Read the instructions carefully')
        }
		precondition {
			// TODO add to extension
			criterion {
				code(code:"ASSERTION",codeSystem:"2.16.840.1.113883.5.4")
				value(make {
					ce(code:'56018004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Wheezing')
				})
			}
		}
		seriesNumber(5)
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
	}
	medicationActivity {
		id('cdbd5b07-6cde-11db-9fe1-0800200c9a66')
		effectiveTime(make {
			ivlts {
				low('20000328')
				high('20000404')
			}
		})
		
		effectiveTime(make {
			pivlts(operator:'A') { period('6 h') }
		})
		routeCode(code:'PO')
		doseQuantity(value:1.0)
        consumable {
		    manufacturedProduct {
    		    manufacturedMaterial {
	                code(code:"197454",
					        codeSystem:"2.16.840.1.113883.6.88",
					        displayName:"Cephalexin 500 MG oral tablet") { originalText('Cephalexin') }
			    }
            }
		}
		indication {
			problemObservation(classCode:'COND') {
				id('cdbd5b08-6cde-11db-9fe1-0800200c9a66')
				code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
				effectiveTime { low('20000328') }
				value(make {
					ce(code:'32398004',
					        codeSystem:'2.16.840.1.113883.6.96',
					        displayName:'Bronchitis')
				})
			}
		}
		
	}
	// TODO: add supplyActivity to test
}
