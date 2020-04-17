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
package org.openehealth.ipf.commons.core.config;

/**
 * Base class for all custom configurers which have to implement their own strategy
 * for lookup and configure.
 *
 * @author Boris Stanojevic
 */
public abstract class OrderedConfigurer<T, R extends Registry> implements Configurer<T, R>, Comparable<OrderedConfigurer<T, R>> {

    private int order = Integer.MAX_VALUE;

    @Override
    public int compareTo(OrderedConfigurer<T, R> configurer) {
        if (configurer.getOrder() < getOrder()) {
            return 1;
        } else if (configurer.getOrder() > getOrder()) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
