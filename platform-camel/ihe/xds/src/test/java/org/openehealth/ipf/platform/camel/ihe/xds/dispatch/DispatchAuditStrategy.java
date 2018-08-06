/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.dispatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti57.Iti57ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti62.Iti62AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti63.Iti63AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti80.Iti80ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti86.Iti86AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti92.Iti92ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75ServerAuditStrategy;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Audit strategy which wraps a set of transaction-specific audit strategies and dispatches
 * calls to one of them depending of which WSDL operation is currently being processed.
 *
 * @author Dmytro Rud
 */
@Slf4j
public class DispatchAuditStrategy<T extends XdsAuditDataset> extends AuditStrategySupport<T> {

    private final Map<QName, XdsAuditStrategy<? extends XdsAuditDataset>> map;

    public DispatchAuditStrategy(Map<QName, XdsAuditStrategy<? extends XdsAuditDataset>> additionalMappings) {
        super(true);
        map = new HashMap<>();
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_RegistryStoredQuery"),
                new Iti18AuditStrategy(true));
        map.put(new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_CrossGatewayQuery"),
                new Iti38AuditStrategy(true));
        map.put(new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_CrossGatewayRetrieve"),
                new Iti39ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_ProvideAndRegisterDocumentSet-b"),
                new Iti41ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_RegisterDocumentSet-b"),
                new Iti42ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_RetrieveDocumentSet"),
                new Iti43ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_MultiPatientStoredQuery"),
                new Iti51AuditStrategy(true));
        map.put(new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_UpdateDocumentSet"),
                new Iti57ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_RegisterOnDemandDocumentEntry"),
                new Iti61ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_DeleteDocumentSet"),
                new Iti62AuditStrategy(true));
        map.put(new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_CrossGatewayFetch"),
                new Iti63AuditStrategy(true));
        map.put(new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_CrossGatewayDocumentProvide"),
                new Iti80ServerAuditStrategy());
        map.put(new QName("urn:ihe:iti:rmd:2017", "DocumentRepository_RemoveDocuments"),
                new Iti86AuditStrategy(true));
        map.put(new QName("urn:ihe:iti:rmu:2018", "UpdateResponder_RestrictedUpdateDocumentSet"),
                new Iti92ServerAuditStrategy());
        map.put(new QName("urn:ihe:rad:xdsi-b:2009", "DocumentRepository_RetrieveImagingDocumentSet"),
                new Rad69ServerAuditStrategy());
        map.put(new QName("urn:ihe:rad:xdsi-b:2009", "RespondingGateway_CrossGatewayRetrieveImagingDocumentSet"),
                new Rad75ServerAuditStrategy());
        if (additionalMappings != null) {
            map.putAll(additionalMappings);
        }
    }

    @Override
    public T createAuditDataset() {
        XdsAuditStrategy<? extends XdsAuditDataset> strategy = getAuditStrategy();
        return (strategy != null) ? (T)strategy.createAuditDataset() : null;
    }

    @Override
    public T enrichAuditDatasetFromRequest(T auditDataset, Object request, Map<String, Object> parameters ) {
        XdsAuditStrategy<T> strategy = (XdsAuditStrategy<T>)getAuditStrategy();
        if (strategy != null) {
            return strategy.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        }
        return null;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(T auditDataset, Object response, AuditContext auditContext) {
        XdsAuditStrategy<T> strategy = (XdsAuditStrategy<T>)getAuditStrategy();
        if (strategy != null) {
            return strategy.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
        }
        return false;
    }

    @Override
    public void doAudit(AuditContext auditContext, T auditDataset) {
        XdsAuditStrategy<T> strategy = (XdsAuditStrategy<T>)getAuditStrategy();
        if (strategy != null) {
            strategy.doAudit(auditContext, auditDataset);
        }
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, T auditDataset) {
        XdsAuditStrategy<T> strategy = (XdsAuditStrategy<T>)getAuditStrategy();
        return (strategy != null) ? strategy.makeAuditMessage(auditContext, auditDataset) : null;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object response) {
        XdsAuditStrategy<? extends XdsAuditDataset> strategy = getAuditStrategy();
        return (strategy != null) ? strategy.getEventOutcomeIndicator(response) : null;
    }

    private XdsAuditStrategy<? extends XdsAuditDataset> getAuditStrategy() {
        MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
        if ("GET".equals(messageContext.get(MessageContext.HTTP_REQUEST_METHOD))) {
            return null;
        }
        if (! messageContext.containsKey(MessageContext.WSDL_OPERATION)) {
            return null;
        }

        QName operationName = (QName) messageContext.get(MessageContext.WSDL_OPERATION);
        XdsAuditStrategy<? extends XdsAuditDataset> auditStrategy = map.get(operationName);
        if (auditStrategy == null) {
            log.debug("No strategy could be found for operation {}", operationName);
        } else {
            log.debug("Found strategy {} for operation {}", auditStrategy.getClass().getCanonicalName(), operationName);
        }
        return auditStrategy;
    } 
}
