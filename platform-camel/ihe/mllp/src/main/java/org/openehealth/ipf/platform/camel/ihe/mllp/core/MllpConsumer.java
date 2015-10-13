package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.experimental.Delegate;
import org.apache.camel.component.mina2.Mina2Consumer;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.mina.core.service.IoAcceptor;

/**
 * MllpConsumer wraps a Mina2Consumer for having a hook to shutdown some Mina
 * resources when the consumer is closing
 */
public class MllpConsumer extends DefaultConsumer {

    // The reason for this interface is to convince the Delegate annotation to *not* delegate
    // the stop method. Weird API, really.
    private interface DoStop {
        @SuppressWarnings("unused")
        void stop() throws Exception;
    }

    @Delegate(excludes=DoStop.class)
    private final Mina2Consumer consumer;

    MllpConsumer(Mina2Consumer consumer) {
        // Everything will be delegated
        super(consumer.getEndpoint(), consumer.getProcessor());
        this.consumer = consumer;
    }

    /**
     * After stopping the consumer, stop the Mina acceptor, too
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        // Delegates call to Mina2Consumer
        IoAcceptor ioAcceptor = getAcceptor();
        if (ioAcceptor != null) {
            ioAcceptor.dispose(true);
        }
    }
}
