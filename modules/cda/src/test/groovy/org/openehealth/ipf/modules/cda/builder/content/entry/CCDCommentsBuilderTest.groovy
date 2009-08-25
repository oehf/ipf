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

import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.modules.cda.builder.content.document.CCDDefinitionLoader
import org.openehealth.ipf.modules.cda.builder.AbstractCDAR2BuilderTest

/**
 * @author Stefan Ivanov
 */
public class CCDCommentsBuilderTest extends AbstractCDAR2BuilderTest {
	
	@Before
	void initialize() throws Exception {
	    new CCDDefinitionLoader(builder).loadComments(loaded)
		new CCDCommentsExtension(builder).register(registered)
	}
	
	@Test
	public void testCCDComment() {
		POCDMT000040Act comment = builder.build {
		    ccd_comment{ 
		        text('This is the only formal \"Free Text comment\" in this document! ' + 
		                'Note that ALL such comments must appear in the narrative text of the parent Section ' + 
		                'and be referenced by the following pointer to it.'){
		            reference(value:'PntrtoSectionText')
		        }
			}
		}
		new CCDCommentsValidator().doValidateComment(comment)
	}
	
	
	
}
