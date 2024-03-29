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
package org.openehealth.ipf.commons.ihe.xds.core.transform.responses;

import static java.util.Objects.requireNonNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

/**
 * Transforms between {@link Response} and the ebXML representation.
 * @author Jens Riemschneider
 */
public class ResponseTransformer {
    private final EbXMLFactory factory;
    private final ErrorInfoListTransformer errorInfoListTransformer;

    /**
     * Constructs the transformer.
     * @param factory
     *          the factory for ebXML objects.
     */
    public ResponseTransformer(EbXMLFactory factory) {
        this.factory = requireNonNull(factory, "factory cannot be null");
        this.errorInfoListTransformer = new ErrorInfoListTransformer(factory);
    }

    /**
     * Transforms a {@link Response} to a {@link EbXMLRegistryResponse}.
     * @param response
     *          the response. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLRegistryResponse<RegistryResponseType> toEbXML(Response response) {
        requireNonNull(response, "response cannot be null");

        var ebXML = factory.createRegistryResponse();

        ebXML.setStatus(response.getStatus());
        if (!response.getErrors().isEmpty()) {
            ebXML.setErrors(errorInfoListTransformer.toEbXML(response.getErrors()));
        }

        return ebXML;
    }

    /**
     * Transforms a {@link EbXMLRegistryResponse} to a {@link Response}.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the response. <code>null</code> if the input was <code>null</code>.
     */
    public Response fromEbXML(EbXMLRegistryResponse<RegistryResponseType> ebXML) {
        requireNonNull(ebXML, "ebXML cannot be null");

        var response = new Response();

        response.setStatus(ebXML.getStatus());
        if (!ebXML.getErrors().isEmpty()) {
            response.setErrors(errorInfoListTransformer.fromEbXML(ebXML.getErrors()));
        }

        return response;
    }
}
