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
package org.openehealth.ipf.tutorials.xds;

import jakarta.activation.DataHandler;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer;
import org.openehealth.ipf.commons.ihe.ws.server.TomcatServer;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.tutorials.xds.store.ContentUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The standalone server in TLS mode, started the way {@link Server#main} starts it. A Provide and Register
 * only succeeds if the repository forwards it to the registry over TLS as well, since the server accepts
 * nothing else.
 *
 * @author Christian Ohr
 */
class SecureServerTest {

    private static TomcatServer server;
    private static ProducerTemplate client;
    private static int port;

    @BeforeAll
    static void startSecureServer() throws Exception {
        port = ServletServer.getFreePort();
        server = Server.start(port, true);
        // the client shares the server's Camel context, and with it the TLS configuration that trusts
        // the server's certificate
        var appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(server.getServletContext());
        client = appContext.getBean("template", ProducerTemplate.class);
    }

    @AfterAll
    static void stopSecureServer() {
        if (server != null) {
            server.stop();
        }
        // the other tests start plain servers in this JVM
        System.clearProperty("xds.secure");
    }

    @Test
    void provideAndRegisterOverTls() {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        var document = provide.getDocuments().get(0);
        var docEntry = document.getDocumentEntry();
        docEntry.getPatientId().setId(UUID.randomUUID().toString());
        docEntry.setUniqueId("1.2.54." + System.nanoTime());
        docEntry.setHash(ContentUtils.sha1(document.getContent(DataHandler.class)));
        docEntry.setSize(ContentUtils.size(document.getContent(DataHandler.class)));

        var response = client.requestBody(secureEndpoint("xds-iti41"), provide, Response.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());

        var query = new FindDocumentsQuery();
        query.setPatientId(docEntry.getPatientId());
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        var request = new QueryRegistry(query);
        request.setReturnType(QueryReturnType.LEAF_CLASS);
        var found = client.requestBody(secureEndpoint("xds-iti18"), request, QueryResponse.class);
        assertEquals(Status.SUCCESS, found.getStatus(), found.toString());
        assertEquals(List.of(docEntry.getUniqueId()),
                found.getDocumentEntries().stream().map(XDSMetaClass::getUniqueId).toList());
    }

    private static String secureEndpoint(String transaction) {
        return transaction + "://localhost:" + port + "/" + transaction + "?secure=true";
    }
}
