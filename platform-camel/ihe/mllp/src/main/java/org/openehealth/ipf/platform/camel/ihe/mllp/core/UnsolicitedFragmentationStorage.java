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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

/**
 * Interface for storages of of HL7 v2 unsolicited fragmentation accumulators.
 * @author Dmytro Rud
 */
public interface UnsolicitedFragmentationStorage {

    /**
     * Puts a fragment accumulator into the storage.
     * @param key
     *      Key consisting of MSH-14/DSC-1, MSH-3-1, MSH-3-2, MSH-3-3.
     * @param accumulator
     *      Accumulator to be stored.
     */
    void put(String key, StringBuilder accumulator);

    /**
     * Returns the fragment accumulator which corresponds to the given key
     * and removes it from the storage.
     * @param key
     *      Key consisting of MSH-14/DCS-1, MSH-3-1, MSH-3-2, MSH-3-3.
     * @return
     *      Accumulator or <code>null</code> when none found.
     */
    StringBuilder getAndRemove(String key);
}
