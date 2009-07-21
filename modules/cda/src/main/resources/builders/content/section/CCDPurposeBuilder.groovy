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

import org.openhealthtools.ihe.common.cdar2.XActRelationshipEntryRelationshipimport org.openhealthtools.ihe.common.cdar2.XDocumentActMood
// Chapter 2.8 : Purpose

// CONF-15: CCD MAY contain exactly one and SHALL NOT contain more than
// one Purpose section (templateId 2.16.840.1.113883.10.20.1.13).
// The Purpose section SHALL contain a narrative block, and SHOULD contain
// clinical statements. Clinical statements SHOULD include one or more purpose activities
// (templateId 2.16.840.1.113883.10.20.1.30).

ccd_purpose(schema:'section') {
	properties {
	    // CONF-16: The purpose section SHALL contain Section / code.
	    // CONF-17: The value for “Section / code” SHALL be “48764-5”
	    // “Summary purpose” 2.16.840.1.113883.6.1 LOINC STATIC.
         code(schema:'loincCode', def: {
             getMetaBuilder().build {
                 loincCode(code:'48764-5',
		                displayName:'Summary purpose')
             }
         })
		
       // CONF-18: The purpose section SHALL contain Section / title.
       // CONF-19: (NOT ENFORCED) Section / title SHOULD be valued with a
       // case-insensitive language-insensitive text string containing “purpose”.
        title(check: { it.text =~ /(?i)purpose/ }, def: {
	        getMetaBuilder().build {
	            st('Summary purpose')
	        }
	    })
	    text(schema:'strucDocText', req:true)
	    purposeActivity(schema:'ccd_purposeActivity')
	}
	collections {
	   templateIds(collection:'templateId', def: {
	     getMetaBuilder().buildList {
	       ii(root:'2.16.840.1.113883.10.20.1.13') // CONF-15
	     }
	   })
	}
}



// CONF-20 - CONF-25: Implemented by MetaClass extension
// CONF-26: A purpose activity SHALL contain exactly one Act / entryRelationship /
// @typeCode, with a value of “RSON” “Has reason” 2.16.840.1.113883.5.1002
// ActRelationshipType STATIC, to indicate the reason or purpose for creating the CCD.

ccd_purposeActivity(schema:'entryRelationship') {
 properties {
     typeCode(factory:'XACT_RELATIONSHIP_ENTRY_RELATIONSHIP',
              def: XActRelationshipEntryRelationship.RSON_LITERAL)
 }
}
