/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.rad75.asyncresponse;

import org.apache.camel.ExchangePattern;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.rad75.asyncresponse.Rad75AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractAsyncResponseWebService;

/**
 * Service implementation for the IHE RAD-75 (XCA-I) asynchronous response.
 * @author Clay Sebourn
 */
public class Rad75AsyncResponseService extends AbstractAsyncResponseWebService implements Rad75AsyncResponsePortType {
    @Override
    public void documentRepositoryRetrieveImagingDocumentSet(RetrieveDocumentSetResponseType response) {
        process(response, null, ExchangePattern.InOnly);
    }
}
