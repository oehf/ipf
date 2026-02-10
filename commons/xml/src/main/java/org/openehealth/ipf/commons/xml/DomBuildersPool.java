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
 *
 * @author Dmytro Rud
 * @since 3.5.1
 */
@Slf4j
@UtilityClass
public class DomBuildersPool {
    public static final String POOL_SIZE_PROPERTY = DomBuildersPool.class.getName() + ".POOLSIZE";
    private static final int DEFAULT_POOL_SIZE = 100;

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
        if (documentBuilder != null) {
            try {
                documentBuilder.reset();
                POOL.restore(documentBuilder);
            } catch (Exception e) {
                log.warn("Failed to return document builder to pool", e);
            }
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
                
                var documentBuilder = factory.newDocumentBuilder();
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
