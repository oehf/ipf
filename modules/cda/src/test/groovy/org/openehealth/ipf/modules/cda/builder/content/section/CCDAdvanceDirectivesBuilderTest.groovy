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
public class CCDAdvanceDirectivesBuilderTest extends AbstractContentBuilderTest {
	
	@BeforeClass
	static void initialize() throws Exception {
		builder().define(getClass().getResource('/builders/content/section/CCDAdvanceDirectivesBuilder.groovy'))
		builder().define(getClass().getResource('/builders/content/section/CCDStatusObservation.groovy'))
		def extension = new CCDAdvanceDirectivesExtension(builder())
		extension.extensions.call()
	}
	
	@Test
	public void testCCDAdvanceDirectives() {
		def POCDMT000040Section advanceDirective = builder.build{
	        ccd_advanceDirectives{
				text('Simple Advance Directives')
				observation{
					id(root:'9b54c3c9-1673-49c7-aef9-b037ed72ed27')
					code(code:'304251008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Resuscitation')
					advanceDirectiveStatus{
					    value(code:'15240007',
					            codeSystem:'2.16.840.1.113883.6.96',
					            displayName:'Current and verified')
					}//observation status
					verifier{ 
					    time('19991107')
					    participantRole{
					        id(root:'20cf14fb-b65c-4c8c-a54d-b0cca834c18c')
					    }
					}//verifier
					reference{
						id(root:'b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3')
						code(code:'371538006',
								codeSystem:'2.16.840.1.113883.6.96',
								displayName:'Advance directive')
						text(mediaType:'application/pdf'){ 
						    reference(value:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf') 
						}
					}//reference to external document
				}//advance directive observation
			}//advance directive
		}
		new CCDAdvanceDirectivesValidator().validate(advanceDirective, null)
	}
	
	
	
}
