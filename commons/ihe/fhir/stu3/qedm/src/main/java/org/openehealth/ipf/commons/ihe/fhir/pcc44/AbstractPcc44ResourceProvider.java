/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.pcc44;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.AbstractResourceProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Christian Ohr
 */

// Must be public
public abstract class AbstractPcc44ResourceProvider<T extends IBaseResource> extends AbstractResourceProvider {

    private final Class<T> clazz;

    protected AbstractPcc44ResourceProvider(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getResourceType() {
        return clazz;
    }

    /**
     * Handles Retrieve. This is not an actual part of the PCC-44 specification, but in the
     * context of restful FHIR IHE transaction it makes sense to be able to retrieve a resource by
     * its ID.
     *
     * @param id                  resource ID
     * @param requestDetails      request details
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return {@link DocumentManifest} resource
     */
    @SuppressWarnings("unused")
    @Read(version = true)
    public T retrieve(
            @IdParam IdType id,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        // Run down the route
        return requestResource(id, null, clazz, httpServletRequest, httpServletResponse, requestDetails);
    }
}
