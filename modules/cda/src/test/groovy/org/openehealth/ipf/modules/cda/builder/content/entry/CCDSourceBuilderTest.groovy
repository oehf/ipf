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

import org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Reference
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.modules.cda.builder.content.document.CCDDefinitionLoader
import org.openehealth.ipf.modules.cda.builder.AbstractCDAR2BuilderTest;
/**
 * @author Stefan Ivanov
 */
public class CCDSourceBuilderTest extends AbstractCDAR2BuilderTest {
	
	@Before
	void initialize() throws Exception {
	    new CCDDefinitionLoader(builder).loadSource(loaded)
		new CCDSourceExtension(builder).register(registered)
	}
	
	@Test
	public void testCCDInformationSource() {
		POCDMT000040Observation informationSource = builder.build {
		    ccd_informationSource{ 
			    value(make {
                    st('Unknown')
                }) 
			}
		}
		new CCDSourceValidator().doValidateInformationSource(informationSource)
	}
	
	@Test
    public void testCCDReferenceSource() {
	    POCDMT000040Reference refSource = builder.build {
            ccd_referenceSource{ 
                externalDocument{
                    templateId(root:'2.16.840.1.113883.10.20.1.36')
                    id(root:'b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3')
                    code(code:'371538006', codeSystem:'2.16.840.1.113883.6.96', displayName:'Advance directive')
                    text(mediaType:'application/pdf'){
                        reference(value:'AdvanceDirective.b50b7910-7ffb-4f4c-bbe4-177ed68cbbf3.pdf')
                    }//text
                }//externalDocument
            }
        }
        new CCDSourceValidator().doValidateReferenceSource(refSource)
    }
	
	
	
}
