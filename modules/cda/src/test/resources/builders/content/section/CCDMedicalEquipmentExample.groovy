package builders.content.section

ccd_medicalEquipment{
    text{
        table(border:'1', width:'100%'){
           thead{
               tr{
                   th('Supply/Device')
                   th('Date Supplied')
               }
           }
           tbody{
               tr{
                   td('Automatic implantable cardioverter/defibrillator')
                   td('Nov 1999')
               }
               tr{
                   td('Total hip replacement prosthesis')
                   td('1998')
               }
               tr{
                   td('Wheelchair')
                   td('1999')
               }
           }
        }
    }//text
    supplyActivity{
        id(root:'2413773c-2372-4299-bbe6-5b0f60664446')
        effectiveTime(make{
            ivlts{
                center(value:'199911')
            }
        })
        productInstance{
            playingDevice{
                code(code:'72506001',
                        codeSystem:'2.16.840.1.113883.6.96',
                        displayName:'Automatic implantable cardioverter/defibrillator')
            }
        }
    }//suplly activity
    supplyActivity{
        id(root:'230b0ab7-206d-42d8-a947-ab4f63aad795')
        effectiveTime(make{
            ivlts{
                center(value:'1998')
            }
        })
        productInstance{
            id(root:'03ca01b0-7be1-11db-9fe1-0800200c9a66')
            playingDevice{
                code(code:'304120007',
                        codeSystem:'2.16.840.1.113883.6.96',
                        displayName:'Total hip replacement prosthesis')
            }
            scopingEntity{
                id(root:'0abea950-5b40-4b7e-b8d9-2a5ea3ac5500')
                desc('Good Health Prostheses Company')
            }
        }
    }//suplly activity
    supplyActivity{
        id(root:'c4ffe98e-3cd3-4c54-b5bd-08ecb80379e0')
        effectiveTime(make{
            ivlts{
                center(value:'1999')
            }
        })
        productInstance{
            playingDevice{
                code(code:'58938008',
                        codeSystem:'2.16.840.1.113883.6.96',
                        displayName:'Wheelchair')
            }//playingDevice
        }
        fulfillmentInstruction{
            id('cdbd5b08-6cde-11db-9fe1-0800200b8a66')
            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
            effectiveTime { low('20000328') }
            text('Instruction in german')
        }
    }//suplly activity
}