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

import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.openehealth.ipf.modules.cda.builder.AbstractCDAR2BuilderTest
import org.openehealth.ipf.modules.ccd.builder.CCDModelExtension

/**
 * Test CCD Model Extenstions - add new components
 * @author Stefan Ivanov
 */
public class CCDModelExtensionsSetterTest extends AbstractCDAR2BuilderTest{
    POCDMT000040ClinicalDocument ccdDoc

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
        ccdDoc = builder.build(
                getClass().getResource('/builders/content/header/CCDHeaderExample.groovy'))
    }

    @Test
    public void testCCDInstance() {
        assert ccdDoc != null
    }
    
    @Test
    public void testCCDFull() {
        POCDMT000040ClinicalDocument ccdDoc1 = builder.build(
                getClass().getResource('/builders/content/header/CCDExample.groovy'))
    }
    

    @Test
    public void testCCDPurposeExtensions() {
        assert ccdDoc.purpose == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDPurposeExample.groovy'))
        ccdDoc.purpose = section
        assert ccdDoc.purpose != null
    }

    @Test
    public void testCCDPayersExtensions() {
        assert ccdDoc.payers == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDPayersExample.groovy'))
        ccdDoc.payers = section
        assert ccdDoc.payers != null
        assert ccdDoc.payers.coverageActivity.size() == 1
    }

    @Test
    public void testCCDAdvanceDirectivesExtensions() {
        assert ccdDoc.advanceDirectives == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDAdvanceDirectivesExample.groovy'))
        ccdDoc.advanceDirectives = section
        assert ccdDoc.advanceDirectives != null
        assert ccdDoc.advanceDirectives.advanceDirectiveObservation.size()== 1
    }

    @Test
    public void testCCDSupportExtensions() {
        assert ccdDoc.recordTarget.patientRole.patient.guardian.flatten().size()== 1
        assert ccdDoc.nextOfKin.size()== 1
        assert ccdDoc.emergencyContact.size()== 1
        assert ccdDoc.caregiver.size()== 1
    }

    @Test
    public void testCCDFunctionalStatusExtensions() {
        assert ccdDoc.functionalStatus == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDFunctionalStatusExample.groovy'))
        ccdDoc.functionalStatus = section
        assert ccdDoc.functionalStatus != null
    }

    @Test
    public void testCCDProblemsExtensions() {
        assert ccdDoc.problems == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDProblemsExample.groovy'))
        ccdDoc.problems = section
        assert ccdDoc.problems != null
    }

    @Test
    public void testCCDFamilyHistoryExtensions() {
        assert ccdDoc.familyHistory == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDFamilyHistoryExample.groovy'))
        ccdDoc.familyHistory = section
        assert ccdDoc.familyHistory != null
    }

    @Test
    public void testCCDSocialHistoryExtensions() {
        assert ccdDoc.socialHistory == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDSocialHistoryExample.groovy'))
        ccdDoc.socialHistory = section
        assert ccdDoc.socialHistory != null
    }

    @Test
    public void testCCDAlertsExtensions() {
        assert ccdDoc.alerts == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDAlertsExample.groovy'))
        ccdDoc.alerts = section
        assert ccdDoc.alerts != null
    }

    @Test
    public void testCCDMedicationsExtensions() {
        assert ccdDoc.medications == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDMedicationsExample.groovy'))
        ccdDoc.medications = section
        assert ccdDoc.medications != null
    }

    @Test
    public void testCCDMedicalEquipmentExtensions() {
        assert ccdDoc.medicalEquipment == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDMedicalEquipmentExample.groovy'))
        ccdDoc.medicalEquipment = section
        assert ccdDoc.medicalEquipment != null
    }

    @Test
    public void testCCDImmunizationsExtensions() {
        assert ccdDoc.immunizations == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDImmunizationsExample.groovy'))
        ccdDoc.immunizations = section
        assert ccdDoc.immunizations != null
    }

    @Test
    public void testCCDVitalSignsExtensions() {
        assert ccdDoc.vitalSigns == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDVitalSignsExample.groovy'))
        ccdDoc.vitalSigns = section
        assert ccdDoc.vitalSigns != null
    }

    @Test
    public void testCCDResultsExtensions() {
        assert ccdDoc.results == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDResultsExample.groovy'))
        ccdDoc.results = section
        assert ccdDoc.results != null
    }

    @Test
    public void testCCDProceduresExtensions() {
        assert ccdDoc.procedures == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDProceduresExample.groovy'))
        ccdDoc.procedures = section
        assert ccdDoc.procedures != null
    }

    @Test
    public void testCCDEncountersExtensions() {
        assert ccdDoc.encounters == null
        POCDMT000040Section section = builder.build(
                getClass().getResource('/builders/content/section/CCDEncountersExample.groovy'))
        ccdDoc.encounters = section
        assert ccdDoc.encounters != null
    }

    void showDocument(def document){
        CDAR2Renderer renderer = new CDAR2Renderer()
        def opts = [:]
        opts[XMLResource.OPTION_DECLARE_XML] = true
        opts[XMLResource.OPTION_ENCODING] = 'utf-8'
        println(renderer.render(document, opts))
    }
}