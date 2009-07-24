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

//CONF-508: A status observation (templateId 2.16.840.1.113883.10.20.1.57) SHALL be
//represented with Observation.
//CONF-509: A status observation SHALL be the target of an entryRelationship whose value for
//“entryRelationship / @typeCode” SHALL be “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
//CONF-510: The value for “Observation / @classCode” in a status observation SHALL be
//“OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
ccd_statusObservation(schema:'observation'){
  properties{
      // CONF-511: The value for “Observation / @moodCode” in a status observation SHALL be
      //           “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.      
      moodCode(def:XActMoodDocumentObservation.EVN_LITERAL)
      // CONF-512: A status observation SHALL contain exactly one Observation / code.
      // CONF-513: The value for “Observation / code” in a status observation SHALL be
      //           “33999-4” “Status” 2.16.840.1.113883.6.1 LOINC STATIC.
      code(def: {
          getMetaBuilder().build{
              loincCode(code:'33999-4',
                      displayName:'Status')
          }
      })
      // CONF-515: The value for “Observation / statusCode” in a status observation SHALL be
      //           “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
      statusCode(req:true, def: {
          getMetaBuilder().build {
              cs('completed')
          }
      })
  }
  collections{
      // CONF-516: A status observation SHALL contain exactly one Observation / value,
      //           which SHALL be of datatype “CE”.
      values(collection:'value', min:1, max:1){
          value(schema:'ce')
      }
      templateIds(collection:'templateId', def: {
          getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.57')
          }
      })
  }
}