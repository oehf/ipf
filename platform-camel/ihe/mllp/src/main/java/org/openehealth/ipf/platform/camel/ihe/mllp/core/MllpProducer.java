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
import org.apache.camel.component.mina.MinaProducer;
import org.apache.camel.support.DefaultProducer;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MllpProducer wraps a MinaProducer for having a hook to shutdown some Mina
 * resources when the consumer is closing
 */
public class MllpProducer extends DefaultProducer {

    // The reason for this interface is to convince the Delegate annotation to *not* delegate
    // the stop method. Weird API, really.
    private interface DoStop {
        @SuppressWarnings("unused")
        void stop() throws Exception;
    }

    @Delegate(excludes = DoStop.class)
    private final MinaProducer producer;

    MllpProducer(MinaProducer producer) {
        // Everything will be delegated
        super(producer.getEndpoint());
        this.producer = producer;
    }

    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Hack to circumvent accessing private members of the super class with the effect that
     * disposing the connector does NOT wait indefinitely.
     * @throws Exception
     */
    @Override
    protected void doStop() throws Exception {
        IoSession session = getField(producer, IoSession.class, "session");
        if (session != null) {
            invoke(producer, "closeSessionIfNeededAndAwaitCloseInHandler", IoSession.class, session);
        }
        IoConnector connector = getField(producer, IoConnector.class, "connector");
        // Do NOT wait indefinitely
        connector.dispose(false);
        super.doStop();
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
