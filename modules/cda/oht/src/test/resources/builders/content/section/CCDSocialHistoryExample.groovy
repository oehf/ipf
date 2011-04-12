package builders.content.section

ccd_socialHistory{
    text{
        table(border:'1', width:'100%'){
            thead{
                tr{
                    th('Social History Element')
                    th('Description')
                    th('Effective Dates')
                }
            }   
            tbody{
                tr{
                    td('Cigarette smoking')
                    td('1 pack per day')
                    td('1947 - 1972')
                }
                tr{
                    td('')
                    td('None')
                    td('1973 -')
                }                            
            }
        }//table
    }//text 
    socialHistoryObservation{
        id(root:'9b56c25d-9104-45ee-9fa4-e0f3afaa01c1')
        code(code:'230056004', codeSystem:'2.16.840.1.113883.6.96',  displayName:'Cigarette smoking')
        effectiveTime{
            low(value:'1947')
            high(value:'1972')
        }
        value(make{
            st('1 pack per day')
        }) 
    }
    socialHistoryObservation{
        id(root:'45efb604-7049-4a2e-ad33-d38556c9636c')
        code( code:'230056004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Cigarette smoking')
        effectiveTime{
            low(value:'1973')
        }
        value(make{
            st('None')
        })
        episodeObservation{
            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
            entryRelationship(typeCode:'SAS'){
                observation(classCode:'OBS', moodCode:'EVN'){
                    id(root:'9b56c25d-9104-45ee-9fa4-e0f3afaa01c1')
                    code(code:'230056004', 
                            codeSystem:'2.16.840.1.113883.6.96',
                            displayName:'Cigarette smoking')
                }
            }//target social history observation
        }
    }
}//social history section
