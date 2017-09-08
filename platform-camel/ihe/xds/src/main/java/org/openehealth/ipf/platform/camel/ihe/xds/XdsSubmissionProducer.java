/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.openehealth.ipf.commons.core.DomBuildersThreadLocal;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSlotList30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.HeaderUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class XdsSubmissionProducer<InType, OutType> extends AbstractWsProducer<XdsSubmitAuditDataset, WsTransactionConfiguration, InType, OutType> {

    private static final DomBuildersThreadLocal DOM_BUILDERS = new DomBuildersThreadLocal();

    public static final String TARGET_HCID_NS = "urn:ihe:iti:xdr:2014";
    public static final String TARGET_HCID_NS_PREFIX = "xdr";
    public static final String TARGET_HCID_BLOCK_LOCAL_PART = "homeCommunityBlock";
    public static final String TARGET_HCID_LOCAL_PART = "homeCommunityId";
    public static final QName  TARGET_HCID_HEADER_NAME = new QName(TARGET_HCID_NS, TARGET_HCID_BLOCK_LOCAL_PART, TARGET_HCID_NS_PREFIX);

    public XdsSubmissionProducer(
            AbstractWsEndpoint<XdsSubmitAuditDataset, WsTransactionConfiguration> endpoint,
            JaxWsClientFactory<XdsSubmitAuditDataset> clientFactory,
            Class<InType> requestClass,
            Class<OutType> responseClass)
    {
        super(endpoint, clientFactory, requestClass, responseClass);
    }

    /**
     * According to the XDR option "Transmit Home Community Id": when the request POJO contains the target home
     * community ID, create a special SOAP header and copy the target home community ID into this header.
     */
    protected static void injectTargetHomeCommunityId(Object client, SubmitObjectsRequest request) throws ParserConfigurationException {
        String targetHomeCommunityId = null;
        try {
            EbXMLSlotList30 slotList = new EbXMLSlotList30(request.getRequestSlotList().getSlot());
            targetHomeCommunityId = slotList.getSingleSlotValue(Vocabulary.SLOT_NAME_HOME_COMMUNITY_ID);
        } catch (NullPointerException e) {
            // nop
        }

        if (targetHomeCommunityId != null) {
            Document document = DOM_BUILDERS.get().newDocument();

            Element homeCommunityIdElement = document.createElementNS(TARGET_HCID_NS, TARGET_HCID_LOCAL_PART);
            homeCommunityIdElement.setTextContent(targetHomeCommunityId);

            Element blockElement = document.createElementNS(TARGET_HCID_NS, TARGET_HCID_BLOCK_LOCAL_PART);
            //blockElement.setAttributeNS(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "role", "urn:ihe:iti:xd:id");
            //blockElement.setAttributeNS(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "relay", "true");
            blockElement.appendChild(homeCommunityIdElement);

            BindingProvider bindingProvider = (BindingProvider) client;
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            List<Header> soapHeaders = HeaderUtils.getHeaders(requestContext, Header.HEADER_LIST, true, true, ArrayList::new);
            soapHeaders.add(new SoapHeader(TARGET_HCID_HEADER_NAME, blockElement));
        }
    }

}
