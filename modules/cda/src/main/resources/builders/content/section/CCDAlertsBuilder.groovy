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
 * Chapter 3.8: Alerts
 * 
 *  Template Definitions:
 *      Alert Section (2.16.840.1.113883.10.20.1.2)
 *      
 *  Dependecies:
 *      Problem Act (ccd_problemAct)
 *      Comments
 */

// This builder depends on the presence of the CCDProblemActBuilder
// This builder depends on the presence of the CCDReactionObservationBuilder

// CONF-256: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Alerts section (templateId 2.16.840.1.113883.10.20.1.2). The Alerts 
//           section SHALL contain a narrative block, and SHOULD contain clinical 
//           statements. Clinical statements SHOULD include one or more 
//           problem acts (templateId 2.16.840.1.113883.10.20.1.27). 
//           A problem act SHOULD include one or more 
//           alert observations (templateId 2.16.840.1.113883.10.20.1.18).
ccd_alerts(schema:'ccd_section'){
    properties{
        //CONF-258: The alert section SHALL contain Section / code.
        //CONF-259: The value for “Section / code” SHALL be “48765-2” “Allergies, 
        //          adverse reactions, alerts” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'48765-2',
                       displayName:'Allergies, adverse reactions, alerts')
           }
       })
       // CONF-260: The alert section SHALL contain Section / title.
       // CONF-261: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “alert” and/or 
       //           “allergies and adverse reactions”.
       title(schema:'st', check: { it.text =~ /(?i)alerts|allergies and adverse reactions/ }, def: {
           getMetaBuilder().build {
               st('Allergies and  adverse reactions, alerts')
           }
       })
       text(schema:'strucDocText', req:true)
       problemAct(schema:'ccd_problemAct')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.2')
            }
          })
    }
    
}