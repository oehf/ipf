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

import java.util.Set;

/**
 * Base for an element of interceptor chain.
 * @author Dmytro Rud
 */
public interface Chainable {

    /**
     * @return ID of this interceptor.
     */
    String getId();

    /**
     * @return IDs of interceptors this interceptor will be/has been deployed before.
     */
    Set<String> getBefore();

    /**
     * @return IDs of interceptors this interceptor will be/has been deployed after.
     */
    Set<String> getAfter();
}
