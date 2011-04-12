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
package org.openehealth.ipf.modules.cda

import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*
import org.junit.BeforeClassimport org.junit.Before
import org.junit.Assert
/**
 * @author Christian Ohr
 */
public abstract class AbstractCDAR2Test {
	
    static def registered = []
		
	@BeforeClass
	public static void setupBeforeClass() {
		ExpandoMetaClass.enableGlobally()
		new CDAR2ModelExtension().register(registered)
	}

	static void shouldFail(Closure c) {
	    shouldFail(Exception.class, c)
	}
	
	static void shouldFail(Class<Exception> clazz, Closure c) {
	    boolean failed = false
	    try {
	        c.call()
	    } catch (Exception e) {
	        if (clazz.isInstance(e)) {
	            failed = true
	        }
	    }
	    Assert.assertTrue("Closure should have failed throwing ${clazz.name}", failed)
	}
		
}