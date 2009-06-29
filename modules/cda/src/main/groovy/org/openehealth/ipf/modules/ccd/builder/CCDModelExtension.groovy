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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.openhealthtools.ihe.common.cdar2.*
import java.lang.Boolean
import org.openhealthtools.ihe.common.cdar2.XActRelationshipEntryimport org.openhealthtools.ihe.common.cdar2.CDAR2Factoryimport org.openhealthtools.ihe.common.cdar2.XServiceEventPerformerimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Actimport org.openhealthtools.ihe.common.cdar2.POCDMT000040EntryRelationshipimport org.openhealthtools.ihe.common.cdar2.XActRelationshipEntryRelationshipimport org.openhealthtools.ihe.common.cdar2.XParticipationAuthorPerformerimport org.openhealthtools.ihe.common.cdar2.ParticipationAncillaryimport org.openhealthtools.ihe.common.cdar2.ParticipationIndirectTargetimport org.openhealthtools.ihe.common.cdar2.ParticipationPhysicalPerformer
import org.eclipse.emf.ecore.xml.type.XMLTypePackage
import org.eclipse.emf.ecore.util.FeatureMap
import org.eclipse.emf.ecore.util.FeatureMapUtil
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.common.util.AbstractEnumerator

/**
 * Make sure that the CDAModelExtensions are called before
 * 
 * @author Christian Ohr
 */
public class CCDModelExtension{

     def builder
     
     def extensions = {

       // --------------------------------------------------------------------------------------------
       // Chapter 2.8 "Purpose"
       // --------------------------------------------------------------------------------------------

       POCDMT000040StructuredBody.metaClass {
          // We assume that this is a CCD Purpose section, enforced by the builder
          setPurpose  {POCDMT000040Section section ->
             POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
             component.section = section
             delegate.component.add(component)
          }
          getPurpose  { ->
             delegate.component.find { it.section.code.code == '48764-5' } ?.section
          }
       }

       POCDMT000040Section.metaClass {
          setPurposeActivity  {POCDMT000040EntryRelationship relationship ->

             POCDMT000040Entry entry = builder.build {
                // CONF-20: A purpose activity (templateId 2.16.840.1.113883.10.20.1.30)
                //          SHALL be represented with Act.
                // CONF-21: The value for “Act / @classCode” in a purpose activity SHALL
                //          be “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
                // CONF-22: The value for “Act / @moodCode” in a purpose activity SHALL
                //          be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
                // CONF-23: A purpose activity SHALL contain exactly one Act / statusCode.
                // CONF-24: The value for “Act / statusCode” in a purpose activity SHALL
                //          be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
                // CONF-25: A purpose activity SHALL contain exactly one Act / code,
                //          with a value of “23745001” “Documentation procedure” 2.16.840.1.113883.6.96
                //          SNOMED CT STATIC.
                entry {
                   typeCode('DRIV')
                   act  {
                     templateId('2.16.840.1.113883.10.20.1.30')
                     statusCode('completed')
                     code(code:'23745001', codeSystem:'2.16.840.1.113883.6.96',
                          codeSystemName:'SNOMED CT', displayName:'Documentation procedure')
                  }
                }
             }

             delegate.entry.add(entry)
             entry.act.entryRelationship.add(relationship)
          }

          getPurposeActivity { ->
             delegate.entry.act.find { it.code.code == '23745001'}?.entryRelationship[0]
          }
       }

        // --------------------------------------------------------------------------------------------
        // Chapter 3.1 "Payers"
        // --------------------------------------------------------------------------------------------

        POCDMT000040StructuredBody.metaClass {
           // TODO: will be always the same
           setPayers {POCDMT000040Section section ->
              POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
              component.section = section
              delegate.component.add(component)
           }
           getPayers  { ->
              delegate.component.find { it.section.code.code == '48768-6' } ?.section
           }           
        }

        POCDMT000040Section.metaClass {
          setCoverageActivity  {POCDMT000040Act act ->
             POCDMT000040Entry entry = CDAR2Factory.eINSTANCE.createPOCDMT000040Entry()
             delegate.entry.add(entry)
             entry.act = act
          }
          getCoverageActivity  { ->
            delegate.entry.find { it.act.code.code == '48768-6' } ?.act
          }
        }

        POCDMT000040Act.metaClass {
            
          setPolicyActivity {POCDMT000040Act act ->
             POCDMT000040EntryRelationship rel = builder.build {
                 entryRelationship {
                     typeCode('COMP')
                 }                 
             }
             delegate.entryRelationship.add(rel)
             rel.act = act
          }
          
          getPolicyActivity { ->
              delegate.entryRelationship.find { 
                  it.typeCode == XActRelationshipEntryRelationship.COMP_LITERAL 
              }?.act
          } 
          
          setPayer  {POCDMT000040AssignedEntity assignedEntity ->
             POCDMT000040Performer2 payer = builder.build {
                clinicalStatementPerformer {
                   typeCode('PRF')
                }
             }
             delegate.performer.add(payer)
             payer.assignedEntity = assignedEntity
          }

          getPayer  { ->
            delegate.performer.find {
                it.typeCode == ParticipationPhysicalPerformer.PRF_LITERAL  
            }?.assignedEntity
          }

          setCoveredParty  {POCDMT000040ParticipantRole participantRole ->
             POCDMT000040Participant2 coveredParty = builder.build {
                clinicalStatementParticipant {
                   typeCode('COV')
                }
             }
             delegate.participant.add(coveredParty)
             coveredParty.participantRole = participantRole
          }

          getCoveredParty  { ->
            delegate.participant.find {
                it.typeCode == ParticipationIndirectTarget.COV_LITERAL 
            }?.participantRole
          }

          setSubscriber  {POCDMT000040ParticipantRole participantRole ->
             POCDMT000040Participant2 subscriber = builder.build {
                 clinicalStatementParticipant {
                   typeCode('HLD')
                }
             }
             delegate.participant.add(coveredParty)
             coveredParty.participantRole = subscriber
          }
           
          getSubscriber  { ->
            delegate.participant.find {
                it.typeCode == ParticipationIndirectTarget.HLD_LITERAL 
            }?.participantRole
          }
          
          setAuthorizationActivity { POCDMT000040Act act1 ->
          POCDMT000040EntryRelationship rel1 = CDAR2Factory.eINSTANCE.createPOCDMT000040EntryRelationship()
          rel1.typeCode = XActRelationshipEntryRelationship.REFR_LITERAL
     /*         POCDMT000040EntryRelationship rel1 = builder.build {
                  // CONF-66: The value for “Act / entryRelationship / @typeCode” 
                  // in a policy activity SHALL be “REFR” 2.16.840.1.113883.5.1002 
                  // ActRelationshipType STATIC.
                  entryRelationship {
                      typeCode('REFR')
                  }
              }
     */         delegate.entryRelationship.add(rel1)
              rel1.act = act1
          }
          
          getAuthorizationActivity { ->
              delegate.entryRelationship.find {
                  it.typeCode == XActRelationshipEntryRelationship.REFR_LITERAL
              }?.act
          }
          
          setPromise { POCDMT000040EntryRelationship rel ->
              delegate.entryRelationship.add(rel)              
          }
          
          getPromise { ->
              delegate.entryRelationship.find {
                  it.typeCode == XActRelationshipEntryRelationship.SUBJ_LITERAL 
              }        
          }
       }
    
   }
}
