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
 * Chapter 3.2.2.1: Advance directive observations 
 * 
 * Template Definitions:
 *      Advance Directive Observation (2.16.840.1.113883.10.20.1.17)
 *      Advance Directive Observation Verification (2.16.840.1.113883.10.20.1.58)
 *      Advance Directive Reference (2.16.840.1.113883.10.20.1.36)
 *      Advance Directive Status Observation (2.16.840.1.113883.10.20.1.37)
 * Dependencies:
 *      Comment (ccd_comment)
 *      Source (ccd_informationSource)
 *      Status Observation (ccd_statusObservation) 
 */


// CONF-82: An advance directive observation (templateId 2.16.840.1.113883.10.20.1.17)
//         SHALL be represented with Observation.
// CONF-83: The value for “Observation / @classCode” in an advance directive observation
//         SHALL be “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
// CONF-89: An advance directive observation SHALL contain exactly one Observation / code.
// CONF-97: An advance directive observation SHALL contain one or more sources of information,
//          as defined in section 5.2 Source.
ccd_advanceDirectiveObservation(schema:'ccd_observation') {
	properties{
		// CONF-84: The value for “Observation / @moodCode” in an advance directive observation
		//          SHALL be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
		moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', def:XActMoodDocumentObservation.EVN_LITERAL)
		// CONF-86: An advance directive observation SHALL contain exactly one Observation / statusCode.
		// CONF-87: The value for “Observation / statusCode” in an advance directive observation SHALL be
		//          “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
		statusCode(schema:'cs', req:true, def: {
			getMetaBuilder().build { 
			    cs('completed') 
			}
		})
		advanceDirectiveReference(schema:'ccd_advanceDirectiveObservationReference')
        //CONF-98: An advance directive observation SHALL contain exactly one advance directive status observation.
        advanceDirectiveStatus(schema:'ccd_advanceDirectiveObservationStatus', req:true)
        verifier(schema:'ccd_advanceDirectiveVerifier')
	}
	collections{
		// CONF-85: An advance directive observation SHALL contain at least one Observation / id.
		ids(min:1)
	    templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { 
			    ii(root:'2.16.840.1.113883.10.20.1.17') 
			}
		})		
	}
}

//CONF-92: A verification of an advance directive observation (templateId 2.16.840.1.113883.10.20.1.58)
//         SHALL be represented with Observation / participant.
ccd_advanceDirectiveVerifier(schema:'clinicalStatementParticipant'){
    properties{
        // CONF-94: The value for “Observation / participant / @typeCode” in a verification SHALL be
        //          “VRF” “Verifier” 2.16.840.1.113883.5.90 ParticipationType STATIC.
        typeCode(factory:'PARTICIPATION_TYPE', def:ParticipationVerifier.VRF_LITERAL)
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.58')
            }
        })
    }
}

//CONF-98: An advance directive observation SHALL contain exactly one advance directive status observation.
//CONF-99: An advance directive status observation (templateId 2.16.840.1.113883.10.20.1.37) SHALL be
//         a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57)
//         (as defined in section 5.1 “Type” and “Status” values).
//CONF-100: The value for “Observation / value” in an advance directive status observation SHALL be
//          selected from ValueSet 2.16.840.1.113883.1.11.20.1 AdvanceDirectiveStatusCode STATIC 20061017.
ccd_advanceDirectiveObservationStatus(schema:'ccd_statusObservation'){
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.37')
            }
        })
        values(collection:'value', min:1, max:1) {
            value(schema:'snomedCode')
        }
    }
}

//CONF-101: An advance directive reference (templateId 2.16.840.1.113883.10.20.1.36) SHALL be
//          represented with Observation / reference / ExternalDocument.
//CONF-102: An advance directive observation MAY contain exactly one advance directive reference.
//CONF-103: The value for “Observation / reference / @typeCode” in an advance directive reference
//          SHALL be “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
//CONF-104: ExternalDocument SHALL contain at least one ExternalDocument / id.
ccd_advanceDirectiveObservationReference(schema:'externalDocument'){
    collections{
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.36')
            }
        })
    }
}

// end of Advance directive observations