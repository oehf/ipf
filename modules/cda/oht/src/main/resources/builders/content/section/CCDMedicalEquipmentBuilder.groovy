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

// Chapter 3.10: Medical Equipment
// This builder depends on the presence of the CCDMedicationBuilder.
// Transitively:
// This builder depends on the presence of the CCDReactionInterventionBuilder
// This builder depends on the presence of the CCDStatusObservationBuilder
// This builder depends on the presence of the CCDProblemsAct
// This builder depends on the presence of the CCDProblemsBuilder until refactoring


// CONF-371: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Medical Equipment section (templateId 2.16.840.1.113883.10.20.1.7). 
//           The Medical Equipment section SHALL contain a narrative block, and 
//           SHOULD contain clinical statements. Clinical statements SHOULD include 
//           one or more supply activities (templateId 2.16.840.1.113883.10.20.1.34) 
//           and MAY include one or more medication activities 
//           (templateId 2.16.840.1.113883.10.20.1.24).
ccd_medicalEquipment(schema:'ccd_section'){
    properties{
        //CONF-372: The alert section SHALL contain Section / code.
        //CONF-373: The value for “Section / code” SHALL be “46264-8” 
        //          “History of medical device use” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'46264-8',
                       displayName:'History of medical device use')
           }
       })
       // CONF-374: The alert section SHALL contain Section / title.
       // CONF-375: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “equipment”.
       title(schema:'st', check: { it.text =~ /(?i)equipment/ }, def: {
           getMetaBuilder().build {
               st('Equipment')
           }
       })
       text(schema:'strucDocText', req:true)       
       medicationActivity(schema:'ccd_medicationActivity')
       supplyActivity(schema:'ccd_supplyActivity')
//       productInstance(schema:'ccd_productInstance')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.7')
            }
          })
    }
    
}


