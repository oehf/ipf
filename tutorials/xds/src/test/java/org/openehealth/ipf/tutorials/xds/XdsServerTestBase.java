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
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SortOrder;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Runs the registry and repository of {@code context.xml} in an embedded web container, and offers the
 * client side of the transactions to the tests.
 *
 * @author Christian Ohr
 */
public abstract class XdsServerTestBase extends StandardTestContainer {

    protected final String iti18 = endpoint("xds-iti18");
    protected final String iti41 = endpoint("xds-iti41");
    protected final String iti42 = endpoint("xds-iti42");
    protected final String iti43 = endpoint("xds-iti43");

    @BeforeAll
    static void startXdsServer() {
        // context.xml reads the port from xds.port, so it has to be known before the server starts
        var port = ServletServer.getFreePort();
        System.setProperty("xds.port", String.valueOf(port));
        // explicitly, because a secure server started earlier in this JVM may have left it set
        System.setProperty("xds.secure", "false");
        startServer(new CXFServlet(), "context.xml", false, port);
    }

    private static String endpoint(String transaction) {
        return transaction + "://localhost:" + getPort() + "/" + transaction;
    }

    /**
     * @return a request for the given query that asks for the full metadata rather than object references
     */
    protected static QueryRegistry leafClassRequest(Query query) {
        var request = new QueryRegistry(query);
        request.setReturnType(QueryReturnType.LEAF_CLASS);
        return request;
    }

    /**
     * Runs a query that is expected to succeed.
     */
    protected QueryResponse query(Query query) {
        var response = send(iti18, leafClassRequest(query), QueryResponse.class);
        assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        return response;
    }

    /**
     * @return the document entries of the patient with one of the given statuses, as stored
     */
    protected List<DocumentEntry> findDocuments(Identifiable patientId, AvailabilityStatus... status) {
        return findDocuments(patientId, query -> query.setStatus(List.of(status)));
    }

    /**
     * @param parameters further parameters of the FindDocuments query, after patient ID and status
     *                   {@code Approved}
     */
    protected List<DocumentEntry> findDocuments(Identifiable patientId, Consumer<FindDocumentsQuery> parameters) {
        var query = new FindDocumentsQuery();
        query.setPatientId(patientId);
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        parameters.accept(query);
        return query(query).getDocumentEntries();
    }

    /**
     * Runs a FindDocuments query for the approved documents of a patient, without expecting it to succeed.
     *
     * @param sortOrder  the requested order, or null
     * @param startIndex the start of the requested window, or null
     * @param maxResults the size of the requested window, or null
     */
    protected QueryResponse findDocumentsResponse(Identifiable patientId, SortOrder sortOrder,
                                                  Integer startIndex, Integer maxResults) {
        var query = new FindDocumentsQuery();
        query.setPatientId(patientId);
        query.setStatus(List.of(AvailabilityStatus.APPROVED));
        query.setSortOrder(sortOrder);
        var request = leafClassRequest(query);
        request.setStartIndex(startIndex);
        request.setMaxResults(maxResults);
        return send(iti18, request, QueryResponse.class);
    }

    protected Identifiable registerThreeDocuments() {
        return registerThreeDocuments(entry -> {});
    }

    /**
     * Registers three documents for one patient, each in a submission set and folder of its own, with the
     * unique ids {@code 2.2.2}, {@code 3.3.3} and {@code 1.1.1}. The order that is not the sorted one,
     * so that a test asserting the order cannot pass by accident.
     *
     * @param customizer changes each document entry before it is registered
     * @return the patient
     */
    protected Identifiable registerThreeDocuments(Consumer<DocumentEntry> customizer) {
        var sharedPatientId = UUID.randomUUID().toString();
        var oidSuffix = 0;
        for (var uniqueId : List.of("2.2.2", "3.3.3", "1.1.1")) {
            var register = SampleData.createRegisterDocumentSet();
            var docEntry = register.getDocumentEntries().get(0);

            // every entry needs its own identity, and the associations have to follow it. Unique ids are
            // OIDs, so they cannot simply be UUIDs like the entry uuids are.
            var newUuids = new HashMap<String, String>();
            for (XDSMetaClass entry : List.of(register.getSubmissionSet(), docEntry, register.getFolders().get(0))) {
                var fresh = "urn:uuid:" + UUID.randomUUID();
                newUuids.put(entry.getEntryUuid(), fresh);
                entry.setEntryUuid(fresh);
                entry.setUniqueId("1.2.3.4." + (++oidSuffix));
                entry.getPatientId().setId(sharedPatientId);
            }
            for (var association : register.getAssociations()) {
                association.setSourceUuid(newUuids.getOrDefault(association.getSourceUuid(), association.getSourceUuid()));
                association.setTargetUuid(newUuids.getOrDefault(association.getTargetUuid(), association.getTargetUuid()));
                association.setEntryUuid("urn:uuid:" + UUID.randomUUID());
            }
            docEntry.setUniqueId(uniqueId);
            customizer.accept(docEntry);

            var response = send(iti42, register, Response.class);
            assertEquals(Status.SUCCESS, response.getStatus(), response.toString());
        }
        return patient(sharedPatientId);
    }

    /**
     * @return a patient ID with the assigning authority of {@link SampleData}
     */
    protected static Identifiable patient(String id) {
        var patientId = SampleData.createRegisterDocumentSet().getSubmissionSet().getPatientId();
        patientId.setId(id);
        return patientId;
    }

    protected static String read(DataHandler dataHandler) {
        try (var inputStream = dataHandler.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
