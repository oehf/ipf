/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.experimental.Delegate;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.component.mina2.Mina2Producer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MllpProducer wraps a Mina2Producer for having a hook to shutdown some Mina
 * resources when the consumer is closing
 */
public class MllpProducer extends DefaultProducer {

    private static final Logger LOG = LoggerFactory.getLogger(MllpProducer.class);

    // The reason for this interface is to convince the Delegate annotation to *not* delegate
    // the stop method. Weird API, really.
    private interface CustomMethods {
        @SuppressWarnings("unused")
        void stop();
        @SuppressWarnings("unused")
        void process(Exchange exchange) throws Exception;
    }

    @Delegate(excludes = CustomMethods.class)
    private final Mina2Producer producer;

    MllpProducer(Mina2Producer producer) {
        // Everything will be delegated
        super(producer.getEndpoint());
        this.producer = producer;
    }

    /**
     * Mitigate CAMEL-17022 and close current session for any timeouts
     *
     * @param exchange exchange
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            producer.process(exchange);
        } catch (ExchangeTimedOutException e) {
            closeCurrentSession(producer);
            throw e;
        } finally {
            if (LOG.isDebugEnabled()) {
                IoSession session = getField(producer, IoSession.class, "session");
                LOG.debug("Used session {}", session);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    /**
     * Hack to circumvent accessing private members of the super class with the effect that
     * disposing the connector does NOT wait indefinitely.
     * @throws Exception
     */
    @Override
    protected void doStop() throws Exception {
        closeCurrentSession(producer);
        IoConnector connector = getField(producer, IoConnector.class, "connector");
        // Do NOT wait indefinitely
        connector.dispose(false);
        super.doStop();
    }

    private static void closeCurrentSession(Mina2Producer producer) throws Exception {
        IoSession session = getField(producer, IoSession.class, "session");
        if (session != null) {
            LOG.debug("Closing session {}", session);
            invoke(producer, "closeSessionIfNeededAndAwaitCloseInHandler", IoSession.class, session);
            LOG.debug("Closed session {}", session);
        }
    }

    private static <T> T getField(Object target, Class<T> clazz, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(target);
    }

    private static <T> void invoke(Object target, String name, Class<T> clazz, T arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = target.getClass().getDeclaredMethod(name, clazz);
        method.setAccessible(true);
        method.invoke(target, arg);
    }
}
