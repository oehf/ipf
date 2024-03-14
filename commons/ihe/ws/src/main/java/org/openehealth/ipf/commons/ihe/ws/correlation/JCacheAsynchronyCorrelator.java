/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.correlation;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import javax.cache.Cache;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;

/**
 * JCache-based implementation of asynchronous message correlator.
 * @author Dmytro Rud
 */
public class JCacheAsynchronyCorrelator<AuditDatasetType extends WsAuditDataset> implements AsynchronyCorrelator<AuditDatasetType> {

    private static final String SERVICE_ENDPOINT_URI_SUFFIX = ".serviceEndpoint";
    private static final String CORRELATION_KEY_SUFFIX      = ".correlationKey";
    private static final String AUDIT_DATASET_SUFFIX        = ".auditDataset";
    private static final String ALTERNATIVE_KEY_SUFFIX      = ".alternativeKey";
    private static final String ALTERNATIVE_KEYS_SUFFIX     = ".alternativeKeys";

    private final Cache<String, Serializable> cache;

    public JCacheAsynchronyCorrelator(Cache<String, Serializable> cache) {
        this.cache = requireNonNull(cache, "ehcache instance");
    }

    private void put(String key, String suffix, Serializable value) {
        cache.put(key + suffix, value);
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> T get(String key, String suffix) {
        return (T) cache.get(key + suffix);
    }

    @Override
    public void storeServiceEndpointUri(String messageId, String serviceEndpointUri) {
        put(messageId, SERVICE_ENDPOINT_URI_SUFFIX, requireNonNull(serviceEndpointUri, "service endpoint URI"));
    }

    @Override
    public void storeCorrelationKey(String messageId, String correlationKey) {
        put(messageId, CORRELATION_KEY_SUFFIX, requireNonNull(correlationKey, "correlation key"));
    }

    @Override
    public void storeAuditDataset(String messageId, WsAuditDataset auditDataset) {
        put(messageId, AUDIT_DATASET_SUFFIX, requireNonNull(auditDataset, "audit dataset"));
    }

    @Override
    public String getServiceEndpointUri(String messageId) {
        return get(messageId, SERVICE_ENDPOINT_URI_SUFFIX);
    }

    @Override
    public String getCorrelationKey(String messageId) {
        return get(messageId, CORRELATION_KEY_SUFFIX);
    }

    @Override
    public AuditDatasetType getAuditDataset(String messageId) {
        return get(messageId, AUDIT_DATASET_SUFFIX);
    }

    @Override
    public void storeAlternativeKeys(String messageId, String... alternativeKeys) {
        requireNonNull(alternativeKeys, "alternative keys should be not null");
        for (var key : alternativeKeys) {
            put(key, ALTERNATIVE_KEY_SUFFIX, messageId);
        }
        put(messageId, ALTERNATIVE_KEYS_SUFFIX, alternativeKeys);
    }

    @Override
    public String getMessageId(String alternativeKey) {
        return get(alternativeKey, ALTERNATIVE_KEY_SUFFIX);
    }

    @Override
    public boolean delete(String messageId) {
        String[] alternativeKeys = get(messageId, ALTERNATIVE_KEYS_SUFFIX);
        if (alternativeKeys != null) {
            for (var key : alternativeKeys) {
                cache.remove(key + ALTERNATIVE_KEY_SUFFIX);
            }
        }
        cache.remove(messageId + ALTERNATIVE_KEYS_SUFFIX);
        cache.remove(messageId + CORRELATION_KEY_SUFFIX);
        cache.remove(messageId + AUDIT_DATASET_SUFFIX);
        cache.remove(messageId + SERVICE_ENDPOINT_URI_SUFFIX);
        return true;
    }
}
