package builders.content.section

ccd_results{
    text('Patient Observation Results')
    title('Other results')
    resultOrganizer(classCode:'BATTERY'){
        id(root:'7d5a02b0-67a4-11db-bd13-0800200c9a66')
        code(code:'43789009',
                codeSystem:'2.16.840.1.113883.6.96',
                displayName:'CBC WO DIFFERENTIAL')
        statusCode(code:'completed')
        effectiveTime(value:'200003231430')
        resultObservation{
            id(root:'107c2dc0-67a5-11db-bd13-0800200c9a66')
            code(code:'30313-1', codeSystem:'2.16.840.1.113883.6.1', displayName:'HGB')
            statusCode(code:'completed')
            effectiveTime(value:'200003231430')
            value(make {
                pq(value:13.2, unit:'g/dl')
            })
            interpretationCode(code:'N', codeSystem:'2.16.840.1.113883.5.83')
            referenceRange{
                observationRange{
                    text('M 13-18 g/dl; F 12-16 g/dl')
                }//observationRange
            }//referenceRange
        }//observation
        resultObservation{
            id(root:'8b3fa370-67a5-11db-bd13-0800200c9a66')
            code(code:'33765-9', codeSystem:'2.16.840.1.113883.6.1', displayName:'WBC')
            statusCode(code:'completed')
            value(make{
                pq(value:6.7, unit:'10+3/ul')
            })
            interpretationCode(code:'N', codeSystem:'2.16.840.1.113883.5.83')
            referenceRange{
                observationRange{
                    value( make {
                        ivlpq{
                            low(value:4.3, unit:'10+3/ul')
                            high(value:10.8, unit:'10+3/ul')
                        }
                    })//value
                }//observationRange
            }//referenceRange
        }//observation
    }//result organizer
}//results section

