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
package org.openehealth.ipf.modules.cda.builder.content.entry

import org.openhealthtools.ihe.common.cdar2.POCDMT000040ServiceEvent
import org.junit.BeforeClassimport org.junit.Test
import org.openehealth.ipf.modules.cda.builder.content.AbstractContentBuilderTest

/**
 * @author Stefan Ivanov
 */
public class CCDMainActivityBuilderTest extends AbstractContentBuilderTest {
	
	@BeforeClass
	static void initialize() throws Exception {
		builder().define(getClass().getResource('/builders/content/entry/CCDMainActivityBuilder.groovy'))
		def extension = new CCDMainActivityExtension(builder())
		extension.extensions.call()
	}
	
	@Test
	public void testCCDMainActivity() {
	    def POCDMT000040ServiceEvent event = builder.build {
	        ccd_serviceEvent{//ccd main activity
                effectiveTime{
                    low(value:'19320924')
                    high(value:'20000407')
                }
            }
	    }
	    new CCDMainActivityValidator().doValidateMainActivity(event)
	}
	
}
