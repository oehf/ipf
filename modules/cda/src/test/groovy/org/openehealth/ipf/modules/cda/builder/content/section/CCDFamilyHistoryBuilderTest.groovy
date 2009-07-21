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
 * @author Christian Ohr
 */
public class CCDFamilyHistoryBuilderTest extends AbstractContentBuilderTest {
	
	@BeforeClass
	static void initialize() throws Exception {
		builder().define(getClass().getResource('/builders/content/section/CCDFamilyHistoryBuilder.groovy'))
		def extension = new CCDFamilyHistoryExtension(builder())
		extension.extensions.call()
	}
	
	@Test
	public void testCCDFamilyHistory() {
		def POCDMT000040Section history = builder().build {
			ccd_familyHistory {
				text('skipped') 
				familyMember {
					familyPerson {
						code(code:'9947008', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological father')
						subject {
							administrativeGenderCode('M')
							birthTime(value:'1912')
						}
					}
					causeOfDeath() {
						id('d42ebf70-5c89-11db-b0de-0800200c9a66')
						code('ASSERTION')
						// value(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI')                                       
						value(
								builder().build { 
									ce(code:'22298006',codeSystem:'2.16.840.1.113883.6.96',displayName:'MI') 
								}
								)
						cause {
							id('6898fae0-5c8a-11db-b0de-0800200c9a66')
							code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
							statusCode('completed')
							// value(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
							value(
									builder().build {
										ce(code:'419099009',codeSystem:'2.16.840.1.113883.6.96',displayName:'Dead')
									}
									)
						}
						age {
							value(
							builder().build { _int(57) }                                    
							)
						}
					}
					observation {
						id('5bfe3ec0-5c8b-11db-b0de-0800200c9a66')
						code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
						value(
								builder.build {
									ce(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')
								}
								)
						// value(code:'59621000',codeSystem:'2.16.840.1.113883.6.96',displayName:'HTN')                                       
						age {
							value(
							builder().build { _int(40) }
							)
						}
					}
				}                           
				familyMember {
					familyPerson {
						code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biological mother')
						subject {
							administrativeGenderCode('F')
							birthTime(value:'1912')
						}
					}                               
					observation {
						id('a13c6160-5c8b-11db-b0de-0800200c9a66')
						code('ASSERTION', codeSystem:'2.16.840.1.113883.5.4')
						value(
								builder().build {
									ce(code:'195967001',codeSystem:'2.16.840.1.113883.6.96',displayName:'Asthma')
								}
								)
						age {
							value(
							builder().build { _int(30) }
							)
						}
					}
				}                           
			}
		}
		new CCDFamilyHistoryValidator().validate(history, null)
	}
	
	
	
}
