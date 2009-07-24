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

// CONF-2: A CCD SHALL contain exactly one ClinicalDocument / documentationOf / serviceEvent.
// CONF-3: The value for “ClinicalDocument / documentationOf / serviceEvent / @classCode”
//         SHALL be “PCPR” “Care provision” 2.16.840.1.113883.5.6 ActClass STATIC.
// CONF-4: ClinicalDocument / documentationOf / serviceEvent SHALL contain exactly 
//         one serviceEvent / effectiveTime / low and
//         exactly one serviveEvent / effectiveTime / high.
ccd_serviceEvent(schema:'serviceEvent'){
    properties {
        classCode(def: ActClassRootMember7.PCPR_LITERAL)
        effectiveTime(schema:'ivlts', req:true)
    }
}
