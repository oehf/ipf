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
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Organizerimport org.openhealthtools.ihe.common.cdar2.POCDMT000040RelatedSubjectimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Subjectimport org.openhealthtools.ihe.common.cdar2.POCDMT000040Observationimport org.openehealth.ipf.modules.cda.builder.BaseModelExtension
import org.eclipse.emf.ecore.xml.type.XMLTypePackage
import org.eclipse.emf.ecore.util.FeatureMap
import org.eclipse.emf.ecore.util.FeatureMapUtil
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.common.util.AbstractEnumerator

/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Christian Ohr
 * @deprecated
 */
public class CCDModelExtension extends BaseModelExtension{

     CCDModelExtension() {
         super()
     }
     
     CCDModelExtension(builder) {
         super(builder)
     }

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
        
        return 1
        
    }//ccd extensions 
    
    
    String templateId() {
        '2.16.840.1.113883.10.20.1'
    }
    
    String extensionName() {
        "Continuity of Care Document (CCD)"
    }    
}
