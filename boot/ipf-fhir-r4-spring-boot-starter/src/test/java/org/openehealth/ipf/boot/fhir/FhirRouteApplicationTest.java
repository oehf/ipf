/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.boot.fhir;

import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.DefaultFhirRegistry;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Asserts that a FHIR route of a Spring Boot application is served by the FHIR servlet of this starter: the
 * endpoint finds the servlet bean by its (non-default) name and connects to the servlet's own
 * {@link org.openehealth.ipf.commons.ihe.fhir.FhirRegistry}, not to the registry shared JVM-wide by servlet name.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@SpringBootTest(
        classes = {TestApplication.class, FhirRouteApplicationTest.Routes.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=fhir-route-test",
                "ipf.fhir.servlet.name=" + FhirRouteApplicationTest.SERVLET_NAME,
                "ipf.atna.audit-enabled=false"
        })
public class FhirRouteApplicationTest {

    static final String SERVLET_NAME = "PixmServlet";
    private static final String ENDPOINT = "pixm-iti83:translation?servletName=" + SERVLET_NAME;
    private static final String TARGET_SYSTEM = "urn:oid:1.2.3.4";
    private static final String TARGET_ID = "0815";

    @LocalServerPort
    private int port;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private IpfFhirServlet fhirServlet;

    @Test
    public void testEndpointUsesRegistryOfServletBean() {
        assertEquals(SERVLET_NAME, fhirServlet.getServletName());
        var registry = fhirServlet.getFhirRegistry();
        assertNotNull(registry);
        assertSame(registry, camelContext.getEndpoint(ENDPOINT, FhirEndpoint.class).getFhirRegistry());
        assertNotSame(registry, DefaultFhirRegistry.getFhirRegistry(SERVLET_NAME));
    }

    @Test
    public void testTransactionWorksOverHttp() throws Exception {
        var sourceIdentifier = URLEncoder.encode("urn:oid:1.2.3|4711", StandardCharsets.UTF_8);
        var request = HttpRequest.newBuilder(URI.create(
                        "http://localhost:" + port + "/fhir/Patient/$ihe-pix?sourceIdentifier=" + sourceIdentifier))
                .header("Accept", "application/fhir+json")
                .GET()
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        assertTrue(response.body().contains(TARGET_ID), response.body());
    }

    @TestConfiguration
    static class Routes {

        // TODO the starter's default ApacheProxyAddressStrategy (HAPI 8.12.1) calls
        //  UriComponentsBuilder.fromHttpRequest, which does not exist in Spring 7 anymore
        @Bean
        IServerAddressStrategy serverAddressStrategy() {
            return new IncomingRequestAddressStrategy();
        }

        @Bean
        RouteBuilder iti83Route() {
            return new RouteBuilder() {
                @Override
                public void configure() {
                    from(ENDPOINT)
                            .process(exchange -> {
                                var result = new Parameters();
                                result.addParameter()
                                        .setName("targetIdentifier")
                                        .setValue(new Identifier().setSystem(TARGET_SYSTEM).setValue(TARGET_ID));
                                exchange.getMessage().setBody(result);
                            });
                }
            };
        }
    }
}
