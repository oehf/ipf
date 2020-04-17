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
package org.openehealth.ipf.commons.ihe.hl7v2.storage;

import ca.uhn.hl7v2.model.Message;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * An Ehcache-based implementation of {@link InteractiveContinuationStorage}.
 *
 * @author Dmytro Rud
 */
public class EhcacheInteractiveContinuationStorage implements InteractiveContinuationStorage {

    private static final transient Logger LOG = LoggerFactory.getLogger(EhcacheInteractiveContinuationStorage.class);
    private final Ehcache ehcache;


    public EhcacheInteractiveContinuationStorage(Ehcache ehcache) {
        requireNonNull(ehcache);
        this.ehcache = ehcache;
    }


    @Override
    public void put(String continuationPointer, String chainId, Message fragment) {
        InteractiveContinuationChain chain;
        Element element = ehcache.get(chainId);
        if (element != null) {
            chain = (InteractiveContinuationChain) element.getObjectValue();
        } else {
            LOG.debug("Create chain for storage key {}", chainId);
            chain = new InteractiveContinuationChain();
            ehcache.put(new Element(chainId, chain));
        }
        chain.put(continuationPointer, fragment);
    }


    @Override
    public Message get(
            String continuationPointer,
            String chainId)
    {
        Element element = ehcache.get(chainId);
        if (element != null) {
            InteractiveContinuationChain chain = (InteractiveContinuationChain) element.getObjectValue();
            return chain.get(continuationPointer);
        }
        return null;
    }


    @Override
    public boolean delete(String chainId) {
        return ehcache.remove(chainId);
    }


    
    /**
     * Chain of interactive continuation fragments of a query's response.
     * <p>
     * Keys correspond to continuation pointers of the fragments;
     * the key of the first fragment is <code>null</code>.
     */
    private static class InteractiveContinuationChain implements Serializable {
        private final Map<String, Message> responseMessages =
            Collections.synchronizedMap(new HashMap<String, Message>());

        public void put(String continuationPointer, Message message) {
            responseMessages.put(continuationPointer, message);
        }

        public Message get(String continuationPointer) {
            return responseMessages.get(continuationPointer);
        }
    }

}
