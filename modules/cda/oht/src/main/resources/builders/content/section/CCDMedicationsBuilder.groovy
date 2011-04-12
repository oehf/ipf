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

/**
 * Chapter 3.9: Medications
 * 
 * Template Definitions:
 *      Medications Section (2.16.840.1.113883.10.20.1.8)
 *      
 * Dependencies:
 *      Medication Activity (ccd_medicationActivity)
 *      Supply Activity (ccd_supplyActivity)
 *      Comments (ccd_section)
 */

// CONF-298: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Medications section (templateId 2.16.840.1.113883.10.20.1.8). 
//           The Medications section SHALL contain a narrative block, and SHOULD 
//           contain clinical statements. Clinical statements SHOULD include one 
//           or more medication activities (templateId 2.16.840.1.113883.10.20.1.24) 
//           and/or supply activities (templateId 2.16.840.1.113883.10.20.1.34).
ccd_medications(schema:'ccd_section'){
    properties{
        //CONF-300: The alert section SHALL contain Section / code.
        //CONF-301: The value for “Section / code” SHALL be “10160-0” 
        //          “History of medication use” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'10160-0',
                       displayName:'History of medication use')
           }
       })
       text(schema:'strucDocText', req:true)
       // CONF-302: The alert section SHALL contain Section / title.
       // CONF-303: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “medication”
       title(check: { it.text =~ /(?i)medication/ }, def: {
           getMetaBuilder().build {
               st('Medication')
           }
       })
       medicationActivity(schema:'ccd_medicationActivity')
       supplyActivity(schema:'ccd_supplyActivity')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.8')
            }
        })
    }   
}

