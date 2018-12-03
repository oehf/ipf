/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.core;

import groovy.lang.Closure;
import lombok.extern.slf4j.Slf4j;
import org.vibur.objectpool.ConcurrentPool;
import org.vibur.objectpool.PoolObjectFactory;
import org.vibur.objectpool.PoolService;
import org.vibur.objectpool.util.ConcurrentLinkedQueueCollection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.function.Function;

/**
 * Pool for DOM document builders (which are not thread-safe).
 *
 * @author Dmytro Rud
 * @since 3.5.1
 */
@Slf4j
public class DomBuildersPool {
    public static final String POOL_SIZE_PROPERTY = DomBuildersPool.class.getName() + ".POOLSIZE";
    private static final int DEFAULT_POOL_SIZE = 100;

    private DomBuildersPool() {
        throw new IllegalStateException("Utility class, not instantiable");
    }

    private static final PoolService<DocumentBuilder> POOL;
    static {
        int poolSize = Integer.getInteger(POOL_SIZE_PROPERTY, -1);
        POOL = new ConcurrentPool<>(new ConcurrentLinkedQueueCollection<>(), new DocumentBuilderPoolFactory(),
                0, (poolSize > 0) ? poolSize : DEFAULT_POOL_SIZE, false);
    }


    /**
     * Returns a document builder instance.
     */
    public static DocumentBuilder take() {
        return POOL.take();
    }

    /**
     * Returns a document builder (previously gained via {@link #take()}) to the pool.
     * This method MUST be called as soon as the use of the document builder is finished.
     *
     * @param documentBuilder document builder, <code>null</code> values are safe.
     */
    public static void restore(DocumentBuilder documentBuilder) {
        if (documentBuilder != null) {
            documentBuilder.reset();
            POOL.restore(documentBuilder);
            log.debug("Returned document builder {} to the pool", documentBuilder);
        }
    }

    /**
     * Takes a document builder object from the pool, uses it to execute the given operation,
     * and returns it to the pool.
     *
     * @param operation operation to execute using a document builder
     * @param <R>       operation return type
     * @return result of the execution of the operation
     */
    public static <R> R use(Function<DocumentBuilder, R> operation) {
        DocumentBuilder builder = null;
        try {
            builder = take();
            return operation.apply(builder);
        } finally {
            restore(builder);
        }
    }

    /**
     * Takes a document builder object from the pool, uses it to execute the given Groovy closure,
     * and returns it to the pool.
     *
     * @param closure   closure to execute using a document builder
     * @param <R>       closure return type
     * @return result of the execution of the closure
     */
    public static <R> R use(Closure<R> closure) {
        return use(closure::call);
    }


    private static class DocumentBuilderPoolFactory implements PoolObjectFactory<DocumentBuilder> {
        @Override
        public DocumentBuilder create() {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                log.debug("Created a new document builder {}", documentBuilder);
                return documentBuilder;
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean readyToTake(DocumentBuilder obj) {
            return true;
        }

        @Override
        public boolean readyToRestore(DocumentBuilder obj) {
            return true;
        }

        @Override
        public void destroy(DocumentBuilder obj) {
            // nop
        }
    }
}
