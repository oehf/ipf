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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareWsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import java.util.List;

/**
 * Camel endpoint implementation for HL7v3-based IHE components
 * with interactive response continuation support.
 * @author Dmytro Rud
 */
public class Hl7v3ContinuationAwareEndpoint
        extends Hl7v3Endpoint<Hl7v3ContinuationAwareWsTransactionConfiguration>
{
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

    /**
     * Whether the system should validate messages which are internally handled
     * when performing HL7v3 interactive continuation.
     */
    private boolean validationOnContinuation = false;


    public Hl7v3ContinuationAwareEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<Hl7v3ContinuationAwareWsTransactionConfiguration> component,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features)
    {
        super(endpointUri, address, component, customInterceptors, features);
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

    /**
     * @return
     *      <code>true</code> when messages, which are internally handled
     *      when performing HL7v3 interactive continuation, should be validated.
     */
    public boolean isValidationOnContinuation() {
        return validationOnContinuation;
    }

    public void setValidationOnContinuation(boolean validationOnContinuation) {
        this.validationOnContinuation = validationOnContinuation;
    }


    /**
     * @return
     *      <code>true</code> if ATNA audit strategies must be manually applied
     *      in Camel producer and consumer instead of CXF interceptors.
     *      This will be the case when interactive response continuation is supported.
     */
    public boolean isManualAudit() {
        return (isAudit() && isSupportContinuation());
    }


    @Override
    public JaxWsClientFactory getJaxWsClientFactory() {
        return new Hl7v3ClientFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isManualAudit() ? null : getComponent().getClientAuditStrategy(isAllowIncompleteAudit()),
                null,
                getCustomInterceptors());
    }


    @Override
    public JaxWsServiceFactory getJaxWsServiceFactory() {
        return new Hl7v3ServiceFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isManualAudit() ? null : getComponent().getServerAuditStrategy(isAllowIncompleteAudit()),
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }
}
