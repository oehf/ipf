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
 * Chapter 3.5.2.1.2: Problem observation
 * 
 * Template Definitions:
 *      Problem Observation (2.16.840.1.113883.10.20.1.28)
 *      Problem Status Observation (2.16.840.1.113883.10.20.1.50)
 *      Problem Healthstatus Observation (2.16.840.1.113883.10.20.1.51)
 * Dependencies:
 *      Age Observation (ccd_ageObservation)
 *      Functional Status Status Observation (ccd_functionalStatusStatusObservation)
 *      Source (ccd_observation)
 *      Status Observation (ccd_statusObservation)
 */

//CONF-154: A problem observation (templateId 2.16.840.1.113883.10.20.1.28) SHALL 
//be represented with Observation.
ccd_problemObservation(schema:'ccd_observation'){
  properties{
      //CONF-155: The value for “Observation / @moodCode” in a problem observation SHALL be 
      //          “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
      moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
      def: XActMoodDocumentObservation.EVN_LITERAL)
      // CONF-156: A problem observation SHALL include exactly one Observation / statusCode.
      // CONF-157: The value for “Observation / statusCode” in a problem observation
      //           SHALL be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.        
      statusCode(schema:'cs', req:true, def: {
          getMetaBuilder().build { cs('completed') }
      })
      // CONF-160: The value for “Observation / entryRelationship / @typeCode” in a 
      //           problem observation MAY be “SUBJ” “Subject” 2.16.840.1.113883.5.1002 
      //           ActRelationshipType STATIC to reference an age observation 
      //           (templateId 2.16.840.1.113883.10.20.1.38)
      ageObservation(schema:'ccd_ageObservation')
      // CONF-162: A problem observation MAY contain exactly one problem status observation.
      problemStatus(schema:'ccd_problemObservationStatus')
      // CONF-165: A problem observation MAY contain exactly one problem healthstatus observation.
      problemHealthstatus(schema:'ccd_problemObservationHealthstatus')
      // CONF-136: A problem observation in the functional status section SHALL 
      //           contain exactly one status of functional status observation.
      functionalStatusStatus(schema:'ccd_functionalStatusStatusObservation')
      // CONF-180: A problem observation MAY contain exactly one patient awareness.
      patientAwareness(schema:'ccd_patientAwareness')
  }
  collections{
      templateIds(collection:'templateId', def: {
          getMetaBuilder().buildList { 
              ii(root:'2.16.840.1.113883.10.20.1.28') 
          }
      })
  }
}

//CONF-163: A problem status observation (templateId 2.16.840.1.113883.10.20.1.50) SHALL be
//          a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) 
//          (as defined in section 5.1 “Type” and “Status” values).
//CONF-164: The value for “Observation / value” in a problem status observation SHALL be selected
//          from ValueSet 2.16.840.1.113883.1.11.20.13 ProblemStatusCode STATIC 20061017.
ccd_problemObservationStatus(schema:'ccd_statusObservation'){
  collections{
      templateIds(collection:'templateId', def: {
          getMetaBuilder().buildList { 
              ii(root:'2.16.840.1.113883.10.20.1.50') 
          }
      })
      values(collection:'value', min:1, max:1) {
          value(schema:'snomedCode')
      }      
  }
}

//CONF-166: A problem healthstatus observation (templateId 2.16.840.1.113883.10.20.1.51) SHALL be
//          a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) 
//         (as defined in section 5.1 “Type” and “Status” values), except that the value for 
//         “Observation / code” in a problem healthstatus observation SHALL be “11323-3” 
//         “Health status” 2.16.840.1.113883.6.1 LOINC STATIC.
//CONF-167: The value for “Observation / value” in a problem healthstatus observation SHALL be selected
//          from ValueSet 2.16.840.1.113883.1.11.20.12 ProblemHealthStatusCode STATIC 20061017.
ccd_problemObservationHealthstatus(schema:'ccd_statusObservation'){
  properties{
      code(schema:'loincCoide', def: {
          getMetaBuilder().build{
              loincCode(code:'11323-3', displayName:'Health status')
          }
      })
  }
  collections{
      templateIds(collection:'templateId', def: {
          getMetaBuilder().buildList { 
              ii(root:'2.16.840.1.113883.10.20.1.51') 
          }
      })
      values(collection:'value', min:1, max:1) {
          value(schema:'snomedCode')
      }       
  }
}

// end of Problem Observation