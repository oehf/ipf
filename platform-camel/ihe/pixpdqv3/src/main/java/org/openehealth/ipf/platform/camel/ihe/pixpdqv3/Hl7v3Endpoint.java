/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3;

import org.apache.camel.Component;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;

/**
 * Camel endpoint class for HL7v3-based IHE components.
 * @author Dmytro Rud
 */
abstract public class Hl7v3Endpoint extends DefaultItiEndpoint {

    /**
     * Whether this endpoint should support HL7v3 continuation.
     */
    private boolean supportContinuation = false;

    /**
     * Producer-side only: Whether a "cancel continuation" message should
     * be automatically sent to the server when all continuation fragments
     * have been read.
     */
    private boolean autoCancel = false;

    /**
     * Consumer-side only: Default count of data records in the first
     * response fragment, when the request does not contain "initialQuantity"
     * element.  Negative values mean "no continuation, when initialQuantity
     * is not specified".
     */
    private int defaultContinuationThreshold = -1;

    /**
     * Consumer-side only: Storage bean for continuation fragments.
     */
    private Hl7v3ContinuationStorage continuationStorage = null;


    public Hl7v3Endpoint(
            String endpointUri,
            String address,
            Component component,
            InterceptorProvider customInterceptors)
    {
        super(endpointUri, address, component, customInterceptors);
    }


    /**
     * Returns <code>true</code> when this endpoint supports HL7v3 continuation.
     */
    public boolean isSupportContinuation() {
        return supportContinuation;
    }

    public void setSupportContinuation(boolean supportContinuation) {
        this.supportContinuation = supportContinuation;
    }

    /**
     * Returns default count of data records to be sent in the first response
     * fragment, when the request does not contain "initialQuantity" element.
     * Negative values mean "no continuation, when initialQuantity is not
     * specified".
     * <p>
     * This parameter is relevant only on consumer side.
     */
    public int getDefaultContinuationThreshold() {
        return defaultContinuationThreshold;
    }

    public void setDefaultContinuationThreshold(int defaultContinuationThreshold) {
        this.defaultContinuationThreshold = defaultContinuationThreshold;
    }

    /**
     * Returns storage bean for continuation fragments.
     * <p>
     * This parameter is relevant only on consumer side.
     */
    public Hl7v3ContinuationStorage getContinuationStorage() {
        return continuationStorage;
    }

    public void setContinuationStorage(Hl7v3ContinuationStorage continuationStorage) {
        this.continuationStorage = continuationStorage;
    }

    /**
     * Returns <code>true</code> when a "cancel continuation" message should
     * be automatically sent to the server after all continuation fragments
     * have been read.
     * <p>
     * This parameter is relevant only on producer side. 
     */
    public boolean isAutoCancel() {
        return autoCancel;
    }

    public void setAutoCancel(boolean autoCancel) {
        this.autoCancel = autoCancel;
    }
}
