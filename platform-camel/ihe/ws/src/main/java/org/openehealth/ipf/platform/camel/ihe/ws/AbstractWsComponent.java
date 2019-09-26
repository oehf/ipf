/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.ws;

import org.apache.camel.ResolveEndpointFailedException;
import org.apache.camel.impl.DefaultComponent;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.AbstractBasicInterceptorProvider;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableComponent;
import org.openehealth.ipf.platform.camel.ihe.atna.util.AuditConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Base component class for Web Service-based IHE components.
 *
 * @param <AuditDatasetType> audit type
 * @param <ConfigType>       configuration type
 * @author Dmytro Rud
 */
abstract public class AbstractWsComponent<
        AuditDatasetType extends WsAuditDataset,
        ConfigType extends WsTransactionConfiguration<AuditDatasetType>,
        InteractionIdType extends WsInteractionId<ConfigType>>
        extends DefaultComponent implements AuditableComponent<AuditDatasetType> {

    private final InteractionIdType interactionId;

    public AbstractWsComponent(InteractionIdType interactionId) {
        this.interactionId = interactionId;
    }

    public InteractionIdType getInteractionId() {
        return interactionId;
    }

    protected InterceptorProvider getCustomInterceptors(Map<String, Object> parameters) {
        AbstractBasicInterceptorProvider provider = new AbstractBasicInterceptorProvider() {
        };
        provider.setInInterceptors(castList(resolveAndRemoveReferenceListParameter(
                parameters, "inInterceptors", Interceptor.class)));
        provider.setInFaultInterceptors(castList(resolveAndRemoveReferenceListParameter(
                parameters, "inFaultInterceptors", Interceptor.class)));
        provider.setOutInterceptors(castList(resolveAndRemoveReferenceListParameter(
                parameters, "outInterceptors", Interceptor.class)));
        provider.setOutFaultInterceptors(castList(resolveAndRemoveReferenceListParameter(
                parameters, "outFaultInterceptors", Interceptor.class)));
        return provider;
    }

    protected AuditContext getAuditContext(Map<String, Object> parameters) {
        return AuditConfiguration.obtainAuditContext(this, parameters);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Interceptor<? extends Message>> castList(
            List<Interceptor> param) {
        return (List<Interceptor<? extends Message>>) (List<?>) param;
    }

    protected List<AbstractFeature> getFeatures(Map<String, Object> parameters) {
        return resolveAndRemoveReferenceListParameter(parameters, "features", AbstractFeature.class);
    }

    protected List<String> getSchemaLocations(Map<String, Object> parameters) {
        return resolveAndRemoveReferenceListParameter(parameters, "schemaLocations", String.class);
    }

    protected Map<String, Object> getProperties(Map<String, Object> parameters) {
        List<Map> mapList = resolveAndRemoveReferenceListParameter(parameters, "properties", Map.class);
        return (mapList != null && mapList.size() == 1) ? mapList.get(0) : null;
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return getWsTransactionConfiguration().getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return getWsTransactionConfiguration().getServerAuditStrategy();
    }

    /**
     * @return static configuration parameters of the Web Service which
     * server endpoints of this transaction.
     */
    public ConfigType getWsTransactionConfiguration() {
        return interactionId.getWsTransactionConfiguration();
    }

}
