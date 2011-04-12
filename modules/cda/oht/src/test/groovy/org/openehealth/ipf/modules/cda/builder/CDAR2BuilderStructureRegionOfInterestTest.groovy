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
public class CDAR2BuilderStructureRegionOfInterestTest extends AbstractCDAR2BuilderTest {
	
	@Test
	public void testRegionOfInterestSchemata(){
		def schemaInTest = builder.getSchema('regionOfInterest')
		Assert.assertEquals 'POCDMT000040_REGION_OF_INTEREST', (String)schemaInTest.attribute('factory')
	}
	
	/**
	 * Test regionOfInterest minimal defaults
	 */
	@Test
	public void testRegionOfInteresttDefaultValues() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/RegionOfInterestExample1.groovy'))		
		Assert.assertEquals 'EVN', entry.regionOfInterest.moodCode.name
		Assert.assertEquals true, entry.regionOfInterest.isSetMoodCode()
		Assert.assertTrue entry.regionOfInterest.id instanceof List
		Assert.assertEquals 1, entry.regionOfInterest.id.size
		Assert.assertEquals "uuid", entry.regionOfInterest.id[0].root
		Assert.assertTrue entry.regionOfInterest.code instanceof CS1
		Assert.assertNotNull entry.regionOfInterest.code.code
		Assert.assertEquals 1, entry.regionOfInterest.value.size
		Assert.assertFalse entry.regionOfInterest.value.data[0].unsorted
		Assert.assertTrue entry.regionOfInterest.value.data[0] instanceof POCDMT000040RegionOfInterestValue
		Assert.assertNull entry.regionOfInterest.value.data[0].value
		
	}
	
	/**
	 * Test simple RegionOfInterest
	 */
	@Test
	public void testSimpleRegionOfInterest() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/RegionOfInterestExample2.groovy'))		
		Assert.assertEquals 'MM1', entry.regionOfInterest.getID()
		Assert.assertEquals 'ELLIPSE', entry.regionOfInterest.code.code
		Assert.assertEquals 1, entry.regionOfInterest.value.data[0].value.intValue()
		Assert.assertEquals 2, entry.regionOfInterest.value.data[1].value.intValue()
		Assert.assertEquals 3, entry.regionOfInterest.value.data[2].value.intValue()
	}
	
	/**
	 *
	 */
	@Test
	public void testComplexRegionOfInterest() {
		def entry = builder.build(getClass().getResource('/builders/content/entry/RegionOfInterestExample3.groovy'))		
		Assert.assertNotNull entry.regionOfInterest
	}
	
	/**
	 * Test regionOfInterest constraints
	 */
	/*
	 public void testRegionOfInterestConstraints() {
	 shouldFail{//mising id
	 builder.build{
	 regionOfInterest(classCode:'ROIOVL', moodCode:'EVN'){
	 code()
	 value(value:'3')
	 }
	 }
	 }
	 shouldFail{//mising code
	 builder.build{
	 regionOfInterest(classCode:'ROIOVL', moodCode:'EVN'){
	 id()
	 value(value:'3')
	 }
	 }
	 }
	 shouldFail{//mising value
	 builder.build{
	 regionOfInterest(classCode:'ROIOVL', moodCode:'EVN'){
	 id()
	 code()
	 }
	 }
	 }
	 }
	 */
}