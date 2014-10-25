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
package org.openehealth.ipf.platform.camel.core.multiplast

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;
import org.junit.BeforeClass;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.security.AccessControlContext
import javax.security.auth.login.LoginContext
import javax.security.auth.Subject
import java.security.AccessController
import javax.security.auth.SubjectDomainCombiner

import static org.junit.Assert.assertTrue
import java.security.PrivilegedAction

/**
 * @author Dmytro Rud
 */
class TestMultiplast {

    private static ApplicationContext appContext
    private static ProducerTemplate producerTemplate
    private static CamelContext camelContext

    public static final String KEYSTORE_PROPERTY = "keystore.target";
    public static final String LOGIN_PROPERTY    = "java.security.auth.login.config";

    @BeforeClass
    static void setUpClass() {
        appContext       = new ClassPathXmlApplicationContext('context-core-extend-multiplast.xml')
        producerTemplate = appContext.getBean('template')
        camelContext     = appContext.getBean('camelContext')
    }

    
    private Exchange send(body, recipients) {
        Exchange ex = new DefaultExchange(camelContext)
        ex.pattern = ExchangePattern.InOut
        ex.in.body = body
        ex.in.headers['recipients'] = recipients
        producerTemplate.send('direct:start', ex)
    }    

    
    private String endpoint(int port) {
        return "mina2:tcp://localhost:${port}?sync=true&lazySessionCreation=true&minaLogger=true&textline=true"
    }

    private String ep(String key) {
        return "direct:${key}"
    }

    @Test
    void testMultiplast() {
        Exchange resultExchange

        // normal parallel processing
        long startTimestamp = System.currentTimeMillis()
        resultExchange = send('abc, def, ghi', [endpoint(8000), endpoint(8001), endpoint(8002)].join(';'))
        assert Exchanges.resultMessage(resultExchange).body == '123456789'

        // TODO: the check below fails on nodes with high CPU load
        // assert System.currentTimeMillis() - startTimestamp < 4000L

        // normal parallel processing again -- to check in logs whether redundant sessions are being created
        resultExchange = send('abc, def, ghi', [endpoint(8000), endpoint(8001), endpoint(8002)].join(';'))
        resultExchange = send('abc, def, ghi', [endpoint(8000), endpoint(8001), endpoint(8002)].join(';'))

        // different lengths of bodies' and recipients' lists -- should fail
        resultExchange = send('abc, def, ghi', [endpoint(8000), endpoint(8001)].join(';'))
        assert resultExchange.failed

        resultExchange = send('abc, def', [endpoint(8000), endpoint(8001), endpoint(8002)].join(';'))
        assert resultExchange.failed
    }

    @Test
    void testMultiplastWithAccessContext() {
        PrivilegedAction action = new PrivilegedAction() {
            public Object run() throws Exception{
                Exchange resultExchange = send('ear, war, jar', [ep('abc'), ep('def'), ep('ghi')].join(';'))
                return Exchanges.resultMessage(resultExchange).body == 'ijklmnopr'
            }
        };

        def uriPath = {String filename -> TestMultiplast.classLoader.getResource(filename).path}

        System.properties[LOGIN_PROPERTY]    = uriPath('login.conf')
        System.properties[KEYSTORE_PROPERTY] = uriPath('client.jks')

        LoginContext lc = new LoginContext('DF', new TestCallbackHandler())
        lc.login();
        Subject subject = lc.subject

        AccessControlContext acc = new AccessControlContext (AccessController.getContext(), new SubjectDomainCombiner(subject))
        assertTrue Subject.doAsPrivileged(subject, action, acc)
    }

}
