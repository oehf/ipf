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
 * Chapter 3.7.2.1: Social History Observation 
 * 
 * Template Definitions:
 *      Social History Observation (2.16.840.1.113883.10.20.1.33)
 *      Social Hisotry Status Observation (2.16.840.1.113883.10.20.1.56)
 * 
 * Dependencies:
 *      Episode Observation (ccd_episodeObservation)
 *      Status Observation (ccd_statusObservation)
 *      Source (ccd_observation)
 */
 
// Depends on     : ccd_episodeObservation, ccd_statusObservation, 

// CONF-237: A social history observation (templateId 2.16.840.1.113883.10.20.1.33) 
//           SHALL be represented with Observation.
ccd_socialHistoryObservation(schema:'ccd_observation'){
    properties{
        // CONF-238: The value for “Observation / @classCode” in an episode observation SHALL
        //           be “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
        classCode(factory:'ACT_CLASS_OBSERVATION',
                def:ActClassObservationMember3.OBS_LITERAL)
        // CONF-239: The value for “Observation / @moodCode” in an episode observation SHALL
        //           be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION',
                def:XActMoodDocumentObservation.EVN_LITERAL)                
        // CONF-241: An social history observation SHALL include exactly one Observation / statusCode.
        // CONF-242: The value for “Observation / statusCode” in an episode observation SHALL
        //           be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
        statusCode(schema:'cs', req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
        // CONF-245: A social history observation SHALL contain one or more sources of 
        //           information, as defined in section 5.2 Source. Defined in base schema.        
		// CONF-246: A social history observation MAY contain exactly one social history 
		//           status observation.
        socialHistoryStatus(schema:'ccd_socialHistoryStatusObservation')
        // CONF-249: A social history observation MAY contain exactly one episode 
        //           observation (templateId 2.16.840.1.113883.10.20.1.41) 
        //           (see section 3.5.2.3 Episode observations).
        episodeObservation(schema:'ccd_episodeObservation')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.33')
            }
        })
        // CONF-240: A social history observation SHALL contain at least one Observation / id.
        ids(min:1)        
    }
}

// CONF-247: A social history status observation (templateId 2.16.840.1.113883.10.20.1.56) 
//           SHALL be a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) 
//           (as defined in section 5.1 “Type” and “Status” values).
// CONF-248: The value for “Observation / value” in a social history status observation 
//           SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.17 
//           SocialHistoryStatusCode STATIC 20061017. (TODO)
ccd_socialHistoryStatusObservation(schema:'ccd_statusObservation') {
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.56')
            }
        })  
        values(collection:'value', min:1, max:1) {
            value(schema:'snomedCode')
        }        
    }
}
// end of Social History Observation