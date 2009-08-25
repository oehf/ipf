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
public class CDAR2BuilderStructureObservationTest extends AbstractCDAR2BuilderTest {
	
	
	/**
	 * Test observation defaults
	 */
	@Test
	public void testObservationDefaultValues() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/ObservationExample1.groovy'))		
		Assert.assertTrue entry.observation.isSetMoodCode()
		Assert.assertEquals 'INT', entry.observation.moodCode.name 
		Assert.assertEquals 'OBS', entry.observation.classCode.name
		Assert.assertFalse entry.observation.isSetNegationInd()
		Assert.assertEquals 0, entry.observation.entryRelationship.size
		Assert.assertNull entry.observation.text
	}
	
	/**
	 * Test observation
	 */
	@Test
	public void testComplexObservation() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/ObservationExample2.groovy'))		
		Assert.assertEquals 'CNOD', entry.observation.classCode.name
		Assert.assertEquals 2, entry.observation.value.size
		Assert.assertEquals 1, entry.observation.targetSiteCode.size
	}
	
	/**
	 *
	 */
	@Test	
	public void testObservationReference() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/ObservationExample3.groovy'))		
		Assert.assertEquals 'SPRT', entry.observation.reference.typeCode[1].name
	}
	
	
}