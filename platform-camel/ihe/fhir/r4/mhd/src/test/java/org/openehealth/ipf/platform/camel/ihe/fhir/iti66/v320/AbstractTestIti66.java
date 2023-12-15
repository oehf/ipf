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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66.v320;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

/**
 *
 */
abstract class AbstractTestIti66 extends FhirTestContainer {

    public static void startServer(String contextDescriptor) {
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        startServer(servlet, contextDescriptor, false, DEMO_APP_PORT, "FhirServlet");

        var loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLogRequestSummary(false);
        loggingInterceptor.setLogRequestHeaders(true);
        loggingInterceptor.setLogResponseBody(true);
        startClient(String.format("http://localhost:%d/", DEMO_APP_PORT), fhirContext -> {
            var clientFactory = new SslAwareMethanolRestfulClientFactory(fhirContext);
            clientFactory.setAsync(true);
            fhirContext.setRestfulClientFactory(clientFactory);
        }).registerInterceptor(loggingInterceptor);
    }

    protected ICriterion<?> manifestPatientIdentifierParameter() {
        return new TokenClientParam("patient.identifier").exactly()
                .systemAndIdentifier("urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1", "1");
    }

    protected ICriterion<?> statusParameter() {
        return new TokenClientParam("status").exactly().code("active");
    }

    protected ICriterion<?> manifestPatientReferenceParameter() {
        return new ReferenceClientParam("patient").hasId("http://fhirserver.org/Patient/1");
    }

    protected Bundle sendManually(ICriterion<?>... requestData) {
        var query = client.search()
                .forResource(DocumentManifest.class);

        if (requestData != null && requestData.length > 0) {
            query = query.where(requestData[0]);
            if (requestData.length > 1) {
                for (var i = 1; i < requestData.length; i++) {
                    query = query.and(requestData[i]);
                }
            }
        }
        return query
                .returnBundle(Bundle.class)
                .encodedXml()
                .execute();
    }

    protected Bundle sendViaProducer(ICriterion<?>... requestData) {
        return producerTemplate.requestBody("direct:input", requestData, Bundle.class);
    }

    protected Bundle nextPage(Bundle bundle) {
        return client.loadPage()
                .next(bundle)
                .execute();
    }

    protected Bundle previousPage(Bundle bundle) {
        return client.loadPage()
                .previous(bundle)
                .execute();
    }


}
