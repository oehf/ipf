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
package ccdbuilders

import groovytools.builder.*
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.XDocumentActMood
import org.openhealthtools.ihe.common.cdar2.XActClassDocumentEntryAct
import org.openhealthtools.ihe.common.cdar2.XActRelationshipEntryRelationship
 //Chapter 3.1 "Payers"

// CONF-30: CCD SHOULD contain exactly one and SHALL NOT contain more than one Payers section
//       (templateId 2.16.840.1.113883.10.20.1.9). The Payers section SHALL contain a
//       narrative block, and SHOULD contain clinical statements. Clinical statements
//       SHOULD include one or more coverage activities (templateId 2.16.840.1.113883.10.20.1.20).

ccd_payers(schema:'section') {
    properties {
        // CONF-31:	The payer section SHALL contain Section / code.
        // CONF-32:	The value for “Section / code” SHALL be “48768-6”
        //          “Payment sources” 2.16.840.1.113883.6.1 LOINC STATIC.
       code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'48768-6',
		       displayName:'Payment sources')
           }
       })
       // CONF-33: The purpose section SHALL contain Section / title.
       // CONF-34: Section / title SHOULD be valued with a
       // case-insensitive language-insensitive text string containing "insurance"
       // or "payers".
       title(check: { it.text =~ /(?i)payers|insurance/ }, def: {
           getMetaBuilder().build {
               st('Payers')
           }
       })
       // Note: the CCD model extension adds coverageActivities to the 
       // entryRelationships collection, so you can specify more than one
       // coverageActivity without overwriting.
       coverageActivity(schema:'ccd_coverageActivity')
    }
	collections {
	   templateIds(collection:'templateId', def: {
	     getMetaBuilder().buildList {
           ii(root:'2.16.840.1.113883.10.20.1.9')
         }
	   })
	}
}

// CONF-35:	A coverage activity (templateId 2.16.840.1.113883.10.20.1.20)
//       SHALL be represented with Act.   
ccd_coverageActivity(schema:'act') {
    properties {
       // CONF-36: The value for “Act / @classCode” in a coverage activity SHALL be
       //          “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
       // CONF-37:	The value for “Act / @moodCode” in a coverage activity SHALL be
       //          “DEF” 2.16.840.1.113883.5.1001 ActMood STATIC.
       classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ACT',
               def: XActClassDocumentEntryAct.ACT_LITERAL)
       moodCode(factory:'XDOCUMENT_ACT_MOOD',
               def: XDocumentActMood.DEF_LITERAL)
       // CONF-39:	A coverage activity SHALL contain exactly one Act / statusCode.
       // CONF-40:	The value for “Act / statusCode” in a coverage activity SHALL be
       //          “completed”  2.16.840.1.113883.5.14 ActStatus STATIC.
       statusCode(schema:'cs', def: {
           getMetaBuilder().build {
               cs('completed')
           }
       })
       // CONF-41:	A coverage activity SHALL contain exactly one Act / code.
       // CONF-42:	The value for “Act / code” in a coverage activity SHALL be
       //          “48768-6” “Payment sources” 2.16.840.1.113883.6.1 LOINC STATIC.
       code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'48768-6',
		                displayName:'Payment sources')
           }
       })
       policyActivity(schema:'ccd_policyActivity')
    }
    collections {
        // CONF-38: A coverage activity SHALL contain at least one Act / id.      
       ids(collection:'id', min:1) {
            id(schema:'ii')
       }
	   templateIds(collection:'templateId', def: {
	       getMetaBuilder().buildList {
	           ii(root:'2.16.840.1.113883.10.20.1.20')
	       }
	   })
    }
}

// CONF-48:	A policy activity (templateId 2.16.840.1.113883.10.20.1.26)
// SHALL be represented with Act.   
ccd_policyActivity(schema:'act') {
   properties {
        // CONF-49:	The value for “Act / @classCode” in a policy activity SHALL be
        //          “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
        // CONF-50:	The value for “Act / @moodCode” in a policy activity SHALL be
        //          “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ACT',
                def: XActClassDocumentEntryAct.ACT_LITERAL)
        moodCode(factory:'XDOCUMENT_ACT_MOOD',
                def: XDocumentActMood.EVN_LITERAL)
        // CONF-52:	A policy activity SHALL contain exactly one Act / statusCode.
        // CONF-53:	The value for “Act / statusCode” in a policy activity SHALL
        //          be “completed”  2.16.840.1.113883.5.14 ActStatus STATIC.
       statusCode(schema:'cs', def: {
           getMetaBuilder().build {
               cs('completed')
           }
       })
       code(schema:'actCode')

       // CONF-56:	A policy activity SHALL contain exactly one Act / performer
       //          [@typeCode=”PRF”], representing the payer.
       payer(schema:'ccd_payerAssignedEntity', req:true)
       // CONF-58:	A policy activity SHALL contain exactly one Act / participant
       //          [@typeCode=”COV”], representing the covered party.
       coveredParty(schema:'participantRole', req:true)
       // CONF-63:	A policy activity MAY contain exactly one Act / participant
       // [@typeCode=”HLD”], representing the subscriber.
       subscriber(schema:'participantRole')
       // CONF-66: The value for “Act / entryRelationship / @typeCode” in a policy activity SHALL 
       // be “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
       // CONF-67: The target of a policy activity with Act / entryRelationship / 
       // @typeCode=”REFR” SHALL be an authorization activity 
       // (templateId 2.16.840.1.113883.10.20.1.19) or an Act, 
       // with Act [@classCode = “ACT”] and Act [@moodCode = “DEF”], 
       // representing a description of the coverage plan.
       authorizationActivity(schema:'ccd_authorizationActivity')
    }
    collections {
        // CONF-51:	A policy activity SHALL contain at least one Act / id,
        //          which represents the group or contract number related to the
        //          insurance policy or program. 
       ids(collection:'id', min:1) {
            id(schema:'ii')
        }
	   templateIds(collection:'templateId', def: {
	       getMetaBuilder().buildList {
	           ii(root:'2.16.840.1.113883.10.20.1.26')
	       }
	   })

    }
}

ccd_payerAssignedEntity(schema:'assignedEntity') {
    collections {
        // CONF-57: A payer in a policy activity SHALL contain one or more 
        // performer / assignedEntity / id, to represent the payer identification number. 
        // For pharamacy benefit programs this can be valued using the RxBIN and RxPCN numbers 
        // assigned by ANSI and NCPDP respectively. 
        // When a nationally recognized payer identification number is available, 
        // it would be placed here.        
		ids(collection:'id', min:1) {
			id(schema:'ii')
		}
    }
}


// CONF-69: An authorization activity (templateId 2.16.840.1.113883.10.20.1.19) 
// SHALL be represented with Act.
ccd_authorizationActivity(schema:'act') {
    properties {
        // CONF-70: The value for “Act / @classCode” in an authorization activity SHALL be 
        // “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.        
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ACT',
                def: XActClassDocumentEntryAct.ACT_LITERAL)
        // CONF-72: The value for “Act / @moodCode” in an authorization activity SHALL 
        // be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.                
        moodCode(factory:'XDOCUMENT_ACT_MOOD',
                def: XDocumentActMood.EVN_LITERAL)
        promise(schema:'ccd_promise')
    }
    collections {
        // CONF-71: An authorization activity SHALL contain at least one Act / id.
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }    
 	   templateIds(collection:'templateId', def: {
 		     getMetaBuilder().buildList {
 	           ii(root:'2.16.840.1.113883.10.20.1.19')
 	         }
 		   })
    }
}

ccd_promise(schema:'entryRelationship') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_ENTRY_RELATIONSHIP',
                def:XActRelationshipEntryRelationship.SUBJ_LITERAL)
    }
}
