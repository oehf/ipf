/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.tutorials.fhir

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.rest.client.api.IGenericClient
import org.hl7.fhir.instance.model.Parameters
import org.hl7.fhir.instance.model.Patient
import org.hl7.fhir.instance.model.StringType
import org.hl7.fhir.instance.model.UriType
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83Constants

/**
 * Entry point for command line execution.
 * @author Christian Ohr
 */
public class Client {
      
    /**
     * Standard main.
     * @param args
     *          used to define usage of SSL.
     * @throws Exception
     *          any problem that occurred.
     */
    public static void main(String[] args) {
        FhirContext context = FhirContext.forDstu2Hl7Org()

        Parameters inParams = new Parameters()
        inParams.addParameter()
                .setName(Constants.SOURCE_IDENTIFIER_NAME)
                .setValue(new StringType("urn:oid:1.2.3.4|0815"))
        inParams.addParameter()
                .setName(Constants.TARGET_SYSTEM_NAME)
                .setValue(new UriType("urn:oid:1.2.3.4.6"))

        IGenericClient client = context.newRestfulGenericClient("http://localhost:9091/")
        Parameters result = client.operation()
                .onType(Patient.class)
                .named(Iti83Constants.PIXM_OPERATION_NAME)
                .withParameters(inParams)
                .useHttpGet()
                .execute()

        System.out.println(context.newXmlParser().encodeResourceToString(result))
    }

}
