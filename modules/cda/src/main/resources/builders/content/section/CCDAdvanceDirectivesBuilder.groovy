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

// Chapter 3.2 : Advance Directives

// CONF-77: CCD SHOULD contain exactly one and SHALL NOT contain more than one
// Advance directives section (templateId 2.16.840.1.113883.10.20.1.1). 
// The Advance directives section SHALL contain a narrative block,
// and SHOULD contain clinical statements. 
// Clinical statements SHOULD include one or more advance directive 
// observations (templateId 2.16.840.1.113883.10.20.1.17). 
// An advance directive observation MAY contain exactly one advance directive 
// reference (templateId 2.16.840.1.113883.10.20.1.36) to an external 
// advance directive document.

ccd_advanceDirectives(schema:'section') {
    properties {
        //CONF-78: The advance directive section SHALL contain Section / code.
        //CONF-79: The value for “Section / code” SHALL be “42348-3”
        // “Advance directives” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
            getMetaBuilder().build {
                loincCode(code:'42348-3',
                        displayName:'Advance directives')
            }
        })

        // CONF-80: The advance directive section SHALL contain Section / title.
        // CONF-81: Section / title SHOULD be valued with a case-insensitive 
        // language-insensitive text string containing “advance directives”.
        title(req:true, def: {
            getMetaBuilder().build {
                st('Advance Directives')
            }
        })
        text(schema:'strucDocText', req:true)
        observation(schema:'ccd_advanceDirectivesObservation')
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.1')
            }
        })

    }
}


// CONF-82: An advance directive observation (templateId 2.16.840.1.113883.10.20.1.17) SHALL be
//          represented with Observation.
// CONF-83: The value for “Observation / @classCode” in an advance directive observation SHALL be
//          “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
// CONF-89: An advance directive observation SHALL contain exactly one Observation / code.
// CONF-97: An advance directive observation SHALL contain one or more sources of information,
//          as defined in section 5.2 Source.
//TODO CONF-97
// CONF-98: An advance directive observation SHALL contain exactly one advance directive status observation.
ccd_advanceDirectivesObservation(schema:'observation') {
    properties{
        // CONF-84: The value for “Observation / @moodCode” in an advance directive observation SHALL be
        //          “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION',
                def:XActMoodDocumentObservation.EVN_LITERAL)
        // CONF-86: An advance directive observation SHALL contain exactly one Observation / statusCode.
        // CONF-87: The value for “Observation / statusCode” in an advance directive observation SHALL be
        //          “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
        statusCode(req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
        advanceDirectiveStatus(schema:'ccd_advanceDirectiveObservationStatus', req:true)
        verifier(schema:'ccd_advanceDirectiveVerifier')
        reference(schema:'ccd_advanceDirectiveObservationReference')
    }
    collections{
        // CONF-85: An advance directive observation SHALL contain at least one Observation / id.
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.17')
            }
        })
    }
}

// CCD Advance Directive Observation Verifier
// CONF-92: A verification of an advance directive observation (templateId 2.16.840.1.113883.10.20.1.58) SHALL be
//          represented with Observation / participant.
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

// CCD Advance Directive Observation Verifier
// CONF-101: An advance directive reference (templateId 2.16.840.1.113883.10.20.1.36) SHALL be
//           represented with Observation / reference / ExternalDocument.
// CONF-102: An advance directive observation MAY contain exactly one advance directive reference.
// CONF-103: The value for “Observation / reference / @typeCode” in an advance directive reference SHALL be
//           “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
// CONF-104: ExternalDocument SHALL contain at least one ExternalDocument / id.
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


// CONF-98: An advance directive observation SHALL contain exactly one advance directive status observation.
// CONF-99: An advance directive status observation (templateId 2.16.840.1.113883.10.20.1.37) SHALL be
//          a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57)
//          (as defined in section 5.1 “Type” and “Status” values).
// CONF-100: The value for “Observation / value” in an advance directive status observation SHALL be
//          selected from ValueSet 2.16.840.1.113883.1.11.20.1 AdvanceDirectiveStatusCode STATIC 20061017.
ccd_advanceDirectiveObservationStatus(schema:'ccd_statusObservation'){
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.37')
            }
        })
    }
}

