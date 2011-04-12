package builders.content.entry


	entry{
		substanceAdministration(classCode:'PROC', moodCode:'EVN'){
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
		}//substance administration
	}//entry

