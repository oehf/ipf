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

// Chapter 3.12: Vital Signs
// Depends on  : ccd_resultOrganizer, ccd_informationSource

// CONF-381: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Vital signs section (templateId 2.16.840.1.113883.10.20.1.16). 
//           The Vital signs section SHALL contain a narrative block, and SHOULD 
//           contain clinical statements. Clinical statements SHOULD include one 
//           or more vital signs organizers (templateId 2.16.840.1.113883.10.20.1.35), 
//           each of which SHALL contain one or more result observations 
//           (templateId 2.16.840.1.113883.10.20.1.31).
ccd_vitalSigns(schema:'ccd_section'){
    properties{
        // CONF-382: The vital signs section SHALL contain Section / code.
        // CONF-383: The value for “Section / code” SHALL be “8716-3” “Vital signs” 
        //           2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'8716-3', displayName:'Vital signs')
           }
       })
       // CONF-384: The vital signs section SHALL contain Section / title.
       // CONF-385: Section / title SHOULD be valued with a case-insensitive language-insensitive
       //           text string containing “vital signs”.
       title(check: { it.text =~ /(?i)vital signs/ }, def: {
           getMetaBuilder().build {
               st('Vital signs')
           }
       })
	   text(schema:'strucDocText', req:true)       
       vitalSignsOrganizer(schema:'ccd_vitalSignsOrganizer')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.16')
            }
          })
    }    
}


// CONF-386: A vital signs organizer (templateId 2.16.840.1.113883.10.20.1.35) SHALL be a 
//           conformant results organizer (templateId 2.16.840.1.113883.10.20.1.32).
// CONF-387: A vital signs organizer SHALL contain one or more sources of information, 
//           as defined in section 5.2 Source. Defined by base schema
ccd_vitalSignsOrganizer(schema:'ccd_resultOrganizer'){
    properties {
        statusCode(schema:'cs', def:{
            getMetaBuilder().build {
                cs('completed')
            }
        })
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.35')
            }
        })
        

    }
}

// end of Vital Signs