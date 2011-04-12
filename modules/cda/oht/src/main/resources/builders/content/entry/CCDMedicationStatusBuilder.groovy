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
package builders.content.entry

import org.openhealthtools.ihe.common.cdar2.*

/**
 * Chapter 3.9.2.3: Representation of “status” values
 * 
 * Template Definitions:
 *      Medication Status Observation (2.16.840.1.113883.10.20.1.47)
 *      
 * Dependencies:
 *      Status Observation (ccd_statusObservation)
 */

// CONF-352: A medication status observation (templateId 2.16.840.1.113883.10.20.1.47)
//           SHALL be a conformant status observation(templateId 2.16.840.1.113883.10.20.1.57) 
//           (as defined in section 5.1 “Type” and “Status” values).
ccd_medicationStatus(schema:'ccd_statusObservation') {
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.47')
            }
        })
        values(collection:'value', min:1, max:1) {
            value(schema:'snomedCode')
        }        
    }
}
 
//End Medication Status Observation

