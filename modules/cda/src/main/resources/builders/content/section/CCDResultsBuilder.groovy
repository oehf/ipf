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

// Chapter 3.13: Results
// Depends on  : ccd_resultOrganizer

// CONF-388: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Results section (templateId 2.16.840.1.113883.10.20.1.14). 
//           The Results section SHALL contain a narrative block, and SHOULD contain 
//           clinical statements. Clinical statements SHOULD include one or more result
//           organizers (templateId 2.16.840.1.113883.10.20.1.32), each of which SHALL contain
//           one or more result observations (templateId 2.16.840.1.113883.10.20.1.31).
ccd_results(schema:'ccd_section'){
    properties{
        // CONF-389: The result section SHALL contain Section / code.
        // CONF-390: The value for “Section / code” SHALL be “30954-2” “Relevant diagnostic 
        //           tests and/or laboratory data” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'30954-2',
                       displayName:'Relevant diagnostic tests and/or laboratory data')
           }
       })
       // CONF-391: The results section SHALL contain Section / title.
       // CONF-392: Section / title SHOULD be valued with a case-insensitive language-insensitive
       //           text string containing “results”.
       title(schema:'st', req: true, def: {
           getMetaBuilder().build {
               st('Labor or diagnostic results')
           }
       })
	   text(schema:'strucDocText', req:true)              
       resultOrganizer(schema:'ccd_resultOrganizer')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.14')
            }
          })
    }
    
}