/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp
import static org.openehealth.ipf.commons.xml.XmlUtils.rootElementName
import static org.openehealth.ipf.commons.xml.XmlUtils.source
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3ContinuationUtils.parseInt
import groovy.util.slurpersupport.GPathResult

import javax.xml.transform.Source

import org.apache.commons.lang3.Validate
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationsPortType
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy
import org.openehealth.ipf.commons.xml.CombinedXmlValidator
import org.openehealth.ipf.commons.xml.XsltTransmogrifier
import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * Generic Web Service implementation for HL7 v3-based transactions
 * with continuations support.
 *
 * @author Dmytro Rud
 */
abstract public class Hl7v3ContinuationAwareWebService
        extends AbstractHl7v3WebService
        implements Hl7v3ContinuationsPortType
{
    private static final transient Logger LOG = LoggerFactory.getLogger(Hl7v3ContinuationAwareWebService.class)

    private static final String XSLT_TEMPLATE = 'xslt/hl7v3-continuations-fragmentize.xslt'
    private static final XsltTransmogrifier XSLT_TRANSMOGRIFIER = new XsltTransmogrifier(String.class)
    private static final CombinedXmlValidator VALIDATOR = new CombinedXmlValidator()

    private final Hl7v3ContinuationStorage storage
    private final int defaultThreshold
    private final boolean validation
    private final WsAuditStrategy auditStrategy

    
    public Hl7v3ContinuationAwareWebService(Hl7v3ContinuationAwareEndpoint endpoint) {
        super(endpoint.component.wsTransactionConfiguration)

        Validate.notNull(endpoint.continuationStorage)

        this.storage          = endpoint.continuationStorage
        this.defaultThreshold = endpoint.defaultContinuationThreshold
        this.validation       = endpoint.validationOnContinuation
        this.auditStrategy    = endpoint.manualAudit ?
            endpoint.component.getServerAuditStrategy() : null
    }


    /**
     * Serves the "main" service operation.
     * @param requestObject
     *      request, actually always a String from CXF.
     * @return
     *      operation response in one of supported formats.
     */
    @Override
    String operation(String requestString) {
        return process0(requestString)
    }

    /**
     * Serves response continuation requests.
     * @param requestObject
     *      continuation request, actually always a String from CXF.
     * @return
     *      next portion of response data in one of supported formats.
     */
    @Override
    String continuation(String requestString) {
        return process0(requestString)
    }

    /**
     * Serves response continuation cancel requests.
     * @param requestObject
     *      continuation cancel request, actually always a String from CXF.
     * @return
     *      cancel acknowledgement response in one of supported formats.
     */
    @Override
    String cancel(String requestString) {
        return process0(requestString)
    }


    String process0(String requestString) {
        String rootElementName = rootElementName(requestString)
        switch (rootElementName) {
            case wsTransactionConfiguration.mainRequestRootElementName:
                return operation0(requestString)
            case 'QUQI_IN000003UV01':
                return continuation0(requestString)
            case 'QUQI_IN000003UV01_Cancel':
                return cancel0(requestString)
        }
        throw new RuntimeException('Cannot dispatch request message with root element ' + rootElementName)
    }


    /**
     * Handles "main operation" requests of the IHE transaction.
     */
    String operation0(String requestString) {
        if (LOG.debugEnabled) {
            LOG.debug('operation(): Got request\n' + requestString)
        }

        // prepare ATNA audit, if necessary
        WsAuditDataset auditDataset = startAtnaAuditing(requestString, auditStrategy)

        // validate request, if necessary
        if (validation) {
            try {
                VALIDATOR.validate(requestString,
                        Hl7v3ValidationProfiles.getRequestValidationProfile(wsTransactionConfiguration.interactionId))
            } catch (ValidationException e) {
                LOG.info('operation(): invalid request', e)
                String nak = createNak(requestString, e)
                finalizeAtnaAuditing(nak, auditStrategy, auditDataset)
                return nak
            }
        }

        // run the route
        final String responseString = doProcess(requestString)

        // validate response, if necessary
        if (validation) {
            try {
                VALIDATOR.validate(responseString,
                        Hl7v3ValidationProfiles.getResponseValidationProfile(wsTransactionConfiguration.interactionId))
            } catch (ValidationException e) {
                LOG.info('operation(): invalid response', e)
                String nak = createNak(requestString, e)
                finalizeAtnaAuditing(nak, auditStrategy, auditDataset)
                return nak
            }
        }

        // finalize ATNA auditing
        finalizeAtnaAuditing(responseString, auditStrategy, auditDataset)

        // process continuations
        GPathResult request = slurp(requestString)
        int threshold = parseInt(request.controlActProcess.queryByParameter.initialQuantity.@value.text())
        if (threshold < 1) {
            threshold = defaultThreshold
        }
        if (threshold < 1) {
            LOG.debug('operation(): Threshold not set')
            return responseString
        }

        try {
            String result = createFragment(request, responseString, 1, threshold)
            if (LOG.debugEnabled) {
                LOG.debug('operation(): Generated fragment\n' + result)
            }
            def key = getQueryKey(request, false)
            storage.storeMessage(key, responseString)
            storage.storeLastResultNumber(key, threshold + 1)
            storage.storeContinuationQuantity(key, threshold)
            return result
        } catch (Exception e) {
            if (e.cause?.message?.startsWith('[ipf]')) {
                LOG.debug('operation(): Negative response or too few subjects')
                return responseString
            } else {
                throw e
            }
        }
    }


    /**
     * Handles continuation requests.
     */
    String continuation0(String requestString) {
        if (LOG.debugEnabled) {
            LOG.debug('continuation(): Got request\n' + requestString)
        }

        // validate
        if (validation) {
            try {
                VALIDATOR.validate(requestString,
                        Hl7v3ValidationProfiles.getRequestValidationProfile(wsTransactionConfiguration.interactionId))
            } catch (ValidationException e) {
                LOG.info('continuation(): invalid request', e)
                def nak = createNak(requestString, e)
                return nak
            }
        }

        // process
        GPathResult request = slurp(requestString)
        String key = getQueryKey(request, true)
        String responseString = storage.getMessage(key)

        if (! responseString) {
            return error(request, 'continuation(): Unknown continuation key', key)
        }

        def queryContinuation = request.controlActProcess.queryContinuation

        int startResultNumber = parseInt(queryContinuation.startResultNumber.@value.text())
        if (startResultNumber < 0) {
            startResultNumber = storage.getLastResultNumber(key)
        }

        int continuationQuantity = parseInt(queryContinuation.continuationQuantity.@value.text())
        if (continuationQuantity < 0) {
            continuationQuantity = storage.getContinuationQuantity(key)
        }

        if ((startResultNumber < 1) || (continuationQuantity < 1)) {
            return error(request, 'continuation(): startResultNumber and/or continuationQuantity not set', key)
        }

        try {
            String result = createFragment(request, responseString, startResultNumber, continuationQuantity)
            if (LOG.debugEnabled) {
                LOG.debug('continuation(): Generated fragment\n' + result)
            }
            storage.storeLastResultNumber(key, startResultNumber + continuationQuantity)
            storage.storeContinuationQuantity(key, continuationQuantity)
            return result
        } catch (Exception e) {
            return error(request, 'continuation(): Internal error: ' + e.message, key)
        }
    }


    /**
     * Handles continuation cancel requests.
     */
    String cancel0(String requestString) {
        if (LOG.debugEnabled) {
            LOG.debug('cancel(): Got request\n' + requestString)
        }

        // validate
        if (validation) {
            try {
                VALIDATOR.validate(requestString,
                        Hl7v3ValidationProfiles.getRequestValidationProfile(wsTransactionConfiguration.interactionId))
            } catch (ValidationException e) {
                LOG.info('cancel(): invalid request', e)
                return createNak(requestString, e)
            }
        }

        // process
        GPathResult request = slurp(requestString)
        String key = getQueryKey(request, true)
        String responseString = storage.getMessage(key)
        if (responseString) {
            storage.remove(key)
            GPathResult response = slurp(responseString)
            String result = Hl7v3NakFactory.response(request, null, 'MCCI_IN000002UV01', null, true)
            if (LOG.debugEnabled) {
                LOG.debug('cancel(): generated ACK\n' + result)
            }
            return result
        } else {
            return error(request, 'cancel(): Unknown continuation key', key)
        }
    }


    /**
     * Creates a continuation fragment.
     * @param request
     *      request (either "main operation" or continuation one).
     * @param responseString
     *      unfragmented response previously received from the route, as XML string.
     * @param startResultNumber
     *      number of first data record to return, starting from 1.
     * @param continuationCount
     *      count of data records to return.
     * @return
     *      generated fragment as XML string.
     * @throws Exception
     *      special exceptions can be thrown from the XSLT template --
     *      they can be recognized using
     *      <code>if (e.cause.message.startsWith('[ipf]')) { ... }</code>.
     */
    private static String createFragment(
            GPathResult request,
            String responseString,
            int startResultNumber,
            int continuationCount) throws Exception
    {
        Source source = source(responseString)
        def params = [
            'startResultNumber'        : startResultNumber,
            'continuationCount'        : continuationCount,
            'messageIdExtension'       : UUID.randomUUID().toString(),
            'targetMessageIdRoot'      : request.id.@root.text(),
            'targetMessageIdExtension' : request.id.@extension.text(),
        ]

        return XSLT_TRANSMOGRIFIER.zap(source, XSLT_TEMPLATE, params)
    }


    /**
     * Logs an error and creates HL7v3 NAK message for it.
     */
    private String error(GPathResult request, String message, String key) {
        LOG.warn("{} {}", message, key)
        return createNak(request, new Exception(message))
    }


    /**
     * Returns storage key of the given message.
     * @param request
     *      HL7v3 request message.
     * @param isContinuation
     *      <code>true</code> when the given message is a continuation request;
     *      <code>false</code> otherwise, i.e. in the case of a "main operation"
     *      request.
     * @return
     */
    private static String getQueryKey(GPathResult request, boolean isContinuation) {
        def queryId = isContinuation ?
            request.controlActProcess.queryContinuation.queryId[0] :
            request.controlActProcess.queryByParameter.queryId[0]
        def sender = request.sender.device.id[0]
        return [queryId.@root, queryId.@extension, queryId.@assigningAuthorityName,
                 sender.@root,  sender.@extension,  sender.@assigningAuthorityName].join('/')
    }

}
