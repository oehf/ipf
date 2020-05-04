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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

/**
 * Ehcache-based implementation of asynchronous message correlator.
 * @author Dmytro Rud
 */
public class EhcacheAsynchronyCorrelator<AuditDatasetType extends WsAuditDataset> implements AsynchronyCorrelator<AuditDatasetType> {

    private static final String SERVICE_ENDPOINT_URI_SUFFIX = ".serviceEndpoint";
    private static final String CORRELATION_KEY_SUFFIX      = ".correlationKey";
    private static final String AUDIT_DATASET_SUFFIX        = ".auditDataset";
    private static final String ALTERNATIVE_KEY_SUFFIX      = ".alternativeKey";
    private static final String ALTERNATIVE_KEYS_SUFFIX     = ".alternativeKeys";

    private final Ehcache ehcache;

    public EhcacheAsynchronyCorrelator(Ehcache ehcache) {
        this.ehcache = requireNonNull(ehcache, "ehcache instance");
    }

    private void put(String key, String suffix, Serializable value) {
        ehcache.put(new Element(key + suffix, value));
    }

    private <T extends Serializable> T get(String key, String suffix) {
        var element = ehcache.get(key + suffix);
        return (element != null) ? (T) element.getObjectValue() : null;
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
                ehcache.remove(key + ALTERNATIVE_KEY_SUFFIX);
            }
        }
        ehcache.remove(messageId + ALTERNATIVE_KEYS_SUFFIX);
        ehcache.remove(messageId + CORRELATION_KEY_SUFFIX);
        ehcache.remove(messageId + AUDIT_DATASET_SUFFIX);
        return ehcache.remove(messageId + SERVICE_ENDPOINT_URI_SUFFIX);
    }
}
