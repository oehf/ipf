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

package org.openehealth.ipf.boot.xds;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.correlation.SpringCacheAsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Asserts that a Spring Boot application depending on this starter starts up with an XDS route in it,
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
                "spring.application.name=xds-starter-test",
                "cxf.path=/services",
                "ipf.xds.caching=true",
                "ipf.atna.audit-enabled=false"
        })
public class XdsApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private IpfXdsConfigurationProperties properties;

    @Autowired
    private AsynchronyCorrelator asynchronyCorrelator;

    @Test
    public void testTheRouteIsRunning() {
        assertTrue(camelContext.getStatus().isStarted(), "the Camel context did not start");
        assertEquals(1, camelContext.getRoutes().size(), "expected exactly the ITI-18 route");
        var route = camelContext.getRoutes().get(0);
        assertTrue(camelContext.getRouteController().getRouteStatus(route.getRouteId()).isStarted(),
                "the ITI-18 route did not start");
    }

    @Test
    public void testTheServiceIsPublished() throws Exception {
        var response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(serviceUrl() + "?wsdl")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "no WSDL served at " + serviceUrl());
        assertTrue(response.body().contains("DocumentRegistry_Service"),
                "the WSDL served is not the one of ITI-18: " + response.body());
    }

    @Test
    public void testTheTransactionWorksOverHttp() {
        var response = producerTemplate.requestBody(
                "xds-iti18://localhost:" + port + "/services/" + Iti18TestRouteBuilder.SERVICE,
                SampleData.createFindDocumentsQuery(),
                QueryResponse.class);
        // the sample response carries PARTIAL_SUCCESS; that the transaction went over the wire is the point
        assertNotNull(response, "no response came back");
        assertNotEquals(Status.FAILURE, response.getStatus(), "the transaction itself failed");
    }

    @Test
    public void testCachingAsynchronyCorrelatorIsConfigured() {
        assertTrue(properties.isCaching());
        assertInstanceOf(SpringCacheAsynchronyCorrelator.class, asynchronyCorrelator);
    }

    private String serviceUrl() {
        return "http://localhost:" + port + "/services/" + Iti18TestRouteBuilder.SERVICE;
    }
}
