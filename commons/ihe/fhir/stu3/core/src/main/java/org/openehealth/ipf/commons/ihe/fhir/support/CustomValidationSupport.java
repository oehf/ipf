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
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

/**
 * Validation loader that first tries to load a custom structure definition before falling back to the
 * default.
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class CustomValidationSupport extends DefaultProfileValidationSupport {

    static final String HTTP_HL7_ORG_FHIR_STRUCTURE_DEFINITION = "http://hl7.org/fhir/StructureDefinition/";
    private String prefix = "profiles/";

    public CustomValidationSupport() {
    }

    public CustomValidationSupport(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public <T extends IBaseResource> T fetchResource(FhirContext fhirContext, Class<T> clazz, String uri) {
        if (uri.startsWith(HTTP_HL7_ORG_FHIR_STRUCTURE_DEFINITION)) {
            return (T) findProfile(fhirContext, uri.substring(HTTP_HL7_ORG_FHIR_STRUCTURE_DEFINITION.length()))
                    .orElseGet(() -> super.fetchResource(fhirContext, clazz, uri));
        } else {
            return super.fetchResource(fhirContext, clazz, uri);
        }
    }

    private <T extends IBaseResource> Optional<T> findProfile(FhirContext fhirContext, String resourceName) {
        String path = prefix + resourceName + ".xml";
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is != null) {
            String profileText = new Scanner(getClass().getClassLoader().getResourceAsStream(path), "UTF-8").useDelimiter("\\A").next();
            IParser parser = EncodingEnum.detectEncodingNoDefault(profileText).newParser(fhirContext);
            T structureDefinition = (T) parser.parseResource(StructureDefinition.class, profileText);
            return Optional.of(structureDefinition);
        }
        return Optional.empty();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
