/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class CustomValidationSupportTest {

    private static final FhirContext FHIR_CONTEXT = FhirContext.forDstu3();
    @Test
    public void testFindStructureDefinition() {
        CustomValidationSupport customValidationSupport = new CustomValidationSupport();
        customValidationSupport.setPrefix("profiles/MHD-");
        StructureDefinition definition = customValidationSupport.fetchResource(FHIR_CONTEXT, StructureDefinition.class,
                CustomValidationSupport.HTTP_HL7_ORG_FHIR_STRUCTURE_DEFINITION + "DocumentReference");
        assertNotNull(definition);
        assertEquals(DocumentReference.class.getSimpleName(), definition.getType());
    }

    @Test
    public void testFindUnknownStructureDefinition() {
        CustomValidationSupport customValidationSupport = new CustomValidationSupport();
        customValidationSupport.setPrefix("profiles/Gablorg-");
        StructureDefinition definition = customValidationSupport.fetchResource(FHIR_CONTEXT, StructureDefinition.class,
                CustomValidationSupport.HTTP_HL7_ORG_FHIR_STRUCTURE_DEFINITION + "DocumentReference");
        assertNull(definition);
    }
}
