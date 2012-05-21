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

import org.junit.Test
import org.junit.Assert
import org.openhealthtools.ihe.common.cdar2.*

/**
 * @author Christian Ohr
 */
public class CDAR2NarrativeTest extends AbstractCDAR2BuilderTest {
	
	@Test
	public void testListWithItems() {
		StrucDocText text = builder.build(getClass().getResource('/builders/content/NarrativeExample1.groovy'))
		Assert.assertNotNull text
		Assert.assertEquals 'Theodur 200mg BID', text.mixed[0].value.item[0].text
		Assert.assertEquals 'Proventil inhaler 2puffs QID PRN', text.mixed[0].value.item[1].text 
	}
	
	@Test
	public void testListWithContentItems() {
		StrucDocText text = builder.build(getClass().getResource('/builders/content/NarrativeExample2.groovy'))
		
		Assert.assertNotNull text
		Assert.assertEquals 'a1', text.mixed[0].value.item[0].mixed[0].value.iD
		Assert.assertEquals 'Asthma', text.mixed[0].value.item[0].mixed[0].value.text 
		Assert.assertEquals 'a2', text.mixed[0].value.item[1].mixed[0].value.iD 
		Assert.assertEquals 'Hypertension (see HTN.cda for details)', text.mixed[0].value.item[1].mixed[0].value.text 
	}
	
	/**
	 * Test Table1
	 */
	@Test
	public void testTable1() {
		StrucDocText text = builder.build(getClass().getResource('/builders/content/NarrativeExample3.groovy'))
		Assert.assertNotNull text
		Assert.assertEquals 'Date / Time', text.mixed[0].value.tbody[0].group[0].value.group[0].value.text
	}
	
	/**
	 * Test Table2
	 */
	@Test
	public void testTable2() {
		StrucDocText text = builder.build(getClass().getResource('/builders/content/NarrativeExample4.groovy'))
		Assert.assertNotNull text
		Assert.assertEquals 'Payer information', text.mixed[0].value.text 
		Assert.assertEquals '100%', text.mixed[1].value.width
		Assert.assertEquals 'Payer name', text.mixed[1].value.thead.group[0].value.group[0].value.text
		Assert.assertEquals 'Colonoscopy.pdf', text.mixed[1].value.tbody[0].group[0].value.group[3].value.mixed[0].value.href
		Assert.assertEquals 'Colonoscopy', text.mixed[1].value.tbody[0].group[0].value.group[3].value.mixed[0].value.text
	}
	
	/**
	 *    Test RenderMultimeida with single string (xs:IDREFS)
	 */
	@Test
	public void testRenderMultimediaSingleReference() {
		StrucDocText text = builder.build(getClass().getResource('/builders/content/NarrativeExample5.groovy'))
		Assert.assertNotNull text
		Assert.assertEquals 'Erythematous rash, palmar surface, left index finger.', text.text
		Assert.assertEquals 'MMS', text.mixed[1].value.referencedObject[0]
	}
	
	/**
	 *    Test RenderMultimeida with array list (xs:IDREFS)
	 */
	@Test
	public void testRenderMultimediaMultiReference() {
		StrucDocText text = builder.build(getClass().getResource('/builders/content/NarrativeExample6.groovy'))
		Assert.assertNotNull text
		Assert.assertEquals 'Erythematous rash, palmar surface, left index finger.', text.text
		Assert.assertEquals 2, text.mixed[1].value.referencedObject.size()
		Assert.assertEquals 'MM1', text.mixed[1].value.referencedObject[0]
	}
	
	
}
