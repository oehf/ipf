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

import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section
import org.junit.BeforeClass
import org.junit.Test

import org.openehealth.ipf.modules.cda.builder.content.AbstractContentBuilderTest

/**
 * @author Stefan Ivanov
 */
public class CCDAlertsBuilderTest extends AbstractContentBuilderTest {
	
	@BeforeClass
	static void initialize() throws Exception {
	    builder().define(getClass().getResource('/builders/content/section/CCDStatusObservation.groovy'))
	    builder().define(getClass().getResource('/builders/content/section/CCDReactionObservation.groovy'))
	    builder().define(getClass().getResource('/builders/content/section/CCDProblemAct.groovy'))
		builder().define(getClass().getResource('/builders/content/section/CCDAlertsBuilder.groovy'))
		def extensionAct = new CCDProblemActExtension(builder())
        extensionAct.extensions.call()
		def extension = new CCDAlertsExtension(builder())
		extension.extensions.call()
	}
	
	@Test
	public void testCCDAlerts() {
		POCDMT000040Section alerts = builder.build {
		    ccd_alerts{
                text('Patient Alerts')
                problemAct{
                    id(root:'d11275e9-67ae-11db-bd13-0800200c9a66')
                    alertObservation{
                        id(root:'9d3d416d-45ab-4da1-912f-4583e0632000')
                        code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                        effectiveTime('20000328')
                        alertStatus{
                            value(code:'55561003', 
                                    codeSystem:'2.16.840.1.113883.6.96', 
                                    displayName:'Active')
                        }
                        participantAgent{
                            playingEntity{
                                code(code:'70618', 
                                        codeSystem:'2.16.840.1.113883.6.88',  
                                        displayName:'Penicillin')
                            }
                        }//participant agent
                        reactionObservation{
                            code(code:'ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
                            value(
                                builder.build{
                                    cd(code:'247472004', 
                                            codeSystem:'2.16.840.1.113883.6.96', 
                                            displayName:'Hives')
                                }
                            )
                            severityObservation{
                                value(
                                        builder.build{
                                            cd(code:'247472004', 
                                                    codeSystem:'2.16.840.1.113883.6.96', 
                                                    displayName:'Hives')
                                        }
                                    )
                            }
                        }//reaction observation
                    }//alert observation
                }//problem act
            }//alerts section
		}
		new CCDAlertsValidator().validate(alerts, null)
	}
	
}
