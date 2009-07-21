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

import org.openhealthtools.ihe.common.cdar2.POCDMT000040Sectionimport org.junit.BeforeClassimport org.junit.Testimport org.openehealth.ipf.modules.ccd.builder.CCDConformanceValidatorHelper
import org.openehealth.ipf.modules.cda.builder.content.AbstractContentBuilderTest

/**
 * @author Christian Ohr
 */
public class CCDPurposeBuilderTest extends AbstractContentBuilderTest {
	
    @BeforeClass
	static void initialize() throws Exception {
		builder().define(getClass().getResource('/builders/content/section/CCDPurposeBuilder.groovy'))
		def extension = new CCDPurposeExtension(builder())
	    extension.extensions.call()
	}
	
    @Test
	public void testCCDPurpose() {
		def POCDMT000040Section purpose = builder().build {
			ccd_purpose {
				text('Transfer of Care!')
				purposeActivity {
					act(moodCode:'EVN') {
						code(
						        code:'308292007',
								codeSystem:'2.16.840.1.113883.6.96',
								displayName:'Transfer of care')
						statusCode('completed')
					}
				}
			}
		}
		new CCDPurposeValidator().validate(purpose, null)
	}
    
    
	
}
