/*
 * Copyright 2019 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.Map;

/**
 * Request Factory for Batch/Transaction requests
 *
 * @author Christian Ohr
 * @since 4.1
 */
public class BatchTransactionClientRequestFactory implements ClientRequestFactory<ITransactionTyped<IBaseBundle>> {

    private static final BatchTransactionClientRequestFactory INSTANCE = new BatchTransactionClientRequestFactory();

    private BatchTransactionClientRequestFactory() {
    }

    public static BatchTransactionClientRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public ITransactionTyped<IBaseBundle> getClientExecutable(IGenericClient client, Object requestData, Map<String, Object> parameters) {
        return client.transaction().withBundle((IBaseBundle)requestData);
    }

}
