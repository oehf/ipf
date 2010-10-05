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
import org.apache.commons.lang.Validate;

/**
 * Ehcache-based implementation of asynchronous message correlator.
 * @author Dmytro Rud
 */
public class EhcacheAsynchronyCorrelator implements AsynchronyCorrelator {

    private final String SERVICE_ENDPOINT_URI_SUFFIX = ".serviceEndpoint";
    private final String CORRELATION_KEY_SUFFIX      = ".correlationKey";
    private final String REQUEST_PAYLOAD_SUFFIX      = ".requestPayload";

    private final Ehcache ehcache;

    public EhcacheAsynchronyCorrelator(Ehcache ehcache) {
        Validate.notNull(ehcache);
        this.ehcache = ehcache;
    }

    private void put(String messageId, String suffix, String value) {
        ehcache.put(new Element(messageId + suffix, value));
    }

    private String get(String messageId, String suffix) {
        Element element = ehcache.get(messageId + suffix);
        return (element != null) ? (String) element.getValue() : null;
    }

    @Override
    public void storeServiceEndpointUri(String messageId, String serviceEndpointUri) {
        put(messageId, SERVICE_ENDPOINT_URI_SUFFIX, serviceEndpointUri);
    }

    @Override
    public void storeCorrelationKey(String messageId, String correlationKey) {
        put(messageId, CORRELATION_KEY_SUFFIX, correlationKey);
    }

    @Override
    public void storeRequestPayload(String messageId, String requestPayload) {
        put(messageId, REQUEST_PAYLOAD_SUFFIX, requestPayload);
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
    public String getRequestPayload(String messageId) {
        return get(messageId, REQUEST_PAYLOAD_SUFFIX);
    }

    @Override
    public boolean delete(String messageId) {
        ehcache.remove(messageId + CORRELATION_KEY_SUFFIX);
        ehcache.remove(messageId + REQUEST_PAYLOAD_SUFFIX);
        return ehcache.remove(messageId + SERVICE_ENDPOINT_URI_SUFFIX);
    }
}
