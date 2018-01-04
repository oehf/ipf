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
package org.openehealth.ipf.platform.camel.ihe.hl7v3

import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import org.apache.camel.Exchange
import org.apache.cxf.message.Message
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.core.DomBuildersThreadLocal
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareWsTransactionConfiguration
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationsPortType
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList

import javax.xml.ws.BindingProvider

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.getElementNS
import static org.openehealth.ipf.commons.xml.XmlUtils.rootElementName
import static org.openehealth.ipf.commons.xml.XmlYielder.yieldElement
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3ContinuationUtils.*

/**
 * Camel producer HL7 v3-based IHE transactions with Continuation support.
 * <p>
 * Format of continuation messages is described, for example,
 * in the IHE PIXv3/PDQv3 Supplement August 2010, pp. 85-87 and 117-119.
 */
class Hl7v3ContinuationAwareProducer extends AbstractWsProducer<Hl7v3AuditDataset, Hl7v3ContinuationAwareWsTransactionConfiguration, String, String> {
    private static final transient Logger LOG = LoggerFactory.getLogger(Hl7v3ContinuationAwareProducer.class)

    private static final DomBuildersThreadLocal DOM_BUILDERS = new DomBuildersThreadLocal()
    private static final CombinedXmlValidator VALIDATOR = new CombinedXmlValidator()

    private final Hl7v3ContinuationAwareWsTransactionConfiguration wsTransactionConfiguration

    private final boolean supportContinuation
    private final boolean autoCancel
    private final boolean validationOnContinuation

    private final AuditStrategy<Hl7v3AuditDataset> auditStrategy
    private final AuditContext auditContext

    // TODO: make this value configurable
    private final int defaultContinuationQuantity = 10


    Hl7v3ContinuationAwareProducer(
            Hl7v3ContinuationAwareEndpoint endpoint,
            JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
        super(endpoint, clientFactory, String.class, String.class)

        this.wsTransactionConfiguration = endpoint.component.wsTransactionConfiguration
        this.supportContinuation        = endpoint.supportContinuation
        this.autoCancel                 = endpoint.autoCancel
        this.validationOnContinuation   = endpoint.validationOnContinuation

        this.auditStrategy = endpoint.manualAudit ? endpoint.component.getClientAuditStrategy() : null
        this.auditContext = endpoint.auditContext
    }


    @Override
    void process(Exchange exchange) {
        if (exchange.in.headers[AbstractWsEndpoint.WSA_REPLYTO_HEADER_NAME]) {
            throw new IllegalStateException('WS-Addressing asynchrony cannot be used in conjunction with interactive response continuation')
        }
        super.process(exchange)
    }


    /**
     * Dispatches the original request message, optionally handling continuations.
     * <p>
     * The response is always an XML String.
     */
    @Override
    protected String callService(Object clientObject, String requestString) {
        Hl7v3ContinuationsPortType client = (Hl7v3ContinuationsPortType) clientObject
        String rootElementName = rootElementName(requestString)
        switch (rootElementName) {
            case wsTransactionConfiguration.mainRequestRootElementName:
                GPathResult request = slurp(requestString)
                String responseString = client.operation(requestString)
                if (! supportContinuation) {
                    return responseString
                }
                return auditStrategy ?
                        processContinuationWithAtnaAuditing(client, request, responseString) :
                        processContinuation(client, request, responseString)

            case 'QUQI_IN000003UV01':
                // continuation is supported by the route, not by us
                return client.continuation(requestString)
            case 'QUQI_IN000003UV01_Cancel':
                return client.cancel(requestString)
        }
        throw new RuntimeException('Cannot dispatch request message with root element ' + rootElementName)
    }


    /**
     * A kind of wrapper which adds ATNA auditing functionality
     * to the "standard" {@link #processContinuation} method.
     */
    private String processContinuationWithAtnaAuditing(
        Hl7v3ContinuationsPortType client,
        GPathResult request,
        String fragmentString) {

        Hl7v3AuditDataset auditDataset = null
        // fill request-related ATNA audit fields
        try {
            auditDataset = auditStrategy.createAuditDataset(auditContext)
            Map<String, Object> requestContext = ((BindingProvider) client).requestContext
            auditDataset.setRemoteAddress(requestContext.get(Message.ENDPOINT_ADDRESS)?.toString())
            auditStrategy.enrichAuditDatasetFromRequest(auditDataset, request, null)
        } catch (Exception e) {
            LOG.error("Phase 1 of client-side ATNA auditing failed", e)
        }

        Exception exception
        String responseString = null
        try {
            responseString = processContinuation(client, request, fragmentString)
        } catch (Exception e) {
            exception = e
        }

        try {
            if (exception) {
                auditDataset.eventOutcomeIndicator = EventOutcomeIndicator.SeriousFailure
            } else {
                auditStrategy.enrichAuditDatasetFromResponse(auditDataset, responseString)
            }
            auditStrategy.doAudit(auditDataset)
        } catch (Exception e) {
            LOG.error("Phase 2 of client-side ATNA auditing failed", e)
        } finally {
            if (exception) {
                throw exception
            }
        }

        return responseString
    }


    /**
     * Performs HL7v3 continuation MEP.
     */
    private String processContinuation(
        Hl7v3ContinuationsPortType client,
        GPathResult request,
        String fragmentString)
    {
        int continuationQuantity = parseInt(request.controlActProcess.queryByParameter.initialQuantity.@value.text())
        if (continuationQuantity < 0) {
            continuationQuantity = defaultContinuationQuantity
        }

        // accumulated response and some its elements
        Document accumulator = null
        Element accumulatorControlActProcess = null
        Element accumulatorSubjectsTail = null
        Element accumulatorQueryAck = null

        int fragmentsCount = 0
        int startResultNumber = 1

        while (true) {
            // validate current fragment
            if (validationOnContinuation) {
                VALIDATOR.validate(fragmentString, wsTransactionConfiguration.responseValidationProfile)
            }

            Document fragment = DOM_BUILDERS.get().parse(new ByteArrayInputStream(fragmentString.getBytes()))
            Element controlActProcess = getElementNS(fragment.documentElement, HL7V3_NSURI_SET, 'controlActProcess')
            Element queryAck = getElementNS(controlActProcess, HL7V3_NSURI_SET, 'queryAck')

            // check whether the fragment is a valid and positive response
            if ((fragment.documentElement.localName != wsTransactionConfiguration.mainResponseRootElementName) ||
                (getAttribute(queryAck, 'queryResponseCode', 'code') != 'OK'))
            {
                LOG.debug('Bad response type, continuation not possible')
                accumulator = null
                break
            }

            // check whether the fragment contains all necessary numbers
            int totalQuantity     = getIntFromValueAttribute(queryAck, 'resultTotalQuantity')
            int currentQuantity   = getIntFromValueAttribute(queryAck, 'resultCurrentQuantity')
            int remainingQuantity = getIntFromValueAttribute(queryAck, 'resultRemainingQuantity')
            if ((totalQuantity < 0) || (currentQuantity < 0) || (remainingQuantity < 0)) {
                LOG.debug('Total/current/remaining quantity not specified, continuation not possible')
                accumulator = null
                break
            }

            ++fragmentsCount
            if (accumulator == null) {
                // check whether the response consists from a single fragment,
                // i.e. whether aggregation is necessary at all
                if (remainingQuantity == 0) {
                    LOG.debug('Response not fragmented, return as is')
                    break
                }

                // first fragment serves as accumulator
                accumulator = fragment
                accumulatorControlActProcess = controlActProcess
                accumulatorQueryAck = queryAck
                // TODO: transactions other than PDQv3 and QED may need different tail determination logic
                accumulatorSubjectsTail = getElementNS(controlActProcess, HL7V3_NSURI_SET, 'reasonOf') ?: accumulatorQueryAck
            } else {
                // yield subjects from fragments 2..n into accumulated response
                NodeList subjects = controlActProcess.getElementsByTagNameNS(HL7V3_NSURI, 'subject')
                for (int i = 0; i < subjects.length; ++i) {
                    def subject = accumulator.importNode(subjects.item(i), true)
                    accumulatorControlActProcess.insertBefore(subject, accumulatorSubjectsTail)
                }

                // check whether the next fragment should be requested
                if (remainingQuantity == 0) {
                    LOG.debug('Processed last fragment')
                    break
                }
            }

            // prepare and send continuation request
            startResultNumber += currentQuantity
            String continuationRequest = createQuqiRequest(
                    request, false, startResultNumber, continuationQuantity)
            LOG.debug('Sending continuation request\n {}', continuationRequest)
            fragmentString = client.continuation(continuationRequest)
        }

        // finalize aggregation and return the aggregated response
        if (accumulator != null) {
            if (autoCancel) {
                // TODO: cancel on errors as well
                String cancelRequest = createQuqiRequest(request, true, 0, 0)
                LOG.debug('Sending automatic cancel request\n {}', cancelRequest)
                client.cancel(cancelRequest)
            }

            // counts
            int totalCount = accumulatorControlActProcess.getElementsByTagNameNS(HL7V3_NSURI, 'subject').length
            setQueryAckContinuationNumbers(accumulatorQueryAck, 0, totalCount, totalCount)

            // additional message for the user
            deleteIpfRelatedAcknowledgementDetails(accumulator)
            addAcknowledgementDetail(accumulator, new StringBuilder()
                    .append('This message has been created by the IPF by aggregating ')
                    .append(fragmentsCount)
                    .append(' interactive HL7v3 continuation fragments')
                    .toString())

            // return
            return XmlUtil.serialize(accumulator.documentElement)
        }

        return fragmentString
    }


    /**
     * Creates a HL7v3 continuation or cancel request on the basis
     * of the given initial request and numerical values.
     * @param initialRequest
     *          initial HL7v3 request message.
     * @param isCancel
     *          whether a continuation or a cancle request should be created.
     * @param startResultNumber0
     *          number of the first element from the result set which should be
     *          requested in this continuation (starting from one).
     * @param continuationQuantity0
     *          count of elements to request in this continuation.
     * @return
     *          XML string containing an HL7v3 continuation request.
     */
    private String createQuqiRequest(
            GPathResult initialRequest,
            boolean isCancel,
            int startResultNumber0,
            int continuationQuantity0)
    {
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output)

        builder."QUQI_IN000003UV01${isCancel ? '_Cancel' : ''}"(
                'ITSVersion': 'XML_1.0',
                'xmlns':      HL7V3_NSURI,
                'xmlns:xsi':  'http://www.w3.org/2001/XMLSchema-instance',
                'xmlns:xsd':  'http://www.w3.org/2001/XMLSchema')
        {
            id(root: initialRequest.id.@root,
               assigningAuthorityName: initialRequest.id.@assigningAuthorityName,
               extension: UUID.randomUUID().toString())
            creationTime(value: hl7timestamp())

            // root attribute: 2.16.840.1.113883.1.6    in PDQv3,
            //                 2.16.840.1.113883.5      in QED
            interactionId(root: initialRequest.interactionId.@root, extension: 'QUQI_IN000003UV01')

            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'AL')
            yieldElement(initialRequest.receiver, builder, HL7V3_NSURI)
            yieldElement(initialRequest.sender, builder, HL7V3_NSURI)

            controlActProcess(classCode: 'CACT', moodCode: 'EVN') {
                code(code: 'QUQI_TE000003UV01')
                queryContinuation {
                    yieldElement(initialRequest.controlActProcess.queryByParameter.queryId, builder, HL7V3_NSURI)
                    if (isCancel) {
                        continuationQuantity(value: '0')
                        statusCode(code: 'aborted')
                    } else {
                        // TODO: the two lines below are in fact optional
                        startResultNumber(value: startResultNumber0)
                        continuationQuantity(value: continuationQuantity0)
                        statusCode(code: 'waitContinuedQueryResponse')
                    }
                }
            }
        }

        return output.toString()
    }


}
