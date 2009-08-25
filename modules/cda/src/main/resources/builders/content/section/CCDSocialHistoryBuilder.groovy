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
package builders.content.section

import org.openhealthtools.ihe.common.cdar2.*

/**
 * Chapter 3.7 : Social History
 * 
 * Template Definitions:
 *      Social History Section (2.16.840.1.113883.10.20.1.15)
 * Dependencies:
 *      Social History Observation (ccd_socialHistoryObservation)
 * 
 */

// CONF-232: CCD SHOULD contain exactly one and SHALL NOT contain more than 
//           one Social history section (templateId 2.16.840.1.113883.10.20.1.15). 
//           The Social history section SHALL contain a narrative block, and SHOULD 
//           contain clinical statements. Clinical statements SHOULD include one or 
//           more social history observations (templateId 2.16.840.1.113883.10.20.1.33). 
ccd_socialHistory(schema:'ccd_section') {
	properties {
		// CONF-233: The advance directive section SHALL contain Section / code.
		// CONF-234: The value for “Section / code” SHALL be “29762-2”
		//            “Social history” 2.16.840.1.113883.6.1 LOINC STATIC.
		code(schema:'loincCode', def: {
			getMetaBuilder().build {
				loincCode(code:'29762-2',
				displayName:'Social history')
			}
		})
		
		// CONF-235: The advance directive section SHALL contain Section / title.
		// CONF-236: Section / title SHOULD be valued with a case-insensitive 
		//           language-insensitive text string containing “social history”.
		title(req:true, def: {
			getMetaBuilder().build { 
			    st('Social History') 
			}
		})
	    text(schema:'strucDocText', req:true)		
		socialHistoryObservation(schema:'ccd_socialHistoryObservation')
	}
	collections {
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { 
			    ii(root:'2.16.840.1.113883.10.20.1.15') 
			}
		})
		
	}
}

// end of Social History
