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
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.NamingSystem;

import java.io.Reader;

/**
 * Default Implementation of a NamingSystem that loads and parses a Bundle of NamingSystem
 * resources using a Reader or from a FHIR server.
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class DefaultNamingSystemServiceImpl extends AbstractNamingSystemServiceImpl {

    private FhirContext fhirContext;

    public DefaultNamingSystemServiceImpl(FhirContext fhirContext) {
        super();
        this.fhirContext = fhirContext;
    }

    public void setNamingSystemsFromXml(Reader reader) {
        setNamingSystems(fhirContext.newXmlParser().parseResource(Bundle.class, reader));
    }

    public void setNamingSystemsFromJson(Reader reader) {
        setNamingSystems(fhirContext.newJsonParser().parseResource(Bundle.class, reader));
    }

    public void setNamingSystemsFromFhirServer(String url) {
        setNamingSystems(fhirContext.newRestfulGenericClient(url)
                .search()
                .forResource(NamingSystem.class)
                .returnBundle(Bundle.class)
                .execute());
    }

}
