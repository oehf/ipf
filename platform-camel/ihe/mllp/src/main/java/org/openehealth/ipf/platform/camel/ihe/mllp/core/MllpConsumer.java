/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import org.apache.camel.*;
import org.apache.camel.component.mina.MinaConsumer;
import org.apache.camel.component.mina.MinaEndpoint;
import org.apache.camel.support.DefaultConsumer;
import org.apache.camel.spi.ExceptionHandler;
import org.apache.camel.spi.UnitOfWork;
import org.apache.mina.core.service.IoAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MllpConsumer wraps a MinaConsumer for having a hook to shutdown some Mina
 * resources when the consumer is closing
 */
public class MllpConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MllpConsumer.class);

    @Override
    public void start() {
        this.consumer.start();
    }

    @Override
    public Processor getProcessor() {
        return this.consumer.getProcessor();
    }

    @Override
    public AsyncProcessor getAsyncProcessor() {
        return this.consumer.getAsyncProcessor();
    }

    @Override
    public boolean isSuspendingOrSuspended() {
        return this.consumer.isSuspendingOrSuspended();
    }

    public IoAcceptor getAcceptor() {
        return this.consumer.getAcceptor();
    }

    @Override
    public boolean isSuspending() {
        return this.consumer.isSuspending();
    }

    @Override
    public boolean isStoppingOrStopped() {
        return this.consumer.isStoppingOrStopped();
    }

    @Override
    public boolean isStopping() {
        return this.consumer.isStopping();
    }

    @Override
    public Route getRoute() {
        return this.consumer.getRoute();
    }

    @Override
    public void setRoute(Route route) {
        this.consumer.setRoute(route);
    }

    @Override
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.consumer.setExceptionHandler(exceptionHandler);
    }

    @Override
    public boolean isStopped() {
        return this.consumer.isStopped();
    }

    @Override
    public boolean isStarting() {
        return this.consumer.isStarting();
    }

    @Override
    public boolean isRunAllowed() {
        return this.consumer.isRunAllowed();
    }

    public void setAcceptor(IoAcceptor acceptor) {
        this.consumer.setAcceptor(acceptor);
    }

    @Override
    public void shutdown() {
        this.consumer.shutdown();
    }

    @Override
    public UnitOfWork createUoW(Exchange exchange) throws Exception {
        return this.consumer.createUoW(exchange);
    }

    @Override
    public ServiceStatus getStatus() {
        return this.consumer.getStatus();
    }

    @Override
    public void resume() {
        this.consumer.resume();
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return this.consumer.getExceptionHandler();
    }

    @Override
    public boolean isSuspended() {
        return this.consumer.isSuspended();
    }

    @Override
    public boolean isStarted() {
        return this.consumer.isStarted();
    }

    @Override
    public MinaEndpoint getEndpoint() {
        return this.consumer.getEndpoint();
    }

    @Override
    public void doneUoW(Exchange exchange) {
        this.consumer.doneUoW(exchange);
    }

    @Override
    public void suspend() {
        this.consumer.suspend();
    }

    private final MinaConsumer consumer;

    MllpConsumer(MinaConsumer consumer) {
        // Everything will be delegated
        super(consumer.getEndpoint(), consumer.getProcessor());
        this.consumer = consumer;
    }

    @Override
    public void stop() {
        this.consumer.stop();
        // super.stop();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        var ioAcceptor = getAcceptor();
        if (ioAcceptor != null) {
            for (var ss : ioAcceptor.getManagedSessions().values()) {
                var future = ss.closeNow();
                if (!future.awaitUninterruptibly(1000)) {
                    LOG.warn("Could not close IoSession, consumer may hang");
                }
            }
            ioAcceptor.unbind();
            ioAcceptor.dispose(false);
        }
    }

}
