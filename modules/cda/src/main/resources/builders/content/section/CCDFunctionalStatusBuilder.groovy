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
 * Chapter 3.4 : Functional Status
 * 
 * Template Definitions:
 *      Functional Status Section (2.16.840.1.113883.10.20.1.5)
 *      
 * Dependencies:
 *      Problem Act (ccd_problemAct)
 *      Result Organizer (ccd_resultOrganizer)
 */
 
// CONF-123: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Functional status section (templateId 2.16.840.1.113883.10.20.1.5). 
//           The Functional status section SHALL contain a narrative block, and SHOULD 
//           contain clinical statements. Clinical statements SHOULD include one or more 
//           problem acts (templateId 2.16.840.1.113883.10.20.1.27) and/or 
//           result organizers (templateId 2.16.840.1.113883.10.20.1.32).
ccd_functionalStatus(schema:'ccd_section') {
    properties {
        // CONF-124: The functional status section SHALL contain Section / code.
        // CONF-125: The value for “Section / code” SHALL be “47420-5” “Functional status assessment”
        //           2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
            getMetaBuilder().build {
                loincCode(code:'47420-5',
                        displayName:'Functional status assessment')
            }
        })
        // CONF-126: The functional status section SHALL contain Section / title.
        // CONF-127: Section / title SHOULD be valued with a case-insensitive 
        //           language-insensitive text string containing “functional status”.
        title(req:true, def: {
            getMetaBuilder().build {
                st('Functional Status')
            }
        })
        text(schema:'strucDocText', req:true)
        problemAct(schema:'ccd_problemAct')
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.5')
            }
        })

    }
}


