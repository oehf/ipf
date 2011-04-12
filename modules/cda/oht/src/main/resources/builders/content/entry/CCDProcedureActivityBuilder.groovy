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

// Chapter 3.14.2.1: Procedure activity (2.16.840.1.113883.10.20.1.29)
// Depends on      : ccd_encounterLocationParticipantRole, ccd_problemAct, ccd_problemObservation
//                   ccd_patientInstruction, ccd_medicationActivity, ccd_productInstance

// CONF-427: A procedure activity (templateId 2.16.840.1.113883.10.20.1.29) 
//           SHALL be represented with Act, Observation, or Procedure.
// TODO: One of
ccd_procedureActivity(schema:'ccd_entry') {
   properties {
       procedureActivityAct(schema:'ccd_procedureActivityAct')
       procedureActivityObservation(schema:'ccd_procedureActivityObservation')
       procedureActivityProcedure(schema:'ccd_procedureActivityProcedure')     
   }
}

ccd_procedureActivityAct(schema:'ccd_act'){
    properties{
        // CONF-428: The value for “[Act | Observation | Procedure] / @moodCode” 
        //           in a procedure activity SHALL be “EVN” 2.16.840.1.113883.5.1001 
        //           ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
                def: XDocumentActMood.EVN_LITERAL)
        // CONF-430: A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / 
        //           statusCode.
        statusCode(schema:'cs', req:true)
        // CONF-431: TODO statusCode value set
        // CONF-433: A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / code.
        code(schema:'cd', req:true)
        // CONF-437: A procedure activity MAY contain one or more location participations
        //           (templateId 2.16.840.1.113883.10.20.1.45) (see section 3.15.2.2 Encounter location), 
        //           to represent where the procedure was performed.
        encounterLocation(schema:'ccd_encounterLocationParticipantRole')
        // CONF-440: [Act | Observation | Procedure] / entryRelationship / @typeCode=”RSON” in a procedure activity 
        //           SHALL have a target of problem act (templateId 2.16.840.1.113883.10.20.1.27),
        //           problem observation (templateId 2.16.840.1.113883.10.20.1.28), 
        //           or some other clinical statement.
        problemActReason(schema:'ccd_problemAct')
        problemObservationReason(schema:'ccd_problemObservation')
        // CONF-441: A procedure activity MAY contain one or more patient instructions 
        //           (templateId 2.16.840.1.113883.10.20.1.49) (see section 3.9.2.2.2 Patient instructions), 
        //            to represent any additional information provided to a patient related to the procedure.
        patientInstruction(schema:'ccd_patientInstruction')
        // CONF-445: The value for “[Act | Observation | Procedure] / entryRelationship / @typeCode” 
        //           in a procedure activity MAY be “SUBJ” “Subject” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC
        //           to reference an age observation (templateId 2.16.840.1.113883.10.20.1.38).11
        age(schema:'ccd_ageObservation')
        // CONF-446: A procedure activity MAY have one or more [Act | Observation | Procedure] / 
        //           entryRelationship [@typeCode=”COMP”], the target of which is a medication activity
        //           (templateId 2.16.840.1.113883.10.20.1.24) (see section 3.9.2.1.1 Medication activity), 
        //           to describe substances administered during the procedure.
        medicationActivity(schema:'ccd_medicationActivity')
        // CONF-447: A procedure activity SHALL contain one or more sources of information, 
        //           as defined in section 5.2 Source. Defined by base schema.
        // CONF-448: A procedure activity MAY have one or more [Act | Observation | Procedure] / 
        //           participant [@typeCode=”DEV”], the target of which is a product instance template.
        productInstance(schema:'ccd_productInstance')
    }
    collections{
        // CONF-429: A procedure activity SHALL contain at least one [Act | Observation | Procedure] / id.
        ids(min:1)
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.29')
            }
        })
    }
}

ccd_procedureActivityObservation(schema:'ccd_observation'){
    properties{
        // CONF-428: The value for “[Act | Observation | Procedure] / @moodCode” 
        //           in a procedure activity SHALL be “EVN” 2.16.840.1.113883.5.1001 
        //           ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
                def: XActMoodDocumentObservation.EVN_LITERAL)
        // CONF-430: A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / 
        //           statusCode.
        statusCode(schema:'cs', req:true)
        // CONF-433: A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / code.
        code(schema:'cd', req:true)
        // CONF-437: A procedure activity MAY contain one or more location participations
        //           (templateId 2.16.840.1.113883.10.20.1.45) (see section 3.15.2.2 Encounter location), 
        //           to represent where the procedure was performed.
        encounterLocation(schema:'ccd_encounterLocationParticipantRole')
        // CONF-440: [Act | Observation | Procedure] / entryRelationship / @typeCode=”RSON” in a procedure activity 
        //           SHALL have a target of problem act (templateId 2.16.840.1.113883.10.20.1.27),
        //           problem observation (templateId 2.16.840.1.113883.10.20.1.28), 
        //           or some other clinical statement.
        problemActReason(schema:'ccd_problemAct')
        problemObservationReason(schema:'ccd_problemObservation')
        // CONF-441: A procedure activity MAY contain one or more patient instructions 
        //           (templateId 2.16.840.1.113883.10.20.1.49) (see section 3.9.2.2.2 Patient instructions), 
        //            to represent any additional information provided to a patient related to the procedure.
        patientInstruction(schema:'ccd_patientInstraction')
        // CONF-445: The value for “[Act | Observation | Procedure] / entryRelationship / @typeCode” 
        //           in a procedure activity MAY be “SUBJ” “Subject” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC
        //           to reference an age observation (templateId 2.16.840.1.113883.10.20.1.38).11
        age(schema:'ccd_ageObservation')
        // CONF-446: A procedure activity MAY have one or more [Act | Observation | Procedure] / 
        //           entryRelationship [@typeCode=”COMP”], the target of which is a medication activity
        //           (templateId 2.16.840.1.113883.10.20.1.24) (see section 3.9.2.1.1 Medication activity), 
        //           to describe substances administered during the procedure.
        medicationActivity(schema:'ccd_medicationActivity')
        // CONF-447: A procedure activity SHALL contain one or more sources of information, 
        //           as defined in section 5.2 Source. Defined by base schema.
        // CONF-448: A procedure activity MAY have one or more [Act | Observation | Procedure] / 
        //           participant [@typeCode=”DEV”], the target of which is a product instance template.
        productInstance(schema:'ccd_productInstance')
    }
    collections{
        // CONF-429: A procedure activity SHALL contain at least one [Act | Observation | Procedure] / id.
        ids(min:1)
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.29')
            }
        })
    }
}

ccd_procedureActivityProcedure(schema:'ccd_procedure'){
    properties{
        // CONF-428: The value for “[Act | Observation | Procedure] / @moodCode” 
        //           in a procedure activity SHALL be “EVN” 2.16.840.1.113883.5.1001 
        //           ActMood STATIC.
        moodCode(factory:'XDOCUMENT_PROCEDURE_MOOD', 
                def: XDocumentProcedureMood.EVN_LITERAL)
        // CONF-430: A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / 
        //           statusCode.
        statusCode(schema:'cs', req:true)
        // CONF-433: A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / code.
        code(schema:'cd', req:true)
        // CONF-437: A procedure activity MAY contain one or more location participations
        //           (templateId 2.16.840.1.113883.10.20.1.45) (see section 3.15.2.2 Encounter location), 
        //           to represent where the procedure was performed.
        encounterLocation(schema:'ccd_encounterLocationParticipantRole')
        // CONF-440: [Act | Observation | Procedure] / entryRelationship / @typeCode=”RSON” in a procedure activity 
        //           SHALL have a target of problem act (templateId 2.16.840.1.113883.10.20.1.27),
        //           problem observation (templateId 2.16.840.1.113883.10.20.1.28), 
        //           or some other clinical statement.
        problemActReason(schema:'ccd_problemAct')
        problemObservationReason(schema:'ccd_problemObservation')       
        // CONF-441: A procedure activity MAY contain one or more patient instructions 
        //           (templateId 2.16.840.1.113883.10.20.1.49) (see section 3.9.2.2.2 Patient instructions), 
        //            to represent any additional information provided to a patient related to the procedure.
        patientInstruction(schema:'ccd_patientInstruction')
        // CONF-445: The value for “[Act | Observation | Procedure] / entryRelationship / @typeCode” 
        //           in a procedure activity MAY be “SUBJ” “Subject” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC
        //           to reference an age observation (templateId 2.16.840.1.113883.10.20.1.38).11
        age(schema:'ccd_ageObservation')
        // CONF-446: A procedure activity MAY have one or more [Act | Observation | Procedure] / 
        //           entryRelationship [@typeCode=”COMP”], the target of which is a medication activity
        //           (templateId 2.16.840.1.113883.10.20.1.24) (see section 3.9.2.1.1 Medication activity), 
        //           to describe substances administered during the procedure.
        medicationActivity(schema:'ccd_medicationActivity')
        // CONF-447: A procedure activity SHALL contain one or more sources of information, 
        //           as defined in section 5.2 Source. Defined by base schema.
        // CONF-448: A procedure activity MAY have one or more [Act | Observation | Procedure] / 
        //           participant [@typeCode=”DEV”], the target of which is a product instance template.
        productInstance(schema:'ccd_productInstance')
    }
    collections{
        // CONF-429: A procedure activity SHALL contain at least one [Act | Observation | Procedure] / id.
        ids(min:1)
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.29')
            }
        })
    }
}