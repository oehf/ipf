/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti43.component;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

/**
 * The Camel component for the ITI-43 transaction.
 */
public class Iti43Component extends DefaultComponent<Exchange> {
    @Override
    @SuppressWarnings("unchecked") // Required because of base class
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Iti43Endpoint(uri, remaining, this);
    }
}
