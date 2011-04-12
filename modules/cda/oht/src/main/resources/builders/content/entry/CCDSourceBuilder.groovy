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
 * Chapter 5.2: Source
 * 
 *  Template Definitions
 *      Information Source
 *      Reference Source
 */


// CONF-522: A reference source of information SHALL be represented with 
//           reference [@typeCode = “XCRPT”].
ccd_referenceSource(schema:'reference'){
    properties{
        typeCode(def:XActRelationshipExternalReference.XCRPT_LITERAL)
    }
}

// CONF-523: Any other source of information SHALL be represented with a source of information observation.
// CONF-524: A source of information observation SHALL be the target of an 
//           entryRelationship whose value for “entryRelationship / @typeCode” SHALL be
//           “REFR” “Refers to” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
// CONF-525: A source of information observation SHALL be represented with Observation.
// CONF-526: The value for “Observation / @classCode” in a source of information observation 
//           SHALL be “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
// CONF-533: The absence of a known source of information SHALL be explicity asserted by 
//           valuing Observation / value in a source of information observation with the 
//           text string “Unknown”.
ccd_informationSource(schema:'observation'){
  properties{
      // CONF-527: The value for “Observation / @moodCode” in a source of information observation 
      //           SHALL be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
      moodCode(def:XActMoodDocumentObservation.EVN_LITERAL)
      // CONF-528: A source of information observation SHALL contain exactly one Observation / statusCode.
      // CONF-529: The value for “Observation / statusCode” in a source of information observation 
      //           SHALL be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
      statusCode(schema:'cs', req:true, def: {
          getMetaBuilder().build {
              cs('completed')
          }
      })
      // CONF-530: A source of information observation SHALL contain exactly one Observation / code.
      // CONF-531: The value for “Observation / code” in a source of information observation 
      //           SHALL be “48766-0” “Information source” 2.16.840.1.113883.6.1 LOINC STATIC.      
      code(schema:'loincCode', req:true, def: {
          getMetaBuilder().build{
              loincCode(code:'48766-0',
                      displayName:'Information source')
          }
      })

  }
  collections{
      // CONF-532: A source of information observation SHALL contain exactly one Observation / value.
      values(min:1, max:1)
  }
}