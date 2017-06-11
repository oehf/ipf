/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti39.asyncresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsAsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.xds.XCA.Interactions.ITI_39_ASYNC_RESPONSE;

/**
 * The Camel component for the ITI-39 (XCA) async response.
 */
public class Iti39AsyncResponseComponent extends XdsComponent<XdsNonconstructiveDocumentSetRequestAuditDataset> {

    public Iti39AsyncResponseComponent() {
        super(ITI_39_ASYNC_RESPONSE);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new XdsAsyncResponseEndpoint<>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                Iti39AsyncResponseService.class);
    }

}
