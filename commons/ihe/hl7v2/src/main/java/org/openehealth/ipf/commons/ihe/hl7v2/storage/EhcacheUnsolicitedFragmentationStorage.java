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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.Validate;

import static java.util.Objects.requireNonNull;

/**
 * A storage of HL7 v2 unsolicited fragmentation accumulators.
 * @author Dmytro Rud
 */
public class EhcacheUnsolicitedFragmentationStorage implements UnsolicitedFragmentationStorage {

    private final Ehcache ehcache;

    public EhcacheUnsolicitedFragmentationStorage(Ehcache ehcache) {
        requireNonNull(ehcache);
        this.ehcache = ehcache;
    }

    public void put(String key, StringBuilder accumulator) {
        Element element = new Element(key, accumulator);
        ehcache.put(element);
    }

    public StringBuilder getAndRemove(String key) {
        Element element = ehcache.get(key);
        if (element != null) {
            ehcache.remove(key);
            return (StringBuilder) element.getObjectValue();
        }
        return null;
    }
}
