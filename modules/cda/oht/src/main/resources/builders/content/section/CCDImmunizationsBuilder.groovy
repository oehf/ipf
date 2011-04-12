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
 * 
 * Chapter 3.10: Immunizations
 * 
 * Template Definitions:
 *      Immunizations Section (2.16.840.1.113883.10.20.1.6)
 * 
 * Dependencies:
 *      Medication Activity(ccd_medicationActivity)
 *      Supply Activity (ccd_supplyActivity)
 *      Product Instance(ccd_productInstance)
 *      Status Observation (ccd_statusOsbervation)
 */


// CONF-376: CCD SHOULD contain exactly one and SHALL NOT contain more than one Immunizations 
//           section (templateId 2.16.840.1.113883.10.20.1.6). The Immunizations section SHALL 
//           contain a narrative block, and SHOULD contain clinical statements. 
//           Clinical statements SHOULD include one or more medication activities 
//           (templateId 2.16.840.1.113883.10.20.1.24) and/or supply activities 
//           (templateId 2.16.840.1.113883.10.20.1.34).ccd_immunization(schema:'section')
ccd_immunizations(schema:'section'){
    properties{
        //CONF-377: The alert section SHALL contain Section / code.
        //CONF-378: The value for “Section / code” SHALL be “11369-6” 
        //          “History of immunizations” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'11369-6',
                       displayName:'History of immunizations')
           }
       })
       // CONF-379: The alert section SHALL contain Section / title.
       // CONF-380: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “immunization”.
       title(schema:'st', check: { it.text =~ /(?i)immunization/ }, def: {
           getMetaBuilder().build {
               st('Immunization')
           }
       })
       text(schema:'strucDocText', req:true)
       medicationActivity(schema:'ccd_medicationActivity')
       supplyActivity(schema:'ccd_supplyActivity')
       productInstance(schema:'ccd_productInstance')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.6')
            }
          })
    }
    
}
