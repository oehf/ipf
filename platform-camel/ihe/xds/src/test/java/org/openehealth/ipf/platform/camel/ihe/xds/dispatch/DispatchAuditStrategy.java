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
import org.openehealth.ipf.commons.ihe.xds.pharm1.Pharm1AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75ServerAuditStrategy;

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

    private final Map<String, XdsAuditStrategy<? extends XdsAuditDataset>> MAP;

    public DispatchAuditStrategy(Map<String, XdsAuditStrategy<T>> additionalMappings) {
        super(true);

        MAP = new HashMap<>();
        MAP.put("urn:ihe:iti:2007:RegistryStoredQuery",                    new Iti18AuditStrategy(true));
        MAP.put("urn:ihe:iti:2007:CrossGatewayQuery",                      new Iti38AuditStrategy(true));
        MAP.put("urn:ihe:iti:2007:CrossGatewayRetrieve",                   new Iti39ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b",        new Iti41ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2007:RegisterDocumentSet-b",                  new Iti42ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2007:RetrieveDocumentSet",                    new Iti43ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2009:MultiPatientStoredQuery",                new Iti51AuditStrategy(true));
        MAP.put("urn:ihe:iti:2010:UpdateDocumentSet",                      new Iti57ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2010:RegisterOnDemandDocumentEntry",          new Iti61ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2010:DeleteDocumentSet",                      new Iti62AuditStrategy(true));
        MAP.put("urn:ihe:iti:2011:CrossGatewayFetch",                      new Iti63AuditStrategy(true));
        MAP.put("urn:ihe:iti:2015:CrossGatewayDocumentProvide",            new Iti80ServerAuditStrategy());
        MAP.put("urn:ihe:iti:2017:RemoveDocuments",                        new Iti86AuditStrategy(true));
        MAP.put("urn:ihe:iti:2018:RestrictedUpdateDocumentSet",            new Iti92ServerAuditStrategy());
        MAP.put("urn:ihe:pharm:cmpd:2010:QueryPharmacyDocuments",          new Pharm1AuditStrategy(true));
        MAP.put("urn:ihe:rad:2009:RetrieveImagingDocumentSet",             new Rad69ServerAuditStrategy());
        MAP.put("urn:ihe:rad:2011:CrossGatewayRetrieveImagingDocumentSet", new Rad75ServerAuditStrategy());

        if (additionalMappings != null) {
            MAP.putAll(additionalMappings);
        }
    }

    @Override
    public T createAuditDataset() {
        XdsAuditStrategy<T> strategy = getAuditStrategy();
        return (strategy != null) ? strategy.createAuditDataset() : null;
    }

    @Override
    public T enrichAuditDatasetFromRequest(T auditDataset, Object request, Map<String, Object> parameters ) {
        XdsAuditStrategy<T> strategy = getAuditStrategy();
        if (strategy != null) {
            return strategy.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        }
        return null;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(T auditDataset, Object response, AuditContext auditContext) {
        XdsAuditStrategy<T> strategy = getAuditStrategy();
        if (strategy != null) {
            return strategy.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
        }
        return false;
    }

    @Override
    public void doAudit(AuditContext auditContext, T auditDataset) {
        XdsAuditStrategy<T> strategy = getAuditStrategy();
        if (strategy != null) {
            strategy.doAudit(auditContext, auditDataset);
        }
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, T auditDataset) {
        XdsAuditStrategy<T> strategy = getAuditStrategy();
        return (strategy != null) ? strategy.makeAuditMessage(auditContext, auditDataset) : null;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object response) {
        XdsAuditStrategy<? extends XdsAuditDataset> strategy = getAuditStrategy();
        return (strategy != null) ? strategy.getEventOutcomeIndicator(response) : null;
    }

    private XdsAuditStrategy<T> getAuditStrategy() {
        MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
        if ("GET".equals(messageContext.get(MessageContext.HTTP_REQUEST_METHOD))) {
            log.debug("Cannot serve HTTP method GET");
            return null;
        }
        String action = DispatchInContextCreatorInterceptor.extractWsaAction(messageContext);
        if (action == null) {
            log.debug("Cannot determine WS-Addressing action");
            return null;
        }

        XdsAuditStrategy<T> auditStrategy = (XdsAuditStrategy<T>) MAP.get(action);
        if (auditStrategy == null) {
            log.debug("No strategy could be found for action {}", action);
        } else {
            log.debug("Found strategy {} for action {}", auditStrategy.getClass().getCanonicalName(), action);
        }
        return auditStrategy;
    } 
}
