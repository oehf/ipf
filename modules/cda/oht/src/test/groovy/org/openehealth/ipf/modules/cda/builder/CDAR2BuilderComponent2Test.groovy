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

import org.openhealthtools.ihe.common.cdar2.*
import groovytools.builder.NodeException
import org.junit.Test
import org.junit.Assert
/**
 * @author Christian Ohr
 */
public class CDAR2BuilderComponent2Test extends AbstractCDAR2BuilderTest {
	/**
	 * Test component nonXMLBody defaults
	 */
	@Test
	public void testNonXMLBodyDefaultValues() {
		def body = builder.build {
			nonXMLBody() { text() }//non xml body
		}
		
		assert body.getClassCode().name == 'DOCBODY'
		assert body.getMoodCode().name == 'EVN'
		assert body.isSetMoodCode() == false
		assert body.getText().mediaType == 'text/plain'
		assert body.getText().isSetMediaType() == false
		assert body.getText().getRepresentation().toString() == 'TXT'
		assert body.getText().getIntegrityCheckAlgorithm().getName() == 'SHA1'
		assert body.getText().getText() == null
	}
	
	/**
	 * Test component with nonXMLBody
	 */
	@Test
	public void testNonXMLBody() {
		def body = builder.build(getClass().getResource('/builders/content/NonXMLBodyExample1.groovy'))
		assert body.getClassCode().toString() == 'DOCBODY'
		assert body.getMoodCode().toString() == 'EVN'
		assert body.getText().getText() == 'this is the binary value as TXT/B64'
	}
	
	/**
	 * Test one of the choice (XOR check)
	 */
	@Test
	public void testChoiceValidation() {
		def componentValid = builder.build {
			component { nonXMLBody() }
		}
		assert componentValid.nonXMLBody
		assert componentValid instanceof POCDMT000040Component2
		shouldFail(NodeException) { 
			builder.build {
				component {
					nonXMLBody()
					structuredBody {
						component { section() }
					}
				}
			}
		}
		shouldFail(NodeException){
			builder.build{ component{ } }
		}
	}
	
	@Test
	public void testSectionNullFlavors() {
		def sectionSchema = builder.getSchema("section")
		def iSection = builder.build {
			section{
				code(code:'34133-9',
				codeSystem:'2.16.840.1.113883.6.1',
				codeSystemName:'LOINC',
				displayName:'Summarization of episode note',
				nullFlavor:'UNK'
				)   
				title('value TesTst is set')
				entry{
					act(classCode:'ACT', moodCode:'INT', negationInd:'false'){
						id(root:'don\'t remove')
						code(code:'remove it',nullFlavor:'NP')
					}
				}//entry
			}//section
		}
		assert iSection.entry[0].act.code.code == null
		assert iSection.entry[0].act.id[0].root == "don't remove"
	}
	
	/**
	 * Test the complete clinical document
	 */
	@Test
	public void testBuildClinicalDocument() {
		def document = builder.build(getClass().getResource('/builders/content/document/CDAWithActExample2.groovy'))
		Assert.assertTrue(document.component.nonXMLBody == null ^ document.component.structuredBody == null)
	}
	
	
}