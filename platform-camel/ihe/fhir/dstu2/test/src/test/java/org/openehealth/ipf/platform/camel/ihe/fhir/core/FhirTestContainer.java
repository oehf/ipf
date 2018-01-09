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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Assert;
import org.openehealth.ipf.commons.ihe.core.atna.MockedAuditMessageQueue;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class FhirTestContainer extends StandardTestContainer {

    protected static IGenericClient client;
    protected static FhirContext context;

    protected static IGenericClient startClient(String base) {
        context = FhirContext.forDstu2Hl7Org();
        context.getRestfulClientFactory().setSocketTimeout(1000000); // for debugging
        client = context.newRestfulGenericClient(base);
        return client;
    }

    public void assertConformance(String type) {
        Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();
        assertEquals(1, conf.getRest().size());
        Conformance.ConformanceRestComponent component = conf.getRest().iterator().next();
        assertTrue(component.getResource().stream()
                .map(Conformance.ConformanceRestResourceComponent::getType)
                .anyMatch(type::equals));
    }

    protected void assertAndRethrow(BaseServerResponseException e, OperationOutcome.IssueType issueType) {
        // Check ATNA Audit
        MockedAuditMessageQueue sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0).getAuditMessage();
        assertEquals(
                RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE.getCode().intValue(),
                event.getEventIdentification().getEventOutcomeIndicator());
        assertAndRethrowException(e, issueType);
    }

    protected void assertAndRethrowException(BaseServerResponseException e, OperationOutcome.IssueType expectedIssue) {
        // Hmm, I wonder if this could not be done automatically...
        OperationOutcome oo = context.newXmlParser().parseResource(OperationOutcome.class, e.getResponseBody());
        Assert.assertEquals(OperationOutcome.IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
        Assert.assertEquals(expectedIssue, oo.getIssue().get(0).getCode());

        // Check ATNA Audit
        MockedAuditMessageQueue sender = getAuditSender();
        Assert.assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0).getAuditMessage();
        Assert.assertEquals(RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE.getCode().intValue(),
                event.getEventIdentification().getEventOutcomeIndicator());

        throw e;
    }

    // For quickly checking output
    protected void printAsXML(IBaseResource resource) {
        System.out.println(context.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource));
    }
}
