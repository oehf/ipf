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

// Chapter 3.5: Problems

// CONF-140: CCD SHOULD contain exactly one and SHALL NOT contain more than one 
//           Problem section (templateId 2.16.840.1.113883.10.20.1.11). 
//           The Problem section SHALL contain a narrative block, and SHOULD contain 
//           clinical statements. Clinical statements SHOULD include one or more 
//           problem acts (templateId 2.16.840.1.113883.10.20.1.27). A problem act 
//           SHOULD include one or more problem observations 
//           (templateId 2.16.840.1.113883.10.20.1.28).
ccd_problems(schema:'section'){
    properties{
        //CONF-141: The problem section SHALL contain Section / code.
        //CONF-142: The value for “Section / code” SHALL be “11450-4” “Problem list”
        //           2.16.840.1.113883.6.1 LOINC STATIC.
        code(schema:'loincCode', def: {
           getMetaBuilder().build {
               loincCode(code:'11450-4',
                       displayName:'Problem list')
           }
       })
       // CONF-143: The problem section SHALL contain Section / title.
       // CONF-144: Section / title SHOULD be valued with a case-insensitive 
       //           language-insensitive text string containing “problems”.
       title(check: { it.text =~ /(?i)problems/ }, def: {
           getMetaBuilder().build {
               st('Problems')
           }
       })
       problemAct(schema:'ccd_problemAct')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.11')
            }
          })
    }
    
}

// CONF-145: A problem act (templateId 2.16.840.1.113883.10.20.1.27) SHALL be represented with Act.
ccd_problemAct(schema:'act'){
    properties{
        // CONF-146: The value for “Act / @classCode” in a problem act SHALL be 
        //           “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ACT', def:XActClassDocumentEntryAct.ACT_LITERAL)
        // CONF-147: The value for “Act / @moodCode” in a problem act SHALL be 
        //           “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        // CONF-149: The value for “Act / code / @NullFlavor” in a problem act SHALL be
        //           “NA” “Not applicable” 2.16.840.1.113883.5.1008 NullFlavor STATIC.
        code(def:{
            getMetaBuilder().build{
                cd(nullFlavor:'NA')
            }
        })
        problemObservation(schema:'ccd_problemObservation')
        // CONF-168: A problem act MAY contain exactly one episode observation.
        episodeObservation(schema:'ccd_episodeObservation')
        //CONF-179: A problem act MAY contain exactly one patient awareness.
        patientAwareness(schema:'ccd_patientAwareness')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.27')
            }
        })
        entryRelationships(collection:'entryRelationship', min:1)
    }
}

// CONF-154: A problem observation (templateId 2.16.840.1.113883.10.20.1.28) SHALL 
//           be represented with Observation.
ccd_problemObservation(schema:'observation'){
    properties{
        //CONF-155: The value for “Observation / @moodCode” in a problem observation SHALL be 
        //          “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION', 
                def: XActMoodDocumentObservation.EVN_LITERAL)
        // CONF-156: A problem observation SHALL include exactly one Observation / statusCode.
        // CONF-157: The value for “Observation / statusCode” in a problem observation
        //           SHALL be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.        
        statusCode(schema:'cs', req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
        // CONF-162: A problem observation MAY contain exactly one problem status observation.
        problemStatus(schema:'ccd_problemObservationStatus')
        // CONF-165: A problem observation MAY contain exactly one problem healthstatus observation.
        problemHealthstatus(schema:'ccd_problemObservationHealthstatus')
        // CONF-180: A problem observation MAY contain exactly one patient awareness.
        patientAwareness(schema:'ccd_patientAwareness')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.28')
            }
        })
    }
}

// CONF-163: A problem status observation (templateId 2.16.840.1.113883.10.20.1.50) SHALL be
//           a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) 
//           (as defined in section 5.1 “Type” and “Status” values).
// CONF-164: The value for “Observation / value” in a problem status observation SHALL be selected
//           from ValueSet 2.16.840.1.113883.1.11.20.13 ProblemStatusCode STATIC 20061017.
ccd_problemObservationStatus(schema:'ccd_statusObservation'){
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.50')
            }
        })
    }
}

// CONF-166: A problem healthstatus observation (templateId 2.16.840.1.113883.10.20.1.51) SHALL be
//           a conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) 
//           (as defined in section 5.1 “Type” and “Status” values), except that the value for 
//           “Observation / code” in a problem healthstatus observation SHALL be “11323-3” 
//           “Health status” 2.16.840.1.113883.6.1 LOINC STATIC.
// CONF-167: The value for “Observation / value” in a problem healthstatus observation SHALL be selected
//           from ValueSet 2.16.840.1.113883.1.11.20.12 ProblemHealthStatusCode STATIC 20061017.
ccd_problemObservationHealthstatus(schema:'ccd_statusObservation'){
    properties{
        code(def: {
            getMetaBuilder().build{
                loincCode(code:'11323-3',
                        displayName:'Health status')
            }
        })
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.51')
            }
        })
    }
}

// CONF-169: An episode observation (templateId 2.16.840.1.113883.10.20.1.41) SHALL be represented with Observation.
ccd_episodeObservation(schema:'observation'){
    properties{
        // CONF-170: The value for “Observation / @classCode” in an episode observation SHALL
        //           be “OBS” 2.16.840.1.113883.5.6 ActClass STATIC.
        classCode(factory:'ACT_CLASS_OBSERVATION',
                def:ActClassObservationMember3.OBS_LITERAL)
        // CONF-171: The value for “Observation / @moodCode” in an episode observation SHALL
        //           be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION',
                def:XActMoodDocumentObservation.EVN_LITERAL)                
        // CONF-172: An episode observation SHALL include exactly one Observation / statusCode.
        // CONF-173: The value for “Observation / statusCode” in an episode observation SHALL
        //           be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
        statusCode(req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.41')
            }
        })
        // CONF-175: “Observation / value” in an episode observation SHOULD be the following SNOMED CT expression:
        values(collection:'value', min:1, max:1, def:{
            getMetaBuilder().buildList {
                cd(code:'404684003', codeSystem:'2.16.840.1.113883.6.96', displayName:'Clinical finding'){
                    qualifier{
                        name(code:'246456000', displayName:'Episodicity')
                        value(code:'288527008', displayName:'New episode')
                    }//qualifier
                }//cd
            }
        })
    }
}

//CONF-178: Patient awareness (templateId 2.16.840.1.113883.10.20.1.48) of a problem,
//          observation, or other clinical statement SHALL be represented with participant.
ccd_patientAwareness(schema:'clinicalStatementParticipant'){
    properties{
        // CONF-181: The value for “participant / @typeCode” in a patient awareness SHALL
        //           be “SBJ” “Subject” 2.16.840.1.113883.5.90 ParticipationType STATIC.
        typeCode(factory:'PARTICIPATION_TYPE', def:ParticipationTargetSubject.SBJ_LITERAL)
        // CONF-182: Patient awareness SHALL contain exactly one participant / awarenessCode.
        awarenessCode(schema:'ce', req:true)
        // CONF-183: Patient awareness SHALL contain exactly one participant / participantRole / id,
        //           which SHALL have exactly one value, which SHALL also be present in 
        //           ClinicalDocument / recordTarget / patientRole / id.
        participantRole(schema:'ccd_problemsParticipantRole')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.48')
            }
        })
    }
    
}

//CONF-183: Patient awareness SHALL contain exactly one participant / participantRole / id,
//           which SHALL have exactly one value, which SHALL also be present in 
//           ClinicalDocument / recordTarget / patientRole / id.
ccd_problemsParticipantRole(schema:'participantRole'){
    collections{
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}
