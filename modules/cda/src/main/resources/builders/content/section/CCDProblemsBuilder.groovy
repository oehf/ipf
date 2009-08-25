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

// Chapter 3.5: Problems

// CONF-140: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Problem section (templateId 2.16.840.1.113883.10.20.1.11). 
//           The Problem section SHALL contain a narrative block, and SHOULD contain 
//           clinical statements. Clinical statements SHOULD include one or more 
//           problem acts (templateId 2.16.840.1.113883.10.20.1.27). A problem act 
//           SHOULD include one or more problem observations 
//           (templateId 2.16.840.1.113883.10.20.1.28).
ccd_problems(schema:'ccd_section'){
    properties{
        //CONF-141: The problem section SHALL contain Section / code.
        //CONF-142: The value for “Section / code” SHALL be “11450-4” “Problem list”
        //           2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'11450-4',
                       displayName:'Problem list')
           }
       })
       // CONF-143: The problem section SHALL contain Section / title.
       // CONF-144: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “problems”.
       title(check: { it.text =~ /(?i)problems/ }, def: {
           getMetaBuilder().build {
               st('Problems')
           }
       })
       text(schema:'strucDocText', req:true)       
       problemAct(schema:'ccd_problemAct')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.11')
            }
          })
    }
    
}
