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

import org.openhealthtools.ihe.common.cdar2.*import org.openehealth.ipf.modules.cda.builder.support.MetaBuilderUtils

/**
 * Chapter 3.10: Plan of Care
 * 
 * Template Definitions:
 *      Plan of Care (2.16.840.1.113883.10.20.1.10)
 *      
 * Dependencies:
 *      Medication Activity (ccd_medicationActivity)
 *      Supply Activity (ccd_supplyActivity)
 *      Encounter Activity (ccd_encounterActivity)
 */

// CONF-480: CCD SHOULD contain exactly one and SHALL NOT contain more than one Plan of Care 
//           section (templateId 2.16.840.1.113883.10.20.1.10). The Plan of Care section SHALL 
//           contain a narrative block, and SHOULD contain clinical statements. 
//           Clinical statements SHALL include one or more plan of care activities 
//           (templateId 2.16.840.1.113883.10.20.1.25).
ccd_planOfCare(schema:'ccd_section'){
    properties{
        //CONF-481: The alert section SHALL contain Section / code.
        //CONF-482: The value for “Section / code” SHALL be “18776-5” 
        //          “Treatment Plan” 2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'18776-5', displayName:'Treatment Plan')
           }
       })
       // CONF-483: The alert section SHALL contain Section / title.
       // CONF-484: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “medication”
       title(check: { it.text =~ /(?i)plan/ }, def: {
           getMetaBuilder().build {
               st('Plan')
           }
       })
       text(schema:'strucDocText', req:true)       
       planOfCareActivity(schema:'ccd_planOfCareActivity')
       
       // Not covered by spec, but reasonable as planned activities:
       encounterActivity(schema:'ccd_plannedMedicationActivity')
       medicationActivity(schema:'ccd_plannedMedicationActivity')
       supplyActivity(schema:'ccd_plannedSupplyActivity')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.10')
            }
        })
    }   
}

// CCD-related Plan Of Care Activities

ccd_plannedEncounterActivity(schema:'ccd_encounterActivity') {
    properties {
        moodCode(factory:'XDOCUMENT_ENCOUNTER_MOOD', req:true, check: [
            XDocumentEncounterMood.INT_LITERAL,
            XDocumentEncounterMood.ARQ_LITERAL,
            XDocumentEncounterMood.PRMS_LITERAL,
            XDocumentEncounterMood.PRP_LITERAL,
            XDocumentEncounterMood.RQO_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.21')
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
    }
}

ccd_plannedMedicationActivity(schema:'ccd_medicationActivity') {
    properties {
        moodCode(factory:'XDOCUMENT_SUBSTANCE_MOOD', check: [
           XDocumentSubstanceMood.INT_LITERAL,
           XDocumentProcedureMood.PRMS_LITERAL,
           XDocumentProcedureMood.PRP_LITERAL,
           XDocumentProcedureMood.RQO_LITERAL])        
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.24')
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
    }
}

ccd_plannedSupplyActivity(schema:'ccd_supplyActivity') {
    properties {
        moodCode(factory:'XDOCUMENT_SUBSTANCE_MOOD', check: [
           XDocumentSubstanceMood.INT_LITERAL,
           XDocumentProcedureMood.PRMS_LITERAL,
           XDocumentProcedureMood.PRP_LITERAL,
           XDocumentProcedureMood.RQO_LITERAL])        
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.34')
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
    }
}


// Generic Plan Of Care Activities

ccd_planOfCareActivity(schema:'infrastructureRoot', factory:'POCDMT000040_ENTRY', check: {
     MetaBuilderUtils.requireChoiceOf(it, ['act', 'encounter', 'observation',
                                           'procedure', 'substanceAdministration', 'supply'])
}) {
     properties {
         typeCode(factory:'XACT_RELATIONSHIP_ENTRY')
         act(schema:'ccd_plannedAct')
         encounter(schema:'ccd_plannedEncounter')
         observation(schema:'ccd_plannedObservation')
         procedure(schema:'ccd_plannedProcedure')
         substanceAdministration(schema:'ccd_plannedSubstanceAdministration')
         supply(schema:'ccd_plannedSupply')      
     }

}

ccd_plannedAct(schema:'ccd_act') {
    properties {
        moodCode(factory:'XDOCUMENT_ACT_MOOD', check: [
           XDocumentActMood.INT_LITERAL,
           XDocumentActMood.ARQ_LITERAL,
           XDocumentActMood.PRMS_LITERAL,
           XDocumentActMood.PRP_LITERAL,
           XDocumentActMood.RQO_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}

ccd_plannedEncounter(schema:'ccd_encounter') {
    properties {
        moodCode(factory:'XDOCUMENT_ENCOUNTER_MOOD', check: [
           XDocumentEncounterMood.INT_LITERAL,
           XDocumentEncounterMood.ARQ_LITERAL,
           XDocumentEncounterMood.PRMS_LITERAL,
           XDocumentEncounterMood.PRP_LITERAL,
           XDocumentEncounterMood.RQO_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}

ccd_plannedObservation(schema:'ccd_observation') {
    properties {
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', check: [
           XActMoodDocumentObservation.INT_LITERAL,
           XActMoodDocumentObservation.PRMS_LITERAL,
           XActMoodDocumentObservation.PRP_LITERAL,
           XActMoodDocumentObservation.RQO_LITERAL,
           XActMoodDocumentObservation.GOL_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}

ccd_plannedProcedure(schema:'ccd_procedure') {
    properties {
        moodCode(factory:'XDOCUMENT_PROCEDURE_MOOD', check: [
           XDocumentProcedureMood.INT_LITERAL,
           XDocumentProcedureMood.ARQ_LITERAL,
           XDocumentProcedureMood.PRMS_LITERAL,
           XDocumentProcedureMood.PRP_LITERAL,
           XDocumentProcedureMood.RQO_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}

ccd_plannedSubstanceAdministration(schema:'ccd_substanceAdministration') {
    properties {
        moodCode(factory:'XDOCUMENT_SUBSTANCE_MOOD', check: [
           XDocumentSubstanceMood.INT_LITERAL,
           XDocumentProcedureMood.PRMS_LITERAL,
           XDocumentProcedureMood.PRP_LITERAL,
           XDocumentProcedureMood.RQO_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}

ccd_plannedSupply(schema:'ccd_supply') {
    properties {
        moodCode(factory:'XDOCUMENT_SUBSTANCE_MOOD', check: [
           XDocumentSubstanceMood.INT_LITERAL,
           XDocumentProcedureMood.PRMS_LITERAL,
           XDocumentProcedureMood.PRP_LITERAL,
           XDocumentProcedureMood.RQO_LITERAL])
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.25')
            }
        })        
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}


