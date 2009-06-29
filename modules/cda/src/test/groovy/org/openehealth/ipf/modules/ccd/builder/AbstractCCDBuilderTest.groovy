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
package org.openehealth.ipf.modules.ccd.builder


import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*

/**
 * @author Christian Ohr
 */
public abstract class AbstractCCDBuilderTest extends GroovyTestCase {
	
	static def builder
	static def renderer
	
	static {
		ExpandoMetaClass.enableGlobally()
		def ccdExtensions = new CCDModelExtension()
		ccdExtensions.extensions.call()
		def cdaExtensions = new CDAR2ModelExtension()
		cdaExtensions.extensions.call()		
	}
	
	public void setUp() throws Exception {
		if (!builder)
            builder = new CCDBuilder(getClass().getClassLoader())
		if (!renderer)
		    renderer = new CDAR2Renderer()
	}
		
}