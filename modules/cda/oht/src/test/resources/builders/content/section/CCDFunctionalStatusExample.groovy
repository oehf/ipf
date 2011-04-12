package builders.content.section

ccd_functionalStatus{
    text{
        table(border:'1', width:'100%'){
            thead{
                tr{
                    th('Functional Condition')
                    th('Effective Dates')
                    th('Condition Status')
                }
            }
            tbody{
                tr{
                    td('Dependence on cane')
                    td('1998')
                    td('Active')
                }
                tr{
                    td('Memory impairment')
                    td('1999')
                    td('Active')
                }
            }
        }//table>
    }
    problemAct{
        id(root:'6z2fa88d-4174-4909-aece-db44b60a3abb') 
        code(nullFlavor:'NA')
        problemObservation{
            id(root:'fd07111a-b15b-4dce-8518-1274d07f142a')
            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4') 
            effectiveTime{low(value:'1998')}
            value( make{
                    cd(code:'105504002', 
                            codeSystem:'2.16.840.1.113883.6.96', 
                            displayName:'Dependence on cane')
                }
            )
            functionalStatusStatus{
                value(code:'55561003',
                        codeSystem:'2.16.840.1.113883.6.96',
                        displayName:'Active') 
            }
        }//problem observation
    }//problem act
    problemAct{
       id(root:'64606e86-c080-11db-8314-0800200c9a66')
       problemObservation{
           id(root:'64606e86-c080-11db-8314-0800200c9a66')
            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
            value( make{
                    cd(code:'386807006',
                            codeSystem:'2.16.840.1.113883.6.96',
                            displayName:'Memory impairment')
                }
            )
            functionalStatusStatus{
                value(code:'55561003',
                        codeSystem:'2.16.840.1.113883.6.96',
                        displayName:'Active')
            }
       }//problem observation
    }//problem act
}//functional status section