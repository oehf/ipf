package builders.content.section

ccd_encounters {
    text('Encounter Location: Very Good Health Clinic')
    title('Encounters')
    encounterActivity{
        id(root:'2a620155-9d11-439e-92b3-5d9815ff4de8')
        code(code:'GENRL', codeSystem:'2.16.840.1.113883.5.4', displayName:'General'){
            originalText('Checkup Examination')
        }//code
        encounterLocation{
            id(root:'2.16.840.1.113883.19.5')
            playingEntity{
                name('Very Good Health Clinic')
            }//playingEntity 
        }
        age {
            value(make { _int(57) }
            )
        }
    }//encounter		
}

