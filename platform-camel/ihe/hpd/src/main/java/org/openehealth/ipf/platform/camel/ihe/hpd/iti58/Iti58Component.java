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
package org.openehealth.ipf.platform.camel.ihe.hpd.iti58;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hpd.HPD;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.hpd.HpdQueryEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Iti58Component extends AbstractWsComponent<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>, WsInteractionId<WsTransactionConfiguration<WsAuditDataset>>> {

    public Iti58Component() {
        super(HPD.ReadInteractions.ITI_58);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new HpdQueryEndpoint<WsAuditDataset>(uri, remaining, this, parameters, Iti58Service.class) {
            @Override
            protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>> endpoint) {
                return new Iti58Service(this);
            }
        };
    }

}
