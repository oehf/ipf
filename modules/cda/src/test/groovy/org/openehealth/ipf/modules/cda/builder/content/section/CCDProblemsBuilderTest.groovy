/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.modules.cda.builder.content.section

import org.openhealthtools.ihe.common.cdar2.POCDMT000040Sectionimport org.junit.BeforeClassimport org.junit.Test
import org.openehealth.ipf.modules.cda.builder.content.AbstractContentBuilderTest

/**
 * @author Stefan Ivanov
 */
public class CCDProblemsBuilderTest extends AbstractContentBuilderTest {
	
	@BeforeClass
	static void initialize() throws Exception {
		builder().define(getClass().getResource('/builders/content/section/CCDStatusObservation.groovy'))
		builder().define(getClass().getResource('/builders/content/section/CCDProblemsBuilder.groovy'))
		def extension = new CCDProblemsExtension(builder())
		extension.extensions.call()
	}
	
	@Test
	public void testCCDProblems() {
		def POCDMT000040Section problems = builder.build {
		    ccd_problems{
                text('Patient Problems Acts')
                problemAct{
                    id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
                    problemObservation{
                        id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
                        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                        value(
                                builder.build{
                                    cd(code:'233604007',
                                            codeSystem:'2.16.840.1.113883.6.96',
                                            displayName:'Pneumonia')
                                }
                        ) 
                        problemStatus{
                            value(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
                        }
                        problemHealthstatus{
                            value(code:'413322009', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resolved')
                        }
                    }//problem observation
                    episodeObservation{
                        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                        value(
                            builder.build{
                                cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding')
                            }
                        )
                        entryRelationship(typeCode:'SAS'){
                            act(classCode:'ACT', moodCode:'EVN'){
                                id(root:'ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7')
                                code(nullFlavor:'NA')
                            }//act
                        }//entryRelationship
                    }//episode observation
                    patientAwareness{
                        awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
                        participantRole{
                            id(root:'c8a6ff8-ed4b-4f7e-82c3-e98e58b45de8')
                        }
                    }
                }//problem act
                problemAct{
                    id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
                    problemObservation{
                        id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
                        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                        value(
                            builder.build{
                                cd(code:'233604007',
                                    codeSystem:'2.16.840.1.113883.6.96',
                                    displayName:'Pneumonia')
                            }
                        )
                        patientAwareness{
                            awarenessCode(code:'TEST', codeSystem:'2.16.840.1.113883.5.4')
                            participantRole{
                                id(root:'c8a6ff8-ed4b-4f7e-82c3-e98e58b45de8')
                            }
                        }
                    }//problem observation
                }//problem act
            }//problems section
		}
		new CCDProblemsValidator().validate(problems, null)
		assert problems.problemAct.size == 2
        assert problems.problemAct[0].problemObservation.size == 1
        assert problems.problemAct[0].problemObservation[0].problemHealthstatus.size == 1
        assert problems.problemAct[0].patientAwareness != null
        assert problems.problemAct[1].problemObservation[0].patientAwareness != null
	}
	
}
