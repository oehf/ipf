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
 * Chapter 4.3: Comments (2.16.840.1.113883.10.20.1.40)
 *
 */

// CONF-502: A CCD section MAY contain one or more comments, either as a clinical statement 
//           or nested under another clinical statement.
// CONF-503: A comment (templateId 2.16.840.1.113883.10.20.1.40) SHALL be represented with Act.
// CONF-504: The value for “Act / @classCode” in a comment SHALL be 
//           “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
ccd_comment(schema:'act'){
  properties{
      // CONF-505: The value for “Act / @moodCode” in a comment SHALL be “EVN” 
      //           2.16.840.1.113883.5.1001 ActMood STATIC.
      moodCode(def:XDocumentActMood.EVN_LITERAL)
      // CONF-506: A comment SHALL contain exactly one Act / code.
      // CONF-507: The value for “Act / code” in a comment SHALL be 48767-8 “Annotation comment”
      //           2.16.840.1.113883.6.1 LOINC STATIC.
      code(schema:'loincCode', req:true, def: {
          getMetaBuilder().build {
              loincCode(code:'48767-8',
                      displayName:'Annotation comment')
          }
      })
      statusCode(schema:'cs', def: {
          getMetaBuilder().build {
              cs(code:'completed')
          }
      })
  }
  collections{
      templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList { 
                ii(root:'2.16.840.1.113883.10.20.1.40') 
            }
        })
      
      
  }
}