/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.apache.cxf.ws.addressing.MAPAggregator;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.ws.cxf.DecoupledDestinationApprovalInterceptor;

import javax.xml.namespace.QName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that IPF pre-approves WS-Addressing decoupled destinations exactly for those
 * transactions that support asynchronous responses. Since CXF 4.1.8, decoupled destinations
 * are rejected by default, and the opt-in either happens JVM-wide via a system property or
 * &mdash; as done here &mdash; per exchange.
 *
 * @author Christian Ohr
 */
public class JaxWsRequestServiceFactoryTest {

    /**
     * Dummy service endpoint interface -- the factory only needs a type here,
     * no service is ever published in these tests.
     */
    public interface DummyPortType {
    }

    private static WsTransactionConfiguration transactionConfiguration(boolean allowAsynchrony) {
        return new WsTransactionConfiguration(
                "dummy",
                "Dummy transaction",
                true,
                null,
                null,
                new QName("urn:ihe:dummy", "DummyService"),
                DummyPortType.class,
                new QName("urn:ihe:dummy", "DummyService_Binding_Soap12"),
                false,
                "wsdl/dummy.wsdl",
                true,
                false,
                false,
                allowAsynchrony);
    }

    private static ServerFactoryBean configuredServerFactory(boolean allowAsynchrony) {
        var serviceFactory = new JaxWsRequestServiceFactory<>(
                transactionConfiguration(allowAsynchrony),
                "http://localhost:8080/dummy",
                null,
                null,
                null,
                null);
        var svrFactory = new JaxWsServerFactoryBean();
        serviceFactory.configureInterceptors(svrFactory);
        return svrFactory;
    }

    private static boolean hasApprovalInterceptor(ServerFactoryBean svrFactory) {
        return svrFactory.getInInterceptors().stream()
                .anyMatch(DecoupledDestinationApprovalInterceptor.class::isInstance);
    }

    @Test
    public void approvalInterceptorInstalledWhenAsynchronyIsAllowed() {
        assertTrue(hasApprovalInterceptor(configuredServerFactory(true)));
    }

    @Test
    public void approvalInterceptorNotInstalledWhenAsynchronyIsNotAllowed() {
        assertFalse(hasApprovalInterceptor(configuredServerFactory(false)));
    }

    @Test
    public void approvalInterceptorRunsBeforeTheMapAggregator() {
        var interceptor = new DecoupledDestinationApprovalInterceptor();
        assertEquals(Phase.PRE_LOGICAL, interceptor.getPhase());
        assertTrue(interceptor.getBefore().contains(MAPAggregator.class.getName()));
    }

    @Test
    public void approvalInterceptorMarksTheExchange() {
        var message = new MessageImpl();
        var exchange = new ExchangeImpl();
        message.setExchange(exchange);

        new DecoupledDestinationApprovalInterceptor().handleMessage(message);

        assertEquals(Boolean.TRUE, exchange.get(ContextUtils.DECOUPLED_DESTINATION_APPROVED_PROPERTY));
    }
}
