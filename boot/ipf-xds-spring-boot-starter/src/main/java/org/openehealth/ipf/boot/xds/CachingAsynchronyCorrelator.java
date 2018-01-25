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
package org.openehealth.ipf.boot.xds;

import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static java.util.Objects.requireNonNull;

/**
 * Ehcache-based implementation of asynchronous message correlator.
 *
 * @author Dmytro Rud
 */
public class CachingAsynchronyCorrelator<AuditDatasetType extends WsAuditDataset> implements AsynchronyCorrelator<AuditDatasetType> {

    private static final String ASYNCHRONY_CORRELATOR_CACHE = "asynchronyCorrelatorCache";
    private static final String SERVICE_ENDPOINT_URI_SUFFIX = ".serviceEndpoint";
    private static final String CORRELATION_KEY_SUFFIX = ".correlationKey";
    private static final String AUDIT_DATASET_SUFFIX = ".auditDataset";
    private static final String ALTERNATIVE_KEY_SUFFIX = ".alternativeKey";
    private static final String ALTERNATIVE_KEYS_SUFFIX = ".alternativeKeys";

    private final Cache cache;

    public CachingAsynchronyCorrelator(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(ASYNCHRONY_CORRELATOR_CACHE);
    }

    @Override
    public void storeServiceEndpointUri(String messageId, String serviceEndpointUri) {
        cache.put(messageId + SERVICE_ENDPOINT_URI_SUFFIX, requireNonNull(serviceEndpointUri, "service endpoint URI"));
    }

    @Override
    public void storeCorrelationKey(String messageId, String correlationKey) {
        cache.put(messageId + CORRELATION_KEY_SUFFIX, requireNonNull(correlationKey, "correlation key"));
    }

    @Override
    public void storeAuditDataset(String messageId, WsAuditDataset auditDataset) {
        cache.put(messageId + AUDIT_DATASET_SUFFIX, requireNonNull(auditDataset, "audit dataset"));
    }

    @Override
    public String getServiceEndpointUri(String messageId) {
        return cache.get(messageId + SERVICE_ENDPOINT_URI_SUFFIX, String.class);
    }

    @Override
    public String getCorrelationKey(String messageId) {
        return cache.get(messageId + CORRELATION_KEY_SUFFIX, String.class);
    }

    @Override
    public AuditDatasetType getAuditDataset(String messageId) {
        return (AuditDatasetType) cache.get(messageId + AUDIT_DATASET_SUFFIX).get();
    }

    @Override
    public void storeAlternativeKeys(String messageId, String... alternativeKeys) {
        requireNonNull(alternativeKeys, "alternative keys should be not null");
        for (String key : alternativeKeys) {
            cache.put(key + ALTERNATIVE_KEY_SUFFIX, messageId);
        }
        cache.put(messageId + ALTERNATIVE_KEYS_SUFFIX, alternativeKeys);
    }

    @Override
    public String getMessageId(String alternativeKey) {
        return cache.get(alternativeKey + ALTERNATIVE_KEY_SUFFIX, String.class);
    }

    @Override
    public boolean delete(String messageId) {
        String[] alternativeKeys = cache.get(messageId + ALTERNATIVE_KEYS_SUFFIX, String[].class);
        if (alternativeKeys != null) {
            for (String key : alternativeKeys) {
                cache.evict(key + ALTERNATIVE_KEY_SUFFIX);
            }
        }
        cache.evict(messageId + ALTERNATIVE_KEYS_SUFFIX);
        cache.evict(messageId + CORRELATION_KEY_SUFFIX);
        cache.evict(messageId + AUDIT_DATASET_SUFFIX);
        if (cache.get(messageId + SERVICE_ENDPOINT_URI_SUFFIX) != null) {
            cache.evict(messageId + SERVICE_ENDPOINT_URI_SUFFIX);
            return true;
        }
        return false;
    }
}
