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

import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resource provider for batch requests or transaction requests
 *
 * @author Christian Ohr
 * @since 4.1
 */
public class BatchTransactionResourceProvider extends SharedFhirProvider {

    /**
     * Singleton instance of this resource provider
     */
    private static final BatchTransactionResourceProvider INSTANCE = new BatchTransactionResourceProvider();

    // Enforce to use singleton instance
    protected BatchTransactionResourceProvider() {
    }

    public static BatchTransactionResourceProvider getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unused")
    @Transaction
    public IBaseBundle provideResponseBundle(@TransactionParam IBaseBundle bundle,
                                        RequestDetails requestDetails,
                                        HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) {
        return requestTransaction(bundle, IBaseBundle.class, httpServletRequest, httpServletResponse, requestDetails);
    }

}
