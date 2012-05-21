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
package org.openehealth.ipf.modules.cda.builder.content.document

import org.openehealth.ipf.modules.cda.*
import org.openhealthtools.ihe.common.cdar2.*
import org.eclipse.emf.ecore.xmi.XMLResource
import org.junit.Before
import org.junit.Test
import org.junit.Ignore
import org.openehealth.ipf.modules.cda.builder.AbstractCDAR2BuilderTest
import org.openehealth.ipf.modules.ccd.builder.CCDModelExtension
import org.junit.BeforeClass

/**
 * Test the CDAr2 Parser in conjunction with CCD Model Extenstion accessors
 *
 * @author Stefan Ivanov
 */
public class CCDModelExtensionsGetterTest extends AbstractCDAR2BuilderTest{
     
    static POCDMT000040ClinicalDocument ccdDoc
    static CDAR2Parser parser

    @BeforeClass
    public static void setUpClass(){
        parser = new CDAR2Parser()
        ccdDoc = parser.parse(
                getClass().getResourceAsStream("/builders/content/document/SampleCCDDocument.xml"))
    }
    
    @Before
    public void initialize(){
    	def loader = new CCDDefinitionLoader(builder)
    	loader.loadContinuityOfCare(loaded)
    	loader.loadPurpose(loaded)
    	loader.loadPayers(loaded)
    	loader.loadAdvanceDirectives(loaded)
    	loader.loadFunctionalStatus(loaded)
    	loader.loadAlerts(loaded)
    	loader.loadEncounters(loaded)
    	loader.loadFamilyHistory(loaded)
    	loader.loadImmunizations(loaded)
    	loader.loadMedicalEquipment(loaded)
    	loader.loadMedications(loaded)
    	loader.loadPlanOfCare(loaded)
    	loader.loadProblems(loaded)
    	loader.loadProcedures(loaded)
    	loader.loadResults(loaded)
    	loader.loadSocialHistory(loaded)
    	loader.loadVitalSigns(loaded)           
    	new CCDModelExtension(builder).register(registered)
    }

    @Test
    public void testCCDParser(){
        assert ccdDoc.component.structuredBody.component.size() == 16
    }

    @Test
    public void testCCDPurposeExtensions() {
        assert ccdDoc.purpose instanceof POCDMT000040Section
        assert ccdDoc.purpose.purposeActivity.size() == 1
        assert ccdDoc.purpose.purposeActivity[0] instanceof POCDMT000040EntryRelationship
    }

    @Test
    public void testCCDPayersExtensions() {
        assert ccdDoc.payers instanceof POCDMT000040Section
        assert ccdDoc.payers.coverageActivity.size() == 1
        def act  = ccdDoc.payers.coverageActivity[0]
        assert act instanceof POCDMT000040Act
        assert act.policyActivity.size() == 1
        def policyAct = act.policyActivity[0]
        assert policyAct instanceof POCDMT000040Act
        assert policyAct.payer instanceof POCDMT000040AssignedEntity
        assert policyAct.coveredParty instanceof POCDMT000040ParticipantRole
        assert policyAct.subscriber == null
        assert policyAct.authorizationActivity.size() == 1
        assert policyAct.authorizationActivity[0].promise instanceof POCDMT000040EntryRelationship
    }

    @Test
    public void testCCDAdvanceDirectivesExtensions() {
        assert ccdDoc.advanceDirectives instanceof POCDMT000040Section
        assert ccdDoc.advanceDirectives.advanceDirectiveObservation.size() == 1
        def obs  = ccdDoc.advanceDirectives.advanceDirectiveObservation[0]
        assert obs instanceof POCDMT000040Observation
        assert obs.verifier.size() == 1
        assert obs.advanceDirectiveStatus instanceof POCDMT000040Observation
        assert obs.advanceDirectiveReference instanceof POCDMT000040ExternalDocument
    }

    @Test
    public void testCCDSupportExtensions() {
        assert ccdDoc.recordTarget.patientRole.patient.guardian.flatten().size()== 0
        assert ccdDoc.nextOfKin.size()== 1
        assert ccdDoc.emergencyContact.size()== 0
        assert ccdDoc.caregiver.size()== 0
    }

    @Test
    public void testCCDFunctionalStatusExtensions() {
        assert ccdDoc.functionalStatus instanceof POCDMT000040Section
        assert ccdDoc.functionalStatus.problemAct.size()== 2
        def problemAct = ccdDoc.functionalStatus.problemAct[0]
        assert problemAct.problemObservation.size()== 1
        assert problemAct.problemObservation[0].functionalStatusStatus instanceof POCDMT000040Observation
    }

    @Test
    public void testCCDProblemsExtensions() {
        assert ccdDoc.problems instanceof POCDMT000040Section
        assert ccdDoc.problems.problemAct.size()== 4
        assert ccdDoc.problems.problemAct[0].problemObservation.size()== 1
        assert ccdDoc.problems.problemAct[0].problemObservation[0].problemStatus instanceof POCDMT000040Observation
        assert ccdDoc.problems.problemAct[2].problemObservation.size()== 1
        assert ccdDoc.problems.problemAct[2].episodeObservation instanceof POCDMT000040Observation
        
    }

    @Test
    public void testCCDFamilyHistoryExtensions() {
        assert ccdDoc.familyHistory instanceof POCDMT000040Section
        assert ccdDoc.familyHistory.familyMember.size()== 2
        assert ccdDoc.familyHistory.familyMember[0] instanceof POCDMT000040Organizer
        assert ccdDoc.familyHistory.familyMember[0].familyPerson instanceof POCDMT000040RelatedSubject
        assert ccdDoc.familyHistory.familyMember[0].causeOfDeath.size()== 1
        assert ccdDoc.familyHistory.familyMember[0].causeOfDeath[0] instanceof POCDMT000040Observation
        assert ccdDoc.familyHistory.familyMember[0].causeOfDeath[0].cause.size()== 1
        assert ccdDoc.familyHistory.familyMember[0].causeOfDeath[0].cause[0] instanceof POCDMT000040Observation
        assert ccdDoc.familyHistory.familyMember[0].causeOfDeath[0].age
    }

    @Test
    public void testCCDSocialHistoryExtensions() {
        assert ccdDoc.socialHistory instanceof POCDMT000040Section
        assert ccdDoc.socialHistory.socialHistoryObservation.size()== 3
        assert ccdDoc.socialHistory.socialHistoryObservation[1].episodeObservation instanceof POCDMT000040Observation
    }

    @Test
    public void testCCDAlertsExtensions() {
        assert ccdDoc.alerts instanceof POCDMT000040Section
        assert ccdDoc.alerts.problemAct.size()== 3
        assert ccdDoc.alerts.problemAct[0].alertObservation.size()== 1
        def obs = ccdDoc.alerts.problemAct[0].alertObservation[0]
        assert obs instanceof POCDMT000040Observation
        assert obs.participantAgent.size()== 1
        assert obs.participantAgent[0] instanceof POCDMT000040ParticipantRole
        assert obs.alertStatus instanceof POCDMT000040Observation
        assert obs.reactionObservation.size()== 1
        assert obs.reactionObservation[0].severityObservation.size()== 0
    }

    @Test
    public void testCCDMedicationsExtensions() {
        assert ccdDoc.medications instanceof POCDMT000040Section
        assert ccdDoc.medications.medicationActivity.size()== 5
        assert ccdDoc.medications.supplyActivity.size()== 0
        assert ccdDoc.medications.medicationActivity[0] instanceof POCDMT000040SubstanceAdministration
        assert ccdDoc.medications.medicationActivity[0].manufacturedProduct instanceof POCDMT000040ManufacturedProduct
        assert ccdDoc.medications.medicationActivity[4].problemObservation.size()== 1
    }

    @Test
    public void testCCDMedicalEquipmentExtensions() {
        assert ccdDoc.medicalEquipment instanceof POCDMT000040Section
        assert ccdDoc.medicalEquipment.medicationActivity.size()== 0
        assert ccdDoc.medicalEquipment.supplyActivity.size()== 3
        assert ccdDoc.medicalEquipment.supplyActivity[1] instanceof POCDMT000040Supply
        assert ccdDoc.medicalEquipment.supplyActivity[1].productInstance.size()== 1

    }

    @Test
    public void testCCDImmunizationsExtensions() {
        assert ccdDoc.immunizations instanceof POCDMT000040Section
        assert ccdDoc.immunizations.medicationActivity.size()== 4
        assert ccdDoc.immunizations.supplyActivity.size()== 0
        assert ccdDoc.immunizations.medicationActivity[3].manufacturedProduct instanceof POCDMT000040ManufacturedProduct 
    }

    @Test
    public void testCCDVitalSignsExtensions() {
        assert ccdDoc.vitalSigns instanceof POCDMT000040Section
        assert ccdDoc.vitalSigns.vitalSignsOrganizer.size()== 2
        assert ccdDoc.vitalSigns.vitalSignsOrganizer[0].resultObservation.size()== 4
        assert ccdDoc.vitalSigns.vitalSignsOrganizer[0].resultObservation[0] instanceof POCDMT000040Observation
        
    }

    @Test
    public void testCCDResultsExtensions() {
        assert ccdDoc.results instanceof POCDMT000040Section
        assert ccdDoc.results.resultOrganizer.size()== 2
        assert ccdDoc.results.resultOrganizer[0].resultObservation.size()== 3
        assert ccdDoc.results.resultOrganizer[0].resultObservation[0] instanceof POCDMT000040Observation
    }

    @Test
    public void testCCDProceduresExtensions() {
        assert ccdDoc.procedures instanceof POCDMT000040Section
        assert ccdDoc.procedures.procedureActivity.size()== 1
        assert ccdDoc.procedures.procedureActivity.procedure?.size()== 1
        assert ccdDoc.procedures.procedureActivity.act[0] == null
        assert ccdDoc.procedures.procedureActivity.observation[0] == null
        assert ccdDoc.procedures.procedureActivity.procedure[0].problemActReason.size()== 0
        assert ccdDoc.procedures.procedureActivity.act.problemActReason.size()== 0
        assert ccdDoc.procedures.procedureActivity.procedure[0].problemObservationReason.size()== 0
        assert ccdDoc.procedures.procedureActivity.procedure[0].medicationActivity.size()== 0
        assert ccdDoc.procedures.procedureActivity.procedure[0].patientInstruction.size()== 0
        assert ccdDoc.procedures.procedureActivity.procedure[0].age == null
        assert ccdDoc.procedures.procedureActivity.procedure[0].encounterLocation.size()== 0
    }

    @Test
    public void testCCDEncountersExtensions() {
        assert ccdDoc.encounters instanceof POCDMT000040Section
        assert ccdDoc.encounters.encounterActivity.size()== 1
        assert ccdDoc.encounters.encounterActivity[0] instanceof POCDMT000040Encounter
        assert ccdDoc.encounters.encounterActivity[0].encounterLocation.size()== 1
    }

    @Test
    public void testCCDPlanOfCareExtensions() {
        assert ccdDoc.planOfCare instanceof POCDMT000040Section
        assert ccdDoc.planOfCare.planOfCareActivity.size()== 1
    }
}