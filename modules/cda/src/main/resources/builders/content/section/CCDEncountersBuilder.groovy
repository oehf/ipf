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

// Chapter 3.15: Encounters (2.16.840.1.113883.10.20.1.3)
// Depends on  : ccd_encounterActivity

// CONF-453: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Encounters section (templateId 2.16.840.1.113883.10.20.1.3). 
//           The Encounters section SHALL contain a narrative block, and SHOULD 
//           contain clinical statements. Clinical statements SHOULD include one 
//           or more encounter activities (templateId 2.16.840.1.113883.10.20.1.21).
ccd_encounters(schema:'ccd_section'){
    properties{
        // CONF-454: The encounters section SHALL contain Section / code.
        // CONF-455: The value for “Section / code” SHALL be “46240-8” 
        //           “History of encounters” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'46240-8', displayName:'History of encounters')
           }
       })
       // CONF-456: The encounters section SHALL contain Section / title.
       // CONF-457: Section / title SHOULD be valued with a case-insensitive language-insensitive
       //           text string containing “encounters”.
       title(schema:'st', req:true, def: {
           getMetaBuilder().build {
               st('Encounters')
           }
       })
	   text(schema:'strucDocText', req:true)
       encounterActivity(schema:'ccd_encounterActivity')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.3')
            }
        })
    }    
}