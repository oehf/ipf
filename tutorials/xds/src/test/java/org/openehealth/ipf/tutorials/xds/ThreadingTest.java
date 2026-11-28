/*
 * Copyright 2009 the original author or authors.
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.tutorials.xds.store.ByteArrayDataSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.APPROVED;
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS;

/**
 * Runs submissions, queries and retrievals concurrently, to find what is not thread-safe.
 *
 * @author Jens Riemschneider
 */
@Slf4j
@Disabled("Takes a while; run it when touching the store or the routes")
class ThreadingTest extends XdsServerTestBase {

    private static final int ROUNDS = 500;

    private final AtomicLong docId = new AtomicLong();

    @Test
    void concurrentTransactions() throws Exception {
        var tasks = new ArrayList<Callable<Void>>();
        for (var round = 0; round < ROUNDS; round++) {
            tasks.add(task(this::provideAndFind));
            tasks.add(task(this::registerAndFind));
            tasks.add(task(this::provideAndRetrieve));
        }

        var executorService = Executors.newFixedThreadPool(4);
        var failures = new ArrayList<Throwable>();
        var started = System.currentTimeMillis();
        try {
            for (var future : executorService.invokeAll(tasks)) {
                try {
                    future.get(1, TimeUnit.HOURS);
                } catch (ExecutionException e) {
                    failures.add(e.getCause());
                }
            }
        } finally {
            executorService.shutdown();
        }
        log.info("{} transactions finished in {} ms", tasks.size(), System.currentTimeMillis() - started);

        failures.forEach(failure -> log.error("transaction failed", failure));
        assertTrue(failures.isEmpty(), failures.size() + " of " + tasks.size() + " transactions failed");
    }

    private void provideAndFind() {
        var provide = provideRequest();
        var docEntry = provide.getDocuments().get(0).getDocumentEntry();
        assertEquals(SUCCESS, send(iti41, provide, Response.class).getStatus());
        assertFoundAlone(docEntry);
    }

    private void registerAndFind() {
        var register = SampleData.createRegisterDocumentSet();
        var docEntry = register.getDocumentEntries().get(0);
        docEntry.getPatientId().setId(UUID.randomUUID().toString());
        docEntry.setUniqueId("4.3.2.1." + docId.incrementAndGet());

        var response = send(iti42, register, Response.class);
        assertEquals(SUCCESS, response.getStatus(), response.toString());
        assertFoundAlone(docEntry);
    }

    private void provideAndRetrieve() {
        var provide = provideRequest();
        var response = send(iti41, provide, Response.class);
        assertEquals(SUCCESS, response.getStatus(), response.toString());

        var reference = new DocumentReference();
        reference.setDocumentUniqueId(provide.getDocuments().get(0).getDocumentEntry().getUniqueId());
        reference.setRepositoryUniqueId("something");
        var retrieve = new RetrieveDocumentSet();
        retrieve.getDocuments().add(reference);
        retrieve.getDocuments().add(reference);
        var retrieveResponse = send(iti43, retrieve, RetrievedDocumentSet.class);
        assertEquals(SUCCESS, retrieveResponse.getStatus(), retrieveResponse.toString());
        assertEquals(2, retrieveResponse.getDocuments().size());
        assertEquals("test", read(retrieveResponse.getDocuments().get(0).getDataHandler()));
        assertEquals("test", read(retrieveResponse.getDocuments().get(1).getDataHandler()));
    }

    /** A provide request for a new patient, with the content "test" and no hash or size to check. */
    private ProvideAndRegisterDocumentSet provideRequest() {
        var provide = SampleData.createProvideAndRegisterDocumentSet();
        var document = provide.getDocuments().get(0);
        document.setContent(DataHandler.class,
                new DataHandler(new ByteArrayDataSource("test".getBytes(StandardCharsets.UTF_8), "text/plain")));
        var docEntry = document.getDocumentEntry();
        docEntry.setHash(null);
        docEntry.setSize(null);
        docEntry.getPatientId().setId(UUID.randomUUID().toString());
        docEntry.setUniqueId("4.3.2.1." + docId.incrementAndGet());
        return provide;
    }

    private void assertFoundAlone(DocumentEntry docEntry) {
        List<DocumentEntry> found = findDocuments(docEntry.getPatientId(), APPROVED);
        assertEquals(1, found.size());
        assertEquals(docEntry.getUniqueId(), found.get(0).getUniqueId());
    }

    private static Callable<Void> task(Runnable transaction) {
        return () -> {
            transaction.run();
            return null;
        };
    }
}
