package builders.content.section

ccd_procedures{
    text('Patient Procedures')
    templateId(root:'2.16.840.1.113883.10.20.1.12')
    templateId(root:'1.3.6.1.4.1.19376.1.5.3.1.3.11',
            assigningAuthorityName:'IHE PCC')
    text{
        table(border:'1', width:'100%'){
            thead{
                tr{
                    th('Procedure')
                    th('Date')
                }
            }//thead
            tbody{
                tr{
                    td{
                        content(ID:'Proc2', 'Total hip replacement, left')
                    }//td
                    td{content('1998')}
                }//tr
            }//tbody
        }//table
    }//text
    procedureActivity{
        procedure{
            id(root:'e401f340-7be2-11db-9fe1-0800200c9a66')
            code(code:'52734007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Total hip replacement'){
                originalText{ reference(value:'#Proc2') }//originalText
                qualifier{
                    name(code:'272741003', displayName:'Laterality')
                    value(code:'7771000', displayName:'Left')
                }//qualifier
            }//code
            text('IHE Requires reference to go here instead of originalText of code.<reference')
            statusCode('completed')
            effectiveTime('1998')
            performer{
                assignedEntity{
                    assignedPerson{ name('Procedure Performers Name') }//assignedPerson
                }//assignedEntity
            }//performer
            age {
                value(make { _int(57) }
                )
            }
            encounterLocation{
                id(root:'2.16.840.1.113883.19.5')
                playingEntity{ name('Very Good Health Clinic') }//playingEntity
            }
            productInstance{
                id(root:'03ca01b0-7be1-11db-9fe1-0800200c9a66')
                playingDevice{
                    code(code:'304120007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Total hip replacement prosthesis')
                }//playingDevice
                scopingEntity{
                    id(root:'0abea950-5b40-4b7e-b8d9-2a5ea3ac5500')
                    desc('Good Health Prostheses Company')
                }//scopingEntity
            }
            entryRelationship(typeCode:'REFR'){
                act(classCode:'ACT', moodCode:'EVN'){
                    templateId(root:'1.3.6.1.4.1.19376.1.5.3.1.4.4', assigningAuthorityName:'IHE PCC')
                    code(nullFlavor:'NA')
                    text{ reference(value:'PtrToSectionText') }//text
                    reference(typeCode:'REFR'){
                        externalDocument(classCode:'DOC', moodCode:'EVN'){ text('Location of Documentation -  URL or other') }//externalDocument
                    }//reference
                }//act
            }//entryRelationship
            informationSource{
                value(make{
                    st('Unknown')
                })
            }
        }//procedure activity procedure
    }
}//procedures section
