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
package org.openehealth.ipf.commons.xml;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.core.pool.PoolEvictor;
import org.vibur.objectpool.ConcurrentPool;
import org.vibur.objectpool.PoolObjectFactory;
import org.vibur.objectpool.PoolService;
import org.vibur.objectpool.util.ConcurrentLinkedQueueCollection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.function.Function;

/**
 * Pool for DOM document builders (which are not thread-safe).
 * <p>
 * Document builders retain their internal parser buffers across {@link DocumentBuilder#reset()}, so
 * builders left idle after a traffic burst keep that memory allocated. The pool is therefore
 * registered with {@link PoolEvictor}, which destroys builders that stay idle; see that class for
 * the interval and sampling system properties.
 *
 * @author Dmytro Rud
 * @since 3.5.1
 */
@Slf4j
@UtilityClass
public class DomBuildersPool {
    public static final String POOL_SIZE_PROPERTY = DomBuildersPool.class.getName() + ".POOLSIZE";
    private static final int DEFAULT_POOL_SIZE = 100;

    /**
     * Creating a factory performs JAXP provider lookup and is far more expensive than creating a
     * builder from it, so it is done once. A {@code DocumentBuilderFactory} is not thread-safe to
     * <em>configure</em>, but it is only configured here, in a static initializer; afterwards
     * {@link DocumentBuilderFactory#newDocumentBuilder()} is the only method called on it.
     */
    private static final DocumentBuilderFactory FACTORY = createFactory();

    private static final PoolService<DocumentBuilder> POOL;
    static {
        int poolSize = Integer.getInteger(POOL_SIZE_PROPERTY, -1);
        POOL = new ConcurrentPool<>(
                new ConcurrentLinkedQueueCollection<>(),
                new DocumentBuilderPoolFactory(),
                0,
                (poolSize > 0) ? poolSize : DEFAULT_POOL_SIZE,
                false);
        log.debug("Initialized DomBuildersPool with size: {}", (poolSize > 0) ? poolSize : DEFAULT_POOL_SIZE);
        PoolEvictor.register(POOL, "DomBuildersPool");
    }

    private static DocumentBuilderFactory createFactory() {
        try {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            // Security features to prevent XXE attacks
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            return factory;
        } catch (ParserConfigurationException e) {
            log.error("Failed to configure DocumentBuilderFactory", e);
            throw new IllegalStateException("Failed to configure DocumentBuilderFactory", e);
        }
    }


    /**
     * Returns a document builder instance from the pool.
     * <p>
     * <strong>Important:</strong> The caller MUST return the builder to the pool
     * using {@link #restore(DocumentBuilder)} when finished. Consider using
     * {@link #use(Function)} for automatic resource management.
     *
     * @return a document builder instance
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
        if (documentBuilder == null) {
            return;
        }
        // Every take() must be matched by exactly one restore(), otherwise the pool permanently
        // loses capacity and eventually take() blocks forever. If reset() fails we therefore still
        // hand the builder back, but flagged as invalid so the pool discards it instead of reusing
        // a builder in an unknown state.
        var valid = true;
        try {
            documentBuilder.reset();
        } catch (Exception e) {
            valid = false;
            log.warn("Failed to reset document builder, discarding it", e);
        }
        try {
            POOL.restore(documentBuilder, valid);
        } catch (Exception e) {
            log.warn("Failed to return document builder to pool", e);
        }
    }

    /**
     * Takes a document builder object from the pool, uses it to execute the given operation,
     * and returns it to the pool.
     * <p>
     * This is the recommended way to use document builders as it ensures proper resource cleanup.
     *
     * @param operation operation to execute using a document builder
     * @param <R>       operation return type
     * @return result of the execution of the operation
     * @throws RuntimeException if the operation throws an exception
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

    private static class DocumentBuilderPoolFactory implements PoolObjectFactory<DocumentBuilder> {
        @Override
        public DocumentBuilder create() {
            try {
                var documentBuilder = FACTORY.newDocumentBuilder();
                log.debug("Created a new document builder {}", documentBuilder);
                return documentBuilder;
            } catch (ParserConfigurationException e) {
                log.error("Failed to create DocumentBuilder", e);
                throw new RuntimeException("Failed to create DocumentBuilder", e);
            }
        }

        @Override
        public boolean readyToTake(DocumentBuilder obj) {
            return obj != null;
        }

        @Override
        public boolean readyToRestore(DocumentBuilder obj) {
            return obj != null;
        }

        @Override
        public void destroy(DocumentBuilder obj) {
            // DocumentBuilder doesn't require explicit cleanup
        }
    }
}
