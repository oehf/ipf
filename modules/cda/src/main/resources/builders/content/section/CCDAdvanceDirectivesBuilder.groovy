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
 * Chapter 3.2 : Advance Directives
 * 
 * Template Definitions:
 *      Advance Directive Section (2.16.840.1.113883.10.20.1.1)
 *      
 * Dependencies:
 *      Advance directive observations (2.16.840.1.113883.10.20.1.17)
 */
 
// CONF-77: CCD SHOULD contain exactly one and SHALL NOT contain more than one
//             Advance directives section (templateId 2.16.840.1.113883.10.20.1.1). 
//             The Advance directives section SHALL contain a narrative block,
//             and SHOULD contain clinical statements. 
//             Clinical statements SHOULD include one or more advance directive 
//             observations (templateId 2.16.840.1.113883.10.20.1.17). 
//             An advance directive observation MAY contain exactly one advance directive 
//             reference (templateId 2.16.840.1.113883.10.20.1.36) to an external 
//             advance directive document.
ccd_advanceDirectives(schema:'ccd_section') {
    properties {
        //CONF-78: The advance directive section SHALL contain Section / code.
        //CONF-79: The value for “Section / code” SHALL be “42348-3”
        //         “Advance directives” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
            getMetaBuilder().build {
                loincCode(code:'42348-3',
                        displayName:'Advance directives')
            }
        })

        // CONF-80: The advance directive section SHALL contain Section / title.
        // CONF-81: Section / title SHOULD be valued with a case-insensitive 
        //          language-insensitive text string containing “advance directives”.
        title(req:true, def: {
            getMetaBuilder().build {
                st('Advance Directives')
            }
        })
        text(schema:'strucDocText', req:true)
        advanceDirectiveObservation(schema:'ccd_advanceDirectiveObservation')
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.1')
            }
        })

    }
}


