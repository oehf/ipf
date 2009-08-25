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
package builders.content.entry

import org.openhealthtools.ihe.common.cdar2.*

// Chapter 3.9.2.2.2: Patient Instruction (2.16.840.1.113883.10.20.1.49)

// CONF-331: A patient instruction (templateId 2.16.840.1.113883.10.20.1.49) 
//           SHALL be represented with Act.
ccd_patientInstruction(schema:'ccd_act') {
	properties {
		// CONF-332: The value for “Act / @moodCode” in a patient instruction 
		//           SHALL be “INT” “Intent” 2.16.840.1.113883.5.1001 ActMood STATIC.
		moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
		def: XActMoodDocumentObservation.INT_LITERAL)       
	}
	collections {        
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { 
			    ii(root:'2.16.840.1.113883.10.20.1.49') 
			}
		})
	}
}

// end of Patient Instruction Act