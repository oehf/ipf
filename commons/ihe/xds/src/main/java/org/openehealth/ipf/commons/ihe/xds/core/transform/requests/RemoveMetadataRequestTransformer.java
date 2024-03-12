/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRemoveMetadataRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveMetadata;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.RemoveObjectsRequest;

/**
 * Transforms between a {@link RemoveMetadata} and its ebXML representation.
 * @author Boris Stanojevic
 */
public class RemoveMetadataRequestTransformer {
    private final EbXMLFactory factory = new EbXMLFactory30();

    /**
     * Constructs the transformer
     */
    public RemoveMetadataRequestTransformer() {
    }

    /**
     * Transforms the request into its ebXML representation.
     * @param request
     *          the request. Can be <code>null</code>.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLRemoveMetadataRequest<RemoveObjectsRequest> toEbXML(RemoveMetadata request) {
        if (request == null) {
            return null;
        }

        var ebXML = factory.createRemoveMetadataRequest();
        ebXML.setReferences(request.getReferences());

        return ebXML;
    }

    /**
     * Transforms the ebXML representation into a request.
     * @param ebXML
     *          the ebXML representation. Can be <code>null</code>.
     * @return the request. <code>null</code> if the input was <code>null</code>.
     */
    public RemoveMetadata fromEbXML(EbXMLRemoveMetadataRequest<RemoveObjectsRequest> ebXML) {
        if (ebXML == null) {
            return null;
        }

        var request = new RemoveMetadata();
        request.getReferences().addAll(ebXML.getReferences());

        return request;
    }

}
