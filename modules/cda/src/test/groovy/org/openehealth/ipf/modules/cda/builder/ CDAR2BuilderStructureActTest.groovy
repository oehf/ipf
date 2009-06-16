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
package org.openehealth.ipf.modules.cda.builder

import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*

/**
 * @author Stefan Ivanov
 */
public class CDAR2BuilderStructureActTest extends AbstractCDAR2BuilderTest {
	
	/**
	 * Test simple act
	 */
	public void testAct() {
		def entry = builder.build {
			entry{
				act(classCode:'ACT', moodCode:'INT', negationInd:'false'){
					id()
					code(
							code:'23426006',
									codeSystem:'2.16.840.1.113883.6.96',
									codeSystemName:'SNOMED CT',
									displayName:'Pulmonary function test'
									)//code
					text('Complete PFTs with lung volumes.')
					subject{ relatedSubject() }//subject
					entryRelationship(typeCode:'COMP'){
						act(classCode:'ACT', moodCode:'INT'){
							code(
							code:'252472004',
									codeSystem:'2.16.840.1.113883.6.96',
									codeSystemName:'SNOMED CT',
									displayName:'Lung volume test'
									)//code    
						}
					}//entry relationship
					entryRelationship(typeCode:'COMP'){
						act(classCode:'ACT', moodCode:'INT'){
							code(
							code:'252472005',
									codeSystem:'2.16.840.1.113883.6.96',
									codeSystemName:'SNOMED CT',
									displayName:'Lung volume test additional'
									)//code    
						}
					}//entry relationship
				}//act
			}//entry
		}
		assertTrue(entry.act.isSetMoodCode())
		assert entry.act.moodCode.name == 'INT'
		assert entry.act.classCode.name == 'ACT'
		assertEquals 2,entry.act.entryRelationship.size
	}
	
	/**
	 * Test act defaults
	 */
	public void testActDefaultValues() {
		def entry = builder.build {
			entry{
				act(){
					code(
					code:'23426006',
							codeSystem:'2.16.840.1.113883.6.96'
							)//code
				}//act
			}//entry
		}
		
		assert entry.act.moodCode.name == 'INT'
		assert entry.act.classCode.name == 'ACT'
		assertEquals 0, entry.act.entryRelationship.size
		assertNull entry.act.text	
	}
	
	public void testCodeQualifier(){
		def entry = builder.build{
			entry{
				act(){
					code(code:'223468009',
					codeSystem:'2.16.840.1.113883.6.96',
					codeSystemName:'SNOMED CT',
					displayName:'Teaching of skills'
					) {  
						qualifier{
							name(code:'363702006',
							codeSystem:'2.16.840.1.113883.6.96',
							codeSystemName:'SNOMED CT',
							displayName:'has focus')
							value(code:'29893006',
									codeSystem:'2.16.840.1.113883.6.96',
									codeSystemName:'SNOMED CT',
									displayName:'Peak flow rate measurement')
						}//code qualifier
					}//code
				}//act
			}//entry
		}
		assertEquals 1, entry.act.code.qualifier.size
	}
	
	/**
	 * Test build complete document
	 */
	public void testBuildClinicalDocument() {
		def document = builder.build {
			clinicalDocument {
				typeId(root:'2.16.840.1.113883.1.3', extension:'POCD_HD000040')
				templateId(root:'2.16.840.1.113883.3.27.1776')
				id(root: '2.16.840.1.113883.19.4', extension: 'c266')
				code(
						code: '11488-4',
								codeSystem: '2.16.840.1.113883.6.1',
								codeSystemName: 'LOINC',
								displayName: 'Consultation note'
								)
				title('Good Health Clinic Consultation Note')
				effectiveTime('20000407')
				versionNumber(2)
				confidentialityCode(
						code: 'N',
								codeSystem: '2.16.840.1.113883.5.25')
				languageCode(code: 'en_US')
				setId(extension: "BB35", root: "2.16.840.1.113883.19.7")
				recordTarget {
					patientRole {
						id(extension: '12345', root: '2.16.840.1.113883.19.5')
					}
				}
				author()
				/* component with structured content*/
				component {
					structuredBody {
						component {
							section() {
								code(
								code: '10164-2',
										codeSystem: '2.16.840.1.113883.6.1',
										codeSystemName: 'LOINC'
										)
								title('History of Illness')
								text('a narrative content.')
								entry {
									act {
										// (classCode: 'ACT', moodCode: 'INT')
										id()
										code(
												code: '23426006',
														codeSystem: '2.16.840.1.113883.6.96',
														codeSystemName: 'SNOMED CT',
														displayName: 'Pulmonary function test'
														)//code
									}//act
								}//entry one
								entry{
									act(classCode:'ACT', moodCode:'INT'){
										id()
										code(
												code:'223468009',
														codeSystem:'2.16.840.1.113883.6.96',
														codeSystemName:'SNOMED CT',
														displayName:'Teaching of skills'
														) {
													qualifier{
														name(
														code:'363702006',
																codeSystem:'2.16.840.1.113883.6.96',
																codeSystemName:'SNOMED CT',
																displayName:'has focus'
																)
														value(
																code:'29893006',
																		codeSystem:'2.16.840.1.113883.6.96',
																		codeSystemName:'SNOMED CT',
																		displayName:'Peak flow rate measurement'
																		)
													}//code qualifier
												}//code
									}//act
								}//entry two
							}//section
						}//component
					}//structured body
				}
			}
		}
		
		def renderer = new CDAR2Renderer()
		def opts = [:]
		opts[XMLResource.OPTION_DECLARE_XML] = true
		opts[XMLResource.OPTION_ENCODING] = 'utf-8'
		System.out.println(renderer.render(document, opts))
	}
	
	
	
}