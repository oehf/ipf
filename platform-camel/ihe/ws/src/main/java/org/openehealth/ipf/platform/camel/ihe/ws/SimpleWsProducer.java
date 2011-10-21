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

import javax.jws.WebMethod;
import java.lang.reflect.Method;

/**
 * Producer for Web Services which have only one operation.
 * @author Dmytro Rud
 */
public class SimpleWsProducer extends AbstractWsProducer {

    private final String operationName;
    private final Class<?> declaredRequestClass;

    /**
     * Constructor.
     * @param endpoint
     *      Camel endpoint instance.
     * @param clientFactory
     *      JAX-WS client object factory.
     */
    public SimpleWsProducer(
            AbstractWsEndpoint endpoint,
            JaxWsClientFactory clientFactory)
    {
        super(endpoint, clientFactory, Object.class);

        for (Method method : endpoint.getComponent().getWsTransactionConfiguration().getSei().getDeclaredMethods()) {
            WebMethod annotation = method.getAnnotation(WebMethod.class);
            if (annotation != null) {
                this.operationName = annotation.operationName();
                this.declaredRequestClass = method.getParameterTypes()[0];
                return;
            }
        }

        throw new IllegalStateException("the SEI does not contain any methods annotated with @WebMethod");
    }


    @Override
    protected Object callService(Object clientObject, Object request) throws Exception {
        ClientImpl client = (ClientImpl) ClientProxy.getClient(clientObject);
        request = getEndpoint().getCamelContext().getTypeConverter().convertTo(declaredRequestClass, request);
        Object[] result = client.invoke(operationName, request);
        return (result != null) ? result[0] : null;
    }
}
