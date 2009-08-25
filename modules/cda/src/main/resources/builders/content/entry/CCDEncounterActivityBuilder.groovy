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

// Chapter 3.15.2.1: Encounter activities (2.16.840.1.113883.10.20.1.21)
// Depends on      : ccd_patientInstruction, ccd_encounterLocationParticipantRole

//CONF-458: An encounter activity (templateId 2.16.840.1.113883.10.20.1.21) SHALL be 
//          represented with Encounter.
ccd_encounterActivity(schema:'ccd_encounter'){
	properties{
	    // CONF-459: The value for “Encounter / @classCode” in an encounter activity 
	    //           SHALL be “ENC” 2.16.840.1.113883.5.6 ActClass STATIC.
		// CONF-460: The value for “Encounter / @moodCode” in an encounter activity 
		//           SHALL be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
		moodCode(factory:'XDOCUMENT_ENCOUNTER_MOOD', def:XDocumentEncounterMood.EVN_LITERAL)
		// CONF-462: An encounter activity SHOULD contain exactly one Encounter / code.
		// CONF-463: The value for “Encounter / code” in an encounter activity SHOULD be selected 
		//           from ValueSet 2.16.840.1.113883.1.11.13955 EncounterCode 2.16.840.1.113883.5.4 
		//           ActCode DYNAMIC
		code(schema:'actCode', req:true)
		// CONF-468: An encounter activity MAY contain one or more 
		//           patient instructions (templateId 2.16.840.1.113883.10.20.1.49).
		patientInstruction(schema:'ccd_patientInstruction')	
		// CONF-470: An encounter activity SHALL contain one or more sources of information, 
		//           as defined in section 5.2 Source. Defined by base schema.
		//CONF-471: An encounter activity MAY contain one or more location participations.
		encounterLocation(schema:'ccd_encounterLocationParticipantRole')
	}
	collections{
	    // CONF-461: An encounter activity SHALL contain at least one Encounter / id.
	    ids(min:1, max:1)
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { 
			    ii(root:'2.16.840.1.113883.10.20.1.21') 
			}
		})
	}
}


// end of Encounter Activity