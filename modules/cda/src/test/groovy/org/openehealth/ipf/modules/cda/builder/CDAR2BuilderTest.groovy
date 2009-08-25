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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.junit.Test
import org.junit.Assert

/**
 * @author Christian Ohr
 */
public class CDAR2BuilderTest extends AbstractCDAR2BuilderTest{
	
	@Test
	public void testBuildPatientRole() {
	    def role = builder.build(getClass().getResource('/builders/content/header/PatientRoleExample.groovy'))
		Assert.assertNotNull role
	}
	
	@Test
	public void testBuildClinicalDocumentMetaData() {		
	    def document = builder.build(getClass().getResource('/builders/content/header/CDAHeaderExample1.groovy'))
//		def renderer = new CDAR2Renderer()
//		def opts = [:]
//		opts[XMLResource.OPTION_DECLARE_XML] = true
//		opts[XMLResource.OPTION_ENCODING] = 'utf-8'
		Assert.assertNotNull document 
//		println(renderer.render(document, opts))
	}
	
}
