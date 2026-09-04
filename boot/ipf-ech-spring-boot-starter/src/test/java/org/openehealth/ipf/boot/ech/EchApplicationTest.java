/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.boot.ech;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.ech.EchUtils;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.Request;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.Response;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.ObjectFactory;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PidsToUPIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Asserts that a Spring Boot application depending on this starter starts up with an eCH route in it,
 * that the transaction's web service is really published, and that a request sent over HTTP arrives in
 * the route. This covers the wiring the starter is responsible for -- auto-configuration, the CXF bus
 * of {@code cxf-spring-boot-starter-jaxws}, the Camel context, and the audit context of the ATNA starter.
 *
 * @author Christian Ohr
 * @since 6.0
 */
@SpringBootTest(
        classes = {TestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=ech-starter-test",
                "cxf.path=/services",
                "ipf.atna.audit-enabled=false"
        })
public class EchApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private IpfEchConfigurationProperties properties;

    @Test
    public void testTheRouteIsRunning() {
        assertNotNull(properties);
        assertTrue(camelContext.getStatus().isStarted(), "the Camel context did not start");
        assertEquals(1, camelContext.getRoutes().size(), "expected exactly the eCH-0213 route");
        var route = camelContext.getRoutes().get(0);
        assertTrue(camelContext.getRouteController().getRouteStatus(route.getRouteId()).isStarted(),
                "the eCH-0213 route did not start");
    }

    @Test
    public void testTheServiceIsPublished() throws Exception {
        var response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(serviceUrl() + "?wsdl")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "no WSDL served at " + serviceUrl());
        assertTrue(response.body().contains("SpidManagementService"),
                "the WSDL served is not the one of eCH-0213: " + response.body());
    }

    @Test
    public void testTheTransactionWorksOverHttp() {
        var pidsToUpi = new PidsToUPIType();
        pidsToUpi.getContent().add(new ObjectFactory().createPidsToUPITypeVn(Ech0213TestRouteBuilder.VN));

        var content = new Request.Content();
        content.setSPIDCategory(EchUtils.getEPR_SPID_ID_CATEGORY());
        content.setResponseLanguage(Ech0213TestRouteBuilder.ECH_UTILS.getResponseLanguage());
        content.setActionOnSPID("generate");
        content.getPidsToUPI().add(pidsToUpi);

        var request = new Request();
        request.setHeader(Ech0213TestRouteBuilder.ECH_UTILS.createHeader("1020", "5"));
        request.setContent(content);

        var response = producerTemplate.requestBody(
                "ech-0213://localhost:" + port + "/services/" + Ech0213TestRouteBuilder.SERVICE,
                request,
                Response.class);
        assertNotNull(response, "no response came back");
        assertEquals(List.of(Ech0213TestRouteBuilder.SPID), response.getPositiveResponse().getPids().getSPID());
    }

    private String serviceUrl() {
        return "http://localhost:" + port + "/services/" + Ech0213TestRouteBuilder.SERVICE;
    }
}
