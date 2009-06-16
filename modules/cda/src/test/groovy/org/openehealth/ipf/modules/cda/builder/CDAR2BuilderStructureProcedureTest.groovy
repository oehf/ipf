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

import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*

/**
 * @author Stefan Ivanov
 */
public class CDAR2BuilderStructureProcedureTest extends AbstractCDAR2BuilderTest {
	
	
	/**
	 * Test simple Procedure
	 */
	public void testProcedure() {
		def entry = builder.build {
			entry{
			    procedure(classCode:'PROC', moodCode:'EVN'){
                    code(
                        code:'30549001', 
                        codeSystem:'2.16.840.1.113883.6.96', 
                        codeSystemName:'SNOMED CT' ,
                        displayName:'Suture removal'
                    )
                    statusCode(code:'completed')
                    effectiveTime(value:'200004071430')
                    targetSiteCode(
                        code:'66480008',
                        codeSystem:'2.16.840.1.113883.6.96',
                        codeSystemName:'SNOMED CT',
                        displayName:'Left forearm'
                    )
			    }//procedure
			}//entry
		}
		// println entry.procedure
	}
	
	/**
	 * Test procedure defaults
	 */
	public void testProcedureDefaultValues() {
		def entry = builder.build {
			entry{
			    procedure(moodCode:'EVN') {
                // classCode('PROC')
                // moodCode('EVN')
             }
			}//entry
		}
		// println entry.procedure
	}	
}