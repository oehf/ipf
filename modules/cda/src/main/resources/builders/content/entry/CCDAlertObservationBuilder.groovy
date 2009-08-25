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
 * Chapter 3.8.2.1.2: Alert Observation (2.16.840.1.113883.10.20.1.18)
 * 
 * Template Definitions:
 *      Alert Observation (2.16.840.1.113883.10.20.1.18)
 *      Alert Status Observation (2.16.840.1.113883.10.20.1.39)
 *      
 * Dependencies:
 *      ReactionObservation (ccd_reactionObservation)
 *      Status Observation (ccd_statusObservation)
 *      Comments      
 */

 
//CONF-262: An alert observation (templateId 2.16.840.1.113883.10.20.1.18) 
//SHALL be represented with Observation.
//CONF-280: An alert observation MAY contain one or more reaction observations 
//(templateId 2.16.840.1.113883.10.20.1.54), each of which MAY contain
//exactly one severity observation (templateId 2.16.840.1.113883.10.20.1.55) 
//AND/OR one or more reaction interventions.
ccd_alertObservation(schema:'ccd_observation'){
    properties{
        // CONF-263: The value for “Observation / @moodCode” in an alert observation SHALL be 
        //           “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
                    def: XActMoodDocumentObservation.EVN_LITERAL)
        // CONF-264: An alert observation SHALL include exactly one Observation / statusCode.
        // CONF-265: The value for “Observation / statusCode” in an alert observation SHALL be
        //           “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.    
        statusCode(schema:'cs', req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
        // CONF-270: An alert observation MAY contain exactly one alert status observation.
        alertStatus(schema:'ccd_alertObservationStatus')
        // CONF-273: An alert observation SHOULD contain at least one Observation / participant, 
        //           representing the agent that is the cause of the allergy or adverse reaction.
        participantAgent(schema:'ccd_participantAgent')
        // CONF-280: An alert observation MAY contain one or more reaction observations 
        //           (templateId 2.16.840.1.113883.10.20.1.54), each of which MAY contain 
        //           exactly one severity observation (templateId 2.16.840.1.113883.10.20.1.55) 
        //           AND/OR one or more reaction interventions.
        reactionObservation(schema:'ccd_reactionObservation')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.18')
            }
        })
    }
}

//CONF-270: An alert observation MAY contain exactly one alert status observation.
//CONF-271: An alert status observation (templateId 2.16.840.1.113883.10.20.1.39) SHALL be
//        a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) 
//        (as defined in section 5.1 “Type” and “Status” values).
ccd_alertObservationStatus(schema:'ccd_statusObservation'){
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.39')
            }
        })
        values(collection:'value', min:1, max:1) {
            value(schema:'snomedCode')
        }        
    }
}

//CONF-274: An agent participation in an alert observation SHALL contain exactly
//one participant / participantRole / playingEntity.
ccd_participantAgent(schema:'participantRole'){
    properties{
        // CONF-276: The value for Observation / participant / participantRole / @classCode in an agent 
        //           participation SHALL be “MANU” “Manufactured” 2.16.840.1.113883.5.110 RoleClass STATIC.
        classCode(factory:'ROLE_CLASS_CONTACT',
                    def: RoleClassManufacturedProduct.MANU_LITERAL)
        playingEntity(schema:'ccd_alertPlayingEntity', req:true)
    }
}

ccd_alertPlayingEntity(schema:'playingEntity'){
    properties{
        // CONF-277: The value for Observation / participant / participantRole / playingEntity / @classCode
        //           in an agent participation SHALL be “MMAT” “Manufactured material” 
        //           2.16.840.1.113883.5.41 EntityClass STATIC. 
        classCode(factory:'ENTITY_CLASS_ROOT',
                    def: EntityClassManufacturedMaterialMember2.MMAT_LITERAL)
        // CONF-278: An agent participation in an alert observation SHALL contain 
        //           exactly one participant / participantRole / playingEntity / code.
        code(schema:'ce', req:true)
    }
}

// end of Alert Observation