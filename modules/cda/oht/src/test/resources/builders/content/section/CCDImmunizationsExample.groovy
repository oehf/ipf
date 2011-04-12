package builders.content.section

ccd_immunizations{
    text{
        table(border:'1', width:'100%'){
           thead{
               tr{
                   th('Vaccine')
                   th('Date')
                   th('Status')
               }
           }
           tbody{
               tr{
                   td('Influenza virus vaccine, IM')
                   td('Nov 1999')
                   td('Completed')
               }
               tr{
                   td('Influenza virus vaccine, IM')
                   td('Dec 1998')
                   td('Completed')
               }
               tr{
                   td('Pneumococcal polysaccharide vaccine, IM')
                   td('Dec 1998')
                   td('Completed')
               }
               tr{
                   td('Tetanus and diphtheria toxoids, IM')
                   td('1997')
                   td('Completed')
               }
           }
        }
    }//text
    medicationActivity{
        id(root:'e6f1ba43-c0ed-4b9b-9f12-f435d8ad8f92')
        effectiveTime(make{
            ivlts{
                center(value:'199911')
            }
        })
        routeCode(code:'IM', displayName:"Intramuscular injection")
        consumable {
            manufacturedProduct {
                manufacturedMaterial {
                    code(code:'88',
                            codeSystem:'2.16.840.1.113883.6.59',
                            displayName:'Influenza virus vaccine') { 
                        originalText('Influenza virus vaccine') 
                    }
                }
            }//manufactured product
        }
    }//medication activity
    medicationActivity{
        id(root:'115f0f70-1343-4938-b62f-631de9749a0a')
        effectiveTime(make{
            ivlts{
                center(value:'199812')
            }
        })
        routeCode(code:'IM', displayName:"Intramuscular injection")
        consumable {
            manufacturedProduct {
                manufacturedMaterial {
                    code(code:'88',
                            codeSystem:'2.16.840.1.113883.6.59',
                            displayName:'Influenza virus vaccine') { 
                        originalText('Influenza virus vaccine') 
                    }
                }
            }//manufactured product
        }
    }//medication activity
    medicationActivity{
        id(root:'78598407-9f16-42d5-8ffd-09281a60fe33')
        effectiveTime(make{
            ivlts{
                center(value:'199812')
            }
        })
        routeCode(code:'IM', displayName:"Intramuscular injection")
        consumable {
            manufacturedProduct {
                manufacturedMaterial {
                    code(code:'33',
                            codeSystem:'2.16.840.1.113883.6.59',
                            displayName:'Pneumococcal polysaccharide vaccine') { 
                        originalText('Pneumococcal polysaccharide vaccine') 
                    }
                }
            }//manufactured product
        }
    }//medication activity
    medicationActivity{
        id(root:'261e94a0-95fb-4975-b5a5-c8e12c01c1bc')
        effectiveTime(make{
            ivlts{
                center(value:'1997')
            }
        })
        routeCode(code:'IM', displayName:"Intramuscular injection")
        consumable {
            manufacturedProduct {
                manufacturedMaterial {
                    code(code:'09',
                            codeSystem:'2.16.840.1.113883.6.59',
                            displayName:'Tetanus and diphtheria toxoids') { 
                        originalText('Tetanus and diphtheria toxoids') 
                    }
                }
            }//manufactured product
        }
    }//medication activity                                  
}//immunizations section
