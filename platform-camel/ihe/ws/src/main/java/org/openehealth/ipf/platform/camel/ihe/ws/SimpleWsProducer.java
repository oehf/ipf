/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.ws;

import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import javax.jws.WebMethod;
import java.lang.reflect.Method;

/**
 * Generic producer for Web Services which have only one operation.
 * @author Dmytro Rud
 */
public class SimpleWsProducer<
        AuditDatasetType extends WsAuditDataset,
        ConfigType extends WsTransactionConfiguration<AuditDatasetType>,
        InType, OutType> extends AbstractWsProducer<AuditDatasetType, ConfigType, InType, OutType> {
    private final String operationName;

    /**
     * Constructor.
     * @param endpoint
     *      Camel endpoint instance.
     * @param clientFactory
     *      JAX-WS client object factory.
     * @param requestClass
     *          type of request messages.
     * @param responseClass
     *          type of response messages.
     */
    public SimpleWsProducer(
            AbstractWsEndpoint<AuditDatasetType, ConfigType> endpoint,
            JaxWsClientFactory<AuditDatasetType> clientFactory,
            Class<InType> requestClass,
            Class<OutType> responseClass)
    {
        super(endpoint, clientFactory, requestClass, responseClass);

        for (Method method : endpoint.getComponent().getWsTransactionConfiguration().getSei().getDeclaredMethods()) {
            WebMethod annotation = method.getAnnotation(WebMethod.class);
            if (annotation != null) {
                this.operationName = annotation.operationName();
                return;
            }
        }
        throw new IllegalStateException("the SEI does not contain any methods annotated with @WebMethod");
    }


    @SuppressWarnings("unchecked")
    @Override
    protected OutType callService(Object clientObject, InType request) throws Exception {
        ClientImpl client = (ClientImpl) ClientProxy.getClient(clientObject);
        Object[] result = client.invoke(operationName, request);
        return (result != null) ? (OutType) result[0] : null;
    }
}
