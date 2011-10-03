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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import static org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3ContinuationUtils.*

import groovy.util.slurpersupport.GPathResult
import javax.xml.parsers.DocumentBuilder
import org.apache.commons.lang3.Validate
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationsPortType
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import static org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils.*
import static org.openehealth.ipf.commons.xml.XmlYielder.yieldElement
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareServiceInfo
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import groovy.xml.XmlUtil
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes
import org.apache.cxf.ws.addressing.AddressingProperties
import org.apache.cxf.ws.addressing.JAXWSAConstants
import javax.xml.ws.BindingProvider
import org.apache.cxf.message.Message
import static org.openehealth.ipf.commons.xml.XmlUtils.*

/**
 * Camel producer HL7 v3-based IHE transactions with Continuation support.
 * <p>
 * Format of continuation messages is described, for example,
 * in the IHE PIXv3/PDQv3 Supplement August 2010, pp. 85-87 and 117-119.
 * <p>
 * The class is parametrized with <code>&lt;Object, Object&gt;</code> 
 * due to Groovy peculiarities, in reality is should be
 * <code>&lt;String, String&gt;</code>.
 */
class Hl7v3ContinuationAwareProducer extends DefaultItiProducer<Object, Object> {
    private static final transient Log LOG = LogFactory.getLog(Hl7v3ContinuationAwareProducer.class);

    private static final ThreadLocal<DocumentBuilder> DOM_BUILDERS = new DomBuildersThreadLocal()
    private static final CombinedXmlValidator VALIDATOR = new CombinedXmlValidator()

    private final Hl7v3ContinuationAwareServiceInfo serviceInfo;

    private final boolean supportContinuation
    private final boolean autoCancel
    private final boolean validationOnContinuation

    private final Hl7v3AuditStrategy auditStrategy

    // TODO: make this value configurable
    private final int defaultContinuationQuantity = 10


    /**
     * Constructor.
     * @param endpoint
     * @param clientFactory
     * @param serviceInfo
     *      parameters of the transaction served by this producer.
     * @param supportContinuation
     *      whether this producer should support HL7v3 continuation.
     * @param autoCancel
     *      whether a "continuation cancel" message should be automatically
     *      sent after the last fragment has been read
     *      (relevant only when continuation support is turned on).
     * @param validationOnContinuation
     *      whether internally handled incoming messages should be validated.
     * @param auditStrategy
     *      client-side ATNA audit strategy; may be <code>null</code>;
     *      will be used only when <code>supportContinuation==true</code>.
     */
    public Hl7v3ContinuationAwareProducer(
            Hl7v3Endpoint endpoint,
            ItiClientFactory clientFactory,
            Hl7v3ContinuationAwareServiceInfo serviceInfo,
            boolean supportContinuation,
            boolean autoCancel,
            boolean validationOnContinuation,
            Hl7v3AuditStrategy auditStrategy)
    {
        super(endpoint, clientFactory)

        Validate.notNull(serviceInfo)
        this.serviceInfo = serviceInfo
        this.supportContinuation = supportContinuation
        this.autoCancel = autoCancel
        this.validationOnContinuation = validationOnContinuation

        if (supportContinuation) {
            this.auditStrategy = auditStrategy
        }
    }


    /**
     * Dispatches the original request message, optionally handling continuations.
     * <p>
     * The response is always an XML String.
     */
    @Override
    protected Object callService(Object clientObject, Object requestObject) {
        Hl7v3ContinuationsPortType client = (Hl7v3ContinuationsPortType) clientObject
        String rootElementName = rootElementName(requestObject).localPart
        switch (rootElementName) {
            case serviceInfo.mainRequestRootElementName:
                String requestString = toString(requestObject, null)
                String responseString = client.operation(requestObject)
                if (! supportContinuation) {
                    return responseString
                }
                return (auditStrategy != null) ?
                        processContinuationWithAtnaAuditing(client, requestString, responseString) :
                        processContinuation(client, requestString, responseString)

            case 'QUQI_IN000003UV01':
                // continuation is supported by the route, not by us
                return client.continuation(requestObject)
            case 'QUQI_IN000003UV01_Cancel':
                return client.cancel(requestObject)
        }
        throw new RuntimeException('Cannot dispatch request message with root element ' + rootElementName)
    }


    /**
     * A kind of wrapper which adds ATNA auditing functionality
     * to the "standard" {@link #processContinuation} method.
     */
    private String processContinuationWithAtnaAuditing(
        Hl7v3ContinuationsPortType client,
        String requestString,
        String fragmentString)
    {
        // fill request-related ATNA audit fields
        WsAuditDataset auditDataset = auditStrategy.createAuditDataset()
        try {
            auditDataset.clientIpAddress = InetAddress.localHost.hostAddress
        } catch (UnknownHostException e) {
            // nop
        }

        Map requestContext = ((BindingProvider) client).requestContext

        // When the user has provided an endpoint URI for the WS-A ReplyTo header, the method
        // org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiProducer.configureWSAHeaders
        // will put this URI into the request context, and we try to retrieve it here.
        // Otherwise, the field auditDataset.userId remains null, and this is Ok as well.
        AddressingProperties apropos = requestContext.get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES)
        auditDataset.userId = apropos?.replyTo?.address?.value

        auditDataset.serviceEndpointUrl = requestContext.get(Message.ENDPOINT_ADDRESS)
        auditStrategy.enrichDatasetFromRequest(requestString, auditDataset)

        try {
            // perform the call and play the interactive continuations MEP
            String responseString = processContinuation(client, requestString, fragmentString)

            // fill response-related ATNA audit fields and perform the audit
            auditStrategy.enrichDatasetFromResponse(responseString, auditDataset)
            auditStrategy.audit(auditDataset)
            return responseString

        } catch (Exception e) {

            // when exception occurs -- audit it and rethrow to the caller's route
            auditDataset.eventOutcomeCode = RFC3881EventOutcomeCodes.SERIOUS_FAILURE
            auditStrategy.audit(auditDataset)
            throw e
        }
    }


    /**
     * Performs HL7v3 continuation MEP.
     */
    private String processContinuation(
        Hl7v3ContinuationsPortType client,
        String requestString,
        String fragmentString)
    {
        GPathResult request = slurp(requestString)

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
                VALIDATOR.validate(fragmentString,
                        Hl7v3ValidationProfiles.getResponseValidationProfile(serviceInfo.getInteractionId()))
            }

            Document fragment = DOM_BUILDERS.get().parse(new ByteArrayInputStream(fragmentString.getBytes()))
            Element controlActProcess = getElementNS(fragment.documentElement, HL7V3_NSURI_SET, 'controlActProcess')
            Element queryAck = getElementNS(controlActProcess, HL7V3_NSURI_SET, 'queryAck')

            // check whether the fragment is a valid and positive response
            if ((fragment.documentElement.localName != serviceInfo.mainResponseRootElementName) ||
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
            LOG.debug('Sending continuation request\n' + continuationRequest)
            fragmentString = client.continuation(continuationRequest)
        }

        // finalize aggregation and return the aggregated response
        if (accumulator != null) {
            if (autoCancel) {
                // TODO: cancel on errors as well
                String cancelRequest = createQuqiRequest(request, true, 0, 0)
                LOG.debug('Sending automatical cancel request\n' + cancelRequest)
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
