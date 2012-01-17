/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.spi.TransactedPolicy;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.SimpleTransactionStatus;

/**
 * Test for handling of HL7v2 exceptions from within transactional contexts.
 * @author Stefan Albrecht
 */
public class TransactedRouteTest extends StandardTestContainer {
    
    private static final String WAN_REQUEST = "MSH|^~\\&|AcmeInc^ACDE48234567ABCD^EUI-64||||20090713090030+0500||ORU^R01^ORU_R01|MSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7\n"
+ "PID|||789567^^^Imaginary Hospital^PI||Doe^John^Joseph^^^^L^A|||M\n"
+ "OBR|1|AB12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|||20090813095715+0500\n"
+ "OBX|1||528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|||||||R|||||||0123456789ABCDEF^EUI-64\n"
+ "OBX|2||150020^MDC_PRESS_BLD_NONINV^MDC|1.0.1|||||||R|||20090813095715+0500\n"
+ "OBX|3|NM|150021^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.1|120|266016^MDC_DIM_MMHG^MDC|||||R\n"
+ "OBX|4|NM|150022^MDC_PRESS_BLD_NONINV_DIA^MDC|1.0.1.2|80|266016^MDC_DIM_MMHG^MDC|||||R\n"
+ "OBX|5|NM|150023^MDC_PRESS_BLD_NONINV_MEAN^MDC|1.0.1.3|100|266016^MDC_DIM_MMHG^MDC|||||R\n";
    
    private static final SpringTransactionPolicy transactionPolicy
        = new SpringTransactionPolicy();
    private static PlatformTransactionManager txManager;

    
    @BeforeClass
    public static void setUpClass () throws Exception {
        txManager = EasyMock.createMock(PlatformTransactionManager.class);
        transactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
        transactionPolicy.setTransactionManager(txManager);
        final URL resource = ClassLoader.getSystemResource("transacted-test-context.xml");
        startServer(new CXFServlet(), "transacted-test-context.xml");
        getCamelContext().addRoutes(new TestRoutes(transactionPolicy));
    }
    
    @Test
    public void testNonTransactedRoute () throws Exception {
        final String response = sendRequest(
                "pcd-pcd01://localhost:" + getPort()
                    + "/communicateLabData/notransaction", WAN_REQUEST);
        assertTrue(response.contains("testexception"));        
        assertTrue(response.contains("MSA|AR"));        
    }
    
    @Test
    public void testTransactedRoute () throws Exception {
        // setup the transaction mock        
        EasyMock.reset(txManager);
        txManager.getTransaction((TransactionDefinition) EasyMock.anyObject());
        EasyMock.expectLastCall().andReturn(new SimpleTransactionStatus()).times(1);
        txManager.rollback((SimpleTransactionStatus) (EasyMock.anyObject()));
        EasyMock.expectLastCall().asStub();
                
        EasyMock.replay(txManager);

        final String response = sendRequest(
                "pcd-pcd01://localhost:" + getPort()
                    + "/communicateLabData/transacted", WAN_REQUEST);
        EasyMock.verify(txManager);
        assertTrue(response.contains("testexception"));        
        assertTrue(response.contains("MSA|AR"));
    }
    
    public String sendRequest(final String url, final String body) throws Exception {
        final Exchange exchange = new DefaultExchange(
                getCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setBody(body);
        final Exchange response = getProducerTemplate().send(url, exchange);
        return response.getOut().getBody(String.class);
    }
    
    private static class TestRoutes extends RouteBuilder {
        
        private TransactedPolicy txPolicy;

        private TestRoutes (final TransactedPolicy policy) {
            txPolicy = policy;
        }

        @Override
        public void configure() throws Exception {
            from("pcd-pcd01://communicateLabData/notransaction")
                .throwException(new Hl7v2AcceptanceException("testexception"));
                
            from("pcd-pcd01://communicateLabData/transacted")
                .policy(txPolicy)
                .throwException(new Hl7v2AcceptanceException("testexception"));
        }
    }
}
