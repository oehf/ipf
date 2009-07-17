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
import org.openhealthtools.ihe.common.cdar2.XActRelationshipEntry
import org.openhealthtools.ihe.common.cdar2.CDAR2Factory
import org.openhealthtools.ihe.common.cdar2.XServiceEventPerformer
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openhealthtools.ihe.common.cdar2.POCDMT000040EntryRelationship
import org.openhealthtools.ihe.common.cdar2.XActRelationshipEntryRelationship
import org.openhealthtools.ihe.common.cdar2.XParticipationAuthorPerformer
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Participant1
import org.openhealthtools.ihe.common.cdar2.ParticipationAncillary
import org.openhealthtools.ihe.common.cdar2.ParticipationIndirectTarget
import org.openhealthtools.ihe.common.cdar2.ParticipationPhysicalPerformer
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Organizerimport org.openhealthtools.ihe.common.cdar2.POCDMT000040RelatedSubjectimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Subjectimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Observation
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
        // Chapter 2.1 Header representation
        // --------------------------------------------------------------------------------------------
        // CONF-2: A CCD SHALL contain exactly one ClinicalDocument / documentationOf / serviceEvent.
        // CONF-3: The value for “ClinicalDocument / documentationOf / serviceEvent
        //         / @classCode” SHALL be “PCPR” “Care provision” 2.16.840.1.113883.5.6 ActClass STATIC.
        // CONF-4: ClinicalDocument / documentationOf / serviceEvent SHALL contain
        //         exactly one serviceEvent / effectiveTime / low and exactly one
        //         serviceEvent / effectiveTime / high.
        POCDMT000040ClinicalDocument.metaClass {

            setMainActivity {POCDMT000040ServiceEvent serviceEvent ->
                POCDMT000040DocumentationOf docOf = CDAR2Factory.eINSTANCE.createPOCDMT000040DocumentationOf()
                docOf.serviceEvent = serviceEvent
                delegate.documentationOf.add(docOf)
            }

            getMainActivity { ->
                delegate.documentationOf.find { it.serviceEvent.classCode.name == 'PCPR' } ?.serviceEvent
            }

        }// ccd header extensions

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
        } //purpose structured body extensions

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
        }// purpose section extensions

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
        }// payers structured body extensions

        POCDMT000040Section.metaClass {
            setCoverageActivity  {POCDMT000040Act act ->
                POCDMT000040Entry entry = CDAR2Factory.eINSTANCE.createPOCDMT000040Entry()
                delegate.entry.add(entry)
                entry.act = act
            }
            getCoverageActivity  { ->
                delegate.entry.find { it.act.code.code == '48768-6' } ?.act
            }
        }// payers section extensions

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
            // CONF-66: The value for “Act / entryRelationship / @typeCode”
            // in a policy activity SHALL be “REFR” 2.16.840.1.113883.5.1002
            // ActRelationshipType STATIC.
                POCDMT000040EntryRelationship rel1 = CDAR2Factory.eINSTANCE.createPOCDMT000040EntryRelationship()
                rel1.typeCode = XActRelationshipEntryRelationship.REFR_LITERAL
                /*         POCDMT000040EntryRelationship rel1 = builder.build {
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
        }//payers act extensions

        // --------------------------------------------------------------------------------------------
        // Chapter 3.2 "Advance Directives"
        // --------------------------------------------------------------------------------------------
        
        POCDMT000040StructuredBody.metaClass {
            // We assume that this is a CCD Advance Directives section, enforced by the builder
            setAdvanceDirectives  {POCDMT000040Section section ->
                POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
                component.section = section
                delegate.component.add(component)
            }
            getAdvanceDirectives  { ->
                delegate.component.find { it.section.code.code == '42348-3' } ?.section
            }
        } //advance directives body extensions

        POCDMT000040Section.metaClass {
            setObservation  {POCDMT000040Observation observation ->

                POCDMT000040Entry entry = builder.build {
                    entry {
                        typeCode('DRIV')
                    }
                }
                entry.observation = observation
                delegate.entry.add(entry)

            }

            getObservation { ->
                delegate.entry.observation
            }
        }// advance directives extensions

        POCDMT000040Observation.metaClass {
            setVerifier  {POCDMT000040Participant2 participant ->
                delegate.participant.add(participant)
            }
            getVerifier { ->
                delegate.participant
            }

            // CONF-509: A status observation SHALL be the target of an entryRelationship whose value for
            //           “entryRelationship / @typeCode” SHALL be “REFR” 2.16.840.1.113883.5.1002
            //           ActRelationshipType STATIC.
            setObservationStatus { POCDMT000040Observation observationStatus ->
                POCDMT000040EntryRelationship rell = CDAR2Factory.eINSTANCE.createPOCDMT000040EntryRelationship()
                rell.typeCode = XActRelationshipEntryRelationship.REFR_LITERAL
                rell.observation = observationStatus
                delegate.entryRelationship.add(rell)
            }
            
            getObservationStatus { ->
                delegate.entryRelationship   //TODO add find with template id
            }

            // CONF-101: An advance directive reference (templateId 2.16.840.1.113883.10.20.1.36) SHALL be
            //           represented with Observation / reference / ExternalDocument.
            // CONF-103: The value for “Observation / reference / @typeCode” in an advance directive reference SHALL be
            //           “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
            setReference{ POCDMT000040ExternalDocument document ->
                POCDMT000040Reference ref = CDAR2Factory.eINSTANCE.createPOCDMT000040Reference()
                ref.typeCode = XActRelationshipExternalReference.REFR_LITERAL
                ref.externalDocument = document
                delegate.reference.add(ref)
            }

            getReference{ ->
                delegate.reference
            }
        }// advance directives observations extensions
        
        // --------------------------------------------------------------------------------------------
        // Chapter 3.6 "Family History"
        // --------------------------------------------------------------------------------------------

        POCDMT000040StructuredBody.metaClass {
            // We assume that this is a CCD Family History section, enforced by the builder
            setFamilyHistory  {POCDMT000040Section section ->
                POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
                component.section = section
                delegate.component.add(component)
            }
            getFamilyHistory  { ->
                delegate.component.find { it.section.code.code == '10157-6' } ?.section
            }
        }
        
        POCDMT000040Section.metaClass {
            setObservation  {POCDMT000040Observation observation ->
                POCDMT000040Entry entry = builder.build {
                    entry {
                        typeCode('DRIV')
                    }
                }
                entry.observation = observation
                delegate.entry.add(entry)
            }

            getObservation { ->
                delegate.entry.observation
            }
            
            setFamilyMember  {POCDMT000040Organizer organizer ->
                POCDMT000040Entry entry = builder.build {
                    entry {
                        typeCode('DRIV')
                    }
                }
                entry.organizer = organizer
                delegate.entry.add(entry)
            }

            getFamilyMember { ->
                delegate.entry.organizer
            }
            
            setCauseOfDeath  {POCDMT000040Observation observation ->
                POCDMT000040Entry entry = builder.build {
                    entry {
                        typeCode('DRIV')
                    }
                }
                entry.observation = observation
                delegate.entry.add(entry)
            }

            getCauseOfDeath { ->
                delegate.entry.observation
            }            
        }
        
        POCDMT000040Observation.metaClass {
            setFamilyMember  {POCDMT000040RelatedSubject relatedSubject ->
                POCDMT000040Subject subject = CDAR2Factory.eINSTANCE.createPOCDMT000040Subject()
                subject.relatedSubject = relatedSubject
                delegate.subject = subject
                relatedSubject
            }
            getFamilyMember { ->
                delegate.subject.relatedSubject
            } 
            setCause  {POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship rel = builder.build {
                    entryRelationship {
                        typeCode('CAUS')
                    }
                }
                rel.observation = observation
                delegate.entryRelationship.add(rel)
            }
            getCause { ->
            	delegate.entryRelationship.find { it.typeCode == 'CAUS' }.observation
            }
            
            setAge {POCDMT000040Observation observation ->
                POCDMT000040EntryRelationship rel = builder.build {
                    entryRelationship(inversionInd:true, typeCode:'SUBJ')
                }
                rel.observation = observation
                delegate.entryRelationship.add(rel)
            }
            getAge { ->
        	    delegate.entryRelationship.find { it.typeCode == 'SUBJ' }.observation            
            }
        }
        
        POCDMT000040Organizer.metaClass {
            setFamilyPerson  {POCDMT000040RelatedSubject relatedSubject ->
                POCDMT000040Subject subject = CDAR2Factory.eINSTANCE.createPOCDMT000040Subject()
                subject.relatedSubject = relatedSubject
                delegate.subject = subject
                relatedSubject
            }
            getFamilyPerson { ->
                delegate.subject.relatedSubject
            }   
            
            setCauseOfDeath { POCDMT000040Observation observation -> 
                POCDMT000040Component4 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component4()
                component.observation = observation
                delegate.component.add(component)
                observation                
            }
            getCauseOfDeath { ->
                delegate.component.find { 
                    it.observation?.templateId[0] == '2.16.840.1.113883.10.20.1.42' 
                }.observation
            }
            
            setObservation  {POCDMT000040Observation observation ->
                POCDMT000040Component4 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component4()
                component.observation = observation
                delegate.component.add(component)
                observation                
            }

        	getObservation { ->
        	    delegate.component.find { 
        	        it.observation?.templateId[0] == '2.16.840.1.113883.10.20.1.22' 
        	    }.observation
        	}
        }
        
        
        // --------------------------------------------------------------------------------------------
        // Chapter 3.3 "Support"
        // --------------------------------------------------------------------------------------------
        POCDMT000040ClinicalDocument.metaClass {

            // CONF-112: The value for “ClinicalDocument / participant / @typeCode” 
            //           in a next of kin participant SHALL be “IND” “Indirect participant” 
            //           2.16.840.1.113883.5.90 ParticipationType STATIC.
            setNextOfKin {POCDMT000040AssociatedEntity kin ->
                POCDMT000040Participant1 kinParticipant = CDAR2Factory.eINSTANCE.createPOCDMT000040Participant1()
                kinParticipant.typeCode = ParticipationIndirectTarget.IND_LITERAL
                kinParticipant.associatedEntity = kin
                delegate.participant.add(kinParticipant)
            }

            getNextOfKin { ->
                delegate.participant.findAll { 
                    it.associatedEntity.classCode.name == 'NOK'
                        } ?.associatedEntity
            }
            
            // CONF-117: The value for “ClinicalDocument / participant / @typeCode”
            //           in an emergency contact participant SHALL be “IND” “Indirect participant”
            //           2.16.840.1.113883.5.90 ParticipationType STATIC.
            setEmergencyContact{POCDMT000040AssociatedEntity emergencyContact ->
                POCDMT000040Participant1 emergencyParticipant = CDAR2Factory.eINSTANCE.createPOCDMT000040Participant1()
                emergencyParticipant.typeCode = ParticipationIndirectTarget.IND_LITERAL
                emergencyParticipant.associatedEntity = emergencyContact
                delegate.participant.add(emergencyParticipant)
                
            }
                
            getEmergencyContact{ ->
                delegate.participant.findAll { 
                    it.associatedEntity.classCode.name == 'ECON'
                        } ?.associatedEntity
            }
            
            // CONF-121: The value for “ClinicalDocument / participant / @typeCode” in a 
            //           patient caregiver participant SHALL be “IND” “Indirect participant”
            //           2.16.840.1.113883.5.90 ParticipationType STATIC.
            setCaregiver{POCDMT000040AssociatedEntity caregiver ->
                POCDMT000040Participant1 caregiverParticipant = CDAR2Factory.eINSTANCE.createPOCDMT000040Participant1()
                caregiverParticipant.typeCode = ParticipationIndirectTarget.IND_LITERAL
                caregiverParticipant.associatedEntity = caregiver
                delegate.participant.add(caregiverParticipant)
                
            }
                
            getCaregiver{ ->
                delegate.participant.findAll { 
                    it.associatedEntity.classCode.name == 'CAREGIVER'
                        } ?.associatedEntity
            }

        }// ccd support extensions
        
        
    }//ccd extensions 
}
