/*
 * Copyright 2009 InterComponentWare AG.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core

import static org.junit.Assert.*

import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.impl.DefaultExchange
import org.junit.After
import org.junit.AfterClass
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Generic Unit Test container for MLLP components.
 * 
 * @author Dmytro Rud
 */
class MllpTestContainer {
    def static CONTEXT_DESCRIPTOR = 'some-mllp-iti-context.xml'
    
    def static producerTemplate
    def static camelContext
    def static auditSender
    def static appContext
    
    
    /**
     * Initializes a test on the basis of a Spring descriptor.
     */
    def static init(String descriptorFile, boolean standalone) {
        appContext = new ClassPathXmlApplicationContext(descriptorFile)
        producerTemplate = appContext.getBean('template', ProducerTemplate.class)
        camelContext = appContext.getBean('camelContext', CamelContext.class)
        
        auditSender = new MockedSender()
        AuditEventMessage[] a = new AuditEventMessage[2];
        auditSender.sendAuditEvent(a)
        assertEquals(2, auditSender.messages.size())
        auditSender.messages.clear()
        AuditorModuleContext.context.sender = auditSender
        AuditorModuleContext.context.config.auditRepositoryHost = 'localhost'
        AuditorModuleContext.context.config.auditRepositoryPort = 514
        AuditorModuleContext.context.config.auditSourceId = 'audit-source-id'
        AuditorModuleContext.context.config.auditEnterpriseSiteId = 'audit-enterprise-site-id'

        if (standalone) {
            Thread.currentThread().join()
        }
    }
    
    @After
    void tearDown() {
        auditSender.messages.clear()
    }
    
    @AfterClass
    static void tearDownAfterClass() {
        appContext.destroy()
    }
    
    
    /**
     * Checks whether the message represents a (positive) ACK.
     */
    static void assertACK(MessageAdapter msg) {
        assertTrue(msg.MSH[9][1].value == 'ACK')
        assertFalse(msg.MSA[1].value[1] in ['R', 'E'])
    }
    
    
    /**
     * Checks whether the message represents a positive ReSPonse.
     */
    static void assertRSP(MessageAdapter msg) {
        assertTrue(msg.MSH[9][1].value == 'RSP')
        assertFalse(msg.MSA[1].value[1] in ['R', 'E'])
    }
    
    
    /**
     * Checks whether the message represents a NAK.
     */
    static void assertNAK(MessageAdapter msg) {
        assertTrue(msg.MSH[9][1].value == 'ACK')
        assertTrue(msg.MSA[1].value[1] in ['R', 'E'])
        assertNotNull(msg.ERR)
    }
    
    /**
     * Checks whether the message represents a NAK with segments QPD and QAK.
     */
    static void assertNAKwithQPD(MessageAdapter msg, String messageType, String triggerEvent) {
        assertTrue(msg.MSH[9][1].value == messageType)
        assertTrue(msg.MSH[9][2].value == triggerEvent)
        assertTrue(msg.MSA[1].value[1] in ['R', 'E'])
        assertNotNull(msg.ERR)
        assertNotNull(msg.QAK)
        assertNotNull(msg.QPD)
    }
    
    /**
     * Sends a request into the route.
     */
    static MessageAdapter send(String endpoint, Object body) {
        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = body
        Exchange result = producerTemplate.send(endpoint, exchange)
        if (result.exception) {
            throw result.exception
        }
        def response = Exchanges.resultMessage(result)
        response.getBody(MessageAdapter.class)
    }
    
    
    /**
     * Returns a sample HL7 message as String. 
     */
    static String getMessageString(String msh9, String msh12, boolean needPid = true) {
        def s = 'MSH|^~\\&|MESA_PD_SUPPLIER|XYZ_HOSPITAL|dummy|dummy|20081204114742||' +
                msh9 +
                '|123456|T|' +
                msh12 +
                '|||ER\n' +
                'EVN|A01|20081204114742\n'
        if(needPid) {
            s = s + 'PID|1||001^^^XREF2005~002^^^HIMSS2005||Multiple^Christof^Maria^Prof.|Eisner|' +
                    '19530429|M|||Bahnhofstr. 1^^Testort^^01234^DE^H|||||||AccNr01^^^ANICPA|' +
                    '111-222-333|\n'
        }
        s = s + 'PV1|1|O|\n'
        return s
    }
}
