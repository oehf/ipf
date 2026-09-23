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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Checks that a {@link HostnameVerifier} passed to
 * {@link SslAwareApacheRestfulClient5Factory#initializeSecurityInformation} is authoritative. The
 * server certificate ({@code CN=localhost}, no subject alternative names) is addressed by IP
 * address, so JSSE's built-in endpoint identification always rejects it.
 */
public class SslAwareApacheRestfulClient5FactoryTest {

    private static final String PATIENT_JSON = "{\"resourceType\":\"Patient\",\"id\":\"p1\"}";

    private static HttpsServer server;
    private static SSLContext clientContext;
    private static String baseUrl;

    @BeforeAll
    public static void startServer() throws Exception {
        var serverContext = SSLContext.getInstance("TLS");
        var keyManagers = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagers.init(keyStore("/security/server.p12", "init"), "init".toCharArray());
        serverContext.init(keyManagers.getKeyManagers(), null, null);

        clientContext = SSLContext.getInstance("TLS");
        var trustManagers = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagers.init(keyStore("/security/ca.truststore", "initinit"));
        clientContext.init(null, trustManagers.getTrustManagers(), null);

        var loopback = InetAddress.getByName("127.0.0.1");
        server = HttpsServer.create(new InetSocketAddress(loopback, 0), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(serverContext));
        server.createContext("/fhir/", exchange -> {
            var body = PATIENT_JSON.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/fhir+json");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        baseUrl = "https://127.0.0.1:" + server.getAddress().getPort() + "/fhir/";
    }

    @AfterAll
    public static void stopServer() {
        server.stop(0);
    }

    @Test
    public void configuredHostnameVerifierIsAuthoritative() {
        var client = newClient(NoopHostnameVerifier.INSTANCE);

        var patient = client.read().resource(Patient.class).withId("p1").execute();

        assertEquals("p1", patient.getIdElement().getIdPart());
    }

    @Test
    public void hostnameIsVerifiedWithoutConfiguredHostnameVerifier() {
        var client = newClient(null);

        var e = assertThrows(FhirClientConnectionException.class,
            () -> client.read().resource(Patient.class).withId("p1").execute());
        assertInstanceOf(SSLHandshakeException.class, e.getCause());
    }

    private static IGenericClient newClient(HostnameVerifier hostnameVerifier) {
        var fhirContext = FhirContext.forR4();
        var factory = new SslAwareApacheRestfulClient5Factory(fhirContext);
        factory.setServerValidationMode(ServerValidationModeEnum.NEVER);
        factory.initializeSecurityInformation(true, clientContext, hostnameVerifier, null, null);
        fhirContext.setRestfulClientFactory(factory);
        return factory.newGenericClient(baseUrl);
    }

    private static KeyStore keyStore(String resource, String password) throws IOException, GeneralSecurityException {
        var keyStore = KeyStore.getInstance("PKCS12");
        try (var in = SslAwareApacheRestfulClient5FactoryTest.class.getResourceAsStream(resource)) {
            keyStore.load(in, password.toCharArray());
        }
        return keyStore;
    }
}
