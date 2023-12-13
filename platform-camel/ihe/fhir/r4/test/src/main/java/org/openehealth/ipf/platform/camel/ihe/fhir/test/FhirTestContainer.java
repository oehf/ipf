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

package org.openehealth.ipf.platform.camel.ihe.fhir.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import javax.servlet.Servlet;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
public class FhirTestContainer extends StandardTestContainer {

    protected static IGenericClient client;
    protected static FhirContext clientFhirContext;
    protected static FhirContext serverFhirContext;

    public static void startServer(Servlet servlet, String appContextName, boolean secure, int serverPort, String servletName) {
        StandardTestContainer.startServer(servlet, appContextName, secure, serverPort, servletName);
        serverFhirContext = appContext.getBean("fhirContext", FhirContext.class);
    }

    protected static IGenericClient startClient(String base) {
        return startClient(base, fhirContext -> {});
    }

    protected static IGenericClient startClient(String base, Consumer<FhirContext> fhirContextCustomizer) {
        clientFhirContext = FhirContext.forR4();
        fhirContextCustomizer.accept(clientFhirContext);
        clientFhirContext.getRestfulClientFactory().setSocketTimeout(1000000); // for debugging
        client = clientFhirContext.newRestfulGenericClient(base);
        return client;
    }

    public void assertConformance(String type) {
        var conf = client.capabilities().ofType(CapabilityStatement.class).execute();
        assertEquals(1, conf.getRest().size());
        var component = conf.getRest().iterator().next();
        assertTrue(component.getResource().stream()
                .map(CapabilityStatement.CapabilityStatementRestResourceComponent::getType)
                .anyMatch(type::equals));
    }

    protected void assertAndRethrow(BaseServerResponseException e, OperationOutcome.IssueType issueType) {
        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        var event = sender.getMessages().get(0);
        assertEquals(
                EventOutcomeIndicator.MajorFailure,
                event.getEventIdentification().getEventOutcomeIndicator());
        assertAndRethrowException(e, issueType);
    }

    protected void assertAndRethrowException(BaseServerResponseException e, OperationOutcome.IssueType expectedIssue) {
        var parser = EncodingEnum.detectEncodingNoDefault(e.getResponseBody()).newParser(clientFhirContext);
        var oo = parser.parseResource(OperationOutcome.class, e.getResponseBody());
        assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
        assertEquals(expectedIssue, oo.getIssue().get(0).getCode());

        // Check ATNA Audit
        var sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        var event = sender.getMessages().get(0);
        assertEquals(EventOutcomeIndicator.MajorFailure,
                event.getEventIdentification().getEventOutcomeIndicator());

        throw e;
    }

    // For quickly checking output
    protected void printAsXML(IBaseResource resource) {
        System.out.println(clientFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource));
    }
}
