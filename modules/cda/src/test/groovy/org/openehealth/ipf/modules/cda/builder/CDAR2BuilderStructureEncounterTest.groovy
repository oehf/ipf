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
public class CDAR2BuilderStructureEncounterTest extends AbstractCDAR2BuilderTest {
	
	/**
	 * Test simple Encounter
	 */
	public void testEncounter() {
		def entry = builder.build {
			entry{
			    encounter(classCode:'ENC', moodCode:'RQO'){
                    code(
                        code:'185389009',
                        codeSystem:'2.16.840.1.113883.6.96',
                        codeSystemName:'SNOMED CT',
                        displayName:'Follow-up visit'
                    )
                    effectiveTime{
                        low(value:'20000412')
                        high(value:'20000417')
                    }
			    }
			}//entry
		}
		// println entry.encounter
	}
	
	/**
	 * Test encounter defaults
	 */
	public void testEncountertDefaultValues() {
		def entry = builder.build {
			entry{
				encounter(classCode:'ENC', moodCode:'RQO')
			}//entry
		}
		
//		assertTrue(entry.observation.isSetMoodCode())
//		assert entry.observation.moodCode.name == 'INT'
//		assert entry.observation.classCode.name == 'OBS'
//		assertFalse(entry.observation.isSetNegationInd())
//		assertEquals 0, entry.observation.entryRelationship.size
//		assertNull entry.observation.text	
	}	
}