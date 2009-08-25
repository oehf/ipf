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

import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.*
import org.openehealth.ipf.modules.cda.builder.content.section.*
import org.openehealth.ipf.modules.cda.builder.content.header.*
import org.openehealth.ipf.modules.cda.builder.*
import org.openehealth.ipf.modules.cda.builder.content.section.CCDEncountersExtensionimport org.openehealth.ipf.modules.cda.builder.content.section.CCDFunctionalStatusExtension
/**
 * Metaclass model extensions for CCD documents
 *
 * @author Christian Ohr
 */
public class CCDModelExtension extends CompositeModelExtension{

    CCDModelExtension() {
    }

    CCDModelExtension(builder) {
        super(builder)
    }
    
    Collection modelExtensions() {
        [   new CDAR2ModelExtension(),
            new CCDMainActivityExtension(builder),
            new CCDPurposeExtension(builder),
            new CCDPayersExtension(builder),
            new CCDSupportExtension(builder),
            new CCDAdvanceDirectivesExtension(builder),
            new CCDFunctionalStatusExtension(builder),
            new CCDProblemsExtension(builder),
            new CCDFamilyHistoryExtension(builder),
            new CCDSocialHistoryExtension(builder),
            new CCDAlertsExtension(builder),
            new CCDMedicationsExtension(builder),
            new CCDMedicalEquipmentExtension(builder),
            new CCDImmunizationsExtension(builder),
            new CCDVitalSignsExtension(builder),
            new CCDResultsExtension(builder),
            new CCDProceduresExtension(builder),
            new CCDEncountersExtension(builder),
            new CCDPlanOfCareExtension(builder)
        ]
    }

    String templateId() {
        '2.16.840.1.113883.10.20.1'
    }

    String extensionName() {
        "Continuity of Care Document (CCD)"
    }

}