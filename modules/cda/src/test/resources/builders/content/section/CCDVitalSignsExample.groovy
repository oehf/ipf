package builders.content.section

ccd_vitalSigns{
    text('Patient Vital Signs')
    vitalSignsOrganizer(classCode:'CLUSTER'){
        id(root:'c6f88320-67ad-11db-bd13-0800200c9a66')
        code(code:'46680005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Vital signs')
        statusCode(code:'completed')
        resultObservation{
            id(root:'c6f88321-67ad-11db-bd13-0800200c9a66')
            code(code:'50373000', codeSystem:'2.16.840.1.113883.6.96', displayName:'Body height')
            statusCode(code:'completed')
            value( make {
                pq(value:177.0, unit:'cm')
            })
        }//result observation
        resultObservation{
            id(root:'c6f88322-67ad-11db-bd13-0800200c9a66')
            code(code:'27113001', codeSystem:'2.16.840.1.113883.6.96', displayName:'Body weight')
            statusCode(code:'completed')
            effectiveTime(value:'19991114')
            value( make {
                pq('86 kg')
            })
        }//result observation
        resultObservation{
            id(root:'c6f88323-67ad-11db-bd13-0800200c9a66')
            code(code:'271649006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Systolic BP')
            statusCode(code:'completed')
            effectiveTime(value:'19991114')
            value( make{
                pq(value:132.0, unit:'mm[Hg]')
            })
        }
        resultObservation{
            id(root:'c6f88324-67ad-11db-bd13-0800200c9a66')
            code(code:'271650006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Diastolic BP')
            statusCode(code:'completed')
            effectiveTime(value:'19991114')
            value( make{
                pq(value:86.0, unit:'mm[Hg]')
            })
        }
    }//vital signs organizer
}//vital signs section
