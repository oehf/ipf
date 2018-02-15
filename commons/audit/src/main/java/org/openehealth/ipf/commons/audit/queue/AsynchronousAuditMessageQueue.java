/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.queue;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Audit queue that uses an injectable {@link ExecutorService} to asynchonously send away audit events.
 * When this queue is {@link #shutdown() shut down}, the executor service is also, waiting for at most
 * {@link #shutdownTimeoutSeconds} until all pending events are sent.
 * <p>
 * Note that the {@link ExecutorService} must be explicitly set, otherwise the implementation sends away the event synchronously.
 * The parameters of the {@link ExecutorService} determine the behavior of the queue implementation, e.g. in case the
 * audit destination is not reachable.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class AsynchronousAuditMessageQueue extends AbstractAuditMessageQueue {

    private static final Logger LOG = LoggerFactory.getLogger(AsynchronousAuditMessageQueue.class);

    private ExecutorService executorService;
    private int shutdownTimeoutSeconds = 30;

    /**
     * Sets the executor service. If this is null (or not used), audit events are sent synchronously
     *
     * @param executorService executor service
     */
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Sets the timeout before the executor service closes. Defaults to 10
     *
     * @param shutdownTimeoutSeconds timeout before the executor service closes
     */
    public void setShutdownTimeoutSeconds(int shutdownTimeoutSeconds) {
        this.shutdownTimeoutSeconds = shutdownTimeoutSeconds;
    }

    @Override
    protected void handle(AuditContext auditContext, String... auditRecords) {
        Runnable runnable = runnable(auditContext, auditRecords);
        if (executorService != null && !executorService.isShutdown()) {
            CompletableFuture.runAsync(runnable, executorService)
                    .exceptionally(e -> {
                        auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditRecords);
                        return null;
                    });
        } else {
            runnable.run();
        }
    }

    private Runnable runnable(AuditContext auditContext, String... auditRecords) {
        return () -> {
            try {
                auditContext.getAuditTransmissionProtocol().send(auditContext, auditRecords);
            } catch (Exception e) {
                throw new AuditException(e);
            }
        };
    }

    @Override
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(shutdownTimeoutSeconds, TimeUnit.SECONDS)) {
                    LOG.warn("Timeout occurred when flushing Audit events, some events might have been lost");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                LOG.warn("Thread interrupt when flushing ATNA events, some events might have been lost", e);
            }
        }
    }

}
