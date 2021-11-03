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
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.component.netty.NettyConstants;
import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.NettyConsumerExceptionHandler;
import org.apache.camel.component.netty.NettyEndpoint;
import org.apache.camel.spi.ExceptionHandler;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.support.DefaultConsumer;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MllpConsumer wraps a NettyConsumer for having a hook to shutdown some Netty
 * resources when the consumer is closing
 */
public class MllpConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MllpConsumer.class);

    @Delegate
    private final NettyConsumer consumer;

    MllpConsumer(NettyConsumer consumer) {
        // Everything will be delegated
        super(consumer.getEndpoint(), consumer.getProcessor());
        this.consumer = consumer;
    }

    /* Need this with Netty?
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
    */


}
