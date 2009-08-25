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
 *  Chapter 3.6.2.4: Age Observation (2.16.840.1.113883.10.20.1.38)
 */

// CONF-225: An age observation (templateId 2.16.840.1.113883.10.20.1.38) SHALL 
//           be represented with Observation.
// CONF-226: The value for “Observation / @classCode” in an age observation SHALL 
//           be “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
ccd_ageObservation(schema:'observation') {
    properties {
       // CONF-227: The value for “Observation / @moodCode” in an age observation SHALL
       //           be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
                    def: XActMoodDocumentObservation.EVN_LITERAL)
        code(schema:'ce', def: {
            getMetaBuilder().build { snomedCode(code:'397659008', displayName:'Age') }
        })
        statusCode(schema:'cs', req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })

    }
    collections {
        values(min:1, max:1)
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList { ii(root:'2.16.840.1.113883.10.20.1.38') }
        })
    }
}


// end of Age Observation