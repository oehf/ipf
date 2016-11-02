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

import lombok.experimental.Delegate;
import org.apache.camel.component.mina2.Mina2Consumer;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MllpConsumer wraps a Mina2Consumer for having a hook to shutdown some Mina
 * resources when the consumer is closing
 */
public class MllpConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MllpConsumer.class);

    // The reason for this interface is to convince the Delegate annotation to *not* delegate
    // the stop method. Weird API, really.
    private interface DoStop {
        @SuppressWarnings("unused")
        void stop() throws Exception;
    }

    @Delegate(excludes = DoStop.class)
    private final Mina2Consumer consumer;

    MllpConsumer(Mina2Consumer consumer) {
        // Everything will be delegated
        super(consumer.getEndpoint(), consumer.getProcessor());
        this.consumer = consumer;
    }

    @Override
    protected void handleException(String message, Throwable t) {
        super.handleException(message, t);
    }

    @Override
    protected void handleException(Throwable t) {
        super.handleException(t);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        IoAcceptor ioAcceptor = getAcceptor();
        if (ioAcceptor != null) {
            for (IoSession ss : ioAcceptor.getManagedSessions().values()) {
                CloseFuture future = ss.closeNow();
                if (!future.awaitUninterruptibly(1000)) {
                    LOG.warn("Could not close IoSession, consumer may hang");
                }
            }
            ioAcceptor.unbind();
            ioAcceptor.dispose(false);
        }
    }

}
