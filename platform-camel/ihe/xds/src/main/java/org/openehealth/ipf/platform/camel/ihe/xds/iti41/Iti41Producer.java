/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSlotList30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.commons.core.DomBuildersThreadLocal;
import org.openehealth.ipf.platform.camel.ihe.ws.HeaderUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConstants;
import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Iti41Producer extends AbstractWsProducer<XdsSubmitAuditDataset, WsTransactionConfiguration, ProvideAndRegisterDocumentSetRequestType, RegistryResponseType> {

    private static final DomBuildersThreadLocal DOM_BUILDERS = new DomBuildersThreadLocal();

    public Iti41Producer(AbstractWsEndpoint<XdsSubmitAuditDataset, WsTransactionConfiguration> endpoint, JaxWsClientFactory<XdsSubmitAuditDataset> clientFactory) {
        super(endpoint, clientFactory, ProvideAndRegisterDocumentSetRequestType.class, RegistryResponseType.class);
    }

    @Override
    protected RegistryResponseType callService(Object client, ProvideAndRegisterDocumentSetRequestType request) throws Exception {
        injectTargetHomeCommunityId(client, request);
        return ((Iti41PortType) client).documentRepositoryProvideAndRegisterDocumentSetB(request);
    }

    /**
     * According to the XDR option "Transmit Home Community Id": when the request POJO contains the target home
     * community ID, create a special SOAP header and copy the target home community ID into this header.
     */
    private void injectTargetHomeCommunityId(Object client, ProvideAndRegisterDocumentSetRequestType request) throws ParserConfigurationException {
        String targetHomeCommunityId = null;
        try {
            EbXMLSlotList30 slotList = new EbXMLSlotList30(request.getSubmitObjectsRequest().getRequestSlotList().getSlot());
            targetHomeCommunityId = slotList.getSingleSlotValue(Vocabulary.SLOT_NAME_HOME_COMMUNITY_ID);
        } catch (NullPointerException e) {
            // nop
        }

        if (targetHomeCommunityId != null) {
            Document document = DOM_BUILDERS.get().newDocument();

            Element homeCommunityIdElement = document.createElementNS(Iti41Component.TARGET_HCID_NS, Iti41Component.TARGET_HCID_LOCAL_PART);
            homeCommunityIdElement.setTextContent(targetHomeCommunityId);

            Element blockElement = document.createElementNS(
                    Iti41Component.TARGET_HCID_NS,
                    Iti41Component.TARGET_HCID_BLOCK_LOCAL_PART);
            //blockElement.setAttributeNS(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "role", "urn:ihe:iti:xd:id");
            //blockElement.setAttributeNS(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "relay", "true");
            blockElement.appendChild(homeCommunityIdElement);

            BindingProvider bindingProvider = (BindingProvider) client;
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            List<Header> soapHeaders = HeaderUtils.getHeaders(requestContext, Header.HEADER_LIST, true, true, ArrayList::new);
            soapHeaders.add(new SoapHeader(Iti41Component.TARGET_HCID_HEADER_NAME, blockElement));
        }
    }

}
