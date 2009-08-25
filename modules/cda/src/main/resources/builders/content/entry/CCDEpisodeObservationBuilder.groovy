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

/**
 * 3.5.2.3 Episode observations
 * 
 * Template Definitions:
 *      Episode Observation (2.16.840.1.113883.10.20.1.41)
  * Dependencies:
 *      Age Observation (ccd_ageObservation)
 *      Comments (ccd_observation)
 */


// CONF-169: An episode observation (templateId 2.16.840.1.113883.10.20.1.41) 
//           SHALL be represented with Observation.
ccd_episodeObservation(schema:'ccd_observation'){
	properties{
		// CONF-170: The value for “Observation / @classCode” in an episode observation SHALL
		//           be “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
		classCode(factory:'ACT_CLASS_OBSERVATION', def:ActClassObservationMember3.OBS_LITERAL)
		// CONF-171: The value for “Observation / @moodCode” in an episode observation SHALL
		//           be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
		moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', def:XActMoodDocumentObservation.EVN_LITERAL)                
		// CONF-172: An episode observation SHALL include exactly one Observation / statusCode.
		// CONF-173: The value for “Observation / statusCode” in an episode observation SHALL
		//           be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
		statusCode(schema:'cs', req:true, def: {
			getMetaBuilder().build { 
			    cs('completed') 
			}
		})
	}
	collections{
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { 
			    ii(root:'2.16.840.1.113883.10.20.1.41') 
			}
		})
		// CONF-175: “Observation / value” in an episode observation SHOULD 
		//           be the following SNOMED CT expression:
		values(collection:'value', min:1, max:1, def:{
			getMetaBuilder().buildList {
				cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding'){
					qualifier{
						name(code:'246456000', displayName:'Episodicity')
						value(code:'288527008', displayName:'New episode')
					}
				}
			}
		})
	}
}

// end of Episode History Observation