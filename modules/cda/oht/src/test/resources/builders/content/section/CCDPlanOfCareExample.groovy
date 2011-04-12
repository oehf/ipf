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
	planOfCareActivity {
	    substanceAdministration(moodCode:'PRP'){
	      id('cdbd5b07-6cde-11db-9fe1-0800200c9a66')
	      effectiveTime(make {
	          ivlts {
	              low('20090328')
	              high('20090404')
	          }
	      })
	      routeCode(code:'PO')
	      doseQuantity(value:1.0)
	      rateQuantity(value:1.0)
	      maxDoseQuantity{
	          numerator(value:3.0)
	          denominator(value:3.0)
	      }
	      consumable{
	          manufacturedProduct {
	              manufacturedMaterial {
	                  code(code:"197454",
	                          codeSystem:"2.16.840.1.113883.6.88",
	                          displayName:"Cephalexin 500 MG oral tablet") { originalText('Cephalexin') }
	                  name('Material name')
	              }
	              id('cdbd5b07-6cde-11db-9fe1-0800200c9c88')
	              manufacturerOrganization{
	                  name('Health Product Manufacturer GmbH')
	                  id('2.16.840.1.113883.19.5')
	              } 
	          }
	      }
	    }
	}//planOfCareActivity SubstanceAdministration
	planOfCareActivity {
	    act(moodCode:'RQO'){
	        id('cdbd5b08-6cde-11db-9fe1-0800200b8a66')
	        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
	        effectiveTime { low('20000328') }
	        text("Come for check up every 3 weeks for 6 months")
	        informant {
	            assignedEntity {
	                id('996-756-495@2.16.840.1.113883.19.5')
	                representedOrganization {
	                    id('2.16.840.1.113883.19.5')
	                    name('Very Good Health Clinic')
	                }
	            }
	        }//informant
	    }
	}//planOfCareActivity Act
	planOfCareActivity {
	    encounter(moodCode:'RQO'){
	        effectiveTime(value:'200003231430')
	        id(root:'2a620155-9d11-439e-92b3-5d9815ff4de8')
	        code(code:'GENRL', codeSystem:'2.16.840.1.113883.5.4', displayName:'General'){
	            originalText('Checkup Examination')
	        }//code
	        text('Checkup every fortnight')
	        informationSource{
	            value(make{
	                st('Unknown')
	            })
	        }//informationSource
	    }
	}//planOfCareActivity Encounter

}
