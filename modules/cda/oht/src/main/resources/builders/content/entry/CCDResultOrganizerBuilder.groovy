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
 * Chapter 3.13.2.1.1: 
 * 
 * Template Definitions:
 *      Result Organizer (2.16.840.1.113883.10.20.1.32)
 *      Result Observation (2.16.840.1.113883.10.20.1.31)
 *      
 * Dependencies:
 *      Functional Status Status Observation (ccd_functionalStatusStatusObservation)
 *      Source (ccd_organizer, ccd_observation)
 *      
 */


// CONF-393: A result organizer (templateId 2.16.840.1.113883.10.20.1.32) SHALL be 
//           represented with Organizer.
// CONF-396: A result organizer SHALL contain exactly one Organizer / statusCode.
ccd_resultOrganizer(schema:'ccd_organizer'){
    properties{
        // CONF-394: The value for “Organizer / @moodCode” in a result organizer SHALL be
        //           “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        // CONF-397: A result organizer SHALL contain exactly one Organizer / code.
        code(schema:'cd', req:true)
        // CONF-405: The target of one or more result organizer Organizer / component 
        //           relationships SHALL be a result observation.
        resultObservation(schema:'ccd_resultObservation')
    }
    collections{
        // CONF-395: A result organizer SHALL contain at least one Organizer / id.
        ids(min:1)
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.32')
            }
        })
        // CONF-402: A result organizer SHALL contain one or more Organizer / component.
        components(collection:'component', min:1){
            component(schema:'ccd_resultsOrganizerComponents')
        }
        // CONF-406: A result organizer SHALL contain one or more sources of information, 
        //           as defined in section 5.2 Source. Defined by base schema.
    }
}

// CONF-405: The target of one or more result organizer Organizer / component relationships
//           SHALL be a result observation.
ccd_resultOrganizerComponents(schema:'ccd_organizerComponents') {
    properties{
        observation(schema:'ccd_resultsObservation')
    }
}

// CONF-407: A result observation (templateId 2.16.840.1.113883.10.20.1.31) 
//           SHALL be represented with Observation.
// CONF-412: A result observation SHALL contain exactly one Observation / code.
// CONF-421: A result observation SHALL contain one or more sources of information, 
//           as defined in section 5.2 Source. Defined in base schema
ccd_resultObservation(schema:'ccd_observation') {
    properties{
        // CONF-408: The value for “Observation / @moodCode” in a result observation 
        //           SHALL be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XActMoodDocumentObservation.EVN_LITERAL)
        // CONF-410: A result observation SHALL contain exactly one Observation / statusCode.
        statusCode(schema:'cs', req:true)
        // CONF-137: A result observation in the functional status section SHALL contain
        //           exactly one status of functional status observation.
        functionalStatusStatus(schema:'ccd_functionalStatusStatusObservation')
    }
    collections{
        // CONF-409: A result observation SHALL contain at least one Observation / id.
        ids(min:1)
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.31')
            }
        })
        // CONF-416: A result observation SHALL contain exactly one Observation / value.
        values(min:1, max:1)
    }
}