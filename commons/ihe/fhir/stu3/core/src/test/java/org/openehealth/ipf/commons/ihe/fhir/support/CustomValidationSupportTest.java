/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.support;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.CustomValidationSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 */
public class CustomValidationSupportTest {

    private static final FhirContext FHIR_CONTEXT = FhirContext.forDstu3();

    @Test
    public void testFindStructureDefinition() {
        var customValidationSupport = new CustomValidationSupport(FHIR_CONTEXT);
        customValidationSupport.setPrefix("profiles/MHD-");
        var definition = customValidationSupport.fetchResource(StructureDefinition.class,
                CustomValidationSupport.HTTP_HL7_ORG_FHIR_STRUCTURE_DEFINITION + "DocumentReference");
        assertNotNull(definition);
        assertEquals(DocumentReference.class.getSimpleName(), definition.getType());
    }

}
