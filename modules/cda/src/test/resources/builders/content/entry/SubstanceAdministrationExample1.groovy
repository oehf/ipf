package builders.content.entry

entry{
	substanceAdministration(classCode:'SBADM', moodCode:'EVN'){
		id(root:'2.16.840.1.113883.19.8.1')
		text('Prednisone 20mg qd')
		routeCode( 
				code:'PO',
						codeSystem:'2.16.840.1.113883.5.112',
						codeSystemName:'RouteOfAdministration'
						)//route code
		doseQuantity(value:'20',unit:'mg')
		consumable{
			manufacturedProduct{
				manufacturedLabeledDrug{
					code(
					code:'10312003',
							codeSystem:'2.16.840.1.113883.6.96' ,
							codeSystemName:'SNOMED CT',
							displayName:'Prednisone preparation')
				}
			}
		}//consumable
	}
}//entry

