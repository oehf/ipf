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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.camel.impl.DefaultComponent;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.AbstractBasicInterceptorProvider;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;

/**
 * Base component class for Web Service-based IHE components.
 * @author Dmytro Rud
 */
abstract public class AbstractWsComponent<ConfigType extends WsTransactionConfiguration>
        extends DefaultComponent
{

    protected InterceptorProvider getCustomInterceptors(Map<String, Object> parameters) {
        AbstractBasicInterceptorProvider provider = new AbstractBasicInterceptorProvider() {};
        
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Interceptor<? extends Message>> castList(
			List<Interceptor> param) {
		return (List<Interceptor<? extends Message>>) (List<?>) param;
	}

    /**
     * @return
     *      static configuration parameters of the Web Service which
     *      server endpoints of this transaction.
     */
    public abstract ConfigType getWsTransactionConfiguration();

    /**
     * @param allowIncompleteAudit
     *      whether incomplete ATNA audit records are allowed.
     * @return
     *      transaction-specific client-side ATNA audit strategy instance.
     */
    public abstract WsAuditStrategy getClientAuditStrategy(boolean allowIncompleteAudit);

    /**
     * @param allowIncompleteAudit
     *      whether incomplete ATNA audit records are allowed.
     * @return
     *      transaction-specific server-side ATNA audit strategy instance.
     */
    public abstract WsAuditStrategy getServerAuditStrategy(boolean allowIncompleteAudit);

    /**
     * Constructs and returns a transaction-specific service class instance
     * for the given endpoint.
     * @param endpoint
     *      Camel endpoint.
     * @return
     *      service class instance for the given endpoint.
     */
    public abstract AbstractWebService getServiceInstance(AbstractWsEndpoint<?> endpoint);

    /**
     * Constructs and returns a transaction-specific Camel producer instance
     * for the given endpoint.
     * @param endpoint
     *      Camel endpoint.
     * @param clientFactory
     *      JAX-WS client factory instance.
     * @return
     *      Camel producer instance.
     */
    public abstract AbstractWsProducer getProducer(
            AbstractWsEndpoint<?> endpoint,
            JaxWsClientFactory clientFactory);


    protected List<AbstractFeature> getFeatures(Map<String, Object> parameters) {
        return resolveAndRemoveReferenceListParameter(parameters, "features", AbstractFeature.class);
    }
}
