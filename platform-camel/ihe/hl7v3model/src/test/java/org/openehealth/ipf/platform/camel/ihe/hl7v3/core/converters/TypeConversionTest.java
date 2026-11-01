/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.converters;

import net.ihe.gazelle.hl7v3.datatypes.II;
import jakarta.xml.bind.JAXBException;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultExchange;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.XcpdQueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.XcpdQueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PatientRecordTransformersTest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.XcpdQueryRequestTransformerTest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses.PdqV3QueryResponseTransformerTest;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti44RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti44ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti46RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti46ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47ResponseValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti55RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti55ResponseValidator;

public class TypeConversionTest extends StandardTestContainer {

    @BeforeAll
    public static void beforeAll() {
        startServer(new CXFServlet(), "context.xml");
    }

    @Test
    public void testPixV3QueryRequestTranslation() throws Exception {
        PixV3QueryRequest request = new PixV3QueryRequest();
        request.setReceiver(new Device());
        request.setSender(new Device());
        request.setQueryPatientId(new II("patient1", "3.14.15.926"));

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(request);

        String s = exchange.getMessage().getMandatoryBody(String.class);
        assertTrue(s.startsWith("<"));
    }

    @Test
    public void testPdqV3QueryRequestTranslation() throws Exception {
        final var request = PdqV3QueryRequestTransformerTest.getSampleQuery();

        final var xml = toValidatedXml(request, iti47RequestValidator());
        final var parsed = fromXml(xml, PdqV3QueryRequest.class);
        assertEquals(request.getOtherIDsScopingOrganizationOids(), parsed.getOtherIDsScopingOrganizationOids());
        assertEquals(request.getLivingSubjectBirthTime(), parsed.getLivingSubjectBirthTime());
    }

    @Test
    public void testPdqV3QueryResponseTranslation() throws Exception {
        final var response = PdqV3QueryResponse.fromQuery(PdqV3QueryRequestTransformerTest.getSampleQuery());
        response.setMessageId(new II("1.2.3.4", "response-1"));
        response.getPatients().add(PdqV3QueryResponseTransformerTest.getSamplePatient());
        response.setDataFound();

        final var xml = toValidatedXml(response, iti47ResponseValidator());
        final var parsed = fromXml(xml, PdqV3QueryResponse.class);
        assertEquals("OK", parsed.getQueryResponseCode());
        assertEquals(1, parsed.getPatients().size());
    }

    @Test
    public void testXcpdQueryRequestTranslation() throws Exception {
        final var request = XcpdQueryRequestTransformerTest.getSampleQuery();
        request.setDeferredResponse("https://initiating.example.org/xcpd-deferred");

        final var xml = toValidatedXml(request, iti55RequestValidator());
        final var parsed = fromXml(xml, XcpdQueryRequest.class);
        assertTrue(parsed.isDeferred());
        assertEquals(request.getCommunityPatientIdAssigningAuthority(), parsed.getCommunityPatientIdAssigningAuthority());
    }

    @Test
    public void testXcpdQueryResponseTranslation() throws Exception {
        final var response = XcpdQueryResponse.fromQuery(XcpdQueryRequestTransformerTest.getSampleQuery());
        response.setMessageId(new II("1.2.3.4", "response-1"));
        response.getPatients().add(PdqV3QueryResponseTransformerTest.getSamplePatient());
        response.setDataFound();

        final var xml = toValidatedXml(response, iti55ResponseValidator());
        final var parsed = fromXml(xml, XcpdQueryResponse.class);
        assertEquals("OK", parsed.getQueryResponseCode());
        assertEquals("NotHealthDataLocator", parsed.getPatients().get(0).getCustodianCode().getCode());
    }

    @Test
    public void testPatientRecordAddedTranslation() throws Exception {
        final var request = PatientRecordTransformersTest.fill(new PatientRecordAddedRequest());
        final var xml = toValidatedXml(request, iti44RequestValidator());
        final var parsed = fromXml(xml, PatientRecordAddedRequest.class);
        assertEquals(request.getPatient().getBirthTime(), parsed.getPatient().getBirthTime());
    }

    @Test
    public void testPatientRecordRevisedTranslation() throws Exception {
        final var request = PatientRecordTransformersTest.fill(new PatientRecordRevisedRequest());
        toValidatedXml(request, iti44RequestValidator());
        final var xml = toValidatedXml(request, iti46RequestValidator());
        final var parsed = fromXml(xml, PatientRecordRevisedRequest.class);
        assertEquals(request.getPatient().getCustodianOid(), parsed.getPatient().getCustodianOid());
    }

    @Test
    public void testPatientDuplicatesResolvedTranslation() throws Exception {
        final var request = PatientRecordTransformersTest.fill(new PatientDuplicatesResolvedRequest());
        request.getObsoletePatientIds().add(new II("1.2.840.114350.1.13.99997.2.3412", "4711"));
        final var xml = toValidatedXml(request, iti44RequestValidator());
        final var parsed = fromXml(xml, PatientDuplicatesResolvedRequest.class);
        assertEquals("4711", parsed.getObsoletePatientIds().get(0).getExtension());
    }

    @Test
    public void testAcceptAcknowledgementTranslation() throws Exception {
        final var request = PatientRecordTransformersTest.fill(new PatientRecordAddedRequest());
        final var acknowledgement = AcceptAcknowledgement.fromRequest(request);
        acknowledgement.setMessageId(new II("1.2.3.4", "ack-1"));
        acknowledgement.setApplicationAccept();

        toValidatedXml(acknowledgement, iti44ResponseValidator());
        final var xml = toValidatedXml(acknowledgement, iti46ResponseValidator());
        final var parsed = fromXml(xml, AcceptAcknowledgement.class);
        assertEquals("AA", parsed.getAcknowledgementTypeCode());
        assertEquals("35423", parsed.getTargetMessageId().getExtension());
    }

    @Test
    public void testDoctypeIsRejected() {
        final var xml = "<!DOCTYPE PRPA_IN201305UV02 [<!ENTITY x \"35423\">]>" +
                "<PRPA_IN201305UV02 xmlns=\"urn:hl7-org:v3\" ITSVersion=\"XML_1.0\">" +
                "<id root=\"1.2.840.114350.1.13.0.1.7.1.1\" extension=\"&x;\"/></PRPA_IN201305UV02>";
        final Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(xml);
        final Throwable exception = assertThrows(InvalidPayloadException.class,
                () -> exchange.getMessage().getMandatoryBody(PdqV3QueryRequest.class));
        // InvalidPayloadException -> TypeConversionException -> JAXBException -> SAXParseException
        Throwable cause = exception;
        while (cause != null && !(cause instanceof JAXBException)) {
            cause = cause.getCause();
        }
        assertTrue(cause instanceof JAXBException jaxbException
                && String.valueOf(jaxbException.getLinkedException()).contains("DOCTYPE"),
                () -> "Expected a DOCTYPE rejection, got " + exception);
    }

    /**
     * Converts the body to XML via the registered type converters and validates it against the
     * XML schema and schematron rules of the transaction.
     */
    private String toValidatedXml(final Object body, final Processor validator) throws Exception {
        final Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(body);
        final var xml = exchange.getMessage().getMandatoryBody(String.class);
        exchange.getMessage().setBody(xml);
        validator.process(exchange);
        return xml;
    }

    private <T> T fromXml(final String xml, final Class<T> type) throws Exception {
        final Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(xml);
        return exchange.getMessage().getMandatoryBody(type);
    }

}
