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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.iti22;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.pixpdq.definitions.CustomModelClassUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.pixpdq.QpdAwareNakFactory;
import org.openehealth.ipf.platform.camel.ihe.pixpdq.pdqcore.PdqClientAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.pixpdq.pdqcore.PdqServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.pixpdq.pdqcore.PdqTransactionConfiguration;

/**
 * Camel component for ITI-22 (PDQ).
 * @author Dmytro Rud
 */
public class Iti22Component extends MllpComponent {
    public static final MllpTransactionConfiguration CONFIGURATION =
        new PdqTransactionConfiguration(
                "2.5", 
                "PDQ adapter", 
                "IPF",
                207, 
                207, 
                new String[] {"QBP", "QCN"},
                new String[] {"ZV1", "J01"},
                new String[] {"RSP", "ACK"},
                new String[] {"ZV2", "*"}, 
                new boolean[] {true, false},                
                new boolean[] {true, false},
                CustomModelClassUtils.createParser("pdq", "2.5"));
  
    private static final MllpAuditStrategy CLIENT_AUDIT_STRATEGY = 
        new PdqClientAuditStrategy("PDVQ");
    private static final MllpAuditStrategy SERVER_AUDIT_STRATEGY = 
        new PdqServerAuditStrategy("PDVQ");
    private static final NakFactory NAK_FACTORY =
        new QpdAwareNakFactory(CONFIGURATION, "RSP", "ZV2");


    public Iti22Component() {
        super();
    }

    public Iti22Component(CamelContext camelContext) {
        super(camelContext);
    }
    
    @Override
    public MllpAuditStrategy getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }

    @Override
    public MllpAuditStrategy getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }
    
    @Override
    public MllpTransactionConfiguration getTransactionConfiguration() {
        return CONFIGURATION;
    }

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }
}
