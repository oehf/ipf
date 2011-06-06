package org.openehealth.ipf.modules.ccd.builder

import org.junit.Assert
import org.junit.Test
import org.openehealth.ipf.modules.cda.builder.content.document.AbstractCCDBuilderTest

class CCDCompleteTest extends AbstractCCDBuilderTest {
    
    @Test
    public void testEncouterSection() {
        def document = builder.build {
            continuityOfCareDocument {
                id('db734647-fc99-424c-a864-7e3cda82e703')
                title('Good Health Clinic Continuity of Care Document')
                effectiveTime('20000407130000+0500')
                confidentialityCode('N')
                languageCode('en-US')
                recordTarget {
                    patientRole {
                        id('996-756-495@2.16.840.1.113883.19.5')
                        patient {
                            name {
                                given('Henry')
                                family('Levin')
                                suffix('the 7th')
                            }
                            administrativeGenderCode('M')
                            birthTime('19320924')
                        }
                        providerOrganization {
                            id('2.16.840.1.113883.19.5')
                            name('Good Health Clinic')
                        }
                    }//patient role
                }//record target
                author {
                    time('20000407130000+0500')
                    assignedAuthor {
                        id('20cf14fb-b65c-4c8c-a54d-b0cca834c18c')
                        assignedPerson {
                            name {
                                prefix('Dr.')
                                given('Robert')
                                family('Dolin')
                            }
                        }
                        representedOrganization {
                            id(root:"2.16.840.1.113883.19.5")
                            name('Good Health Clinic')
                        }
                    }
                }//author
                // mainActivity (documentationOf)
                mainActivity{
                    effectiveTime{
                        low(value:'19320924')
                        high(value:'20000407')
                    }
                }//main activity
                component{
                    structuredBody{
                        encounters {
                            text('Encounter Location: Very Good Health Clinic')
                            title('Encounters')
                            encounterActivity{
                                id(root:'2a620155-9d11-439e-92b3-5d9815ff4de8')
                                code(code:'GENRL', codeSystem:'2.16.840.1.113883.5.4', displayName:'General'){  originalText('Checkup Examination')  }//code
                                encounterLocation{
                                    id(root:'2.16.840.1.113883.19.5')
                                    playingEntity{ name('Very Good Health Clinic') }//playingEntity
                                }
                            }//encounterActivity
                        }//encounter
                        
                        problems{
                            text('Patient Problems Acts')
                            problemAct{
                                id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
                                problemObservation{
                                    id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
                                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                                    value(make{
                                        cd(code:'233604007',
                                                codeSystem:'2.16.840.1.113883.6.96',
                                                displayName:'Pneumonia')
                                    }
                                    )
                                    problemStatus{
                                        value(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
                                    }
                                    problemHealthstatus{
                                        value(code:'81323004', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
                                    }
                                }//problem observation
                                episodeObservation{
                                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                                    value(make {
                                        cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding')
                                    })
                                    entryRelationship(typeCode:'SAS'){
                                        act(classCode:'ACT', moodCode:'EVN'){
                                            id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
                                            code(nullFlavor:'NA')
                                        }//act
                                    }//entryRelationship
                                }//episode observation
                            }//problem act
                            
                            problemAct{
                                id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
                                problemObservation{
                                    id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
                                    code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                                    value(make{
                                        cd(code:'233604007',
                                                codeSystem:'2.16.840.1.113883.6.96',
                                                displayName:'Pneumonia')
                                    }
                                    )
                                }//problem observation
                            }//problem act
                        }//problems section
                    }//structuredBody
                }//component
            }//ccd doc
        }
        Assert.assertNotNull document
        showDocument(document)
    }
}
