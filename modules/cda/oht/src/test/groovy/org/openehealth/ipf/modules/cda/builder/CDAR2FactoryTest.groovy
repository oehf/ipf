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
import org.openhealthtools.ihe.common.cdar2.impl.*
import org.junit.Test
import org.junit.Assert


/**
 * @author Christian Ohr
 */
public class CDAR2FactoryTest {
	
	/* (non-Javadoc)
	 * @see org.openehealth.ipf.modules.cda.factory.oht.CDAR2#factoryFor(java.lang.String)
	 */
	@Test
	void testFactoryForClass() {
		/* Test for correct init */
		CDAR2Factory factory = CDAR2Factory.factoryFor('CD')
		def code = factory.newInstance(null, 'code', null, null)
		Assert.assertTrue code instanceof CDImpl
		
		/* Test for reusing the preinitialized factory */
		CDAR2Factory factory2 = CDAR2Factory.factoryFor('CD')
		Assert.assertTrue factory == factory2
	}
	@Test
	void testFactoryForAttribute() {
		CDAR2Factory factory = CDAR2Factory.factoryFor('ED')
		new CDAR2ModelExtension().extensions.call()
		def iEd = factory.newInstance(null, 'text', 'replace with encoded value', [mediaType: 'application/pdf', representation: 'B64'])
		Assert.assertEquals 'replace with encoded value', iEd.getText()
		// assert iEd.getRepresentation().toString() == 'B64'
	}
	
	/**
	 * Attributes cann't be applied using the newInstance method!
	 */
	@Test
	void testNewInstanceWithAttributes() {		
		CDAR2Factory factory = CDAR2Factory.factoryFor('CD')
		def code2 = factory.newInstance(null, 'code', null, [code: "suspended"])
		Assert.assertTrue code2 instanceof CDImpl
		Assert.assertNull code2.code
	}
	
	@Test
	void testNewInstanceWithValue() {
		CDAR2Factory factory = CDAR2Factory.factoryFor('ST')
		def code = factory.newInstance(null, 'code', 'some string', null)
		Assert.assertTrue code instanceof String
		Assert.assertEquals "some string", code 
	}
	
}
