/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.fhir.chppqm;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.HapiClientInterceptorFactory;

public class LoggingInterceptorFactory implements HapiClientInterceptorFactory {

    @Override
    public IClientInterceptor newInstance(FhirEndpoint endpoint, Exchange exchange) {
        LoggingInterceptor interceptor = new LoggingInterceptor(false);
        interceptor.setLogResponseHeaders(true);
        return interceptor;
    }

}
