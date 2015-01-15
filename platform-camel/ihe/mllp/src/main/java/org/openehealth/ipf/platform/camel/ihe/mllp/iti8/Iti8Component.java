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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import ca.uhn.hl7v2.ErrorCode;
import org.apache.camel.CamelContext;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;

/**
 * Camel component for ITI-8 (PIX Feed).
 * @author Dmytro Rud
 */
public class Iti8Component extends MllpTransactionComponent<Iti8AuditDataset> {
    public static final Hl7v2TransactionConfiguration CONFIGURATION =
        new Hl7v2TransactionConfiguration(
                "2.3.1", 
                "PIX adapter", 
                "IPF",
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                ErrorCode.APPLICATION_INTERNAL_ERROR,
                new String[] {"ADT"},
                new String[] {"A01 A04 A05 A08 A40"},
                new String[] {"ACK"},
                new String[] {"*"},
                new boolean[] {true},
                new boolean[] {false},
                HapiContextFactory.createHapiContext(PixPdqTransactions.ITI8));
  
    private static final MllpAuditStrategy<Iti8AuditDataset> CLIENT_AUDIT_STRATEGY = 
        new Iti8ClientAuditStrategy();
    private static final MllpAuditStrategy<Iti8AuditDataset> SERVER_AUDIT_STRATEGY = 
        new Iti8ServerAuditStrategy();
    private static final NakFactory NAK_FACTORY = new NakFactory(CONFIGURATION);

    
    public Iti8Component() {
        super();
    }

    public Iti8Component(CamelContext camelContext) {
        super(camelContext);
    }
    
    @Override
    public MllpAuditStrategy<Iti8AuditDataset> getClientAuditStrategy() {
        return CLIENT_AUDIT_STRATEGY;
    }

    @Override
    public MllpAuditStrategy<Iti8AuditDataset> getServerAuditStrategy() {
        return SERVER_AUDIT_STRATEGY;
    }
    
    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return CONFIGURATION;
    }

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }
}
