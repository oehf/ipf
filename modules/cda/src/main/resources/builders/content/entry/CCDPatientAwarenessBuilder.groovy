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
 * Chapter 3.5.2.4: Patient awareness of a problem
 * 
 * Template Definitions:
 *      Patient awareness (2.16.840.1.113883.10.20.1.48)
 * 
 * Dependecies:
 * 
 */

//CONF-178: Patient awareness (templateId 2.16.840.1.113883.10.20.1.48) of a problem,
//          observation, or other clinical statement SHALL be represented with participant.
ccd_patientAwareness(schema:'clinicalStatementParticipant'){
    properties{
        // CONF-181: The value for “participant / @typeCode” in a patient awareness SHALL
        //           be “SBJ” “Subject” 2.16.840.1.113883.5.90 ParticipationType STATIC.
        typeCode(factory:'PARTICIPATION_TYPE', def:ParticipationTargetSubject.SBJ_LITERAL)
        // CONF-182: Patient awareness SHALL contain exactly one participant / awarenessCode.
        awarenessCode(schema:'ce', req:true)
        // CONF-183: Patient awareness SHALL contain exactly one participant / participantRole / id,
        //           which SHALL have exactly one value, which SHALL also be present in 
        //           ClinicalDocument / recordTarget / patientRole / id.
        participantRole(schema:'ccd_problemsParticipantRole')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.48')
            }
        })
    }
}