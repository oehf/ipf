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
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.AfterClass
import org.openehealth.ipf.commons.ihe.core.atna.AbstractMockedAuditSender
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor
import org.openehealth.ipf.commons.ihe.core.atna.custom.Hl7v3Auditor
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openhealthtools.ihe.atna.auditor.*
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import org.springframework.web.context.support.WebApplicationContextUtils

/**
 * Base class for tests that are run within an embedded web container.
 * This class requires that an application context named "context.xml" is
 * placed within the root of the test resources.
 * @author Jens Riemschneider
 */
class StandardTestContainer {
     private static final transient Logger log = LoggerFactory.getLogger(StandardTestContainer.class)
     
     static ProducerTemplate producerTemplate
     static ServletServer servletServer
     static ApplicationContext appContext

     static CamelContext camelContext
     
     static int port

     /*
      *  Port to be used, when the test is subclassed by java applications.
      */
     public static int DEMO_APP_PORT = 8999
     
     static void startServer(servlet, String appContextName, boolean secure, int serverPort, AbstractMockedAuditSender auditSender = new MockedSender(), String servletName = null) {
         URL contextResource = StandardTestContainer.class.getResource(appContextName.startsWith("/") ? appContextName : "/" + appContextName)
         
         port = serverPort
         log.info("Publishing services on port: ${port}")
         
         servletServer = new JettyServer(
                 contextResource: contextResource.toURI().toString(),
                 port: port,
                 contextPath: '/',
                 servletPath: '/*',
                 servlet: servlet,
                 servletName: servletName,
                 secure: secure,
                 keystoreFile: 'server.jks',
                 keystorePass: 'changeit',
                 truststoreFile: 'server.jks',
                 truststorePass: 'changeit')
         
         servletServer.start()
         
         def servletContext = servlet.servletConfig.servletContext
         appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
         producerTemplate = appContext.getBean('template', ProducerTemplate.class)
         camelContext = appContext.getBean('camelContext', CamelContext.class)

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
         CustomXdsAuditor.auditor.config.auditSourceId = 'customXdsSourceId'
         CustomXdsAuditor.auditor.config.auditRepositoryHost = 'localhost'
         CustomXdsAuditor.auditor.config.auditRepositoryHost = 'localhost'
         CustomXdsAuditor.auditor.config.auditRepositoryPort = auditPort
         CustomXdsAuditor.auditor.config.systemUserId = 'customXdsUserId'
         //CustomXdsAuditor.auditor.config.systemAltUserId = 'customXdsAltUserId'

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

         AuditorModuleContext.context.sender = auditSender
     }

     static <T extends AbstractMockedAuditSender> T getAuditSender() {
         return (T) AuditorModuleContext.context.sender
     }

     static int startServer(servlet, String appContextName, AbstractMockedAuditSender auditSender = new MockedSender(), String servletName = null) {
         startServer(servlet, appContextName, false, auditSender, servletName)
     }
     
     static int startServer(servlet, String appContextName, boolean secure, AbstractMockedAuditSender auditSender = new MockedSender(), String servletName = null) {
         int port = JettyServer.freePort
         startServer(servlet, appContextName, secure, port, auditSender, servletName)
         port
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
      * @param headers
      *          optional Camel request message headers.
      * @return the output object.
      */
    def send(endpoint, input, outType, Map headers = null) {
         Exchange result = send(endpoint, input, headers)
         Exchanges.resultMessage(result).getBody(outType)
     }

     /**
      * Sends the given object to the endpoint.
      * @param endpoint
      *          the endpoint to send the object to.
      * @param input
      *          the input object.
      * @param headers
      *          optional Camel request message headers.
      * @return the resulting exchange.
      */
    def send(endpoint, input, Map headers = null) {
         def exchange = new DefaultExchange(camelContext)
         exchange.in.body = input
         if (headers) {
             exchange.in.headers.putAll(headers)
         }
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
         assert actual.'@csd-code' == code && actual.@codeSystemName == scheme
     }

     def checkEvent(event, code, iti, actionCode, outcome) {
         checkCode(event.EventID, code, 'DCM')
         checkCode(event.EventTypeCode, iti, 'IHE Transactions')         
         assert event.@EventActionCode == actionCode
         assert event.@EventDateTime != null && event.@EventDateTime != ''
         assert event.@EventOutcomeIndicator == outcome
     }
     
     def checkSource(source, String httpAddr, String requestor) {
         checkSource(source, requestor, true)
         assert source.@UserID == httpAddr
     }
     
     def checkSource(source, String requestor, boolean userIdRequired = false) {
         // This should be something useful, but it isn't fully specified yet (see CP-402)
         if (userIdRequired) {
             assert source.@UserID != null && source.@UserID != ''
         }
         assert source.@UserIsRequestor == requestor
         assert source.@NetworkAccessPointTypeCode == '2' || source.@NetworkAccessPointTypeCode == '1'
         assert source.@NetworkAccessPointID != null && source.@NetworkAccessPointID != '' 
         // This will be required soon:
         // assert source.@AlternativeUserID != null && source.@AlternativeUserID != ''
         checkCode(source.RoleIDCode, '110153', 'DCM')
     }

     def checkDestination(destination, String httpAddr, String requestor) {
         checkDestination(destination, requestor, true)
         assert destination.@UserID == httpAddr
     }
     
     def checkDestination(destination, String requestor, boolean userIdRequired = true) {
         // This should be something useful, but it isn't fully specified yet (see CP-402)
         if (userIdRequired) {
             assert destination.@UserID != null && destination.@UserID != ''
         }
         assert destination.@UserIsRequestor == requestor
         assert destination.@NetworkAccessPointTypeCode == '1' || destination.@NetworkAccessPointTypeCode == '2'
         assert destination.@NetworkAccessPointID != null && destination.@NetworkAccessPointID != '' 
         // This will be required soon:
         // assert source.@AlternativeUserID != null && source.@AlternativeUserID != ''
         checkCode(destination.RoleIDCode, '110152', 'DCM')
     }
     
     def checkAuditSource(auditSource, String sourceId) {
         assert auditSource.@AuditSourceID == sourceId 
     }
     
     def checkHumanRequestor(human, String name, List<CodedValueType> roles = []) {
         assert human.@UserIsRequestor == 'true'
         assert human.@UserID == name
         assert human.@UserName == name
         assert human.RoleIDCode.size() == roles.size()
         roles.eachWithIndex { CodedValueType cvt, int i ->
             assert human.RoleIDCode[i].'@csd-code' == cvt.code
             assert human.RoleIDCode[i].@codeSystemName == cvt.codeSystemName
             assert human.RoleIDCode[i].@originalText== cvt.originalText
         }
     }
     
     def checkPatient(patient, String... allowedIds = ['id3^^^&1.3&ISO']) {
         assert patient.@ParticipantObjectTypeCode == '1'
         assert patient.@ParticipantObjectTypeCodeRole == '1'
         checkCode(patient.ParticipantObjectIDTypeCode, '2', 'RFC-3881')
         assert patient.@ParticipantObjectID in allowedIds
     }
     
     def checkQuery(query, String iti, String queryText, String queryUuid) {
         assert query.@ParticipantObjectTypeCode == '2'
         assert query.@ParticipantObjectTypeCodeRole == '24'
         checkCode(query.ParticipantObjectIDTypeCode, iti, 'IHE Transactions')
         assert query.@ParticipantObjectID == queryUuid
         def base64 = query.ParticipantObjectQuery.text().getBytes('UTF8')
         def decoded = new String(Base64.decodeBase64(base64))
         assert decoded.contains(queryText)
     }
     
     def checkUri(uri, String docUri, String docUniqueId) {
         assert uri.@ParticipantObjectTypeCode == '2'
         assert uri.@ParticipantObjectTypeCodeRole == '3'
         checkCode(uri.ParticipantObjectIDTypeCode, '12', 'RFC-3881')
         assert uri.@ParticipantObjectID == docUri
         assert uri.@ParticipantObjectDetail == docUniqueId
     }
     
    def checkDocument(uri, String docUniqueId, String homeId, String repoId) {
        assert uri.@ParticipantObjectTypeCode == '2'
        assert uri.@ParticipantObjectTypeCodeRole == '3'
        checkCode(uri.ParticipantObjectIDTypeCode, '9', 'RFC-3881')
        assert uri.@ParticipantObjectID == docUniqueId

        checkParticipantObjectDetail(uri.ParticipantObjectDetail[0], 'Repository Unique Id', repoId)
        checkParticipantObjectDetail(uri.ParticipantObjectDetail[1], 'ihe:homeCommunityID', homeId)
    }

    def checkImageDocument(uri, String docUniqueId, String homeId, String repoId, String studyId, String seriesId) {
        assert uri.@ParticipantObjectTypeCode == '2'
        assert uri.@ParticipantObjectTypeCodeRole == '3'
        checkCode(uri.ParticipantObjectIDTypeCode, '9', 'RFC-3881')
        assert uri.@ParticipantObjectID == docUniqueId

        checkParticipantObjectDetail(uri.ParticipantObjectDetail[0], 'Study Instance Unique Id', studyId)
        checkParticipantObjectDetail(uri.ParticipantObjectDetail[1], 'Series Instance Unique Id', seriesId)
        checkParticipantObjectDetail(uri.ParticipantObjectDetail[2], 'Repository Unique Id', repoId)
        checkParticipantObjectDetail(uri.ParticipantObjectDetail[3], 'ihe:homeCommunityID', homeId)
    }

     def checkParticipantObjectDetail(detail, String expectedType, String expectedValue) {
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

     def checkRegistryObjectParticipantObjectDetail(detail, String typeCode, String registryObjectUuid) {
         assert detail.@ParticipantObjectTypeCode == '2'
         assert detail.@ParticipantObjectTypeCodeRole == '3'
         checkCode(detail.ParticipantObjectIDTypeCode, typeCode, 'IHE XDS Metadata')
         assert detail.@ParticipantObjectID == registryObjectUuid
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
