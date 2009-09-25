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
package builders

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.support.MetaBuilderUtils

clinicalStatementChoice(schema:'infrastructureRoot', check: {
     MetaBuilderUtils.requireChoiceOf(it, ['act', 'encounter', 'observation', 'observationMedia',
                                           'organizer', 'procedure', 'regionOfInterest',
                                           'substanceAdministration', 'supply'])
 }) {
    properties {
        contextConductionInd(def:true)
        /* One of */
        act(schema:'act')
        encounter(schema:'encounter')
        observation(schema:'observation')
        observationMedia(schema:'observationMedia')
        organizer(schema:'organizer')
        procedure(schema:'procedure')
        regionOfInterest(schema:'regionOfInterest')
        substanceAdministration(schema:'substanceAdministration')
        supply(schema:'supply')      
    }
}

externalActChoice(schema:'infrastructureRoot', check: {
    MetaBuilderUtils.requireChoiceOf(it, ['externalAct', 'externalObservation',
                                          'externalProcedure', 'externalDocument'])
}) {
  properties {
      /* One of */
      externalAct(schema:'externalAct')
      externalObservation(schema:'externalObservation')
      externalProcedure(schema:'externalProcedure')
      externalDocument(schema:'externalDocument')
  }
}

authorization(schema:'infrastructureRoot', factory:'POCDMT000040_AUTHORIZATION') {
    properties {
        // typeCode(factory:'ACT_RELATIONSHIP_TYPE')
        consent(schema:'consent')
    }
}

//TODO implement the check closure for all choices as list ot property names
component(
        schema:'infrastructureRoot', 
        factory:'POCDMT000040_COMPONENT2', 
        check: {
            MetaBuilderUtils.requireChoiceOf(it, ['structuredBody', 'nonXMLBody']) 
        }) {
    properties {
        contextConductionInd()
        // typeCode(factory:'ACT_RELATIONSHIP_HAS_COMPONENT')
        /* one of */
        structuredBody(schema:'structuredBody')
        nonXMLBody(schema:'nonXMLBody')
    }
}

componentOf(schema:'infrastructureRoot', factory:'POCDMT000040_COMPONENT1') {
    properties {
        encompassingEncounter(schema:'encompassingEncounter')
    }
}

documentationOf(schema:'infrastructureRoot', factory:'POCDMT000040_DOCUMENTATION_OF') {
    properties {
        serviceEvent(schema:'serviceEvent')
    }
}

entry(schema:'clinicalStatementChoice', factory:'POCDMT000040_ENTRY') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_ENTRY')
        /* one of clinical statement choice: see schema definition */
    }
}

entryRelationship(schema:'clinicalStatementChoice', factory:'POCDMT000040_ENTRY_RELATIONSHIP') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_ENTRY_RELATIONSHIP')
        inversionInd()
        sequenceNumber(schema:'_int')
        negationInd()
        seperatableInd()
        /* one of clinical statement choice: see schema definition */
    }
}

inFulfillmentOf(schema:'infrastructureRoot', factory:'POCDMT000040_IN_FULFILLMENT_OF') {
    properties {
        // typeCode(factory:'ACT_RELATIONSHIP_FULFILLS')
        order(schema:'order')
    }
}

relatedDocument(schema:'infrastructureRoot', factory:'POCDMT000040_RELATED_DOCUMENT') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_DOCUMENT')
        parentDocument(schema:'parentDocument')
    }
}

sections(schema:'infrastructureRoot', factory:'POCDMT000040_COMPONENT3') {
    properties {
        // typeCode(factory:'ACT_RELATIONSHIP_HAS_COMPONENT')
        contextConductionInd()
        section(schema:'section')
    }
}

organizerComponents(schema:'clinicalStatementChoice', factory:'POCDMT000040_COMPONENT4') {
    properties{
        typeCode(factory:'ACT_RELATIONSHIP_HAS_COMPONENT')
        sequenceNumber(schema:'_int')
        seperatableInd()
    }
    collections{
        section(collection:'section'){
            section(schema:'section')
        }
    }
}

precondition(schema:'infrastructureRoot', factory:'POCDMT000040_PRECONDITION'){
    properties{
        criterion(schema:'criterion', req:true)
    }
}

//TODO check XOR choice
reference(schema:'externalActChoice', factory:'POCDMT000040_REFERENCE'){
  properties {
      typeCode(factory:'XACT_RELATIONSHIP_EXTERNAL_REFERENCE',
              def:XActRelationshipExternalReference.XCRPT_LITERAL)
      seperatableInd(schema:'bl')
  }
}

referenceRange(schema:'infrastructureRoot', factory:'POCDMT000040_REFERENCE_RANGE') {
   properties {
      // typeCode(factory:'ACT_RELATIONSHIP_TYPE')
      observationRange(schema:'observationRange')
   }
}

sectionComponents(schema:'infrastructureRoot', factory:'POCDMT000040_COMPONENT5') {
    properties {
        typeCode(factory:'ACT_RELATIONSHIP_HAS_COMPONENT')
    }
    collections{
        section(collection:'section'){
            section(schema:'section')
        }
    }
}
