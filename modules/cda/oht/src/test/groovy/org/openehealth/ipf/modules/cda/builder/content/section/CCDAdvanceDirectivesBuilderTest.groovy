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
package org.openehealth.ipf.modules.cda.builder.content.section

import org.openehealth.ipf.modules.cda.builder.AbstractCDAR2BuilderTest;
import org.openehealth.ipf.modules.cda.builder.content.document.CCDDefinitionLoader
import org.openehealth.ipf.modules.cda.builder.content.entry.*
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section
import org.junit.Before
import org.junit.Test

/**
 * @author Stefan Ivanov
 */
public class CCDAdvanceDirectivesBuilderTest extends AbstractCDAR2BuilderTest {
	
	@Before
	void initialize() throws Exception {
        new CCDDefinitionLoader(builder).loadAdvanceDirectives(loaded)
        new CCDAdvanceDirectivesExtension(builder).register(registered)
	}
	
	@Test
	public void testCCDAdvanceDirectives() {
		def POCDMT000040Section advanceDirectives = builder.build(
		        getClass().getResource('/builders/content/section/CCDAdvanceDirectivesExample.groovy'))
		new CCDAdvanceDirectivesValidator().validate(advanceDirectives, null)
	}
	
	
	
}
