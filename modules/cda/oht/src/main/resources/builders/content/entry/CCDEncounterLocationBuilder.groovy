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

// Chapter 3.15.2.2: Encounter location (2.16.840.1.113883.10.20.1.45)

// CONF-472: A location participation (templateId 2.16.840.1.113883.10.20.1.45) SHALL be 
//           represented with the participant participation.
ccd_encounterLocationParticipant(schema:'clinicalStatementParticipant'){
	properties{
	    // CONF-473: The value for “participant / @typeCode” in a location participation 
	    //           SHALL be “LOC” 2.16.840.1.113883.5.90 ParticipationType STATIC.
	    typeCode(def:ParticipationTargetLocation.LOC_LITERAL)
	    // CONF-474: A location participation SHALL contain exactly one participant / participantRole.
		participantRole(req:true)
	}
	collections{
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { 
			    ii(root:'2.16.840.1.113883.10.20.1.45') 
			}
		})
	}
}

ccd_encounterLocationParticipantRole(schema:'participantRole'){
    properties{
        // CONF-475: The value for “participant / participantRole / @classCode” in a 
        //           location participation SHALL be “SDLOC” “Service delivery location” 
        //           2.16.840.1.113883.5.110 RoleClass STATIC.
        classCode(factory:'ROLE_CLASS_ROOT', def:RoleClassServiceDeliveryLocation.SDLOC_LITERAL)
        // CONF-476: Participant / participantRole in a location participation MAY contain exactly
        //           one participant / participantRole / code.
		// CONF-477: The value for “participant / participantRole / code” in a location 
		//           participation SHOULD be selected from ValueSet 2.16.840.1.113883.1.11.17660 
		//           ServiceDeliveryLocationRoleType 2.16.840.1.113883.5.111 RoleCode DYNAMIC.
		code(schema:'roleCode')
        // CONF-478: Participant / participantRole in a location participation MAY contain exactly one 
        //           participant / participantRole / playingEntity.
        playingEntity(schema:'ccd_encounterLocationPlayingEntity')
    }
}

ccd_encounterLocationPlayingEntity(schema:'playingEntity'){
    properties{
        // CONF-479: The value for “participant / participantRole / playingEntity / @classCode” 
        //           in a location participation SHALL be “PLC” “Place” 2.16.840.1.113883.5.41 
        //           EntityClass STATIC.
        classCode(factory:'ENTITY_CLASS_ROOT', def:EntityClassPlace.PLC_LITERAL)
    }
}

// end of Encounter Location