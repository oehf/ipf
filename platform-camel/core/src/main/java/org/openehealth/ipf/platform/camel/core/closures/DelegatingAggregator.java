/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import java.util.Collection;
import java.util.List;

import org.openehealth.ipf.commons.core.modules.api.Aggregator;

/**
 * An aggregator that delegates to a {@link Closure}.
 * 
 * @author Martin Krasser
 */
public class DelegatingAggregator extends ClosureAdapter implements Aggregator<Object, Object> {

    public DelegatingAggregator(Closure closure) {
        super(closure);
    }

    @Override
    public Object zap(Collection<Object> input, Object... params) {
        List<Object> list = (List<Object>)input;
        if (getClosure().getParameterTypes().length == 3) {
            return call(list.get(0), list.get(1), params);
        } else {
            return call(list.get(0), list.get(1));
        }
    }
    
}