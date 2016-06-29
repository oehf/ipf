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

import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A processor that delegates to a {@link Closure}
 *
 * @author Martin Krasser
 */
public class DelegatingProcessor extends ClosureAdapter implements Processor {

    public DelegatingProcessor(@ClosureParams(value = SimpleType.class, options = {"org.apache.camel.Exchange"})
                                       Closure closure) {
        super(closure);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        call(exchange);
    }

}