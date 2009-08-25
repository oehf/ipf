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
import org.junit.Test
import org.junit.Assert
/**
 * @author Stefan Ivanov
 */
public class CDAR2BuilderStructureActTest extends AbstractCDAR2BuilderTest {
	
	/**
	 * Test simple act
	 */
	@Test
	public void testAct() {
	    def entry = builder.build(getClass().getResource('/builders/content/entry/ActExample1.groovy'))
		Assert.assertTrue(entry.act.isSetMoodCode())
		Assert.assertEquals 'INT', entry.act.moodCode.name
		Assert.assertEquals 'ACT', entry.act.classCode.name
		Assert.assertEquals 2, entry.act.entryRelationship.size
	}
	
	/**
	 * Test act defaults
	 */
	@Test
	public void testActDefaultValues() {
	    def entry = builder.build(getClass().getResource('/builders/content/entry/ActExample2.groovy'))		
		Assert.assertEquals 'INT', entry.act.moodCode.name
		Assert.assertEquals 'ACT', entry.act.classCode.name
		Assert.assertEquals 0, entry.act.entryRelationship.size
		Assert.assertNull entry.act.text	
	}
	
	@Test
	public void testCodeQualifier(){
	    def entry = builder.build(getClass().getResource('/builders/content/entry/ActExample3.groovy'))		
		Assert.assertEquals 1, entry.act.code.qualifier.size
	}
	
	/**
	 * Test build complete document
	 */
	@Test
	public void testBuildClinicalDocument() {
		def document = builder.build(getClass().getResource('/builders/content/document/CDAWithActExample1.groovy'))
		Assert.assertNotNull(document)
		
		// def renderer = new CDAR2Renderer()
		// def opts = [:]
		// opts[XMLResource.OPTION_DECLARE_XML] = true
		// opts[XMLResource.OPTION_ENCODING] = 'utf-8'
		// println(renderer.render(document, opts))
	}
	
	
	
}
