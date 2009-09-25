/**
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
package org.openehealth.ipf.modules.cda.builder.content.document;

import groovytools.builder.MetaBuilder;
import groovytools.builder.MetaBuilderException;

import java.io.IOException;
import java.util.Collection;

import org.openehealth.ipf.modules.cda.builder.CDAR2DefinitionLoader;

public class CCDDefinitionLoader extends CDAR2DefinitionLoader {

    public CCDDefinitionLoader(MetaBuilder builder) {
        super(builder);
    }

    @Override
    public void load(Collection<String> loaded) {
        try {
            super.load(loaded);
            loadContinuityOfCare(loaded);
            loadPurpose(loaded);
            loadPayers(loaded);
            loadAdvanceDirectives(loaded);
            loadFunctionalStatus(loaded);
            loadAlerts(loaded);
            loadEncounters(loaded);
            loadFamilyHistory(loaded);
            loadImmunizations(loaded);
            loadMedicalEquipment(loaded);
            loadMedications(loaded);
            loadPlanOfCare(loaded);
            loadProblems(loaded);
            loadProcedures(loaded);
            loadResults(loaded);
            loadSocialHistory(loaded);
            loadVitalSigns(loaded);           
        } catch (Exception e) {
            throw new MetaBuilderException(e);        
       }
    }
    
    public void loadContinuityOfCare(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/document/ContinuityOfCareDocumentBuilder.groovy", loaded);
        loadMainActivity(loaded);
        loadSupport(loaded);        
    }
    
    public void loadPurpose(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDPurposeBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadPayers(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDPayersBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadAdvanceDirectives(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDAdvanceDirectivesBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAdvanceDirectiveObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadFunctionalStatus(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDFunctionalStatusBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAgeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAlertObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDReactionObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEpisodeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDFunctionalStatusStatusObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadPlanOfCare(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDPlanOfCareBuilder.groovy", loaded);
        loadMedications(loaded);
        loadEncounters(loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }    

    public void loadFamilyHistory(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDFamilyHistoryBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAgeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadEncounters(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDEncountersBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEncounterActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEncounterLocationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientInstructionBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadProcedures(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDProceduresBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProcedureActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEncounterLocationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientInstructionBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAgeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductInstanceBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadResults(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDResultsBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDResultOrganizerBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadSocialHistory(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDSocialHistoryBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDSocialHistoryObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEpisodeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDSourceBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadVitalSigns(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDVitalSignsBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDResultOrganizerBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadAlerts(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDAlertsBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAgeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAlertObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDReactionObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEpisodeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductInstanceBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProcedureActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEncounterLocationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientInstructionBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadProblems(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDProblemsBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAgeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDAlertObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDReactionObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDEpisodeObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadMedications(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDMedicationsBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientInstructionBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductInstanceBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDReactionObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDSupplyActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationStatusBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadMedicalEquipment(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDMedicalEquipmentBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientInstructionBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductInstanceBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDReactionObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDSupplyActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationStatusBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadImmunizations(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/document/CCDCommonElementsBuilder.groovy", loaded);
        doLoad("/builders/content/section/CCDImmunizationsBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemActBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProblemObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientAwarenessBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDPatientInstructionBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDProductInstanceBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDReactionObservationBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDSupplyActivityBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDMedicationStatusBuilder.groovy", loaded);
        doLoad("/builders/content/entry/CCDStatusObservationBuilder.groovy", loaded);
        loadSource(loaded);   
        loadComments(loaded);        
    }
    
    public void loadMainActivity(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/header/CCDMainActivityBuilder.groovy", loaded);        
    }
    
    public void loadSupport(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/header/CCDSupportBuilder.groovy", loaded);        
    }
    
    public void loadComments(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/entry/CCDCommentsBuilder.groovy", loaded);        
    }
    
    public void loadSource(Collection<String> loaded) throws IOException {
        doLoad("/builders/content/entry/CCDSourceBuilder.groovy", loaded);        
    }

}
