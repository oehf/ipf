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
package builders.content.document

import groovytools.builder.*
import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.support.MetaBuilderUtils


// Comments can be added to every section and clinical statement, so we define
// it once here

ccd_section(schema:'section') {
    properties {
        comment(schema:'ccd_comment')
    }
}

ccd_act(schema:'act') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }
}

ccd_encounter(schema:'encounter') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }
}

ccd_observation(schema:'observation') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_observationMedia(schema:'observationMedia') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_organizer(schema:'organizer') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_procedure(schema:'procedure') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_regionOfInterest(schema:'regionOfInterest') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_substanceAdministration(schema:'substanceAdministration') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_supply(schema:'supply') {
    properties {
        comment(schema:'ccd_comment')
        informationSource(schema:'ccd_informationSource')
    }    
}

ccd_clinicalStatementChoice(schema:'clinicalStatementChoice', check: {
    MetaBuilderUtils.requireChoiceOf(it, ['act', 'encounter', 'observation', 'observationMedia',
                                          'organizer', 'procedure', 'regionOfInterest',
                                          'substanceAdministration', 'supply'])
}) {
   properties {
       act(schema:'ccd_act')
       encounter(schema:'ccd_encounter')
       observation(schema:'ccd_observation')
       observationMedia(schema:'ccd_observationMedia')
       organizer(schema:'ccd_organizer')
       procedure(schema:'ccd_procedure')
       regionOfInterest(schema:'ccd_regionOfInterest')
       substanceAdministration(schema:'ccd_substanceAdministration')
       supply(schema:'ccd_supply')      
   }
}

ccd_entry(schema:'ccd_clinicalStatementChoice', factory:'POCDMT000040_ENTRY') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_ENTRY')
        /* one of clinical statement choice: see schema definition */
    }
}

ccd_entryRelationship(schema:'ccd_clinicalStatementChoice', factory:'POCDMT000040_ENTRY_RELATIONSHIP') {
    properties {
        typeCode(factory:'XACT_RELATIONSHIP_ENTRY_RELATIONSHIP')
        inversionInd()
        sequenceNumber(schema:'_int')
        negationInd()
        seperatableInd()
        /* one of clinical statement choice: see schema definition */
    }
}

ccd_organizerComponents(schema:'ccd_clinicalStatementChoice', factory:'POCDMT000040_COMPONENT4') {
    properties{
        typeCode(factory:'ACT_RELATIONSHIP_HAS_COMPONENT')
        sequenceNumber(schema:'_int')
        seperatableInd()
    }
    collections{
        section(collection:'section'){
            section(schema:'ccd_section')
        }
    }
}

