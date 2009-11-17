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
package org.openehealth.ipf.commons.ihe.xds.core;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.AuditFinalInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;

/**
 * Factory for XDS web-services.
 * @author Jens Riemschneider
 */
public class XdsServiceFactory extends ItiServiceFactory {
    private final XdsAuditStrategy auditStrategy;

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the service to produce.
     * @param auditStrategy
     *          the auditing strategy to use.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     */
    public XdsServiceFactory(ItiServiceInfo serviceInfo, XdsAuditStrategy auditStrategy, String serviceAddress) {
        super(serviceInfo, serviceAddress);
        this.auditStrategy = auditStrategy;
    }
    
    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);
        
        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            svrFactory.getInInterceptors().add(new AuditDatasetEnrichmentInterceptor(auditStrategy, true));
    
            AuditFinalInterceptor auditOutInterceptor = new AuditFinalInterceptor(auditStrategy, true);
            svrFactory.getOutInterceptors().add(auditOutInterceptor);
            svrFactory.getOutFaultInterceptors().add(auditOutInterceptor);
            
            if(((XdsServiceInfo) serviceInfo).isAuditPayload()) {
                svrFactory.getInInterceptors().add(new InPayloadExtractorInterceptor());
            }        
        }
    }
}
