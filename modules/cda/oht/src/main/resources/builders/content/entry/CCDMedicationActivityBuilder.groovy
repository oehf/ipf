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
 *  * 3.9.2.1.1: Medication activity
 * 
 * Template Definitions:
 *      Medication Activity (2.16.840.1.113883.10.20.1.24)
 *      Medication Series Number Observation (2.16.840.1.113883.10.20.1.46)
 *      
 * Dependencies:
 *      Medication Status Observation (ccd_medicationStatus)
 *      Patient Instruction (ccd_patientInstruction)
 *      Reaction Observation (ccd_reactionObservation)
 *      Product (ccd_manufacturedProduct)
 *      Product Instance (ccd_manufacturedProduct)
 *      Comments (ccd_substanceAdministration)
 *      Source (ccd_substanceAdministration)
 */

//CONF-304: A medication activity (templateId 2.16.840.1.113883.10.20.1.24) 
//          SHALL be represented with SubstanceAdministration
ccd_medicationActivity(schema:'ccd_substanceAdministration'){
    properties{
        moodCode(factory:'XACT_MOOD_DOCUMENT_SUBSTANCE',
                def: XDocumentSubstanceMood.EVN_LITERAL)
        statusCode(schema:'cs', def: {
            getMetaBuilder().build {
                cs('active')
            }
        })
        routeCode(schema:'routeCode')
        // CONF-328: A medication activity MAY contain one or more 
        //           SubstanceAdministration / entryRelationship, whose value 
        //           for “entryRelationship / @typeCode” SHALL be “RSON” 
        //           “Has reason”  2.16.840.1.113883.5.1002 ActRelationshipType STATIC,
        //           where the target of the relationship represents the indication 
        //           for the activity.
        indication(schema:'ccd_indication')
        // CONF-330: A medication activity MAY contain one or more patient instructions. 
        patientInstruction(schema:'ccd_patientInstruction')
        seriesNumber()
        // CONF-348: A medication activity MAY contain one or more reaction observations 
        //          (templateId 2.16.840.1.113883.10.20.1.54), each of which MAY contain 
        //          exactly one severity observation (templateId 2.16.840.1.113883.10.20.1.55) 
        //          AND/OR one or more reaction interventions.
        //          see CCDReactionObservationBuilder
        reactionObservation(schema:'ccd_reactionObservation')
        // CONF-350: A medication activity MAY contain exactly one medication
        //           status observation.       
        medicationStatus(schema:'ccd_medicationStatus')
        consumable(schema:'ccd_consumable', req:true)
        productInstance(schema:'ccd_productInstance')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.24')
            }
        })
        // CONF-306: A medication activity SHALL contain at least one 
        //           SubstanceAdministration / id
        ids(min:1)
    }
}

// CONF-329: SubstanceAdministration / entryRelationship / @typeCode=”RSON” 
//           in a medication activity SHALL have a target of problem act 
//           (templateId 2.16.840.1.113883.10.20.1.27), problem observation 
//           (templateId 2.16.840.1.113883.10.20.1.28), or some other clinical 
//           statement.
// TODO: one of
ccd_indication(schema:'entryRelationship') {
    properties {
        problemObservation(schema:'ccd_problemObservation')
        problemAct(schema:'ccd_problemAct')
    }
}

//CONF-354: A medication activity SHALL contain exactly one SubstanceAdministration
//          consumable, the target of which is a product template.
ccd_consumable(schema:'consumable') {
    properties {
        manufacturedProduct(schema:'ccd_manufacturedProduct', req:true)
    }
}