/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v3.storage;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import static java.util.Objects.requireNonNull;

/**
 * @author Dmytro Rud
 * @author Christian Ohr
 */
public class EhcacheHl7v3ContinuationStorage implements Hl7v3ContinuationStorage {

    private final Ehcache ehcache;

    private static final String MESSAGE_SUFFIX = ".message";
    private static final String LAST_RESULT_NUMBER_SUFFIX = ".lastIndex";
    private static final String CONTINUATION_QUANTITY_SUFFIX = ".quantity";

    EhcacheHl7v3ContinuationStorage(Ehcache ehcache) {
        requireNonNull(ehcache);
        this.ehcache = ehcache;
    }

    public void storeMessage(String key, String message) {
        ehcache.put(new Element(key + MESSAGE_SUFFIX, message));
    }

    public String getMessage(String key) {
        Element element = ehcache.get(key + MESSAGE_SUFFIX);
        return element != null ? (String) element.getObjectValue() : null;
    }

    public void storeLastResultNumber(String key, int lastResultNumber) {
        ehcache.put(new Element(key + LAST_RESULT_NUMBER_SUFFIX, lastResultNumber));
    }

    public int getLastResultNumber(String key) {
        Element element = ehcache.get(key + LAST_RESULT_NUMBER_SUFFIX);
        if (element != null) {
            Integer i = (Integer) element.getObjectValue();
            return (i != null) ? i : -1;
        }
        return -1;
    }

    public void storeContinuationQuantity(String key, int continuationQuantity) {
        ehcache.put(new Element(key + CONTINUATION_QUANTITY_SUFFIX, continuationQuantity));
    }

    public int getContinuationQuantity(String key) {
        Element element = ehcache.get(key + CONTINUATION_QUANTITY_SUFFIX);
        if (element != null) {
            Integer i = (Integer) element.getObjectValue();
            return (i != null) ? i : -1;
        }
        return -1;
    }

    public boolean remove(String key) {
        ehcache.remove(key + LAST_RESULT_NUMBER_SUFFIX);
        ehcache.remove(key + CONTINUATION_QUANTITY_SUFFIX);
        return ehcache.remove(key + MESSAGE_SUFFIX);
    }

}

