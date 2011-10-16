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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55

import groovy.util.slurpersupport.GPathResult
import javax.xml.ws.handler.MessageContext
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.cxf.jaxws.context.WebServiceContextImpl
import org.apache.cxf.ws.addressing.AddressingProperties
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl
import org.apache.cxf.ws.addressing.AttributedURIType
import org.apache.cxf.ws.addressing.RelatesToType
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Exception
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55PortType
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55DeferredResponsePortType
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy
import org.openehealth.ipf.platform.camel.ihe.hl7v3.DefaultHl7v3WebService
import static org.apache.cxf.ws.addressing.JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND
import static org.apache.cxf.ws.addressing.JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_OUTBOUND
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory.response

/**
 * Service implementation for the Responding Gateway actor
 * of the IHE ITI-55 transaction (XCPD)
 * with support for the Deferred Response option.
 *
 * @author Dmytro Rud
 */
public class Iti55Service extends DefaultHl7v3WebService implements Iti55PortType {
    private static final transient Log LOG = LogFactory.getLog(Iti55Service.class)

    private static final String

    private final Iti55Endpoint endpoint
    private final ProducerTemplate producerTemplate
    private final WsAuditStrategy auditStrategy


    /**
     * Constructor.
     * @param endpoint
     *      Camel endpoint instance this Web Service corresponds to.
     */
    Iti55Service(Iti55Endpoint endpoint) {
        super(endpoint.component.wsTransactionConfiguration)
        this.endpoint = endpoint
        this.producerTemplate = endpoint.camelContext.createProducerTemplate()
        this.auditStrategy = endpoint.audit ?
            new Iti55AuditStrategy(true, endpoint.allowIncompleteAudit) : null
    }


    @Override
    Object discoverPatients(Object requestString) {
        return doProcess(requestString);
    }

    @Override
    Object discoverPatientsDeferred(Object requestString) {
        return doProcess(requestString);
    }


    @Override
    protected Object doProcess(Object request) {
        String requestString = (String) request
        GPathResult requestXml = Hl7v3Utils.slurp(requestString)
        String responsePriorityCode = requestXml.controlActProcess.queryByParameter.responsePriorityCode.@code.text()

        MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
        AddressingProperties apropos = (AddressingProperties) messageContext.get(SERVER_ADDRESSING_PROPERTIES_INBOUND)
        final String requestMessageId = apropos?.messageID?.value

        WsAuditDataset auditDataset = startAtnaAuditing(requestString, auditStrategy)

        if (responsePriorityCode == 'I') {
            // process synchronous route
            Object response = doProcess0(requestString, requestXml)
            finalizeAtnaAuditing(response, auditStrategy, auditDataset)
            configureWsaHeaders(Iti55PortType.REGULAR_REQUEST_OUTPUT_ACTION, requestMessageId)
            return response
        }

        else if (responsePriorityCode == 'D') {
            String respondToUri = requestXml.respondTo[0].telecom.@value.text()

            // check whether deferred response URI is specified
            if (! respondToUri) {
                Hl7v3Exception hl7v3Exception = new Hl7v3Exception(
                        message: 'Deferred response receiver URI is not specified',
                        typeCode : 'AE',
                        acknowledgementDetailCode: 'SYN105',
                        queryResponseCode: 'QE')
                String nak = createNak(requestXml, hl7v3Exception)
                finalizeAtnaAuditing(nak, auditStrategy, auditDataset)
                return nak
            }

            // in a separate thread: run the route, send its result synchronously
            // to the deferred response URI, ignore all errors and ACKs
            endpoint.deferredResponseExecutorService.submit(new Runnable() {
                @Override
                void run() {
                    Object response = doProcess0(requestString, requestXml)
                    finalizeAtnaAuditing(response, auditStrategy, auditDataset)
                    configureWsaHeaders(Iti55DeferredResponsePortType.DEFERRED_RESPONSE_INPUT_ACTION, requestMessageId)
                    producerTemplate.sendBody(respondToUri, response)
                }
            })

            // return an immediate MCCI ACK
            configureWsaHeaders(Iti55PortType.DEFERRED_REQUEST_OUTPUT_ACTION, requestMessageId)
            return response(requestXml, null, "MCCI_IN000002UV01", false, false)
        }

        else {
            Hl7v3Exception hl7v3exception = new Hl7v3Exception(
                    message: "Unsupported processing mode '${responsePriorityCode}'",
                    typeCode: 'AE',
                    acknowledgementDetailCode: 'NS250',
                    queryResponseCode: 'QE')
            String nak = createNak(requestXml, hl7v3exception)
            finalizeAtnaAuditing(nak, auditStrategy, auditDataset)
            return nak
        }
    }


    public Object doProcess0(String requestString, GPathResult requestXml) {
        Exchange result = process(requestString)
        return (result.exception != null) ?
            nak(result.exception, requestXml) :
            prepareBody(result)
    }


    /**
     * Generates an XCPD-specific NAK from the given exception.
     * @param exception
     *      occurred exception.
     * @param requestString
     *      original request as XML string.
     * @return
     *      NAK as XML string.
     */
    private static String nak(Exception exception, GPathResult requestXml) {
        Hl7v3Exception hl7v3Exception
        if (exception instanceof Hl7v3Exception) {
            hl7v3Exception = (Hl7v3Exception) exception
        } else {
            hl7v3Exception = new Hl7v3Exception(
                cause: exception,
                detectedIssueManagementCode: 'InternalError',
                detectedIssueManagementCodeSystem: '1.3.6.1.4.1.19376.1.2.27.3')
        }
        return createNak(requestXml, hl7v3Exception)
    }


    /**
     * Configures outbound WS-Addressing headers "Action" and "RelatesTo".
     */
    private static void configureWsaHeaders(String action, String relatesTo) {
        MessageContext messageContext = new WebServiceContextImpl().messageContext
        AddressingProperties apropos = (AddressingProperties) messageContext.get(SERVER_ADDRESSING_PROPERTIES_OUTBOUND)
        if (! apropos) {
            apropos = new AddressingPropertiesImpl()
            messageContext.put(SERVER_ADDRESSING_PROPERTIES_OUTBOUND, apropos)
        }
        apropos.action = new AttributedURIType(value: action)
        apropos.relatesTo = new RelatesToType(value: relatesTo)
    }
}
