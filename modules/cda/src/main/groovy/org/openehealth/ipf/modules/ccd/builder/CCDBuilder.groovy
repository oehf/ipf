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
package org.openehealth.ipf.modules.ccd.builder

import java.lang.ClassLoader
import org.openehealth.ipf.modules.cda.builder.CDAR2Builder

import org.openhealthtools.ihe.common.cdar2.*


/**
 * CCDBuilder extends CDABuilder by implementing contraints as described in
 * the CCD specification. It furthermore introduces an additional level of
 * builder 'abstractness', primarily in the body sections. This avoids
 * building 'boilerplate' structures.
 *
 * Example: the CCD spec defines the "purpose" of a CCD document being written
 * as a structured body section with specific structure, codes, templateIds etc.
 * By defining a "purpose" and "purposeActivity" extension, the following CCD
 * fragment
 *
 * <pre>
 *
 * <component>
 *  <section>
 *    <templateId root="2.16.840.1.113883.10.20.1.13"/>
 *    <code code="48764-5" codeSystem="2.16.840.1.113883.6.1"
 *          codeSystemName="LOINC" displayName="Summary purpose"/>
 *    <title>Summary purpose</title>
 *    <text>Transfer of Care</text>
 *    <entry typeCode="DRIV">
 *      <act classCode="ACT" moodCode="EVN">
 *        <templateId root="2.16.840.1.113883.10.20.1.30"/>
 *        <code code="23745001" codeSystem="2.16.840.1.113883.6.96"
 *              codeSystemName="SNOMED CT" displayName="Documentation procedure"/>
 *        <statusCode code="completed"/>
 *        <entryRelationship typeCode="RSON">
 *          <act classCode="ACT" moodCode="EVN">
 *            <code code="308292007" codeSystem="2.16.840.1.113883.6.96" displayName="Transfer of care"/>
 *            <statusCode code="completed"/>
 *          </act>
 *        </entryRelationship>
 *      </act>
 *    </entry>
 *  </section>
 * </component>
 *
 * </pre>
 *
 * drastically boils down by exploiting all static constraints to:
 *
 * <pre>
 *
 *  purpose {
 *    text('Transfer of Care')
 *    purposeActivity() {
 *       act {
 *          code(code:'308292007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Transfer of care')
 *          statusCode('completed')
 *       }
 *    }
 *  }
 *
 * </pre>
 * 
 * @author Christian Ohr
 */
public class CCDBuilder extends CDAR2Builder{
	
	public CCDBuilder(ClassLoader loader) {
	   super(loader)
      new CCDModelExtension(builder:this).extensions.call()
	}


	protected void actBuilder() {
        super.actBuilder()
		// define(getClass().getResource('/ccdbuilders/ContinuityOfCareDocumentBuilder.groovy'))
		// define(getClass().getResource('/ccdbuilders/ClinicalStatementBuilder.groovy'))

        // TODO externalize schemas retaining delegation to this builder
        // TODO check whether createFromString can be applied to defaults

define {

continuityOfCareDocument(schema:'clinicalDocument') {
   properties {
      code(schema:'loincCode', def: {
         build {
            loincCode(code:'34133-9',
	                   displayName:'Summarization of episode note')
         }
	   })
      component(schema:'ccd_component')
	}

	collections {
	   templateIds(collection:'templateId', def: {
	      buildList {
	         ii(root:'2.16.840.1.113883.3.27.1776')
	      }
	   })
	   {
	      templateId(schema:'ii')
	   }
	}
}

ccd_component(schema:'component') {
   properties {
      structuredBody(schema:'ccd_structuredBody')
   }
}

// Add CCD sections as properties

ccd_structuredBody(schema:'structuredBody') {
   properties {
      purpose(schema:'ccd_purpose')
      payers(schema:'ccd_payers')
      // ...
   }
}

}


define {

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
         build {
            loincCode(code:'48764-5',
		                displayName:'Summary purpose')
         }
      })
		
      // CONF-18: The purpose section SHALL contain Section / title.
      // CONF-19: (NOT ENFORCED) Section / title SHOULD be valued with a
      // case-insensitive language-insensitive text string containing “purpose”.
      title(def: {
         build {
            st('Summary purpose')
         }
      })
      purposeActivity(schema:'ccd_purposeActivity')
	}
	collections {
	   templateIds(collection:'templateId', def: {
         buildList {
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

// Chapter 3.1 "Payers"

// CONF-30: CCD SHOULD contain exactly one and SHALL NOT contain more than one Payers section
//          (templateId 2.16.840.1.113883.10.20.1.9). The Payers section SHALL contain a
//          narrative block, and SHOULD contain clinical statements. Clinical statements
//          SHOULD include one or more coverage activities (templateId 2.16.840.1.113883.10.20.1.20).

ccd_payers(schema:'section') {
   properties {
      // CONF-31:	The payer section SHALL contain Section / code.
      // CONF-32:	The value for “Section / code” SHALL be “48768-6”
      //          “Payment sources” 2.16.840.1.113883.6.1 LOINC STATIC.
      code(schema:'loincCode', def: {
         build {
            loincCode(code:'48764-6',
		                displayName:'Payment sources')
         }
      })
      // CONF-33: The purpose section SHALL contain Section / title.
      // CONF-34: (NOT ENFORCED) Section / title SHOULD be valued with a
      // case-insensitive language-insensitive text string containing “purpose”.
      title(def: {
         build {
            st('Payers')
         }
      })
      coverageActivity(schema:'ccd_coverageActivity')
   }
	collections {
	   templateIds(collection:'templateId', def: {
         buildList {
            ii(root:'2.16.840.1.113883.10.20.1.9')
         }
	   })
	}
}

// CONF-35:	A coverage activity (templateId 2.16.840.1.113883.10.20.1.20)
//          SHALL be represented with Act.   
ccd_coverageActivity(schema:'act') {
   properties {
      // CONF-36: The value for “Act / @classCode” in a coverage activity SHALL be
      //          “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
      // CONF-37:	The value for “Act / @moodCode” in a coverage activity SHALL be
      //          “DEF” 2.16.840.1.113883.5.1001 ActMood STATIC.
      moodCode(factory:'XDOCUMENT_ACT_MOOD',
               def: XDocumentActMood.DEF_LITERAL)
      // CONF-39:	A coverage activity SHALL contain exactly one Act / statusCode.
      // CONF-40:	The value for “Act / statusCode” in a coverage activity SHALL be
      //          “completed”  2.16.840.1.113883.5.14 ActStatus STATIC.
      statusCode(schema:'cs', def: {
         build {
            cs('completed')
         }
      })
      // CONF-41:	A coverage activity SHALL contain exactly one Act / code.
      // CONF-42:	The value for “Act / code” in a coverage activity SHALL be
      //          “48768-6” “Payment sources” 2.16.840.1.113883.6.1 LOINC STATIC.
      code(schema:'loincCode', def: {
         build {
            loincCode(code:'48764-6',
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
         buildList {
            ii(root:'2.16.840.1.113883.10.20.1.20')
         }
	   })
   }
}

// CONF-48:	A policy activity (templateId 2.16.840.1.113883.10.20.1.26)
//          SHALL be represented with Act.   
ccd_policyActivity(schema:'act') {
   // CONF-49:	The value for “Act / @classCode” in a policy activity SHALL be
   //          “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
   // CONF-50:	The value for “Act / @moodCode” in a policy activity SHALL be
   //          “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
   properties {
      // CONF-52:	A policy activity SHALL contain exactly one Act / statusCode.
      // CONF-53:	The value for “Act / statusCode” in a policy activity SHALL
      //          be “completed”  2.16.840.1.113883.5.14 ActStatus STATIC.
      statusCode(schema:'cs', def: {
         build {
            cs('completed')
         }
      })

      // CONF-56:	A policy activity SHALL contain exactly one Act / performer
      //          [@typeCode=”PRF”], representing the payer.
		payer(schema:'ccd_payerAssignedEntity', req:true)
      // CONF-58:	A policy activity SHALL contain exactly one Act / participant
      //          [@typeCode=”COV”], representing the covered party.
      coveredParty(schema:'participantRole', req:true)
      // CONF-63:	A policy activity MAY contain exactly one Act / participant
      // [@typeCode=”HLD”], representing the subscriber.
      subscriber(schema:'participantRole')

   }
   collections {
      // CONF-51:	A policy activity SHALL contain at least one Act / id,
      //          which represents the group or contract number related to the
      //          insurance policy or program. 
      ids(collection:'id', min:1) {
         id(schema:'ii')
      }
	   templateIds(collection:'templateId', def: {
         buildList {
            ii(root:'2.16.840.1.113883.10.20.1.26')
         }
	   })

   }
}

ccd_payerAssignedEntity(schema:'assignedEntity') {
   collections {
  		ids(collection:'id', min:1) {
  			id(schema:'ii')
  		}
   }
}
   
// TODO rename, continue   
ccd_authorizationActivity(schema:'entryRelationship') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_ENTRY_RELATIONSHIP',
                 def: XActRelationshipEntryRelationship.REFR_LITERAL)
    }
}

}   // define

	
	}
	
}
