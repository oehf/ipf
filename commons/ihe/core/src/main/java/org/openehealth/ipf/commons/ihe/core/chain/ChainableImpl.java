/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.chain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Base for an element of a chain.
 * @author Dmytro Rud
 */
public abstract class ChainableImpl implements Chainable {
    private String id = getClass().getName();
    private Set<String> before = new HashSet<>();
    private Set<String> after = new HashSet<>();


    /**
     * Sets the ID of this chain element.
     * @param id
     *      ID of this chain element.
     */
    public void setId(String id) {
        this.id = requireNonNull(id);
    }

    /**
     * @return ID of this chain element.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Configures this chain element to be deployed before the given ones.
     * @param ids
     *      IDs of chain elements this chain element should be deployed before.
     */
    public void addBefore(String... ids) {
        Collections.addAll(before, ids);
    }

    /**
     * Configures this chain element to be deployed after the given ones.
     * @param ids
     *      IDs of chain elements this chain element should be deployed after.
     */
    public void addAfter(String... ids) {
        Collections.addAll(after, ids);
    }

    /**
     * @return IDs of chain elements this chain element will be/has been deployed before.
     */
    @Override
    public Set<String> getBefore() {
        return before;
    }

    /**
     * @return IDs of chain elements this chain element will be/has been deployed after.
     */
    @Override
    public Set<String> getAfter() {
        return after;
    }

}
