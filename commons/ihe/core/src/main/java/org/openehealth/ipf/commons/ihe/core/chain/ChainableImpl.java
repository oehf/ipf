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

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base for an element of interceptor chain.
 * @author Dmytro Rud
 */
abstract public class ChainableImpl implements Chainable {
    private String id = getClass().getName();
    private Set<String> before = new HashSet<String>();
    private Set<String> after = new HashSet<String>();


    /**
     * Sets the ID of this interceptor.
     * @param id
     *      ID of this interceptor.
     */
    public void setId(String id) {
        this.id = Validate.notEmpty(id);
    }

    /**
     * @return ID of this interceptor.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Configures this interceptor to be deployed before the given ones.
     * @param ids
     *      IDs of interceptors this interceptor should be deployed before.
     */
    public void addBefore(String... ids) {
        Collections.addAll(before, ids);
    }

    /**
     * Configures this interceptor to be deployed after the given ones.
     * @param ids
     *      IDs of interceptors this interceptor should be deployed after.
     */
    public void addAfter(String... ids) {
        Collections.addAll(after, ids);
    }

    /**
     * @return IDs of interceptors this interceptor will be/has been deployed before.
     */
    @Override
    public Set<String> getBefore() {
        return before;
    }

    /**
     * @return IDs of interceptors this interceptor will be/has been deployed after.
     */
    @Override
    public Set<String> getAfter() {
        return after;
    }

}
