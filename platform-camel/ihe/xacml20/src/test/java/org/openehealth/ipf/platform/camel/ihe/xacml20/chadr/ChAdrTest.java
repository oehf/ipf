/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chadr;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Status;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import jakarta.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class ChAdrTest extends StandardTestContainer {

    @BeforeAll
    public static void beforeClass() {
        startServer(new CXFServlet(), "chadr-context.xml");
        Xacml20Utils.initializeHerasaf();
    }

    @BeforeEach
    public void before() {
        getAuditSender().clear();
    }

    private static String getUri(String suffix) {
        return "ch-adr://localhost:" + getPort() + "/ch-adr-" + suffix;
    }

    private static <T> T loadFile(String fn) throws Exception {
        var stream = ChAdrTest.class.getClassLoader().getResourceAsStream("messages/chadr/" + fn);
        var unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        var object = unmarshaller.unmarshal(stream);
        if (object instanceof JAXBElement jaxbElement) {
            object = jaxbElement.getValue();
        }
        return (T) object;
    }

    private static void checkCodeValueType(CodedValueType value, String[]... allowedCodes) {
        for (var allowed : allowedCodes) {
            if (allowed[0].equals(value.getCode()) && allowed[1].equals(value.getCodeSystemName()) && allowed[2].equals(value.getOriginalText())) {
                return;
            }
        }
        assertNull(value);
    }

    @Test
    public void redundantNewLineShowcase() throws Exception {
        /* ARRANGE **/
        var expectedII = new II();
        expectedII.setRoot("2.16.756.5.30.1.127.3.10.3");
        expectedII.setExtension("765000000000000000");


        var chadrQuery = (XACMLAuthzDecisionQueryType) loadFile("chadr-request-1.xml");
        var requestType = (RequestType) chadrQuery.getRest().get(0).getValue();

        var iiElement = requestType.getResources().get(0).getAttributes().get(1).getAttributeValues().get(0).getContent();

        /* Actual XML for [iiElement]

          ```xml
                  <Resource>
                      <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:resource:resource-id"
                                 DataType="http://www.w3.org/2001/XMLSchema#anyURI">
                          <AttributeValue>urn:uuid:5a478b92-0b20-40a9-9bee-30ce7d831ca2</AttributeValue>
                      </Attribute>
                      <Attribute AttributeId="urn:e-health-suisse:2015:epr-spid" DataType="urn:hl7-org:v3#II">
                          <AttributeValue>
                              <ns10:InstanceIdentifier root="2.16.756.5.30.1.127.3.10.3" extension="765000000000000000"/>
                          </AttributeValue>
                      </Attribute>
                      <Attribute AttributeId="urn:e-health-suisse:2015:policy-attributes:referenced-policy-set"
                                 DataType="http://www.w3.org/2001/XMLSchema#anyURI">
                          <AttributeValue>urn:e-health-suisse:2015:policies:access-level:full</AttributeValue>
                      </Attribute>
                  </Resource>
          ```

         */

        /* ASSERT **/
        var actualII = ((JAXBElement<II>) iiElement.get(1)).getValue();

        // Erroneous (?) Behaviour
        assertEquals(3, iiElement.size());
        assertTrue(((String) iiElement.get(0)).startsWith("\n"));
        assertTrue(((String) iiElement.get(2)).startsWith("\n"));

        // Correct Behaviour
        assertEquals(expectedII.getExtension(), actualII.getExtension());
        assertEquals(expectedII.getRoot(), actualII.getRoot());

    }

    @Test
    public void testQuerySuccess() throws Exception {
        doTestQuery("success", Xacml20Status.SUCCESS, EventOutcomeIndicator.Success, true, "urn:oid:1.44.567");
    }

    @Test
    public void testQueryFailure() throws Exception {
        doTestQuery("failure", Xacml20Status.RESPONDER_ERROR, EventOutcomeIndicator.SeriousFailure, false, "urn:oid:1.2.4");
    }

    private void doTestQuery(
        String suffix,
        Xacml20Status status,
        EventOutcomeIndicator outcomeIndicator,
        boolean decisionPresent,
        String issuerId) throws Exception
    {
        var response = (ResponseType) send(getUri(suffix), loadFile("chadr-request-1.xml"), ResponseType.class);
        assertEquals(status.getCode(), response.getStatus().getStatusCode().getValue());
        var assertion = (AssertionType) response.getAssertionOrEncryptedAssertion().get(0);
        assertEquals(issuerId, assertion.getIssuer().getValue());

        List<AuditMessage> messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());

        for (var message : messages) {
            var event = message.getEventIdentification();
            assertEquals(EventActionCode.Execute, event.getEventActionCode());
            assertEquals(outcomeIndicator, event.getEventOutcomeIndicator());
            checkCodeValueType(event.getEventID(), new String[]{"110112", "DCM", "Query"});
            assertEquals(1, event.getEventTypeCode().size());
            checkCodeValueType(event.getEventTypeCode().get(0), new String[]{"ADR", "e-health-suisse", "Authorization Decisions Query"});

            assertEquals(0, event.getPurposesOfUse().size());

            assertEquals(2, message.getActiveParticipants().size());
            assertEquals(1, message.getActiveParticipants().get(0).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(0).getRoleIDCodes().get(0), new String[]{"110153", "DCM", "Source Role ID"});
            assertEquals(1, message.getActiveParticipants().get(1).getRoleIDCodes().size());
            checkCodeValueType(message.getActiveParticipants().get(1).getRoleIDCodes().get(0), new String[]{"110152", "DCM", "Destination Role ID"});

            assertEquals(4, message.getParticipantObjectIdentifications().size());

            {
                var requesterParticipant = message.getParticipantObjectIdentifications().get(0);
                assertEquals(ParticipantObjectTypeCode.Person, requesterParticipant.getParticipantObjectTypeCode());
                assertEquals(ParticipantObjectTypeCodeRole.SecurityUserEntity, requesterParticipant.getParticipantObjectTypeCodeRole());
                checkCodeValueType(requesterParticipant.getParticipantObjectIDTypeCode(), new String[]{"HCP", "2.16.756.5.30.1.127.3.10.6", "Healthcare Professional"});
                assertEquals("7600000000000", requesterParticipant.getParticipantObjectID());
                assertEquals(0, requesterParticipant.getParticipantObjectQuery().length);
                assertTrue(requesterParticipant.getParticipantObjectDetails().isEmpty());
            }

            Map<String, String> expectedDecisionsByResourceIds = new java.util.HashMap<>(Map.of(
                "urn:uuid:5a478b92-0b20-40a9-9bee-30ce7d831ca2", "Permit",
                "urn:uuid:a43e8041-5afd-40bf-9c7c-9d9fc6f8c1a8", "Permit",
                "urn:uuid:1d78d91d-73c9-49b7-94f5-76b2a44e1c9c", "NotApplicable"));

            for (int i = 0; i < 3; ++i) {
                var resourceParticipant = message.getParticipantObjectIdentifications().get(i + 1);
                assertEquals(ParticipantObjectTypeCode.System, resourceParticipant.getParticipantObjectTypeCode());
                assertEquals(ParticipantObjectTypeCodeRole.SecurityResource, resourceParticipant.getParticipantObjectTypeCodeRole());
                checkCodeValueType(resourceParticipant.getParticipantObjectIDTypeCode(), new String[]{"ADR", "e-health-suisse", "Authorization Decisions Query"});
                String resourceId = resourceParticipant.getParticipantObjectID();
                assertTrue(expectedDecisionsByResourceIds.containsKey(resourceId));
                assertEquals(0, resourceParticipant.getParticipantObjectQuery().length);
                if (decisionPresent) {
                    assertEquals(1, resourceParticipant.getParticipantObjectDetails().size());
                    assertEquals("decision", resourceParticipant.getParticipantObjectDetails().get(0).getType());
                    assertEquals(expectedDecisionsByResourceIds.get(resourceId), new String(resourceParticipant.getParticipantObjectDetails().get(0).getValue()));
                } else {
                    assertTrue(resourceParticipant.getParticipantObjectDetails().isEmpty());
                }
                expectedDecisionsByResourceIds.remove(resourceId);
            }
        }
    }

}
