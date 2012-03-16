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
package org.openehealth.ipf.platform.camel.ihe.ws

import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.impl.DefaultExchange
import org.apache.commons.codec.binary.Base64
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.junit.After
import org.junit.AfterClass
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender
import org.openehealth.ipf.commons.ihe.core.atna.custom.Hl7v3Auditor
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openhealthtools.ihe.atna.auditor.*
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.web.context.support.WebApplicationContextUtils
import org.apache.commons.io.IOUtils
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor

/**
 * Base class for tests that are run within an embedded web container.
 * This class requires that an application context named "context.xml" is
 * placed within the root of the test resources.
 * @author Jens Riemschneider
 */
class StandardTestContainer {
     private static final transient Log log = LogFactory.getLog(StandardTestContainer.class)
     
     static ProducerTemplate producerTemplate
     static ServletServer servletServer
     static ApplicationContext appContext

     static MockedSender auditSender
     static CamelContext camelContext
     
     static int port

     /*
      *  Port to be used, when the test is subclassed by java applications.
      */
     public static int DEMO_APP_PORT = 8999
     
     static void startServer(servlet, String appContextName, boolean secure, int serverPort) {
         def contextResource = new ClassPathResource(appContextName)
         
         port = serverPort
         log.info("Publishing services on port: ${port}")
         
         servletServer = new JettyServer(
                 contextResource: contextResource.getURI().toString(),
                 port: port,
                 contextPath: '',
                 servletPath: '/*',
                 servlet: servlet,
                 secure: secure,
                 keystoreFile: 'keystore',
                 keystorePass: 'changeit',
                 truststoreFile: 'keystore',
                 truststorePass: 'changeit')
         
         servletServer.start()
         
         def servletContext = servlet.servletConfig.servletContext
         appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
         producerTemplate = appContext.getBean('template')  
         camelContext = appContext.getBean('camelContext')  

         def auditPort = 514
         
         XDSRegistryAuditor.auditor.config = new AuditorModuleConfig()
         XDSRegistryAuditor.auditor.config.auditSourceId = 'registryId'
         XDSRegistryAuditor.auditor.config.auditRepositoryHost = 'localhost'
         XDSRegistryAuditor.auditor.config.auditRepositoryPort = auditPort
         XDSRegistryAuditor.auditor.config.systemUserId = 'registryUserId'
         XDSRegistryAuditor.auditor.config.systemAltUserId = 'registryAltUserId'

         XDSSourceAuditor.auditor.config = new AuditorModuleConfig()
         XDSSourceAuditor.auditor.config.auditSourceId = 'sourceId'
         XDSSourceAuditor.auditor.config.auditRepositoryHost = 'localhost'
         XDSSourceAuditor.auditor.config.auditRepositoryPort = auditPort
         XDSSourceAuditor.auditor.config.systemUserId = 'sourceUserId'
         XDSSourceAuditor.auditor.config.systemAltUserId = 'sourceAltUserId'

         XDSConsumerAuditor.auditor.config = new AuditorModuleConfig()
         XDSConsumerAuditor.auditor.config.auditSourceId = 'consumerId'
         XDSConsumerAuditor.auditor.config.auditRepositoryHost = 'localhost'
         XDSConsumerAuditor.auditor.config.auditRepositoryPort = auditPort
         XDSConsumerAuditor.auditor.config.systemUserId = 'consumerUserId'
         XDSConsumerAuditor.auditor.config.systemAltUserId = 'consumerAltUserId'

         XDSRepositoryAuditor.auditor.config = new AuditorModuleConfig()
         XDSRepositoryAuditor.auditor.config.auditSourceId = 'repositoryId'
         XDSRepositoryAuditor.auditor.config.auditRepositoryHost = 'localhost'
         XDSRepositoryAuditor.auditor.config.auditRepositoryPort = auditPort
         XDSRepositoryAuditor.auditor.config.systemUserId = 'repositoryUserId'
         XDSRepositoryAuditor.auditor.config.systemAltUserId = 'repositoryAltUserId'
             
         Hl7v3Auditor.auditor.config = new AuditorModuleConfig()
         Hl7v3Auditor.auditor.config.auditRepositoryHost = 'localhost'
         Hl7v3Auditor.auditor.config.auditRepositoryPort = auditPort

         CustomXdsAuditor.auditor.config = new AuditorModuleConfig()
         CustomXdsAuditor.auditor.config.auditSourceId = 'registryId'
         CustomXdsAuditor.auditor.config.auditRepositoryHost = 'localhost'
         CustomXdsAuditor.auditor.config.auditRepositoryHost = 'localhost'
         CustomXdsAuditor.auditor.config.auditRepositoryPort = auditPort
         CustomXdsAuditor.auditor.config.systemUserId = 'registryUserId'
         CustomXdsAuditor.auditor.config.systemAltUserId = 'registryAltUserId'

         XCAInitiatingGatewayAuditor.auditor.config = new AuditorModuleConfig()
         XCAInitiatingGatewayAuditor.auditor.config.auditSourceId = 'initiatingGwId'
         XCAInitiatingGatewayAuditor.auditor.config.auditRepositoryHost = 'localhost'
         XCAInitiatingGatewayAuditor.auditor.config.auditRepositoryPort = auditPort
         XCAInitiatingGatewayAuditor.auditor.config.systemUserId = 'initiatingGwUserId'
         XCAInitiatingGatewayAuditor.auditor.config.systemAltUserId = 'initiatingGwAltUserId'

         XCARespondingGatewayAuditor.auditor.config = new AuditorModuleConfig()
         XCARespondingGatewayAuditor.auditor.config.auditSourceId = 'respondingGwId'
         XCARespondingGatewayAuditor.auditor.config.auditRepositoryHost = 'localhost'
         XCARespondingGatewayAuditor.auditor.config.auditRepositoryPort = auditPort
         XCARespondingGatewayAuditor.auditor.config.systemUserId = 'respondingGwUserId'
         XCARespondingGatewayAuditor.auditor.config.systemAltUserId = 'respondingGwAltUserId'

         auditSender = new MockedSender()
         AuditorModuleContext.context.sender = auditSender
     }

     static void startServer(servlet, String appContextName) {
         startServer(servlet, appContextName, false)
     }
     
     static void startServer(servlet, String appContextName, boolean secure) {
         startServer(servlet, appContextName, secure, JettyServer.freePort)
     }

     
     @AfterClass
     static void stopServer() {
         if (servletServer != null) {
             servletServer.stop()
         }
     }

     @After
     void tearDown() {
         if (servletServer  == null){
             String msg = "The test server is not started!\n"
             msg += "You should call startServer(...) in the @BeforeClass method of your test to initialize the server."
             throw new IllegalStateException(msg)
         }
         auditSender.messages.clear()
     }
          
     /**
      * Sends the given object to the endpoint.
      * @param endpoint
      *          the endpoint to send the object to.
      * @param input
      *          the input object.
      * @param outType
      *          the type of the output object.
      * @return the output object.
      */
     def send(endpoint, input, outType) {
         Exchange result = send(endpoint, input)
         Exchanges.resultMessage(result).getBody(outType)
     }

     /**
      * Sends the given object to the endpoint.
      * @param endpoint
      *          the endpoint to send the object to.
      * @param input
      *          the input object.
      * @return the resulting exchange.
      */
     def send(endpoint, input) {
         def exchange = new DefaultExchange(camelContext)
         exchange.in.body = input       
         Exchange result = producerTemplate.send(endpoint, exchange)
         if (result.exception) {
             throw result.exception
         }
         return result
     }

     def getAudit(actionCode, addr) {
         auditSender.messages.collect { getMessage(it) }.findAll {
             it.EventIdentification.@EventActionCode == actionCode
         }.findAll {
             it.ActiveParticipant.any { obj -> obj.@UserID == addr } ||
             it.ParticipantObjectIdentification.any { obj -> obj.@ParticipantObjectID == addr }
         }
     }
     
     def getMessage(rawMessage) {
         def xmlText = rawMessage.auditMessage.toString()
         new XmlSlurper().parseText(xmlText)         
     }
     
     def checkCode(actual, code, scheme) {
         assert actual.@code == code && actual.@codeSystemName == scheme
     }

     def checkEvent(event, code, iti, actionCode, outcome) {
         checkCode(event.EventID, code, 'DCM')
         checkCode(event.EventTypeCode, iti, 'IHE Transactions')         
         assert event.@EventActionCode == actionCode
         assert event.@EventDateTime != null && event.@EventDateTime != ''
         assert event.@EventOutcomeIndicator == outcome
     }
     
     def checkSource(source, httpAddr, requestor) {
         checkSource(source, requestor)
         assert source.@UserID == httpAddr
     }
     
     def checkSource(source, requestor) {
         // This should be something useful, but it isn't fully specified yet (see CP-402)
         assert source.@UserID != null && source.@UserID != ''  
         assert source.@UserIsRequestor == requestor
         assert source.@NetworkAccessPointTypeCode == '2' || source.@NetworkAccessPointTypeCode == '1'
         assert source.@NetworkAccessPointID != null && source.@NetworkAccessPointID != '' 
         // This will be required soon:
         // assert source.@AlternativeUserID != null && source.@AlternativeUserID != ''
         checkCode(source.RoleIDCode, '110153', 'DCM')
     }
     
     def checkDestination(destination, httpAddr, requestor) {
         checkDestination(destination, requestor)
         assert destination.@UserID == httpAddr
     }
     
     def checkDestination(destination, requestor) {
         // This should be something useful, but it isn't fully specified yet (see CP-402)
         assert destination.@UserID != null && destination.@UserID != ''
         assert destination.@UserIsRequestor == requestor
         assert destination.@NetworkAccessPointTypeCode == '1' || destination.@NetworkAccessPointTypeCode == '2'
         assert destination.@NetworkAccessPointID != null && destination.@NetworkAccessPointID != '' 
         // This will be required soon:
         // assert source.@AlternativeUserID != null && source.@AlternativeUserID != ''
         checkCode(destination.RoleIDCode, '110152', 'DCM')
     }
     
     def checkAuditSource(auditSource, sourceId) {
         assert auditSource.@AuditSourceID == sourceId 
     }
     
     def checkHumanRequestor(human) {
         assert human.@UserIsRequestor == 'true'
         assert human.@UserID != null 
     }
     
     def checkPatient(patient) {
         assert patient.@ParticipantObjectTypeCode == '1'
         assert patient.@ParticipantObjectTypeCodeRole == '1'
         checkCode(patient.ParticipantObjectIDTypeCode, '2', 'RFC-3881')
         assert patient.@ParticipantObjectID == 'id3^^^&1.3&ISO'         
     }
     
     def checkQuery(query, iti, queryText, queryUuid) {
         assert query.@ParticipantObjectTypeCode == '2'
         assert query.@ParticipantObjectTypeCodeRole == '24'
         checkCode(query.ParticipantObjectIDTypeCode, iti, 'IHE Transactions')
         assert query.@ParticipantObjectID == queryUuid
         def base64 = query.ParticipantObjectQuery.text().getBytes('UTF8')
         def decoded = new String(Base64.decodeBase64(base64))
         assert decoded.contains(queryText)
     }
     
     def checkUri(uri, docUri, docUniqueId) {
         assert uri.@ParticipantObjectTypeCode == '2'
         assert uri.@ParticipantObjectTypeCodeRole == '3'
         checkCode(uri.ParticipantObjectIDTypeCode, '12', 'RFC-3881')
         assert uri.@ParticipantObjectID == docUri
         assert uri.@ParticipantObjectDetail == docUniqueId
     }
     
     def checkDocument(uri, docUniqueId, homeId, repoId) {
         assert uri.@ParticipantObjectTypeCode == '2'
         assert uri.@ParticipantObjectTypeCodeRole == '3'
         checkCode(uri.ParticipantObjectIDTypeCode, '9', 'RFC-3881')
         assert uri.@ParticipantObjectID == docUniqueId

         checkParticipantObjectDetail(uri.ParticipantObjectDetail[0], 'Repository Unique Id', repoId)
         checkParticipantObjectDetail(uri.ParticipantObjectDetail[1], 'ihe:homeCommunityID', homeId)
     }
     
     def checkParticipantObjectDetail(detail, expectedType, expectedValue) {
         assert detail.@type == expectedType
         String base64Expected = new String(Base64.encodeBase64(expectedValue.getBytes('UTF8')))
         String base64Actual = detail.@value
         assert base64Actual == base64Expected  
     }
     
     def checkSubmissionSet(submissionSet) {
         assert submissionSet.@ParticipantObjectTypeCode == '2'
         assert submissionSet.@ParticipantObjectTypeCodeRole == '20'
         checkCode(submissionSet.ParticipantObjectIDTypeCode, 'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd', 'IHE XDS Metadata')
         assert submissionSet.@ParticipantObjectID == '123'
     }

     static String readFile(String fn) {
         InputStream inputStream
         String s = null
         try {
             inputStream = StandardTestContainer.class.classLoader.getResourceAsStream(fn)
             s = IOUtils.toString(inputStream)
         } finally {
             IOUtils.closeQuietly(inputStream)
             return s
         }
     }
}
