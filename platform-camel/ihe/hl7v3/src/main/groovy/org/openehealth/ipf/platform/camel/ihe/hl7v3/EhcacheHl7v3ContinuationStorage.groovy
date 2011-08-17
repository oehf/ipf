/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v3

import net.sf.ehcache.Ehcache
import org.apache.commons.lang.Validate
import net.sf.ehcache.Element

/**
 * @author Dmytro Rud
 */
class EhcacheHl7v3ContinuationStorage implements Hl7v3ContinuationStorage {

    private final Ehcache ehcache

    private static final String MESSAGE_SUFFIX = '.message'
    private static final String LAST_RESULT_NUMBER_SUFFIX = '.lastIndex'
    private static final String CONTINUATION_QUANTITY_SUFFIX = '.quantity'

    EhcacheHl7v3ContinuationStorage(Ehcache ehcache) {
        Validate.notNull(ehcache)
        this.ehcache = ehcache
    }

    void storeMessage(String key, String message) {
        ehcache.put(new Element(key + MESSAGE_SUFFIX, message))
    }

    String getMessage(String key) {
        return ehcache.get(key + MESSAGE_SUFFIX)?.value
    }

    void storeLastResultNumber(String key, int lastResultNumber) {
        ehcache.put(new Element(key + LAST_RESULT_NUMBER_SUFFIX, lastResultNumber))
    }

    int getLastResultNumber(String key) {
        Integer i = (Integer) ehcache.get(key + LAST_RESULT_NUMBER_SUFFIX)?.value
        return (i != null) ? i.intValue() : -1
    }

    void storeContinuationQuantity(String key, int continuationQuantity) {
        ehcache.put(new Element(key + CONTINUATION_QUANTITY_SUFFIX, continuationQuantity))
    }

    int getContinuationQuantity(String key) {
        Integer i = (Integer) ehcache.get(key + CONTINUATION_QUANTITY_SUFFIX)?.value
        return (i != null) ? i.intValue() : -1
    }

    boolean remove(String key) {
        ehcache.remove(key + LAST_RESULT_NUMBER_SUFFIX)
        ehcache.remove(key + CONTINUATION_QUANTITY_SUFFIX)
        return ehcache.remove(key + MESSAGE_SUFFIX)
    }

}
