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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.server.RestfulServer;

/**
 * Keeps track of {@link RequestConsumer} and {@link IpfFhirServlet} and connects them to each other.
 *
 * @author Christian Ohr
 * @since 3.2
 */
public interface FhirRegistry {

    /**
     * Registers the FHIR resource provider.
     *
     * @param resourceProvider resource provider
     */
    void register(Object resourceProvider);

    /**
     * Unregisters the FHIR resource provider.
     *
     * @param resourceProvider resource provider
     */
    void unregister(Object resourceProvider);

    /**
     * Registers the FHIR servlet, usually during its init phase.
     *
     * @param servlet FHIR servlet
     */
    void register(RestfulServer servlet);

    /**
     * Unregisters the FHIR servlet, usually during its destroy phase
     *
     * @param servlet FHIR servlet
     */
    void unregister(RestfulServer servlet);

    /**
     * Returns true if there is already a registered FHIR servlet with the provided name
     *
     * @param servletName name of the servlet
     * @return true if there is already a registered FHIR servlet with this name
     */
    boolean hasServlet(String servletName);
}
