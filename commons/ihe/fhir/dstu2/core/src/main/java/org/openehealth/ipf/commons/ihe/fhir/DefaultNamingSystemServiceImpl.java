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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

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

    public DefaultNamingSystemServiceImpl addNamingSystemsFromXml(Reader reader) {
        addNamingSystems(fhirContext.newXmlParser().parseResource(Bundle.class, reader));
        return this;
    }

    public void setNamingSystemsFromXml(URL... urls) throws IOException {
        for (URL url : urls) {
            addNamingSystemsFromXml(new BufferedReader(new InputStreamReader(url.openStream())));
        }
    }

    public DefaultNamingSystemServiceImpl addNamingSystemsFromJson(Reader reader) {
        addNamingSystems(fhirContext.newJsonParser().parseResource(Bundle.class, reader));
        return this;
    }

    public void setNamingSystemsFromJson(URL... urls) throws IOException {
        for (URL url : urls) {
            addNamingSystemsFromJson(new BufferedReader(new InputStreamReader(url.openStream())));
        }
    }

    public DefaultNamingSystemServiceImpl addNamingSystemsFromFhirServer(String url) {
        addNamingSystems(fhirContext.newRestfulGenericClient(url)
                .search()
                .forResource(NamingSystem.class)
                .returnBundle(Bundle.class)
                .execute());
        return this;
    }

}
